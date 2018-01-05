package com.shellshellfish.aaas.dto;

public class FinanceProdSellInfo {

	private String id;
	private String userProdId;
	/*基金号*/
	private String fundCode;
	/*基金名*/
	private String fundName;
	/*基金份额*/
	private String fundShare;
	/*基金数量*/
	private String fundQuantity;
	
	private String targetSellAmount;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserProdId() {
		return userProdId;
	}

	public void setUserProdId(String userProdId) {
		this.userProdId = userProdId;
	}

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public String getFundName() {
		return fundName;
	}

	public void setFundName(String fundName) {
		this.fundName = fundName;
	}

	public String getFundShare() {
		return fundShare;
	}

	public void setFundShare(String fundShare) {
		this.fundShare = fundShare;
	}

	public String getFundQuantity() {
		return fundQuantity;
	}

	public void setFundQuantity(String fundQuantity) {
		this.fundQuantity = fundQuantity;
	}

	public String getTargetSellAmount() {
		return targetSellAmount;
	}

	public void setTargetSellAmount(String targetSellAmount) {
		this.targetSellAmount = targetSellAmount;
	}

	@Override
	public String toString() {
		return "FinanceProdSellInfo [id=" + id + ", userProdId=" + userProdId + ", fundCode=" + fundCode + ", fundName="
				+ fundName + ", fundShare=" + fundShare + ", fundQuantity=" + fundQuantity + ", targetSellAmount="
				+ targetSellAmount + "]";
	}
	
	
	
	
	
	
}
