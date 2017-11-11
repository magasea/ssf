package com.shellshellfish.aaas.risk.util;

public class Dummy {
	
	public Dummy(Integer id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	
	
	private Integer id;
	private String description;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

}
