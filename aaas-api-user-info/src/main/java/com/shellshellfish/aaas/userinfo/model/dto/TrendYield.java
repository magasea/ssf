package com.shellshellfish.aaas.userinfo.model.dto;

import java.math.BigDecimal;

/**
 * @Author pierre 18-3-13
 */
public class TrendYield {

	String date;
	BigDecimal value;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public BigDecimal getValue() {
		return value;
	}

	public void setValue(BigDecimal value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "TrendYield{" +
				"date='" + date + '\'' +
				", value=" + value +
				'}';
	}
}
