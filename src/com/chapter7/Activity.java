package com.chapter7;

import org.bson.Document;

public class Activity
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String ACTIVITY_ID = "activity_id";
	private static final String NAME = "name";
	private static final String COST = "cost";
	
	private Document document;
	private Integer activityId;
	private String name;
	private float cost;
	
	public Activity (){
		document = new Document ();
	}
	
	public Activity (Integer activityId, String name, float cost){
		this.activityId = activityId;
		this.name = name;
		this.cost = cost;

		document = new Document ();
		
		document.append(ACTIVITY_ID, activityId);
		document.append(NAME, name);
		document.append(COST, cost);
	}

	public Integer getActivityId()
	{
		return activityId;
	}

	public void setActivityId(Integer activityId)
	{
		this.activityId = activityId;
		document.append(ACTIVITY_ID, activityId);
	}

	public float getCost()
	{
		return cost;
	}

	public void setCost(float cost)
	{
		this.cost = cost;
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
		return String.format("Activity Id : %d :: Activity Name : %s :: Activity Cost %d", getActivityId(), getName(), getCost());
	}
}
