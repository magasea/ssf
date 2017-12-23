package com.shellshellfish.aaas.userinfo.model.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class BankCardDTO {
	@JsonIgnore
    Long userId;
    String cardNumber;
    String userName;

  public String getUserPid() {
    return userPid;
  }

  public void setUserPid(String userPid) {
    this.userPid = userPid;
  }

  String userPid;
    Date expireDate;
    String bankName;

    public BankCardDTO(Long userId, String cardNumber, String userName, Date expireDate,
        String bankName, String userPid) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.expireDate = expireDate;
        this.bankName = bankName;
        this.userPid = userPid;
    }

    public BankCardDTO() {
    }

    public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
