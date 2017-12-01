package com.shellshellfish.aaas.assetallocation.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestParam;

public class SimulationRequest {
	@Value("中期")
	private String investmentPeriod; 
	
	@Value("C")
	private String riskLevel;
	
	@Value("10000")
	private Double amount;
	
	
	public SimulationRequest() {
		
	}
	
	public SimulationRequest(String investmentPeriod, String riskLevel, Double amount) {
		super();
		this.investmentPeriod = investmentPeriod;
		this.riskLevel = riskLevel;
		this.amount = amount;
	}
	public String getInvestmentPeriod() {
		return investmentPeriod;
	}
	public void setInvestmentPeriod(String investmentPeriod) {
		this.investmentPeriod = investmentPeriod;
	}
	public String getRiskLevel() {
		return riskLevel;
	}
	public void setRiskLevel(String riskLevel) {
		this.riskLevel = riskLevel;
	}
	public Double getAmount() {
		return amount;
	}
	public void setAmount(Double amount) {
		this.amount = amount;
	}
	
	
	
}
