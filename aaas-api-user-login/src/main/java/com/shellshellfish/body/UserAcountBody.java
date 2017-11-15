package com.shellshellfish.body;

public class UserAcountBody {
	private String telnum;
	private String password;
	private String pwdconfirm;
	private String identifyingcode;

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

	public String getPwdconfirm() {
		return pwdconfirm;
	}

	public void setPwdconfirm(String pwdconfirm) {
		this.pwdconfirm = pwdconfirm;
	}

	public String getIdentifyingcode() {
		return identifyingcode;
	}

	public void setIdentifyingcode(String identifyingcode) {
		this.identifyingcode = identifyingcode;
	}

}
