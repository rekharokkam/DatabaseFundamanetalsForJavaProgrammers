package com.chapter5;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@SequenceGenerator (name="actSeq", initialValue = 21, allocationSize=100)
@Table (name="activities")
@NamedQuery (name="getAllActivities", query="SELECT r FROM Activity r")
public class Activity {
	
	@Id //Indicates primary key
	@Column (name="activity_id")
	@GeneratedValue (strategy = GenerationType.SEQUENCE, generator="actSeq")
	private Integer activityId;
	
	@Column
	private String name;
	
	@Column
	private BigDecimal cost;

	public Integer getActivityId() {
		return activityId;
	}

	//Since this field is auto generated nothing should be set explicitly here.
//	public void setActivityId(Integer activityId) {
//		this.activityId = activityId;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	
	public String toString ()
	{
		return String.format("Activities Id : " + activityId + " :: Activity name : " + name + " :: cost : " + cost);
	}

}
