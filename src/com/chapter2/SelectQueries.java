package com.chapter2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectQueries
{
	private static final String SIMPLE_SELECT_QUERY = "SELECT * FROM activities";
	private static final String FILTERED_SELECT_QUERY_CUSTOMER = "SELECT * FROM customers where customer_id > 20";
	private static final String FILTERED_SELECT_QUERY_ACTIVITY = "SELECT * FROM activities where cost between 45 and 60 and activity_id > 10";
	private static final String PROJECTED_SELECT_QUERY_ACTIVITY = "SELECT cost FROM activities";
	private static final String PROJECTED_SELECT_QUERY_SESSION = "SELECT session_id, fromD, toD FROM sessions";
	private static final String DISTINCT_SELECT = "SELECT DISTINCT toD from sessions";
	private static final String DISTINCT_SELECT_GROUP_BY = "SELECT DISTINCT toD from sessions GROUP BY activity_id";
	private static final String SUM_SELECT = "SELECT SUM (cost) as TOTAL from activities";
	private static final String COUNT_SELECT = "SELECT COUNT (customer_id) as TOTAL from customers";
	private static final String JOIN_SELECT = "SELECT c.name as CustomerName, a.name as ActivityName, a.cost, s.fromD, s.toD FROM customers c JOIN sessions s ON c.customer_id = s.customer_id"
			+ " JOIN activities a ON a.activity_id = s.activity_id";
	private static final String SUBQUERY_SELECT = "SELECT name FROM customers WHERE customer_id IN "
			+ "(SELECT customer_id FROM sessions WHERE activity_id IN (SELECT activity_id FROM activities WHERE name = 'swimming'))";
	
	private void demo ()
	{
//		simpleSelectQuery (); //all rows, all columns, 1 table
//		log ("\n");		
//		filteredSelectQueries (); // some rows, all columns, 1 table
//		log ("\n");	
//		projectSelectQueries (); //all rows, some columns, 1 table
//		log ("\n");	
//		miscFunctionsAndModifiers (); //sum, count, distinct, group by
//		log ("\n");	
//		miscJoins (); // rows/columns from more than 1 table
//		log ("\n");	
		subQuerySelect (); //subQuery example
	}
	
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
	
	private void simpleSelectQuery ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SIMPLE_SELECT_QUERY) ;
			
			while (rs.next())
			{
				int activity_id = rs.getInt("activity_id");
				String name = rs.getString("name");
				BigDecimal cost = rs.getBigDecimal("cost");
				
				log("Simple Select Query - Each Activity : " + String.format("%d %s $%.2f", activity_id, name, cost));		
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
	
	
	//Some columns and all rows is called Projections
	private void projectSelectQueries ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(PROJECTED_SELECT_QUERY_ACTIVITY) ;
			
			while (rs1.next())
			{
				BigDecimal cost = rs1.getBigDecimal("cost");
				
				log("Projected Query - Each Activity : " + String.format("%5.2f", cost));		
			}
			
			ResultSet rs2 = stmt.executeQuery(PROJECTED_SELECT_QUERY_SESSION) ;
			while (rs2.next())
			{
				int session_id = rs2.getInt("session_id");
				String from = rs2.getString("fromD");
				String to = rs2.getString("toD");
				log ("Projected Query - Each Session : " + String.format("%2d %s %s", session_id, from, to));
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

	private void filteredSelectQueries ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(FILTERED_SELECT_QUERY_CUSTOMER) ;
			
			while (rs1.next())
			{
				int customer_id = rs1.getInt("customer_id");
				String name = rs1.getString("name");
				
				log("Filtered Query - Each customer : " + String.format("%d %s", customer_id, name));		
			}
			
			ResultSet rs2 = stmt.executeQuery(FILTERED_SELECT_QUERY_ACTIVITY);
			
			while (rs2.next())
			{
				int activity_id = rs2.getInt("activity_id");
				String name = rs2.getString("name");
				BigDecimal cost = rs2.getBigDecimal("cost");
				
				log (String.format("Filtered Query - Each activity : " + "%d %s $%.2f", activity_id, name, cost));
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

	private void miscFunctionsAndModifiers ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs1 = stmt.executeQuery(DISTINCT_SELECT) ;
			
			while (rs1.next())
			{
				String toDate = rs1.getString("toD");
				log("Distinct Query - To date  : " + String.format("%s", toDate));		
			}
			
			ResultSet rs2 = stmt.executeQuery(DISTINCT_SELECT_GROUP_BY);
			
			while (rs2.next())
			{
				String toDate = rs2.getString("toD");
				log("Distinct Query GROUP BY - To date  : " + String.format("%s", toDate));	
			}
			
			ResultSet rs3 = stmt.executeQuery(SUM_SELECT) ;
			
			while (rs3.next())
			{
				BigDecimal costSum = rs3.getBigDecimal("TOTAL");
				log("SUM of activities cost  : " + String.format("%f", costSum));		
			}

			ResultSet rs4 = stmt.executeQuery(COUNT_SELECT) ;
			
			while (rs4.next())
			{
				int countCustomers = rs4.getInt("TOTAL");
				log("COUNT Query - total Customers  : " + String.format("%d", countCustomers));		
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
	
	private void miscJoins ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(JOIN_SELECT) ;
			
			while (rs.next())
			{
				String customerName = rs.getString("CustomerName");
				String activityName = rs.getString("ActivityName");
				BigDecimal activityCost = rs.getBigDecimal("cost");
				String from = rs.getString("fromD");
				String to = rs.getString("toD");
				log("JOIN Select Query - Each row : " + String.format("%s %s $%.2f %s %s", customerName, activityName, activityCost, from, to));		
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

	private void subQuerySelect ()
	{
		Connection conn = openConnection();
		try
		{
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery(SUBQUERY_SELECT) ;
			
			while (rs.next())
			{
				String name = rs.getString("name");
				
				log("SUBQUERY Select - Each Customer playing 'go' : " + String.format("%s", name));		
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

	public static void main(String[] args)
	{
		new SelectQueries().demo();
	}
}
