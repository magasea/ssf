package com.shellshellfish.aaas.account.model.dto;

public class AccountDTO {
	private long id;
	private String password;
	private String telnum;

	public String getPassword() {
		return password;
	}
	public void setPassword(String pwd) {
		this.password = pwd;
	}
	public String getTelnum() {
		return telnum;
	}
	public void setTelnum(String num) {
		this.telnum = num;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

}
