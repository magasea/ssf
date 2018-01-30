package com.shellshellfish.aaas.userinfo.model.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class BankcardDetailBodyDTO {

	private String userUuid;
	private Long userId;
	private String cardNumber;
	private String cardUserName;
	private String cardCellphone;
	private String cardUserPid;
	private String cardUuId;
	private String bankName;


	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

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

	public String getCardUuId() {
		return cardUuId;
	}

	public void setCardUuId(String cardUuId) {
		this.cardUuId = cardUuId;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	@Override
	public String toString() {
		return "BankcardDetailBodyDTO{" +
				"userUuid='" + userUuid + '\'' +
				", userId=" + userId +
				", cardNumber='" + cardNumber + '\'' +
				", cardUserName='" + cardUserName + '\'' +
				", cardCellphone='" + cardCellphone + '\'' +
				", cardUserPid='" + cardUserPid + '\'' +
				", cardUuId='" + cardUuId + '\'' +
				", bankName='" + bankName + '\'' +
				'}';
	}
}
