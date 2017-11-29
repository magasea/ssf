package com.shellshellfish.aaas.account.body;

import javax.validation.constraints.NotNull;

public class PwdSettingBody {
	@NotNull
	private String telnum;
	@NotNull
	private String password;
	@NotNull
	private String pwdconfirm;

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
}
