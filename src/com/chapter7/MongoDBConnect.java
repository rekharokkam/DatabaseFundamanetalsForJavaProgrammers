package com.chapter7;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.client.ListCollectionsIterable;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoIterable;

public class MongoDBConnect
{
	public static void main(String[] args)
	{
		MongoClient mongoClient = null;
		
		try{ // Not required to catch the exception. Doing it only for reference
			
			mongoClient = new MongoClient("localhost", 27017);//Connecting to default values
			
			MongoIterable<String> databaseNames = mongoClient.listDatabaseNames();
			
			MongoCursor<String> dbNames = databaseNames.iterator();
			
			while (dbNames.hasNext()){
				String dbName = dbNames.next();
System.out.printf("\nDatabase Name :: %s", dbName);	

				MongoDatabase db = mongoClient.getDatabase(dbName);
				
				ListCollectionsIterable<Document> collections = db.listCollections();
				for (Document doc : collections) {
				    System.out.println("\n\t Collection: " + doc.toString());
				}
			}
			
		}catch (Exception e){
			e.printStackTrace(System.err);
		}finally{
			if (null != mongoClient){
				mongoClient.close();
			}
		}
	}
}
