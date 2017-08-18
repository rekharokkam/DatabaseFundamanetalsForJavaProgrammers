package com.chapter5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table (name="sessions")
@SequenceGenerator (name="sessSeq", initialValue=30, allocationSize=75)
@NamedQuery (name="allSessions", query="SELECT r FROM Session r")
public class Session {

	/*
	 * One advantage of annotating the fields instead of methods - we can use fields in the JPQL directly even if the field is private
	 */
	
	@Id
	@Column (name="session_id")
	@GeneratedValue (generator="sessSeq", strategy=GenerationType.SEQUENCE)
	private Integer sessionId;
	
	@Column
	private String fromD;
	
	@Column
	private String toD;
	
	@Column (name="customer_id")
	private Integer customerId;
	
	@Column (name="activity_id")
	private Integer activityId;

	public String getFromD() {
		return fromD;
	}

	public void setFromD(String fromD) {
		this.fromD = fromD;
	}

	public String getToD() {
		return toD;
	}

	public void setToD(String toD) {
		this.toD = toD;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getActivityId() {
		return activityId;
	}

	public void setActivityId(Integer activityId) {
		this.activityId = activityId;
	}

	public Integer getSessionId() {
		return sessionId;
	}
	
	@Override
	public String toString ()
	{
		return "Session Id : " + getSessionId() + " :: from Date : " + getFromD() + " :: to Date : " + getToD() + " :: customer Id : " + getCustomerId() + " :: activity Id : " + getActivityId();
	}
	
}
