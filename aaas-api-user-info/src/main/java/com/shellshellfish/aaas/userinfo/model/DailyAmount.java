package com.shellshellfish.aaas.userinfo.model;

import java.math.BigDecimal;

public class DailyAmount {

	private String date;
	private String userUuid;
	private Long prodId;
	private Long userProdId;
	private String fundCode;
	private BigDecimal asset;
	private BigDecimal bonus;
	private BigDecimal buyAmount;
	private BigDecimal sellAmount;

	public String getUserUuid() {
		return userUuid;
	}

	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}

	public Long getProdId() {
		return prodId;
	}

	public void setProdId(Long prodId) {
		this.prodId = prodId;
	}

	public Long getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(Long userProdId) {
		this.userProdId = userProdId;
	}

	public BigDecimal getAsset() {
		return asset;
	}

	public void setAsset(BigDecimal asset) {
		this.asset = asset;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public BigDecimal getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(BigDecimal buyAmount) {
		this.buyAmount = buyAmount;
	}

	public BigDecimal getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(BigDecimal sellAmount) {
		this.sellAmount = sellAmount;
	}

	@Override
	public String toString() {
		return "DailyAmount{" +
				"date='" + date + '\'' +
				", userUuid='" + userUuid + '\'' +
				", prodId=" + prodId +
				", userProdId=" + userProdId +
				", fundCode='" + fundCode + '\'' +
				", asset=" + asset +
				", bonus=" + bonus +
				", buyAmount=" + buyAmount +
				", sellAmount=" + sellAmount +
				'}';
	}
}
