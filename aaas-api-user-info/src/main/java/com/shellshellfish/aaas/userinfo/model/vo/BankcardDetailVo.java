package com.shellshellfish.aaas.userinfo.model.vo;

import java.math.BigInteger;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BankcardDetailVo {

	private String cardNumber;
	private String cardUserName;
	private String cardCellphone;
	private String cardUserPid;
	private BigInteger cardUserId;
	private String bankName;

	public String getCardNumber() {
		return cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCardUserName() {
		return cardUserName;
	}

	public void setCardUserName(String cardUserName) {
		this.cardUserName = cardUserName;
	}

	public String getCardCellphone() {
		return cardCellphone;
	}

	public void setCardCellphone(String cardCellphone) {
		this.cardCellphone = cardCellphone;
	}

	public String getCardUserPid() {
		return cardUserPid;
	}

	public void setCardUserPid(String cardUserPid) {
		this.cardUserPid = cardUserPid;
	}

	public BigInteger getCardUserId() {
		return cardUserId;
	}

	public void setCardUserId(BigInteger cardUserId) {
		this.cardUserId = cardUserId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
