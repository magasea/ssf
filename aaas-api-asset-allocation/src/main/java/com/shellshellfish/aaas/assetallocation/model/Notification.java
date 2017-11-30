package com.shellshellfish.aaas.assetallocation.model;

public class Notification {
	private Integer id;
	private String name;
	private String content;
		
	public Notification(Integer id, String name, String content) {
		super();
		this.id = id;
		this.name = name;
		this.content = content;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
		
	
}
