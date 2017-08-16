package com.chapter5;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

public class PersistenceManager {

	private EntityManagerFactory emf;
	
	public PersistenceManager ()
	{
		emf = Persistence.createEntityManagerFactory("ProductPU");
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
	
	private EntityManager getEntityManager ()
	{
		if (null == emf)
		{
			emf = Persistence.createEntityManagerFactory("ProductPU"); // ProductPU - persistence context. Application level persistence context
		}
		return emf.createEntityManager();
	}
	
	private void populateProduct ()
	{
		EntityManager em = getEntityManager();
		
		Product product1 = new Product ();
		product1.setProductName("Super stuff 5");

		if (! em.contains(product1)) // EntityManager maintains a list of things to persist
		{
			em.persist(product1); // Add product to the list of objects to persist
		}

		Product product2 = new Product ();
		product2.setProductName("Super stuff 6");

		if (! em.contains(product2)) // EntityManager maintains a list of things to persist
		{
			em.persist(product2); // Add product to the list of objects to persist
		}
		
		em.getTransaction().begin(); // Create Transaction. Without transaction persistence is not possible as an error is thrown
		
		//methods persist and flush will always occur in the context of explicit transaction
		try
		{			
			em.flush(); //Sync the list with DB. Persist the data in DB
		}
		catch (PersistenceException e) // It's not mandatory to catch this exception. But I am catching it for logging purposes in case of any error
		{
			logError(e);
		}
		em.getTransaction().commit();
		
		em.close();
	}
	
	private void fetchProducts ()
	{
		EntityManager em = getEntityManager();
		
		Query productsQuery = em.createQuery("select p from Product p"); // This is QL not SQL
		List<Product> products = productsQuery.getResultList();
		
		for (Product product : products)
		{
			log (product.toString());
		}
		
		em.close();
	}
	
	private void demo ()
	{
		populateProduct();
		log ("Populated some products");
		
		fetchProducts();
	}
	
	public static void main(String[] args) {
		new PersistenceManager().demo();
	}
}
