package com.chapter5;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table (name="products")
public class Product {
	
	@Id //Primary Key
	@Column (name="productid")
	@GeneratedValue
	private Integer productId;
	
	@Column (name="productname") //This is optional. If omitted column name will be same as the attribute name
	private String productName;

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	@Override
	public String toString ()
	{
		return String.format("Product Id : %d :: product name : %s", productId, productName);
	}

}
