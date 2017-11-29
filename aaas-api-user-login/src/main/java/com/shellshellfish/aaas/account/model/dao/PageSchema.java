package com.shellshellfish.aaas.account.model.dao;

import java.util.HashMap;

public class PageSchema {
   
	private HashMap<String,Object> properties;
   
	public void setProperties(HashMap<String,Object> props) {
		this.properties = props;
	}
	
	public HashMap<String,Object> getProperties() {
		return this.properties; 
	}
	
	
}
