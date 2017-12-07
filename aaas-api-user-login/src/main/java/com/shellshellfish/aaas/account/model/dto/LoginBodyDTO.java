package com.shellshellfish.aaas.account.model.dto;

import javax.validation.constraints.NotNull;
public class LoginBodyDTO {
	@NotNull
	private String telnum;

	@NotNull
	private String password;

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
