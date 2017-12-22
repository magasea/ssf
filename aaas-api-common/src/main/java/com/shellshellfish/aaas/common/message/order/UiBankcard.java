package com.shellshellfish.aaas.common.message.order;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;


/**
 * The persistent class for the ui_bankcard database table.
 * 
 */



public class UiBankcard implements Serializable {
	private static final long serialVersionUID = 1L;

	
	
	
	private Long id;

	
	private String bankName;

	
	private String cardNumber;

	private String cellphone;

	
	private String createdBy;

	
	private BigInteger createdDate;

	
	
	private Date expireDate;

	
	private String lastModifiedBy;

	
	private BigInteger lastModifiedDate;

	
	private Long userId;

	
	private String userName;

	
	private String userPid;

	public UiBankcard() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBankName() {
		return this.bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getCardNumber() {
		return this.cardNumber;
	}

	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}

	public String getCellphone() {
		return this.cellphone;
	}

	public void setCellphone(String cellphone) {
		this.cellphone = cellphone;
	}

	public String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public BigInteger getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(BigInteger createdDate) {
		this.createdDate = createdDate;
	}

	public Date getExpireDate() {
		return this.expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	public BigInteger getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(BigInteger lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserName() {
		return this.userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserPid() {
		return this.userPid;
	}

	public void setUserPid(String userPid) {
		this.userPid = userPid;
	}

}