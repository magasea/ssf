package com.shellshellfish.datamanager.model;

import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "coinfund_yieldrate")
public class CoinFundYieldRate {

	@Id
	private String id;

	@Field("querydate")
	private Long querydate;    //查询日期(精确到秒)

	@Field("code")
	private String code;    //基金代码

	@Field("10KUNITYIELD")
	private BigDecimal tenKiloUnityYield;    //万份收益

	@Field("update")
	private Long update;    //查询日期

	@Field("querydatestr")
	private String queryDateStr;  //查询日期

	@Field("YIELDOF7DAYS")
	private BigDecimal yieldOf7Days; //七日年化


	@Field("NAVADJ")
	private BigDecimal navAdj; //复权单位净值


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

	public BigDecimal getTenKiloUnityYield() {
		return tenKiloUnityYield;
	}

	public void setTenKiloUnityYield(BigDecimal tenKiloUnityYield) {
		this.tenKiloUnityYield = tenKiloUnityYield;
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

	public BigDecimal getYieldOf7Days() {
		return yieldOf7Days;
	}

	public void setYieldOf7Days(BigDecimal yieldOf7Days) {
		this.yieldOf7Days = yieldOf7Days;
	}

	public BigDecimal getNavAdj() {
		return navAdj;
	}

	public void setNavAdj(BigDecimal navAdj) {
		this.navAdj = navAdj;
	}

	@Override
	public String toString() {
		return "CoinFundYieldRate{" +
				"id='" + id + '\'' +
				", querydate=" + querydate +
				", code='" + code + '\'' +
				", tenKiloUnityYield=" + tenKiloUnityYield +
				", update=" + update +
				", queryDateStr='" + queryDateStr + '\'' +
				", yieldOf7Days=" + yieldOf7Days +
				", navAdj=" + navAdj +
				'}';
	}
}
