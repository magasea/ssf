package com.shellshellfish.aaas.transfer.exception;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

import com.shellshellfish.aaas.transfer.utils.EasyKit;

/**
 * 异常
 * @author chenwei
 *
 */
public class ReturnedException extends RuntimeException{
  
  private String errorMsg;
  private final static String ResourceAccessExceptionErrMSG="IO异常，与远程的API调用失败";
  private final static String HttpClientErrorExceptionErrMSG="";

public String getErrorMsg() {
	return errorMsg;
}

public void setErrorMsg(String errorMsg) {
	
	this.errorMsg = errorMsg;
}
  
public ReturnedException(Exception e){
	if(e instanceof ResourceAccessException){
	   setErrorMsg(ResourceAccessExceptionErrMSG);
	   return;
	}
	else if(e instanceof HttpClientErrorException){
		String error=((HttpClientErrorException)e).getResponseBodyAsString();
		setErrorMsg(HttpClientErrorExceptionErrMSG+EasyKit.getErrorMessage(error));
		return;
	}
	else{
			setErrorMsg(e.getMessage());	
	}
	
}
	
	
	
	
	
}
