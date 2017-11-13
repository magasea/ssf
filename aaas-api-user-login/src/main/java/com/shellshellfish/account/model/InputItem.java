package com.shellshellfish.account.model;


public class InputItem {
    
	private String title;
	private String name;
	private String descripiton;
	//private String selflink;
	private String type; //页面元素类型, include :editor,label,pwdeditor
    //private String action; 	
    private String ordinal; //页面元素顺序
    private int minlength=0;
    private int maxlength=0;
    //private boolean validflag=false;//是否需要验证
    private boolean editable=false;//是否可编辑
    //private boolean checkflag=false; //是否可check,checkobx
    //private boolean iconflag=false; //是否有图标
    //private String iconurl; //图标 url
    
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
    
    /*
    public void setAction(String action) {
    	this.action=action;
    }
    
    public String getAction() {
    	return this.action;
    }*/
    
    public void setOrdinal(String ordinal) {
    	this.ordinal=ordinal;
    }
    
    public String getOrdinal() {
    	return this.ordinal;
    }
    
    public void setMinlength(int  len) {
    	this.minlength =len;
    }
    
    public int  getMinlength() {
    	return this.minlength;
    }
         
    public void setMaxlength(int  len) {
    	this.maxlength =len;
    }
    
    public int  getMaxlength() {
    	return this.maxlength;
    }
    
    public void setEditable(boolean flag) {
    	this.editable=flag;
    }
    
    public boolean getEditable() {
    	return this.editable;
    }
    
    /*
    public void setValidflag(boolean flag) {
    	this.validflag=flag;
    }
    
    public boolean getValidflag() {
    	return this.validflag;
    }
    
    
    public void setIconflag(boolean flag) {
    	this.iconflag=flag;
    }
    
    public boolean getIconflag() {
    	return this.iconflag;
    }
    
    public boolean getCheckflag() {
    	return this.checkflag;
    }
    
    public void setCheckflag(boolean flag) {
    	this.checkflag=flag;
    }
    
    public void setIconurl(String url) {
    	this.iconurl=url;
    }
    
    public String getIconurl() {
    	return this.iconurl;
    }
    */
}

