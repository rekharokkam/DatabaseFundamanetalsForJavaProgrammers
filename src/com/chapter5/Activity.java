package com.chapter5;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="activities")
public class Activity {
	
	@Id //Indicates primary key
	@Column (name="activity_id")
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Integer activityId;
	
	@Column
	private String name;
	
	@Column
	private BigDecimal cost;
	
	

}
