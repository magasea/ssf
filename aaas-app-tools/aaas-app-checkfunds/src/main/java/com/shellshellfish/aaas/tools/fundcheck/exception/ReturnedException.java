package com.shellshellfish.aaas.tools.fundcheck.exception;

import com.shellshellfish.aaas.tools.fundcheck.commons.EasyKit;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;

/**
 * 异常
 *
 */
public class ReturnedException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorMsg;
	private final static String ResourceAccessExceptionErrMSG = "IO异常，与远程的API调用失败";
	private final static String HttpClientErrorExceptionErrMSG = "";

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {

		this.errorMsg = errorMsg;
	}

	public ReturnedException(Exception e) {
		if (e instanceof ResourceAccessException) {
			setErrorMsg(ResourceAccessExceptionErrMSG);
			return;
		} else if (e instanceof HttpClientErrorException) {
			String error = ((HttpClientErrorException) e).getResponseBodyAsString();
			setErrorMsg(HttpClientErrorExceptionErrMSG + EasyKit.getErrorMessage(error));
			return;
		} else {
			setErrorMsg(e.getMessage());
		}

	}

}
