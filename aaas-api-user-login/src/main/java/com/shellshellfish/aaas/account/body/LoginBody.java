package com.shellshellfish.aaas.account.body;

import javax.validation.constraints.NotNull;
public class LoginBody {
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
