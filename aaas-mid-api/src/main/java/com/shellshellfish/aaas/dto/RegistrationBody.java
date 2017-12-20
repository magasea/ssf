package com.shellshellfish.aaas.dto;

import javax.validation.constraints.NotNull;

public class RegistrationBody {
	@NotNull
	private String telnum;
	@NotNull
	private String identifyingcode;

	public String getTelnum() {
		return telnum;
	}

	public void setTelnum(String telnum) {
		this.telnum = telnum;
	}

	public String getIdentifyingcode() {
		return identifyingcode;
	}

	public void setIdentifyingcode(String identifyingcode) {
		this.identifyingcode = identifyingcode;
	}
}
