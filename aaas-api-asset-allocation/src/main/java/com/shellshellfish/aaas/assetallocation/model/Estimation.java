package com.shellshellfish.aaas.assetallocation.model;

import java.util.Date;

public class Estimation {
	private Date date;
	private Double value;
	
	
	
	public Estimation(Date date, Double value) {
		super();
		this.date = date;
		this.value = value;
	}
	
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
}
