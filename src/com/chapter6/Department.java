package com.chapter6;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table (name="departments")
public class Department
{
	@Id
	@Column (name="department_id")
	@GeneratedValue (strategy=GenerationType.AUTO)
	private Long departmentId;
	
	@Column
	private String name;
	
	private List<Employee> employees;
	
	public Department (){
		employees = new ArrayList<Employee>();
	}
	
	public void addEmployee (Employee emp){
		employees.add(emp);
	}

	public Long getDepartmentId()
	{
		return departmentId;
	}

	public void setDepartmentId(Long departmentId)
	{
		this.departmentId = departmentId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@OneToMany (orphanRemoval = true, //At ORM level remove employees if department is removed 
			mappedBy = "department") //Annotated field in employee class
	public List<Employee> getEmployees()
	{
		return employees;
	}

	public void setEmployees(List<Employee> employees)
	{
		this.employees = employees;
	}
	
	@Override
	public String toString (){
		StringBuilder sb = new StringBuilder(5000);
		sb.append("Department Id : " + getDepartmentId()).append(" :: Name : ").append(getName());
		sb.append(" :: Employees length : " + employees.size ());
		
		return sb.toString();
	}
}
