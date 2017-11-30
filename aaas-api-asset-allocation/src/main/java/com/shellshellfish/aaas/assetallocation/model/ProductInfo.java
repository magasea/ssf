package com.shellshellfish.aaas.assetallocation.model;

import java.util.Date;
import java.util.Map;

import com.shellshellfish.aaas.assetallocation.util.NameValuePair;

public class ProductInfo {
	private Integer groupId;
	private Integer subGroupId;
	private String name;
//	private NameValuePair<String, String> investmentPeriod; // 短期 中期 长期
//	private NameValuePair<String, String> riskToleranceLevel; // 保守型 稳健型 平衡型 积极型 进取型
	private Double minAnnualizedReturn;
	private Double maxAnnualizedReturn;
	private Double currentAnnualizedReturn;
	private Double minRiskLevel;
	private Double maxRiskLevel;
	private Double currrentRiskLevel;
	private Date creationTime = new Date();
	
	private Map<String, Double> assetsRatios;
	


	public ProductInfo(Integer groupId, Integer subGroupId, String name) {
		super();
		this.groupId = groupId;
		this.subGroupId = subGroupId;
		this.name = name;
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
	public String getName() {
		return name;
	}
	public void setName(String productName) {
		this.name = productName;
	}

//	public NameValuePair<String, String> getInvestmentPeriod() {
//		return investmentPeriod;
//	}
//
//	public void setInvestmentPeriod(NameValuePair<String, String> investmentPeriod) {
//		this.investmentPeriod = investmentPeriod;
//	}
//
//	public NameValuePair<String, String> getRiskToleranceLevel() {
//		return riskToleranceLevel;
//	}
//
//	public void setRiskToleranceLevel(NameValuePair<String, String> riskToleranceLevel) {
//		this.riskToleranceLevel = riskToleranceLevel;
//	}

	public Double getMinAnnualizedReturn() {
		return minAnnualizedReturn;
	}

	public void setMinAnnualizedReturn(Double minAnnualizedReturn) {
		this.minAnnualizedReturn = minAnnualizedReturn;
	}

	public Double getMaxAnnualizedReturn() {
		return maxAnnualizedReturn;
	}

	public void setMaxAnnualizedReturn(Double maxAnnualizedReturn) {
		this.maxAnnualizedReturn = maxAnnualizedReturn;
	}

	public Double getMinRiskLevel() {
		return minRiskLevel;
	}

	public void setMinRiskLevel(Double minRiskLevel) {
		this.minRiskLevel = minRiskLevel;
	}

	public Double getMaxRiskLevel() {
		return maxRiskLevel;
	}

	public void setMaxRiskLevel(Double maxRiskLevel) {
		this.maxRiskLevel = maxRiskLevel;
	}

	public Double getCurrentAnnualizedReturn() {
		return currentAnnualizedReturn;
	}

	public void setCurrentAnnualizedReturn(Double currentAnnualizedReturn) {
		this.currentAnnualizedReturn = currentAnnualizedReturn;
	}

	public Double getCurrrentRiskLevel() {
		return currrentRiskLevel;
	}

	public void setCurrrentRiskLevel(Double currrentRiskLevel) {
		this.currrentRiskLevel = currrentRiskLevel;
	}
	public Integer getGroupId() {
		return groupId;
	}
	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	public Integer getSubGroupId() {
		return subGroupId;
	}
	public void setSubGroupId(Integer subGroupId) {
		this.subGroupId = subGroupId;
	}

}
