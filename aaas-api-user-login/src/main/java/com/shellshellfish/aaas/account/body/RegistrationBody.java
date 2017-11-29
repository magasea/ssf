package com.shellshellfish.aaas.account.body;

import javax.validation.constraints.NotNull;

public class RegistrationBody {
	@NotNull
	private String telnum;

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}
}
