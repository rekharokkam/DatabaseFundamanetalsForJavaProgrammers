package com.chapter7;

import java.util.Scanner;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCursor;

public class MongoCRUDSupport
{
	private static final String DB_NAME = "recClub";
	private static final String CUSTOMER_TABLE = "customers";

	private Scanner scanner;
	
	public MongoCRUDSupport()
	{
		scanner = new Scanner (System.in);
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
	
	private void createCustomer (MongoClient mongoClient ){
		Customer customer = new Customer (51, "Jumbalaya");
		mongoClient.getDatabase(DB_NAME).getCollection(CUSTOMER_TABLE).
			insertOne(customer.getDocument());
	}
	
	private void getAllCustomers (MongoClient mongoClient){
		
		FindIterable<Document> allCustomers = mongoClient.getDatabase(DB_NAME).
				getCollection(CUSTOMER_TABLE).find();
		MongoCursor<Document> allCustomersIt = allCustomers.iterator();
		while (allCustomersIt.hasNext()){
			log( allCustomersIt.next().toString());			
		}
	}
	
	private void getOneCustomer (MongoClient mongoClient){
		Customer customer = new Customer ();
		customer.setCustomerId(51);
		FindIterable<Document> oneCustomer = mongoClient.getDatabase(DB_NAME).
			getCollection(CUSTOMER_TABLE).find(customer.getDocument());
		oneCustomer.forEach(new Block<Document>(){

			/* (non-Javadoc)
			 * @see com.mongodb.Block#apply(java.lang.Object)
			 */
			@Override
			public void apply(Document document)
			{
				log(document.toJson());				
			}
		});
	}
	
	private void getRegExFilteredCustomers(MongoClient mongoClient){
		Document regExQuery = new Document ("name", Pattern.compile("Piggly", Pattern.CASE_INSENSITIVE));
		FindIterable<Document> regExFilteredCustomers = mongoClient.getDatabase(DB_NAME).
			getCollection(CUSTOMER_TABLE).find(regExQuery);
		
		MongoCursor<Document> regExFilteredCustomersIt = regExFilteredCustomers.iterator();
		while (regExFilteredCustomersIt.hasNext()){
			log(regExFilteredCustomersIt.next().toJson());			
		}
	}
	
	private void updateCustomer (MongoClient mongoClient){
		Customer oldCustomer = new Customer ();
		oldCustomer.setCustomerId(51);
		Document toBeUpdatedCustomer = new Document ("$set", new Document ("name", "newJumbalaya"));
		
		mongoClient.getDatabase(DB_NAME).getCollection(CUSTOMER_TABLE).updateOne(oldCustomer.getDocument(), toBeUpdatedCustomer);
	}
	
	private void deleteCustomer (MongoClient mongoClient) {
		
		Customer customer = new Customer ();
		customer.setCustomerId(51);

		mongoClient.getDatabase(DB_NAME).getCollection(CUSTOMER_TABLE).deleteOne(customer.getDocument());
	}
	
	private void demo ()
	{
		MongoClient mongoClient = getConnection();
		try{
			pause ("Creating new Customer \"Jumbalaya\"");
			
			createCustomer(mongoClient);
			
			pause ("Get All the customers");
			
			getAllCustomers(mongoClient);
			
			pause ("Get Only one customer");
			
			getOneCustomer(mongoClient);
			
			pause ("Get Customers matching a pattern");
			
			getRegExFilteredCustomers(mongoClient);
			
			pause ("Updating the customer JUmabalaya");
			
			updateCustomer (mongoClient);
			
			pause ("Get all customers");
			
			getAllCustomers(mongoClient);
			
			pause ("Deleting the customer Jumbalaya");
			
			deleteCustomer(mongoClient);
			
			pause ("Get All Customers to verify");
			
			getAllCustomers(mongoClient);
			
		}catch (Exception exception){
			logError(exception);
		}finally{
			if (null != mongoClient){
				closeConnection(mongoClient);
			}
		}
	}

	private void pause(String message) {
		log("\n" + message + "\tPress Enter to continue\n");
		scanner.nextLine ();
	}
	
	public static void main(String[] args)
	{
		new MongoCRUDSupport().demo();
	}
}
