package com.shellshellfish.datamanager.model;

public class Head {
	private String status;
	private String msg;
	public Head(){}
	public Head(String status,String msg){
		this.status=status;
		this.msg=msg;	
	}
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
