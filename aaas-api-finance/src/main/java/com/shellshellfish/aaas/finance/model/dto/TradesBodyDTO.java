package com.shellshellfish.aaas.finance.model.dto;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class TradesBodyDTO {

	private String bankCardNum;
	private String bankName;
	private BigDecimal originalFee;
	private BigDecimal costsaving;
	private Map<String, Object> fundMap = new HashMap<String, Object>();

	public String getBankCardNum() {
		return bankCardNum;
	}

	public void setBankCardNum(String bankCardNum) {
		this.bankCardNum = bankCardNum;
	}

	public BigDecimal getOriginalFee() {
		return originalFee;
	}

	public void setOriginalFee(BigDecimal originalFee) {
		this.originalFee = originalFee;
	}

	public BigDecimal getCostsaving() {
		return costsaving;
	}

	public void setCostsaving(BigDecimal costsaving) {
		this.costsaving = costsaving;
	}

	public Map<String, Object> getFundMap() {
		return fundMap;
	}

	public void setFundMap(Map<String, Object> fundMap) {
		this.fundMap = fundMap;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

}
