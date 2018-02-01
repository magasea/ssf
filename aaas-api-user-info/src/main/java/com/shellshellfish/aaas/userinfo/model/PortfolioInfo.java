package com.shellshellfish.aaas.userinfo.model;

import java.math.BigDecimal;

public class PortfolioInfo {

	//总资产
	private BigDecimal totalAssets;
	//日收益
	private BigDecimal dailyIncome;


	//日收益率
	private BigDecimal dailyIncomeRate;

	//总收益
	private BigDecimal totalIncome;
	//总收益率
	private BigDecimal totalIncomeRate;

	public BigDecimal getTotalAssets() {
		return totalAssets;
	}

	public void setTotalAssets(BigDecimal totalAssets) {
		this.totalAssets = totalAssets;
	}

	public BigDecimal getDailyIncome() {
		return dailyIncome;
	}

	public void setDailyIncome(BigDecimal dailyIncome) {
		this.dailyIncome = dailyIncome;
	}

	public BigDecimal getDailyIncomeRate() {
		return dailyIncomeRate;
	}

	public void setDailyIncomeRate(BigDecimal dailyIncomeRate) {
		this.dailyIncomeRate = dailyIncomeRate;
	}

	public BigDecimal getTotalIncome() {
		return totalIncome;
	}

	public void setTotalIncome(BigDecimal totalIncome) {
		this.totalIncome = totalIncome;
	}

	public BigDecimal getTotalIncomeRate() {
		return totalIncomeRate;
	}

	public void setTotalIncomeRate(BigDecimal totalIncomeRate) {
		this.totalIncomeRate = totalIncomeRate;
	}

	@Override
	public String toString() {
		return "PortfolioInfo{" +
				"totalAssets=" + totalAssets +
				", dailyIncome=" + dailyIncome +
				", dailyIncomeRate=" + dailyIncomeRate +
				", totalIncome=" + totalIncome +
				", totalIncomeRate=" + totalIncomeRate +
				'}';
	}
}
