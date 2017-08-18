package com.chapter5;

import java.util.Arrays;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.sun.xml.internal.bind.v2.runtime.unmarshaller.XsiNilLoader.Array;

public class PersistenceManagerCAS {
	
	private EntityManagerFactory emf;
	
	public PersistenceManagerCAS ()
	{
		emf = Persistence.createEntityManagerFactory("recClubPU");
	}
	
	public EntityManager getEntityManager ()
	{
		if (null == emf){
			emf = Persistence.createEntityManagerFactory("recClubPU");
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

	private void nativeSQLQuery ()
	{
		EntityManager em = getEntityManager();
		
		String nativeSQL = "SELECT * FROM activities WHERE cost BETWEEN 45 AND 60";
		Query nativeQuery = em.createNativeQuery(nativeSQL, Activity.class);
		
		List<Activity> activities = nativeQuery.getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();		
	}

	private void namedQuerySelect ()
	{
		EntityManager em = getEntityManager();
		
		TypedQuery<Session> namedSessionQuery = em.createNamedQuery("allSessions", Session.class);
		List<Session> sessions = namedSessionQuery.getResultList();
				
		for (Session session : sessions){
			log (session.toString());
		}
		
		em.close();		
	}

	private void implicitJOINQuery ()
	{
		EntityManager em = getEntityManager();
		
		Query implicitJOINQuery = em.createQuery("SELECT c.name FROM Customer c, Session s WHERE s.customerId = c.customerId and c.customerId > 20", String.class);//customers who are registered for sessions and customer id > 20
		
		List<String> customerNames = implicitJOINQuery.getResultList();
		
		for (String customerName : customerNames){
			log (customerName);
		}
		
		em.close();		
		
	}
	
	private void parametarisedQueryWithIN (){
		EntityManager em = getEntityManager();
		
		String parameterizedSelect = "SELECT a FROM Activity AS a WHERE a.name IN :namesArray";
		Query parameterizedQuery = em.createQuery(parameterizedSelect, Activity.class);
		parameterizedQuery.setParameter("namesArray", Arrays.asList("go", "judo", "akito"));
		
		List<Activity> activities = parameterizedQuery.getResultList();
		
		for (Activity activity : activities){
			log (activity.toString());
		}
		
		em.close();		
	}

	private void explicitJOINQuery ()
	{
		EntityManager em = getEntityManager();
		
		Query implicitJOINQuery = em.createQuery("SELECT c.name FROM Customer c JOIN Session s ON s.customerId = c.customerId and c.customerId > 20", String.class);//customers who are registered for sessions and customer id > 20
		
		List<String> customerNames = implicitJOINQuery.getResultList();
		
		for (String customerName : customerNames){
			log (customerName);
		}
		
		em.close();		
	}

	private void updateCustomer ()
	{
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		Customer customer = em.find(Customer.class, 1); //
		
		if (null != customer){
			log ("Found Customer so updating : " + customer);
			customer.setName("LucyUpdated");
			
			em.getTransaction().commit();//Implies Flush
		}
		
		em.close();
	}

	private void deleteCustomer ()
	{
		EntityManager em = getEntityManager();
		
		em.getTransaction().begin();
		Customer customer = em.find(Customer.class, 34); 
		
		if (null != customer){
			log ("Found Customer so deleting : " + customer);
			em.remove(customer);
			em.getTransaction().commit();//Implies Flush
		}
		
		em.close();
	}

	private void demo (){
		
//		nativeSQLQuery();
		
//		namedQuerySelect ();
		
//		implicitJOINQuery ();
		
//		parametarisedQueryWithIN ();
		
//		explicitJOINQuery ();
		
//		updateCustomer();
		
		deleteCustomer ();
	}
	
	public static void main(String[] args) {
		new PersistenceManagerCAS().demo();
	}
}
