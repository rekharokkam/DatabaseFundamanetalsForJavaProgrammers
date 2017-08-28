package com.chapter7;

import java.math.BigDecimal;

import org.bson.Document;

public class Session
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final String SESSION_ID = "session_id";
	private static final String FROMD = "fromD";
	private static final String TOD = "toD";
	private static final String CUSTOMER_ID = "customer_id";
	private static final String ACTIVITY_ID = "activity_id";
	
	private Document document;
	private Integer sessionId;
	private String fromD;
	private String toD;
	private Integer customerId;
	private Integer activityId;
	
	public Session (){
		document = new Document ();
	}
	
	public Session (Integer sessionId, String fromD, String toD, Integer customerId, Integer activityId){
		this.sessionId = sessionId;
		this.fromD = fromD;
		this.toD = toD;
		this.customerId = customerId;
		this.activityId = activityId;
		

		document = new Document ();
		
		document.append(SESSION_ID, sessionId);
		document.append(FROMD, fromD);
		document.append(TOD, toD);
		document.append(CUSTOMER_ID, customerId);
		document.append(ACTIVITY_ID, activityId);
	}

	public Integer getSessionId()
	{
		return sessionId;
	}

	public void setSessionId(Integer sessionId)
	{
		this.sessionId = sessionId;
	}

	public String getFromD()
	{
		return fromD;
	}

	public void setFromD(String fromD)
	{
		this.fromD = fromD;
	}

	public String getToD()
	{
		return toD;
	}

	public void setToD(String toD)
	{
		this.toD = toD;
	}

	public Integer getCustomerId()
	{
		return customerId;
	}

	public void setCustomerId(Integer customerId)
	{
		this.customerId = customerId;
	}

	public Integer getActivityId()
	{
		return activityId;
	}

	public void setActivityId(Integer activityId)
	{
		this.activityId = activityId;
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
		return String.format("Session Id : %d :: From Date : %s :: To Date %d :: Customer Id : %d :: Activity Id : %d", 
				getActivityId(), getFromD(), getToD(), getCustomerId(), getActivityId());
	}
}
