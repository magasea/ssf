package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Id;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The persistent class for the funds.fund_yieldrate database table.
 */
@Document(collection = "fund_yieldrate")
@AutoConfigureDataMongo
public class FundYieldRate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private BigDecimal amount;

	@Field(value = "update")
	private Long update;

	@Field(value = "code")
	private String code;

	@Field(value = "UNITNAV")
	private BigDecimal unitNav;

	@Field(value = "ACCUMULATEDNAV")
	private BigDecimal accumulatedNav;


	@Field(value = "querydatestr")
	private String queryDateStr;

	@Field(value = "ADJUSTEDNAV")
	private BigDecimal adjustedNav;

	@Field(value = "querydate")
	private Long queryDate;

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public Long getUpdate() {
		return update;
	}

	public void setUpdate(Long update) {
		this.update = update;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getUnitNav() {
		return unitNav;
	}

	public void setUnitNav(BigDecimal unitNav) {
		this.unitNav = unitNav;
	}

	public BigDecimal getAccumulatedNav() {
		return accumulatedNav;
	}

	public void setAccumulatedNav(BigDecimal accumulatedNav) {
		this.accumulatedNav = accumulatedNav;
	}

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	public BigDecimal getAdjustedNav() {
		return adjustedNav;
	}

	public void setAdjustedNav(BigDecimal adjustedNav) {
		this.adjustedNav = adjustedNav;
	}

	public Long getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Long queryDate) {
		this.queryDate = queryDate;
	}

	@Override
	public String toString() {
		return "FundYieldRate{" +
				"id='" + id + '\'' +
				", amount=" + amount +
				", update=" + update +
				", code='" + code + '\'' +
				", unitNav=" + unitNav +
				", accumulatedNav=" + accumulatedNav +
				", queryDateStr='" + queryDateStr + '\'' +
				", adjustedNav=" + adjustedNav +
				", queryDate=" + queryDate +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		FundYieldRate that = (FundYieldRate) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(amount, that.amount) &&
				Objects.equals(update, that.update) &&
				Objects.equals(code, that.code) &&
				Objects.equals(unitNav, that.unitNav) &&
				Objects.equals(accumulatedNav, that.accumulatedNav) &&
				Objects.equals(queryDateStr, that.queryDateStr) &&
				Objects.equals(adjustedNav, that.adjustedNav) &&
				Objects.equals(queryDate, that.queryDate);
	}

	@Override
	public int hashCode() {

		return Objects
				.hash(id, amount, update, code, unitNav, accumulatedNav, queryDateStr, adjustedNav,
						queryDate);
	}
}