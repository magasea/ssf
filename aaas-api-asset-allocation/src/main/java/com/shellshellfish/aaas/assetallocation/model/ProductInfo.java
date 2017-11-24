package com.shellshellfish.aaas.assetallocation.model;

import java.util.Date;
import java.util.Map;

import com.shellshellfish.aaas.assetallocation.util.NameValuePair;

public class ProductInfo {
	private String productUuid;
	private String productName;
	private NameValuePair<String, String> investmentPeriod; // 短期 中期 长期
	private NameValuePair<String, String> riskToleranceLevel; // 保守型 稳健型 平衡型 积极型 进取型
	private Date creationTime;
	
	private Map<String, Double> assetsRatios;
	

	public ProductInfo(String productUuid, String productName, Date creationTime) {
		super();
		this.productUuid = productUuid;
		this.productName = productName;
		this.creationTime = creationTime;
	}
	
	public String getProductUuid() {
		return productUuid;
	}
	public void setProductUuid(String productUuid) {
		this.productUuid = productUuid;
	}

	public Map<String, Double> getAssetsRatios() {
		return assetsRatios;
	}
	public void setAssetsRatios(Map<String, Double> assetsRatios) {
		this.assetsRatios = assetsRatios;
	}
	public Date getCreationTime() {
		return creationTime;
	}
	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}

	public NameValuePair<String, String> getInvestmentPeriod() {
		return investmentPeriod;
	}

	public void setInvestmentPeriod(NameValuePair<String, String> investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}

	public NameValuePair<String, String> getRiskToleranceLevel() {
		return riskToleranceLevel;
	}

	public void setRiskToleranceLevel(NameValuePair<String, String> riskToleranceLevel) {
		this.riskToleranceLevel = riskToleranceLevel;
	}

}
