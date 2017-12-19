package com.shellshellfish.aaas.userinfo.model.dto;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserBaseInfoDTO {

	Long id;

	String cellPhone;

	Date birthAge;

	String occupation;

	String passwordhash;
	
	String isTestFlag;

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

	public String getIsTestFlag() {
		return isTestFlag;
	}

	public void setIsTestFlag(String isTestFlag) {
		this.isTestFlag = isTestFlag;
	}

}
