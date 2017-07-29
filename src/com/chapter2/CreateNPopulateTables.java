package com.chapter2;

import java.sql.Connection;
import java.sql.DriverManager;

public class CreateNPopulateTables
{
	private static final String CREATE_CUSTOMERS_TABLE = "";
	private static final String CREATE_ACTIVITIES_TABLE = "";
	private static final String CREATE_SESSIONS_TABLE = "";
	
	private static final String CUSTOMERS_INSERT_TEMPLATE = "";
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
	
	
	private Connection openConnection ()
	{
		try
		{
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection("jdbc:sqlite:recClub.db");
		}catch (Exception e) {
System.out.println(e.getClass().getName() + " : " + e.getMessage());
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
System.err.println(e.getClass().getName() + " : " + e.getMessage());
				System.exit(-1);
			}
		}
	}
	
	private void demo ()
	{
		
	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
