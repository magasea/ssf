package com.shellshellfish.aaas.userinfo.model;

import java.math.BigDecimal;

public class PortfolioInfo {

	//总资产
	private BigDecimal totalAssets;
	//日收益
	private BigDecimal dailyIncome;


	//总收益
	private BigDecimal totalIncome;
	//总收益率
	private BigDecimal totalIncomeRate;


	private BigDecimal sellAmount;
	private BigDecimal buyAmount;
	private BigDecimal bonus;

	//最后一日申购赎回和分红
	private BigDecimal buyAmountOfEndDay;
	private BigDecimal sellAmountOfEndDay;
	private BigDecimal bonusOfEndDay;


	private BigDecimal assetOfOneDayBefore;

	public static PortfolioInfo getNullInstance() {
		PortfolioInfo instance = new PortfolioInfo();
		instance.setTotalIncome(BigDecimal.ZERO);
		instance.setAssetOfOneDayBefore(BigDecimal.ZERO);
		instance.setSellAmountOfEndDay(BigDecimal.ZERO);
		instance.setBuyAmountOfEndDay(BigDecimal.ZERO);
		instance.setBuyAmount(BigDecimal.ZERO);
		instance.setBuyAmountOfEndDay(BigDecimal.ZERO);
		instance.setBonusOfEndDay(BigDecimal.ZERO);
		instance.setBonus(BigDecimal.ZERO);
		instance.setSellAmount(BigDecimal.ZERO);
		instance.setTotalIncomeRate(BigDecimal.ZERO);
		instance.setTotalAssets(BigDecimal.ZERO);
		return instance;
	}

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

	public BigDecimal getSellAmount() {
		return sellAmount;
	}

	public void setSellAmount(BigDecimal sellAmount) {
		this.sellAmount = sellAmount;
	}

	public BigDecimal getBuyAmount() {
		return buyAmount;
	}

	public void setBuyAmount(BigDecimal buyAmount) {
		this.buyAmount = buyAmount;
	}

	public BigDecimal getBonus() {
		return bonus;
	}

	public void setBonus(BigDecimal bonus) {
		this.bonus = bonus;
	}

	public BigDecimal getBuyAmountOfEndDay() {
		return buyAmountOfEndDay;
	}

	public void setBuyAmountOfEndDay(BigDecimal buyAmountOfEndDay) {
		this.buyAmountOfEndDay = buyAmountOfEndDay;
	}

	public BigDecimal getSellAmountOfEndDay() {
		return sellAmountOfEndDay;
	}

	public void setSellAmountOfEndDay(BigDecimal sellAmountOfEndDay) {
		this.sellAmountOfEndDay = sellAmountOfEndDay;
	}

	public BigDecimal getBonusOfEndDay() {
		return bonusOfEndDay;
	}

	public void setBonusOfEndDay(BigDecimal bonusOfEndDay) {
		this.bonusOfEndDay = bonusOfEndDay;
	}


	public BigDecimal getAssetOfOneDayBefore() {
		return assetOfOneDayBefore;
	}

	public void setAssetOfOneDayBefore(BigDecimal assetOfOneDayBefore) {
		this.assetOfOneDayBefore = assetOfOneDayBefore;
	}


	@Override
	public String toString() {
		return "PortfolioInfo{" +
				"totalAssets=" + totalAssets +
				", dailyIncome=" + dailyIncome +
				", totalIncome=" + totalIncome +
				", totalIncomeRate=" + totalIncomeRate +
				", sellAmount=" + sellAmount +
				", buyAmount=" + buyAmount +
				", bonus=" + bonus +
				", buyAmountOfEndDay=" + buyAmountOfEndDay +
				", sellAmountOfEndDay=" + sellAmountOfEndDay +
				", bonusOfEndDay=" + bonusOfEndDay +
				", assetOfOneDayBefore=" + assetOfOneDayBefore +
				'}';
	}
}
