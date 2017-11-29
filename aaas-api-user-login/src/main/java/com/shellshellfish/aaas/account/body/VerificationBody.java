package com.shellshellfish.aaas.account.body;

import javax.validation.constraints.NotNull;

public class VerificationBody {
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
