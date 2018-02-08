package com.shellshellfish.aaas.finance.trade.pay.model;

import java.math.BigDecimal;

/**
 * @Author pierre 18-1-24
 */
public class FundNet {

	// 基金代码
	private String fundCode;
	//单位净值
	private BigDecimal unitNet;
	//累计净值
	private BigDecimal accumNet;
	//日涨跌幅
	private BigDecimal chngPac;
	//净值日期
	private String tradeDate;

	public String getFundCode() {
		return fundCode;
	}

	public void setFundCode(String fundCode) {
		this.fundCode = fundCode;
	}

	public BigDecimal getUnitNet() {
		return unitNet;
	}

	public void setUnitNet(BigDecimal unitNet) {
		this.unitNet = unitNet;
	}

	public BigDecimal getAccumNet() {
		return accumNet;
	}

	public void setAccumNet(BigDecimal accumNet) {
		this.accumNet = accumNet;
	}

	public BigDecimal getChngPac() {
		return chngPac;
	}

	public void setChngPac(BigDecimal chngPac) {
		this.chngPac = chngPac;
	}

	public String getTradeDate() {
		return tradeDate;
	}

	public void setTradeDate(String tradeDate) {
		this.tradeDate = tradeDate;
	}

	@Override
	public String toString() {
		return "FundNet{" +
				"fundCode='" + fundCode + '\'' +
				", unitNet=" + unitNet +
				", accumNet=" + accumNet +
				", chngPac=" + chngPac +
				", tradeDate='" + tradeDate + '\'' +
				'}';
	}
}
