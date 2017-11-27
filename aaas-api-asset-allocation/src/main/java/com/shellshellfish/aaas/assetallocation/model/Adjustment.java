package com.shellshellfish.aaas.assetallocation.model;

import java.util.Date;
import java.util.Map;

public class Adjustment {
	private String title;
	private Date date;
	private Map<String, Double> assetsRatios;	
	
	
	public Adjustment(String title, Date date) {
		super();
		this.title = title;
		this.date = date;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public Map<String, Double> getAssetsRatios() {
		return assetsRatios;
	}
	public void setAssetsRatios(Map<String, Double> assetsRatios) {
		this.assetsRatios = assetsRatios;
	}

}
