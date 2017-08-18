package com.chapter5;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NamedQuery;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PersistenceManagerActivity {

	private EntityManagerFactory emf;
	
	public PersistenceManagerActivity ()
	{
		emf = Persistence.createEntityManagerFactory("ActivityPU");
	}
	
	private EntityManager getEntityManager ()
	{
		if (null == emf){
			emf = Persistence.createEntityManagerFactory("ActivityPU");
		}
		return emf.createEntityManager();
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

	/*
	 * A test I added to test if i could add more than 1 persistence-unit in persistence.xml file
	 */
	private void addActivity ()
	{
		EntityManager em = getEntityManager();
		
		Activity activity = new Activity ();
		activity.setCost(new BigDecimal(14.99));
		activity.setName("Embossing");
		
		if (! em.contains(activity))
		{
			em.getTransaction().begin();
			try
			{
				//Generated value for ID gets added during this method invocation.
				em.persist(activity);				
				log ("Activity that will be added : " + activity.toString());
				
				em.flush();

				em.getTransaction().commit();
			}
			catch (PersistenceException e)
			{				
				logError(e);
				em.getTransaction().rollback();
			}			
		}
		
		em.close();
	}
	
//	Simple QL[JPQL] Select query
	private void simpleSelectQuery ()
	{
		EntityManager em = getEntityManager(); // Can be used for more than one db operation similar to Connection Object in Core Java
		
		Query simpleSelectQuery = em.createQuery("SELECT r FROM Activity r"); //'AS' keyword can be omitted.
		List<Activity> allActivities = simpleSelectQuery.getResultList();
		
		for (Activity activity : allActivities){
			log (activity.toString());
		}
		
		em.close();
	}
	
	//Select Just One Activity - filter condition
	private void selectOneRowQuery ()
	{
		EntityManager em = getEntityManager();
		
		Query selectOneRowQuery = em.createQuery("SELECT r FROM Activity r WHERE r.activityId = 1", Activity.class);
		Object singleActivity = selectOneRowQuery.getSingleResult();
		
		log (singleActivity.toString());
	}
	
	//Select Just One Activity that does not exist - filter condition
	private void selectOneNonExistentRowQuery ()
	{
		EntityManager em = getEntityManager();
		
		Query selectOneNonExistentRowQuery = em.createQuery("SELECT r FROM Activity r WHERE r.activityId = 999");
		try
		{
			Object singleActivity = selectOneNonExistentRowQuery.getSingleResult();// Throws error -  javax.persistence.NoResultException: getSingleResult() did not retrieve any entities.So this call should be wrapped in try catch block
			log (singleActivity.toString());
		}
		catch (NoResultException e)
		{
			log ("There is no result for the activityId : 999");
			logError(e);
		}
	}
	
	
	//Type Safe queries - First approach to parameterized queries
	private void firstParameterizedQuery ()
	{
		EntityManager em = getEntityManager();
		
		TypedQuery<Activity> typedQuery = em.createQuery("SELECT r FROM Activity r WHERE r.cost > ?1", Activity.class);//2nd argument is the return type
		typedQuery.setParameter(1, new BigDecimal(20.99));
		
		List<Activity> activities = typedQuery.getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();
	}

	//Type Safe queries - Second approach to parameterized queries
	private void secondParameterizedQuery ()
	{
		EntityManager em = getEntityManager();
		
		TypedQuery<Activity> typedQuery = em.createQuery("SELECT r FROM Activity r WHERE r.cost > :cost", Activity.class);//2nd argument is the return type
		typedQuery.setParameter("cost", new BigDecimal(20.99));
		
		List<Activity> activities = typedQuery.getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();
	}
	
	private void withoutReferenceQuery ()
	{
		EntityManager em = getEntityManager();
		
		List<Activity> activities = em.
				createQuery("SELECT r FROM Activity r WHERE r.cost > :cost").
				setParameter("cost", new BigDecimal(20.99)).
				getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();
	}
	
	private void namedQuerySelect ()
	{
		EntityManager em = getEntityManager();
		
		TypedQuery<Activity> namedQuery = em.createNamedQuery("getAllActivities", Activity.class);
		List<Activity> activities = namedQuery.getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();
	}
	
	//projection example - fetching only one field
	private void oneFieldProjection ()
	{
		EntityManager em = getEntityManager();
		
		Query oneFieldProjection = em.createQuery("SELECT r.name FROM Activity r", String.class);
		
		List<String> activityNames = oneFieldProjection.getResultList();
		
		for (String eachActivityName : activityNames){
			log (eachActivityName);
		}
		
		em.close ();
	}
	
	//projection example - fetching more than one field
	private void manyFieldsProjection ()
	{
		EntityManager em = getEntityManager();
		
		Query manyFieldsProjection = em.createQuery("SELECT r.name, r.cost FROM Activity AS r");
		
		List<Object[]> activityAttributes = manyFieldsProjection.getResultList();
		
		for (Object[] eachActivityAttributes : activityAttributes){
			for (Object eachAttribute : eachActivityAttributes){
System.out.print ("\t" + eachAttribute);				
			}
			log ("\n");
		}
		
		em.close ();
	}
	
	
	private void demo ()
	{
		addActivity(); //This method is only for testing multiple persistence-unit in the persistence.xml file
		log ("An activity is added");
		
//		simpleSelectQuery ();
		
//		selectOneRowQuery ();
		
//		selectOneNonExistentRowQuery ();
		
//		firstParameterizedQuery ();
		
//		secondParameterizedQuery ();
		
//		withoutReferenceQuery ();
		
//		namedQuerySelect ();
		
//		oneFieldProjection ();
		
//		manyFieldsProjection ();
	}
	
	public static void main(String[] args) {
		new PersistenceManagerActivity().demo();
	}
}
