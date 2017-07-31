package com.chapter2;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Random;

public class CreateNPopulateTables
{
	private static final String CREATE_CUSTOMERS_TABLE = "DROP TABLE IF EXISTS customers; CREATE TABLE customers (customer_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (24) NOT NULL)";// "if exists" is not standard, but SQLite supports it
	private static final String CREATE_ACTIVITIES_TABLE = "";
	private static final String CREATE_SESSIONS_TABLE = "";
	
	private static final String CUSTOMERS_INSERT_TEMPLATE = "INSERT INTO customers (name) values (?)";
	private static final String ACTIVITIES_INSERT_TEMPLATE = "";
	private static final String SESSIONS_INSERT_TEMPLATE = "";
	
	private static final String[] fakeNames = {	"Bob", "Carol", "Ted", "Alice",
			"Moe", "Curly", "Larry",
			"Fred", "Wilma", "Pebbles", "Barney", "Betty",
			"Lou", "Bud", "Gracie", "Lucy",
			"Homer", "Marge", "Selma", "Patty", "Lisa",
			"Piggly", "Wiggly"
	};
	private static final String[] fakeActivities = {	"squash", "3-on-3 basketball", "tiddly winks", "tennis",
			"basic spinning", "intermediate spinning", "insane spinning",
			"swimming", "water polo", "diving", 
			"rock climbing", "skate boarding",
			"chess", "go", "checkers", 
			"judo", "akido", "boxing", "wrestling", "extreme fighting"
	}; 
	private static final Random random = new Random();
	private static final int TOTAL_CUSTOMERS_TO_BE_POPULATED = 32;
		
	private Connection openConnection ()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:recClub.db");
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
	}
	
	private void createTable (String createTableSQL)
	{
		Connection conn = openConnection();
		if (null != conn){
			try
			{				
				Statement stmt = conn.createStatement();				
				stmt.executeUpdate(createTableSQL);
				
			}catch (Exception e)
			{
				logError(e);
			}finally {
				closeConnection(conn);
			}
		}
	}
	
	private void populateCustomers ()
	{
		Connection conn = openConnection();
		if (null != conn){
			try
			{
				PreparedStatement pstmt = conn.prepareStatement(CUSTOMERS_INSERT_TEMPLATE);
				
				for (int i = 0; i < TOTAL_CUSTOMERS_TO_BE_POPULATED; i ++)
				{				
					pstmt.setString(1, fakeNames[random.nextInt(fakeNames.length)] + (i + 1));
					
					int rowCount = pstmt.executeUpdate();					
//					log ("Number of customers inserted are : " + rowCount);
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
	
	private void demo ()
	{
		createTable(CREATE_CUSTOMERS_TABLE);
		log ("Successfully Created table customers");
		
		populateCustomers ();
		log ("Customers successfully populated");
	}
	
	public static void main(String[] args)
	{
		new CreateNPopulateTables().demo();
	}
}
