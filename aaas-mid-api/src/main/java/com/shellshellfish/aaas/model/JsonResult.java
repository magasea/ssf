package com.shellshellfish.aaas.model;

import java.util.HashMap;
import java.util.Map;

public class JsonResult {

 public static final Map EMPTYRESULT = new HashMap();	
 public static final String SUCCESS="1";
 public static final String Fail="0";
 
 private Head head;
 private Object result;
 public JsonResult(){}
 
 public JsonResult(String success,String message,Object object){
	this.head=new Head(success,message);
	switch (success) {
	case JsonResult.SUCCESS:
		this.result=object;
		break;
    
	default:
		break;
	}
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
