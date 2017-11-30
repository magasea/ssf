package com.shellshellfish.aaas.assetallocation.model;

public class Contribution {
	private Integer id;
	private String name;
	private Double value;
	
	public Contribution(Integer id, String name, Double value) {
		super();
		this.id = id;
		this.name = name;
		this.value = value;
	}
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Double getValue() {
		return value;
	}
	public void setValue(Double value) {
		this.value = value;
	}
		
}
