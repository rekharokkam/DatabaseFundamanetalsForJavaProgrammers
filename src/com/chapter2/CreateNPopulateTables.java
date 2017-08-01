package com.chapter2;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class CreateNPopulateTables
{
	private static final String CREATE_CUSTOMERS_TABLE = "DROP TABLE IF EXISTS customers; CREATE TABLE customers (customer_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (24) NOT NULL)";// "if exists" is not standard, but SQLite supports it
	private static final String CREATE_ACTIVITIES_TABLE = "DROP TABLE IF EXISTS activities; CREATE TABLE activities (activity_id INTEGER PRIMARY KEY AUTOINCREMENT, name VARCHAR (24) NOT NULL, cost DECIMAL not null)";
	private static final String CREATE_SESSIONS_TABLE = "DROP TABLE IF EXISTS sessions; CREATE TABLE sessions (session_id INTEGER PRIMARY KEY AUTOINCREMENT, fromD DATE NOT NULL, toD DATE NOT NULL, "
			+ "customer_id INTEGER, activity_id INTEGER, FOREIGN KEY (customer_id) REFERENCES customers(customer_id), FOREIGN KEY (activity_id) REFERENCES activities (activity_id) )";
	
	private static final String CUSTOMERS_INSERT_TEMPLATE = "INSERT INTO customers (name) values (?)";
	private static final String ACTIVITIES_INSERT_TEMPLATE = "INSERT INTO activities (name, cost) values (?, ?)";
	private static final String SESSIONS_INSERT_TEMPLATE = "INSERT INTO sessions (fromD, toD, customer_id, activity_id) values (?, ?, ?, ?)";
	
	private static final String[] fakeNames = {	"Bob", "Carol", "Ted", "Alice",
			"Moe", "Curly", "Larry",
			"Fred", "Wilma", "Pebbles", "Barney", "Betty",
			"Lou", "Bud", "Gracie", "Lucy",
			"Homer", "Marge", "Selma", "Patty", "Lisa",
			"Piggly", "Wiggly"
	};
	private static final int TOTAL_CUSTOMERS_TO_BE_POPULATED = 32;
	
	private static final String[] fakeActivities = {	"squash", "3-on-3 basketball", "tiddly winks", "tennis",
			"basic spinning", "intermediate spinning", "insane spinning",
			"swimming", "water polo", "diving", 
			"rock climbing", "skate boarding",
			"chess", "go", "checkers", 
			"judo", "akido", "boxing", "wrestling", "extreme fighting"
	}; 
	private static final float[ ] fakeWeights = {6.2f, 12.4f, 24.8f, 31.0f}; // to get some variety in the pricing
	
	private static final int[ ] fakeDayDurs = {3,4,5,6,7,9,11,14,17,21,23,25};  // durations in days
	
	private static final Random random = new Random();		
		
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
e.printStackTrace(System.err);
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
	
	private void populateActivities ()
	{
		float[ ] fakeWeights = {6.2f, 12.4f, 24.8f, 31.0f}; // to get some variety in the pricing
		
		Connection conn = openConnection();
		if (null != conn){
			try
			{
				PreparedStatement pstmt = conn.prepareStatement(ACTIVITIES_INSERT_TEMPLATE);
				
				for (int i = 0; i < fakeActivities.length; i ++)
				{				
					pstmt.setString(1, fakeActivities[random.nextInt(fakeActivities.length)]);
					
					float costF = (random.nextFloat() + 1.1f) * fakeWeights[random.nextInt(fakeWeights.length)];
					pstmt.setFloat(2, costF);// same as pstmt.setBigDecimal (2, newBigdecimal (costF));
					
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
	
	private void populateSessions ()
	{	
		Connection conn = openConnection();
		if (null != conn){
			try
			{
				PreparedStatement pstmt = conn.prepareStatement(SESSIONS_INSERT_TEMPLATE);
				long from = new Date().getTime();
				long to;
				
				TimeUnit tunit = TimeUnit.MILLISECONDS;
								
				for (int i = 0; i < 64; i ++)
				{	
					int plusDays = fakeDayDurs[random.nextInt(fakeDayDurs.length)];
					long plusMillis = tunit.convert(plusDays, TimeUnit.DAYS);
					to = from + plusMillis;
							
					String fromStr = new Date (from).toString().substring(0, 10);
					String toStr = new Date (to).toString().substring(0, 10);
					
					pstmt.setString(1, fromStr);
					pstmt.setString(2, toStr);
					pstmt.setInt(3, random.nextInt(TOTAL_CUSTOMERS_TO_BE_POPULATED));
					pstmt.setInt(4, random.nextInt(fakeActivities.length));
					
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
		log ("customers table Successfully Created ");
		
		populateCustomers ();
		log ("Customers successfully populated");
		
		createTable (CREATE_ACTIVITIES_TABLE);
		log ("activities table successfully created");
		
		populateActivities();
		log ("Activities successfully populated");
		
		createTable(CREATE_SESSIONS_TABLE);
		log ("sessions table successfully Created");		
		
		populateSessions();
		log ("Sessions successfully populated");
	}
	
	public static void main(String[] args)
	{
		new CreateNPopulateTables().demo();
	}
}
