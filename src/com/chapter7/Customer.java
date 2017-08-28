package com.chapter7;

import org.bson.Document;

public class Customer
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String CUSTOMER_ID = "customer_id";
	private static final String NAME = "name";
	
	private Document document;
	private Integer customerId;
	private String name;
	
	public Customer (){
		document = new Document ();
	}
	
	public Customer (Integer customerId, String name){
		this.customerId = customerId;
		this.name = name;

		document = new Document ();
		
		document.append(CUSTOMER_ID, customerId);
		document.append(NAME, name);
	}

	public Integer getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Integer customerId)
	{
		this.customerId = customerId;
		document.append(CUSTOMER_ID, customerId);
	}

	public String getName()
	{
		return name;
	}

	public void setName(String name)
	{
		this.name = name;
		document.append(NAME, name);
	}
	
	public Document getDocument()
	{
		return document;
	}

	public void setDocument(Document document)
	{
		this.document = document;
	}

	@Override
	public String toString (){
		return String.format("Customer Id : %d :: Customer Name : %s", getCustomerId(), getName());
	}
}
