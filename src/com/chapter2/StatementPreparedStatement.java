package com.chapter2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class StatementPreparedStatement
{
	private static final String SELECT_STATEMENT = "SELECT name FROM customers WHERE customer_id IN "
			+ "(SELECT customer_id FROM sessions WHERE activity_id IN "
				+ "(SELECT activity_id FROM activities WHERE name = ";
	private static final String SELECT_PREPARED_STATEMENT = "SELECT name FROM customers WHERE customer_id IN "
			+ "(SELECT customer_id FROM sessions WHERE activity_id IN "
				+ "(SELECT activity_id FROM activities WHERE name = ?))";
	private Connection openConnection ()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:recClub.db");//If database does not exist then SQLite3 creates one and connects to it. If db already exists then sqlite3 connects to the database
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

	private void statementQuery (String gameName)
	{
		Connection conn = openConnection();
		try
		{
			String sql = SELECT_STATEMENT + "'" + gameName + "'))";
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(sql) ;
			
			while (rs.next())
			{
				String customerName = rs.getString("name");
				log("STATEMENT -  : " + String.format("%s plays %s", customerName, gameName));		
			}
		}
		catch (SQLException sqlE)
		{
			logError(sqlE);
		}
		finally {
			closeConnection(conn);
		}
	}

	//Prepared statement takes care of escaping the special characters. No hassels of adding quotes around the input parameters.
	private void preparedStatementQuery (String gameName)
	{
		Connection conn = openConnection();
		try
		{
			PreparedStatement pstmt = conn.prepareStatement(SELECT_PREPARED_STATEMENT);
			pstmt.setString(1, gameName);
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next())
			{
				String customerName = rs.getString("name");
				log("STATEMENT -  : " + String.format("%s plays %s", customerName, gameName));		
			}
		}
		catch (SQLException sqlE)
		{
			logError(sqlE);
		}
		finally {
			closeConnection(conn);
		}
	}

	private void demo ()
	{
		statementQuery ("go");
		log ("\n");
		preparedStatementQuery ("go");
	}
	
	public static void main(String[] args)
	{
		new StatementPreparedStatement().demo();
	}

}
