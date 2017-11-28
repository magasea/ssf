package com.shellshellfish.aaas.userinfo.exception;

public class UserException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	private String code;
	private String msg;

	public UserException(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	@Override
	public String toString() {
		return "UserException [code=" + code + ", msg=" + msg + "]";
	}

}
