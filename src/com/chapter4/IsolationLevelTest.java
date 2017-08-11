package com.chapter4;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class IsolationLevelTest {

	private Connection openConnection ()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:db/recClub.db");//If database does not exist then SQLite3 creates one and connects to it. If db already exists then sqlite3 connects to the database
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

	private void dirtyReadTest ()
	{
		Connection conn = openConnection();
		if (null != conn){
			try
			{
				conn.setAutoCommit(false);
				log ("default isolation level : " + conn.getTransactionIsolation() + " WHIch is : TRANSACTION_SERIALIZABLE");
				
				conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
				
				log ("isolation level : " + conn.getTransactionIsolation());
				
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery("SELECT COUNT (*) FROM customers");
				
				while (rs.next())
				{
					log ("Count from dirty read - " + rs.getInt(1));
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

	
	public static void main(String[] args) {
		new IsolationLevelTest().dirtyReadTest();
	}

}
