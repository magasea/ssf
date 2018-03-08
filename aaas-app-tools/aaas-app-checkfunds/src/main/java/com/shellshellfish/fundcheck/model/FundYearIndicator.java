package com.shellshellfish.fundcheck.model;

import java.math.BigDecimal;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_yieldrate")
//历史净值
public class FundYearIndicator {

	@Id
	private String id;
	@Field("querystdate")
	private String stdate; //查询开始日期
	@Field("queryenddate")
	private String enddate; //查询结束日期

	@Field("code")
	private String code; //基金代码

	@Field("querydate")
	private Long querydate; //查询日期

	//@Field("navaccumreturnp")
	//private String navaccumreturnp; //区间累计单位净值增长率

//	@Field("navreturnrankingp")//同类基金区间收益排名
	//private String navreturnrankingp;

	@Field("UNITNAV")
	private BigDecimal navunit; //单位净值

	@Field("ACCUMULATEDNAV")
	private BigDecimal navaccum; //累计单位净值

	@Field("ADJUSTEDNAV")
	private BigDecimal navadj; //复权单位净值

	@Field("gdaxigi_closeval")
	private BigDecimal gdaxigi_close;//德国DAX

	@Field("h11001csi_closeval")
	private BigDecimal h11001csi_close; //中证全债

	@Field("onyear_val")
	private BigDecimal onyear_val; //oneyear_val

	@Field("Sh300_closeval")
	private BigDecimal sh300_close; //沪深300

	@Field("ZZ500_closeval")
	private BigDecimal zz500_close; //中证500

	@Field("Sh300_h11csi_closeval")
	private BigDecimal sh300_h11csi_close; //沪深300+中证全债

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStdate() {
		return stdate;
	}

	public void setStdate(String stdate) {
		this.stdate = stdate;
	}

	public String getEnddate() {
		return enddate;
	}

	public void setEnddate(String enddate) {
		this.enddate = enddate;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getQuerydate() {
		return querydate;
	}

	public void setQuerydate(Long querydate) {
		this.querydate = querydate;
	}

	public BigDecimal getNavunit() {
		return navunit;
	}

	public void setNavunit(BigDecimal navunit) {
		this.navunit = navunit;
	}

	public BigDecimal getNavaccum() {
		return navaccum;
	}

	public void setNavaccum(BigDecimal navaccum) {
		this.navaccum = navaccum;
	}

	public BigDecimal getNavadj() {
		return navadj;
	}

	public void setNavadj(BigDecimal navadj) {
		this.navadj = navadj;
	}

	public BigDecimal getGdaxigi_close() {
		return gdaxigi_close;
	}

	public void setGdaxigi_close(BigDecimal gdaxigi_close) {
		this.gdaxigi_close = gdaxigi_close;
	}

	public BigDecimal getH11001csi_close() {
		return h11001csi_close;
	}

	public void setH11001csi_close(BigDecimal h11001csi_close) {
		this.h11001csi_close = h11001csi_close;
	}

	public BigDecimal getOnyear_val() {
		return onyear_val;
	}

	public void setOnyear_val(BigDecimal onyear_val) {
		this.onyear_val = onyear_val;
	}

	public BigDecimal getSh300_close() {
		return sh300_close;
	}

	public void setSh300_close(BigDecimal sh300_close) {
		this.sh300_close = sh300_close;
	}

	public BigDecimal getZz500_close() {
		return zz500_close;
	}

	public void setZz500_close(BigDecimal zz500_close) {
		this.zz500_close = zz500_close;
	}

	public BigDecimal getSh300_h11csi_close() {
		return sh300_h11csi_close;
	}

	public void setSh300_h11csi_close(BigDecimal sh300_h11csi_close) {
		this.sh300_h11csi_close = sh300_h11csi_close;
	}

	@Override
	public String toString() {
		return "FundYearIndicator{" +
				"id='" + id + '\'' +
				", stdate='" + stdate + '\'' +
				", enddate='" + enddate + '\'' +
				", code='" + code + '\'' +
				", querydate=" + querydate +
				", navunit=" + navunit +
				", navaccum=" + navaccum +
				", navadj=" + navadj +
				", gdaxigi_close=" + gdaxigi_close +
				", h11001csi_close=" + h11001csi_close +
				", onyear_val=" + onyear_val +
				", sh300_close=" + sh300_close +
				", zz500_close=" + zz500_close +
				", sh300_h11csi_close=" + sh300_h11csi_close +
				'}';
	}
}
