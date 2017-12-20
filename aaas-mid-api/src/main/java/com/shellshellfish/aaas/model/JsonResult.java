package com.shellshellfish.aaas.model;

public class JsonResult {
 public static final String SUCCESS="1";
 public static final String Fail="0";
 
 private Head head;
 private Object result;
 
 public JsonResult(String success,String message,Object object){
	this.head=new Head(success,message);
	this.result=object;
 }
 
public Head getHead() {
	return head;
}
public void setHead(Head head) {
	this.head = head;
}
public Object getResult() {
	return result;
}
public void setResult(Object data) {
	this.result = data;
}
 
}
