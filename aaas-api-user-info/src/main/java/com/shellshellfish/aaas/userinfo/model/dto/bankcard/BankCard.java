package com.shellshellfish.aaas.userinfo.model.dto.bankcard;

import java.util.Date;

public class BankCard {



    Long userId;
    String cardNumber;
    String userName;
    Date expireDate;
    String bankName;

    public BankCard(Long userId, String cardNumber, String userName, Date expireDate,
        String bankName) {
        this.userId = userId;
        this.cardNumber = cardNumber;
        this.userName = userName;
        this.expireDate = expireDate;
        this.bankName = bankName;
    }

    public BankCard() {
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
