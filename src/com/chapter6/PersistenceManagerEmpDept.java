package com.chapter6;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class PersistenceManagerEmpDept
{
	private static final String[ ] EMPLOYEES = {"Bob", "Carol", "Ted", "Alice", "Fred", "Wilma", "Barney", "Betty"};
	private static final String[ ] DEPARTMENT = {"Computing"};
	
	private EntityManagerFactory emf;
	
	public PersistenceManagerEmpDept (){
		emf = Persistence.createEntityManagerFactory("empDeptPU");
	}
	
	private void demo () {
//		loadEmployeesNDepartment ();
		
		fetchDeptNEmployees ();
	}
	
	private void fetchDeptNEmployees (){
		EntityManager em = getEntityManager();
		
		Department department = em.getReference(Department.class, 151L); //Lighter weight. First checks in the context if available fetches from there
System.out.println(department);		

		em.close();
	}
	
	private void loadEmployeesNDepartment (){
		EntityManager em = getEntityManager();
		
		Department department = new Department ();
		department.setName(DEPARTMENT[0]);
		
		Employee[] employees = new Employee[EMPLOYEES.length];
		
		for (int i = 0; i < EMPLOYEES.length; i++)
		{
			Employee employee = new Employee ();
			employee.setDepartment(department);
			
			employee.setName(EMPLOYEES[i]);
			
			department.addEmployee(employee);
			
			employees[i] = employee;
		}
		
		try{
			em.getTransaction().begin();
			
			if (! em.contains(department)){
				em.persist(department);
				
				for (Employee employee : employees){
					em.persist(employee);
				}
			}
			
			em.getTransaction().commit();//Implies flush ()
		}catch (Exception exception){ //No need of try and catch block. I am explicitly catching to see errors
exception.printStackTrace(System.err);			
		}
		
		em.close();
	}
	
	private EntityManager getEntityManager (){
		if (null == emf){
			emf = Persistence.createEntityManagerFactory("empDeptPU");
		}
		return emf.createEntityManager();
	}
	
	public static void main(String[] args)
	{
		new PersistenceManagerEmpDept().demo();
	}
}
