package com.shellshellfish.aaas.userinfo.model.dto;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TradeLogDTO {
	@JsonIgnore
	private String id;

	private BigDecimal amount;

	private int operations;

	private BigInteger prodId;

	private Long tradeDate;

	private int tradeStatus;

	@JsonIgnore
	private BigInteger userId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public int getOperations() {
		return operations;
	}

	public void setOperations(int operations) {
		this.operations = operations;
	}

	public BigInteger getProdId() {
		return prodId;
	}

	public void setProdId(BigInteger prodId) {
		this.prodId = prodId;
	}

	public Long getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(Long tradeDate) {
		this.tradeDate = tradeDate;
	}

	public int getTradeStatus() {
		return tradeStatus;
	}

	public void setTradeStatus(int tradeStatus) {
		this.tradeStatus = tradeStatus;
	}

	public BigInteger getUserId() {
		return userId;
	}

	public void setUserId(BigInteger userId) {
		this.userId = userId;
	}

}
