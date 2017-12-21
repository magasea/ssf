package com.shellshellfish.aaas.userinfo.model.dto;

import java.util.Date;

public class UserBaseInfoDTO {

	Long id;

	String cellPhone;

	Date birthAge;

	String occupation;

	String passwordhash;
	
	Integer isTestFlag;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCellPhone() {
		return cellPhone;
	}

	public void setCellPhone(String cellPhone) {
		this.cellPhone = cellPhone;
	}

	public Date getBirthAge() {
		return birthAge;
	}

	public void setBirthAge(Date birthDay) {
		this.birthAge = birthDay;
	}

	public String getOccupation() {
		return occupation;
	}

	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}

	public String getPasswordhash() {
		return passwordhash;
	}

	public void setPasswordhash(String passwordhash) {
		this.passwordhash = passwordhash;
	}

	public Integer getIsTestFlag() {
		return isTestFlag;
	}

	public void setIsTestFlag(Integer isTestFlag) {
		this.isTestFlag = isTestFlag;
	}

}
