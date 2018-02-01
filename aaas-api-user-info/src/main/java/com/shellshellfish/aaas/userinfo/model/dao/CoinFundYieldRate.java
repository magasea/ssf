package com.shellshellfish.aaas.userinfo.model.dao;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Id;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * The persistent class for the funds.coinfund_yieldrate database table.
 */
@Document(collection = "coinfund_yieldrate")
public class CoinFundYieldRate implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String id;


	@Field(value = "10KUNITYIELD")
	private String tenKiloUnitYield;

	@Field(value = "NAVADJ")
	private BigDecimal navadj;

	@Field(value = "YIELDOF7DAYS")
	private BigDecimal yieldOf7Days;

	@Field(value = "update")
	private BigDecimal update;


	@Field(value = "querydate")
	private Long queryDate;

	@Field(value = "code")
	private String code;

	@Field(value = "querydatestr")
	private String queryDateStr;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTenKiloUnitYield() {
		return tenKiloUnitYield;
	}

	public void setTenKiloUnitYield(String tenKiloUnitYield) {
		this.tenKiloUnitYield = tenKiloUnitYield;
	}

	public BigDecimal getNavadj() {
		return navadj;
	}

	public void setNavadj(BigDecimal navadj) {
		this.navadj = navadj;
	}

	public BigDecimal getYieldOf7Days() {
		return yieldOf7Days;
	}

	public void setYieldOf7Days(BigDecimal yieldOf7Days) {
		this.yieldOf7Days = yieldOf7Days;
	}

	public BigDecimal getUpdate() {
		return update;
	}

	public void setUpdate(BigDecimal update) {
		this.update = update;
	}

	public Long getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Long queryDate) {
		this.queryDate = queryDate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	@Override
	public String toString() {
		return "CoinFundYieldRate{" +
				"id='" + id + '\'' +
				", tenKiloUnitYield='" + tenKiloUnitYield + '\'' +
				", navadj=" + navadj +
				", yieldOf7Days=" + yieldOf7Days +
				", update=" + update +
				", queryDate=" + queryDate +
				", code='" + code + '\'' +
				", queryDateStr='" + queryDateStr + '\'' +
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
		CoinFundYieldRate that = (CoinFundYieldRate) o;
		return Objects.equals(id, that.id) &&
				Objects.equals(tenKiloUnitYield, that.tenKiloUnitYield) &&
				Objects.equals(navadj, that.navadj) &&
				Objects.equals(yieldOf7Days, that.yieldOf7Days) &&
				Objects.equals(update, that.update) &&
				Objects.equals(queryDate, that.queryDate) &&
				Objects.equals(code, that.code) &&
				Objects.equals(queryDateStr, that.queryDateStr);
	}

	@Override
	public int hashCode() {

		return Objects
				.hash(id, tenKiloUnitYield, navadj, yieldOf7Days, update, queryDate, code, queryDateStr);
	}
}