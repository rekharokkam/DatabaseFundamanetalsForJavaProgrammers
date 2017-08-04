package com.chapter2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UpdateNDelete {

	private static final String UPDATE_PREP_STATEMENT = "UPDATE activities SET cost = (cost * ?) WHERE cost < ?"; 
	private static final String DELETE_PREP_STATEMENT = "DELETE FROM activities WHERE activity_id = ?"; 
	
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

	private void printRows (Connection conn, String message)
	{
		if (null != conn)
		{
			log(message);
			Statement stmt = null;
			ResultSet rs = null;
			try
			{
				stmt = conn.createStatement();
				rs = stmt.executeQuery("SELECT * FROM activities ORDER BY name;");
				
				while (rs.next())
				{
					log (String.format("activity - %s :: costs - %.2f", rs.getString("name"), rs.getBigDecimal("cost")));
				}
			}
			catch (Exception e)
			{
				logError(e);
			}
			finally
			{
				if (null != stmt){
					try
					{
						stmt.close();
					}catch (Exception e1)
					{
						//do nothing
					}
				}
				if (null != rs)
				{
					try
					{
						rs.close();
					}catch (Exception e2){
						//do nothing
					}
				}
			}
		}
	}

	private void printRows (Connection conn, String message, BigDecimal minCost)
	{
		if (null != conn)
		{
			log(message);
			PreparedStatement pstmt = null;
			ResultSet rs = null;
			try
			{
				pstmt = conn.prepareStatement("SELECT * FROM activities WHERE cost < ?");
				pstmt.setBigDecimal(1, minCost);
				rs = pstmt.executeQuery();
				
				while (rs.next())
				{
					log (String.format("activity - %s :: costs - %.2f", rs.getString("name"), rs.getBigDecimal("cost")));
				}
			}
			catch (Exception e)
			{
				logError(e);
			}
			finally
			{
				if (null != pstmt){
					try
					{
						pstmt.close();
					}catch (Exception e1)
					{
						//do nothing
					}
				}
				if (null != rs)
				{
					try
					{
						rs.close();
					}catch (Exception e2){
						//do nothing
					}
				}
			}
		}
	}

	private void deleteRow ()
	{
		Connection conn = openConnection();
		if (null != conn)
		{
			//print before update
			printRows(conn, "Before Deleting an activity");
			try
			{
				PreparedStatement pstmt = conn.prepareStatement(DELETE_PREP_STATEMENT);
				pstmt.setInt(1, 1);//increase the cost by 112%
				
				pstmt.execute();
				
				pstmt.close();
				
				printRows (conn, "\nAfter Deleting an activity");
			}
			catch (Exception e)
			{
				logError(e);
			}
			finally
			{
				closeConnection(conn);
			}
		}
	}

	private void updateRow ()
	{
		Connection conn = openConnection();
		if (null != conn)
		{
			BigDecimal minCost = new BigDecimal(12.99);
			BigDecimal costIncreasePercentage = new BigDecimal (1.12) ;
			
			//print before update
			printRows(conn, "Before updating activities", minCost);
			try
			{
				PreparedStatement pstmt = conn.prepareStatement(UPDATE_PREP_STATEMENT);
				
						
				pstmt.setBigDecimal(1, costIncreasePercentage);//increase the cost by 112%
				pstmt.setBigDecimal(2, minCost);
				
				pstmt.executeUpdate();
				
				pstmt.close();
				
				printRows(conn, "\nAfter updating activities", minCost);
			}
			catch (Exception e)
			{
				logError(e);
			}
			finally
			{
				closeConnection(conn);
			}
		}
	}

	private void demo ()
	{
//		updateRow();
		deleteRow();
	}
	
	public static void main(String[] args) {
		new UpdateNDelete().demo();
	}
}
