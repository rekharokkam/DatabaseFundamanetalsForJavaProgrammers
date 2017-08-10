package com.chapter4;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Scanner;

public class TransactionUsage 
{
	private static final String CREATE_USER_PREPARED_STMT = "INSERT INTO users (login, password) values (?, ?)"; 
	private static final String[] USERS = {"curly", "larry", "moe"};
	
	private Connection openConnection ()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:db/users.db");//If database does not exist then SQLite3 creates one and connects to it. If db already exists then sqlite3 connects to the database
		}catch (Exception e) 
		{
			logError(e);
			return null;
		}
	}

	private void closeConnection (Connection conn)
	{
		if (null != conn)
		{
			try{
				conn.close();
			}catch (Exception e){
				logAndExit (e);
			}
		}
	}
	
	private void logAndExit (Exception e)
	{
		logError(e);
		System.exit(-1);
	}
	
	private void log (String message)
	{
System.out.println (message);		
	}
	
	private void logError (Exception e){
System.err.println (e.getClass().getName() + " : " + e.getMessage());	
e.printStackTrace(System.err);
	}

	private void addSomeUsers ()
	{
		Connection conn = openConnection();
		if (null != conn){
			
			//First check to see if the users table has already been populated
			try
			{
				String sql = "SELECT COUNT(*) FROM users;";
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(sql);
				
				while (rs.next())
				{
					int count = rs.getInt(1);
					if (count > 0)
					{
						log ("Users already loaded so returning");
						return;
					}
				}			
			
				PreparedStatement pstmt = conn.prepareStatement(CREATE_USER_PREPARED_STMT);
				
				for (String name : USERS)
				{				
					pstmt.setString(1, name);
					pstmt.setString(2, getEncryptedPassword(name + "123"));
					
					int count = pstmt.executeUpdate();					
				}
			}
			catch (Exception e) 
			{
				logError (e);
			}
			finally
			{
				closeConnection(conn);
			}			
		}
	}
	
	private String getEncryptedPassword (String password)
	{
		try
		{
			MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
			sha1.update(password.getBytes());
			
			return Arrays.toString(sha1.digest());
			
		}catch (NoSuchAlgorithmException e)
		{
			logError(e);
		}
		return password;
	}
	
	private int getUserId (Connection conn)
		throws SQLException
	{
		log ("Enter User Name and Pwd");
//		Scanner stdIn = new Scanner(System.in);
//		String userNamePwd = stdIn.next();
//		stdIn.close();
		
		String[] up = "curly,aaa".split(",");
		String userName = up[0];
		String pwd = up[1];
		
		
		
		log ("User Name : " + userName + " :: Pwd : " + pwd);
		
		if (null != conn)
		{
			//Get User Id from Database for Updates
			// A parameterized query, turned into a PreparedStatement, protects against so-called SQL-injection attacks.
			PreparedStatement pstmt = conn.prepareStatement("SELECT uid FROM users where login = ? and password = ?");
			
			pstmt.setString(1, userName);
			pstmt.setString(2, getEncryptedPassword(pwd));
			
			ResultSet rs = pstmt.executeQuery();
			while (rs.next())
			{
				log ("Found the user for the combination of username and pwd : "  + userName + ":" + pwd);
				return rs.getInt(1);
			}
		}
		log ("Can't find the user for the combination of username and pwd : "  + userName + ":" + pwd);
		return 0;
	}
	
	private void changePasswordAndLog ()
	{
		log ("Enter New password");
		Scanner stdIn = new Scanner(System.in);
		String newPassword = stdIn.next();
		
		stdIn.close();
		
		Connection conn = openConnection();
		if (null != conn)
		{
			//Get User Id from Database for Updates
			try
			{
				//fetch the userid record from database
				int userId = getUserId(conn);
				
				//update both password and password change log in a transaction
				
				conn.setAutoCommit(false);
				String updatePasswordSQL = "UPDATE users SET password = ? WHERE uid = ?";
				String passwordChangeLogSQL = "INSERT INTO pwdchanges (user_id, timestamp) VALUES (?, CURRENT_TIMESTAMP)";
				
				PreparedStatement pstmt1 = conn.prepareStatement(updatePasswordSQL);
				PreparedStatement pstmt2 = conn.prepareStatement(passwordChangeLogSQL);
				
				pstmt1.setString(1, getEncryptedPassword(newPassword));
				pstmt1.setInt(2, userId);
				
				pstmt2.setInt(1, userId);
				
				pstmt1.executeUpdate();
				pstmt2.executeUpdate();
				
				conn.commit();
			}
			catch (Exception e) 
			{
				logError (e);
				try
				{
					conn.rollback();
				}catch (SQLException sqle)
				{
					logError(sqle);
				}
			}
			finally
			{
				closeConnection(conn);
			}
		}
	}
	
	private void demo ()
	{
		addSomeUsers ();
		changePasswordAndLog();
		
	}
	
	public static void main(String[] args) 
	{
		new TransactionUsage().demo();
	}
}
