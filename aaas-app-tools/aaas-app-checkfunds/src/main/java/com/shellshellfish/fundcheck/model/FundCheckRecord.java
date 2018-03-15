package com.shellshellfish.fundcheck.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_check_record")
public class FundCheckRecord {
	
	@Id
	private String id;
	@Field("date")
	private String date; //更新日期
	@Field("code")
	private String code; //基金代码
	@Field("ACCUMULATEDNAV")
	private String accumulatedNav; //区间复权单位净值增长率

	@Field("ADJUSTEDNAV")
	private String adjustedNav; //区间复权单位净值增长率

	@Field("UNITNAV")
	private String unitNav; //区间复权单位净值增长率

	@Field("result")
	private int result;// < 0 -> diff = 0 -> equal >0 updated to equal -2 failed to make equal


	@Field("CSVACCUMULATEDNAV")
	private String csvAccumulatedNav; //区间复权单位净值增长率

	@Field("CSVADJUSTEDNAV")
	private String csvAdjustedNav; //区间复权单位净值增长率

	@Field("CSVUNITNAV")
	private String csvUnitNav; //区间复权单位净值增长率

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getAccumulatedNav() {
		return accumulatedNav;
	}

	public void setAccumulatedNav(String accumulatedNav) {
		this.accumulatedNav = accumulatedNav;
	}

	public String getAdjustedNav() {
		return adjustedNav;
	}

	public void setAdjustedNav(String adjustedNav) {
		this.adjustedNav = adjustedNav;
	}

	public String getUnitNav() {
		return unitNav;
	}

	public void setUnitNav(String unitNav) {
		this.unitNav = unitNav;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCsvAccumulatedNav() {
		return csvAccumulatedNav;
	}

	public void setCsvAccumulatedNav(String csvAccumulatedNav) {
		this.csvAccumulatedNav = csvAccumulatedNav;
	}

	public String getCsvAdjustedNav() {
		return csvAdjustedNav;
	}

	public void setCsvAdjustedNav(String csvAdjustedNav) {
		this.csvAdjustedNav = csvAdjustedNav;
	}

	public String getCsvUnitNav() {
		return csvUnitNav;
	}

	public void setCsvUnitNav(String csvUnitNav) {
		this.csvUnitNav = csvUnitNav;
	}
}
