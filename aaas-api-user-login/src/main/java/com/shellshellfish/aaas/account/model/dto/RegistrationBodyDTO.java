package com.shellshellfish.aaas.account.model.dto;

import javax.validation.constraints.NotNull;

public class RegistrationBodyDTO {
	@NotNull
	private String telnum;

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}
}
