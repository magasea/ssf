package com.shellshellfish.aaas.datacollection.server.model;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Getter
@Setter
@Document(collection = "coinfund_yieldrate")
public class CoinFundYieldRate {


	@Field("_id")
	String id;

	@Field("querydate")
	Long querydate;

	@Field("code")
	String code;

	@Field("10KUNITYIELD")
	Double tenKiloUnitYield;

	@Field("update")
	Long update;

	@Field("querydatestr")
	String queryDateStr;

	@Field("YIELDOF7DAYS")
	Double yieldoOf7Days;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Long getQuerydate() {
		return querydate;
	}

	public void setQuerydate(Long querydate) {
		this.querydate = querydate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Double getTenKiloUnitYield() {
		return tenKiloUnitYield;
	}

	public void setTenKiloUnitYield(Double tenKiloUnitYield) {
		this.tenKiloUnitYield = tenKiloUnitYield;
	}

	public Long getUpdate() {
		return update;
	}

	public void setUpdate(Long update) {
		this.update = update;
	}

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	public Double getYieldoOf7Days() {
		return yieldoOf7Days;
	}

	public void setYieldoOf7Days(Double yieldoOf7Days) {
		this.yieldoOf7Days = yieldoOf7Days;
	}


	@Override
	public String toString() {
		return "CoinFundYieldRate{" +
				"id='" + id + '\'' +
				", querydate=" + querydate +
				", code=" + code +
				", tenKiloUnitYield=" + tenKiloUnitYield +
				", update=" + update +
				", queryDateStr='" + queryDateStr + '\'' +
				", yieldoOf7Days=" + yieldoOf7Days +
				'}';
	}
}
