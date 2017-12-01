package com.shellshellfish.aaas.assetallocation.model;

import java.util.Date;
import java.util.Map;

public class Adjustment {
	private Integer id;
	private String name;
	private String title;
	private Date date;
	private Map<String, Double> assetsRatios;	
	
	
	public Adjustment(Integer id, String name, Date date) {
		super();
		this.id = id;
		this.name = name;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

}
