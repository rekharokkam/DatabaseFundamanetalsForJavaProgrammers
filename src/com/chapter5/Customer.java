package com.chapter5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table (name="customers")
@SequenceGenerator (name="custSeq", initialValue=30, allocationSize=50)
public class Customer {

	@Id
	@Column (name="customer_id")
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator="custSeq")
	private Integer customerId;
	
	@Column
	private String name;

	public Integer getCustomerId() {
		return customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString ()
	{
		return String.format("Customer Id : %d :: Customer Name : %s", customerId, name);
	}	
}
