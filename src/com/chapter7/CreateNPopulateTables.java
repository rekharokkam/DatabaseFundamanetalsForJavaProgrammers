package com.chapter7;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.bson.Document;

import com.mongodb.MongoClient;

public class CreateNPopulateTables
{
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
	
	private static final String DB_NAME = "recClub";
	
	private static final String CUSTOMER_TABLE = "customers";
	private static final String ACTIVITY_TABLE = "activities";
	private static final String SESSION_TABLE = "sessions";
	
	private AtomicInteger customerAtomicInteger ;
	private AtomicInteger activityAtomicInteger ;
	private AtomicInteger sessionAtomicInteger ;
	
	public CreateNPopulateTables()
	{
		customerAtomicInteger = new AtomicInteger();//initialized to zero
		activityAtomicInteger = new AtomicInteger();
		sessionAtomicInteger = new AtomicInteger();
	}
	
	private MongoClient getConnection ()
	{
		try
		{
			return new MongoClient("localhost", 27017);
			
		}catch (Exception e) 
		{
			logError(e);
			return null;
		}
	}

	private void closeConnection (MongoClient conn)
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
	
	// { "_id" : ObjectId("5810f82379e6a6564f06472f"), "customer_id" : 1, "name" : "Homer1" }
	private void populateONECustomer (Customer customer, MongoClient mongoClient) {
		
		mongoClient.getDatabase(DB_NAME).getCollection(CUSTOMER_TABLE).
			insertOne(customer.getDocument());
	}
	
	private void populateCustomers (MongoClient mongoClient)
	{
		//Create a bunch of customers
		List<Document> customers = new ArrayList<Document>();
		for (int i = 0; i < TOTAL_CUSTOMERS_TO_BE_POPULATED; i ++){
			Customer customer = new Customer (customerAtomicInteger.incrementAndGet(), fakeNames[random.nextInt(fakeNames.length)] + (i + 1));
			customers.add(customer.getDocument());
		}
		
		mongoClient.getDatabase(DB_NAME).getCollection(CUSTOMER_TABLE).
			insertMany(customers);;
	}
	
	private void populateActivities (MongoClient mongoClient)
	{
		List<Document> activities = new ArrayList<Document>();
		for (int i = 0; i < fakeActivities.length; i ++)
		{	
			Activity activity = new Activity (activityAtomicInteger.incrementAndGet(), fakeActivities[random.nextInt(fakeActivities.length)],
					(random.nextFloat() + 1.1f) * fakeWeights[random.nextInt(fakeWeights.length)]);
			activities.add(activity.getDocument());
		}
		mongoClient.getDatabase(DB_NAME).getCollection(ACTIVITY_TABLE).
			insertMany(activities);
	}
	
	private void populateSessions (MongoClient mongoClient)
	{	
		List<Document> sessions = new ArrayList<Document>();
		
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
			
			int customerId = random.nextInt(TOTAL_CUSTOMERS_TO_BE_POPULATED);
			int activityId = random.nextInt(fakeActivities.length);
			Session session = new Session (sessionAtomicInteger.incrementAndGet(), fromStr, toStr, 
					((customerId == 0) ? 1 : customerId), ((activityId == 0) ? 1 : activityId));
			
			sessions.add(session.getDocument());
		}
		
		mongoClient.getDatabase(DB_NAME).getCollection(SESSION_TABLE).
			insertMany(sessions);
	}
	
	private void demo ()
	{
		MongoClient mongoClient = getConnection();
		try{
			Customer customer = new Customer (customerAtomicInteger.incrementAndGet(), "FirstCustomer");
			populateONECustomer (customer, mongoClient);
//			
			populateCustomers(mongoClient);
//
//			log ("populated Customers");
//			
//			populateActivities (mongoClient);
//			
//			log ("populated Activities");
			
//			populateSessions(mongoClient);
//			log ("Populated Sessions");
			
		}catch (Exception exception){
			logError(exception);
		}finally{
			if (null != mongoClient){
				closeConnection(mongoClient);
			}
		}
	}
	
	public static void main(String[] args)
	{
		new CreateNPopulateTables().demo();
	}}
