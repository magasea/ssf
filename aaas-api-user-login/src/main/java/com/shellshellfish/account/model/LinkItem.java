package com.shellshellfish.account.model;


public class LinkItem {
    
	private String name;
	private String title;
	private String descripiton;
	//private String selflink;
	private String type; //页面元素类型
    private String href;   //link href	
    private String ordinal; //页面元素顺序
    
    public void setTitle(String title) {
    	this.title =title;
    }
    
    public String getTitle() {
    	return this.title;
    }
    
    public void setName(String name) {
    	this.name =name;
    }
    
    public String getName() {
    	return this.name;
    }
    /*
    public void setSelfLink(String link) {
    	this.selflink=link;
    }
    
    public String getSelfLink() {
    	return this.selflink;
    }*/
    
    public void setDescription(String description) {
       this.descripiton=description;
    }
    
    public String getDescription() {
        return this.descripiton;
    }
    
    public void setType(String type) {
    	this.type=type;
    }
    
    public String getType() {
    	return this.type;
    }
    
    public void setHref(String href) {
    	this.href=href;
    }
    
    public String getHref() {
    	return this.href;
    }
    
    public void setOrdinal(String ordinal) {
    	this.ordinal=ordinal;
    }
    
    public String getOrdinal() {
    	return this.ordinal;
    }
    
}

