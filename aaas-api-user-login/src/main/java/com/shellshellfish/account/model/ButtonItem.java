package com.shellshellfish.account.model;


public class ButtonItem {
    
	private String name;
	private String title;
	private String descripiton;
	//private String selflink;
	private String type; //页面元素类型
    private String action; 	
    private String ordinal; //页面元素顺序
    //private String iconurl; //图标url
    
    private boolean clickable=false;//是否可点击 ,false: turn grey
    
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
    
    public void setAction(String action) {
    	this.action=action;
    }
    
    public String getAction() {
    	return this.action;
    }
    
    public void setOrdinal(String ordinal) {
    	this.ordinal=ordinal;
    }
    
    public String getOrdinal() {
    	return this.ordinal;
    }
    
    public void setClickable(boolean flag) {
    	this.clickable=flag;
    }
    
    public boolean getclickable() {
    	return this.clickable;
    }
    
    /*
    public void setIconurl(String url) {
    	this.iconurl=url;
    }
    
    public String getIconurl() {
    	return this.iconurl;
    }*/
    
    
}

