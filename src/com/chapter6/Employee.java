package com.chapter6;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table (name="employees")
public class Employee
{
	@Id
	@Column (name="employee_id")
	@GeneratedValue (strategy=GenerationType.AUTO)
	private Long employeeId;
	
	@Column
	private String name;
	
	private Department department;

	public Long getEmployeeId()
	{
		return employeeId;
	}

	public void setEmployeeId(Long employeeId)
	{
		this.employeeId = employeeId;
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	@ManyToOne (fetch = FetchType.LAZY) // to prevent fetching of all employees automatically
	@JoinColumn (name="department_id", table="departments", nullable = false) //primary key in the department table
	public Department getDepartment()
	{
		return department;
	}

	public void setDepartment(Department department)
	{
		this.department = department;
	}
	
	@Override
	public String toString () {
		return String.format("Employee Id : %d :: Name : %s ", getEmployeeId(), getName());
	}
}
