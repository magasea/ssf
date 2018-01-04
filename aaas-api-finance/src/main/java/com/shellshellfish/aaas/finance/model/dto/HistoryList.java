package com.shellshellfish.aaas.finance.model.dto;

/**
 * @Author pierre
 * 17-12-22
 */
public class HistoryList {

	/**
	 * 日期
	 **/
	private String date;
	/**
	 * 单位净值
	 **/
	private String unitNetWorth;
	/**
	 * 累计净值
	 **/
	private String accumulativeNetValue;
	/**
	 * 日涨幅
	 **/
	private String dayGains;


	public HistoryList() {
	}

	public HistoryList(String date, String unitNetWorth, String accumulativeNetValue, String dayGains) {
		this.date = date;
		this.unitNetWorth = unitNetWorth;
		this.accumulativeNetValue = accumulativeNetValue;
		this.dayGains = dayGains;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getUnitNetWorth() {
		return unitNetWorth;
	}

	public void setUnitNetWorth(String unitNetWorth) {
		this.unitNetWorth = unitNetWorth;
	}

	public String getAccumulativeNetValue() {
		return accumulativeNetValue;
	}

	public void setAccumulativeNetValue(String accumulativeNetValue) {
		this.accumulativeNetValue = accumulativeNetValue;
	}

	public String getDayGains() {
		return dayGains;
	}

	public void setDayGains(String dayGains) {
		this.dayGains = dayGains;
	}
}
