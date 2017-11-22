package com.shellshellfish.aaas.finance.model;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Product {
	@Id
	private String id;
	
	private String name;
	private String riskLevel;
	private Double annualizedReturn;
	private Double maxPullback;
	private Map<String, Double> assetsRatios;
	
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Double getAnnualizedReturn() {
		return annualizedReturn;
	}
	public void setAnnualizedReturn(Double annualizedReturn) {
		this.annualizedReturn = annualizedReturn;
	}
	public Double getMaxPullback() {
		return maxPullback;
	}
	public void setMaxPullback(Double maxPullback) {
		this.maxPullback = maxPullback;
	}
	public Map<String, Double> getAssetsRatios() {
		return assetsRatios;
	}
	public void setAssetsRatios(Map<String, Double> assetsRatios) {
		this.assetsRatios = assetsRatios;
	}
	
}
