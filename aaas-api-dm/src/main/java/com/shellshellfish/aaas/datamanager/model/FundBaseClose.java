package com.shellshellfish.aaas.datamanager.model;

import java.math.BigDecimal;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @Author pierre 18-2-6
 */
@Document(collection = "fundbaseclose")
public class FundBaseClose {


	@Id
	private String id;

	@Field("000300SH")
	private BigDecimal SH300;

	@Field("querydatestr")
	private String queryDateStr;

	@Field("300SH_6_CSI_4")
	private BigDecimal SH300_6_CSI_4;

	@Field("300SH_5_CSI_5")
	private BigDecimal SH300_5_CSI_5;

	@Field("000905SH")
	private BigDecimal SH905;

	@Field("300SH_4_CSI_6")
	private BigDecimal SH300_4_CSI_6;

	@Field("GDAXIGI")
	private BigDecimal GDAXIGI;

	@Field("H11001CSI")
	private BigDecimal H11001CSI;

	@Field("000906SH")
	private BigDecimal SH906;

	@Field("querydate")
	private Long queryDate;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public BigDecimal getSH300() {
		return SH300;
	}

	public void setSH300(BigDecimal SH300) {
		this.SH300 = SH300;
	}

	public String getQueryDateStr() {
		return queryDateStr;
	}

	public void setQueryDateStr(String queryDateStr) {
		this.queryDateStr = queryDateStr;
	}

	public BigDecimal getSH300_6_CSI_4() {
		return SH300_6_CSI_4;
	}

	public void setSH300_6_CSI_4(BigDecimal SH300_6_CSI_4) {
		this.SH300_6_CSI_4 = SH300_6_CSI_4;
	}

	public BigDecimal getSH300_5_CSI_5() {
		return SH300_5_CSI_5;
	}

	public void setSH300_5_CSI_5(BigDecimal SH300_5_CSI_5) {
		this.SH300_5_CSI_5 = SH300_5_CSI_5;
	}

	public BigDecimal getSH905() {
		return SH905;
	}

	public void setSH905(BigDecimal SH905) {
		this.SH905 = SH905;
	}

	public BigDecimal getSH300_4_CSI_6() {
		return SH300_4_CSI_6;
	}

	public void setSH300_4_CSI_6(BigDecimal SH300_4_CSI_6) {
		this.SH300_4_CSI_6 = SH300_4_CSI_6;
	}

	public BigDecimal getGDAXIGI() {
		return GDAXIGI;
	}

	public void setGDAXIGI(BigDecimal GDAXIGI) {
		this.GDAXIGI = GDAXIGI;
	}

	public BigDecimal getH11001CSI() {
		return H11001CSI;
	}

	public void setH11001CSI(BigDecimal h11001CSI) {
		H11001CSI = h11001CSI;
	}

	public BigDecimal getSH906() {
		return SH906;
	}

	public void setSH906(BigDecimal SH906) {
		this.SH906 = SH906;
	}

	public Long getQueryDate() {
		return queryDate;
	}

	public void setQueryDate(Long queryDate) {
		this.queryDate = queryDate;
	}

	@Override
	public String toString() {
		return "FundBaseClose{" +
				"id='" + id + '\'' +
				", SH300=" + SH300 +
				", queryDateStr='" + queryDateStr + '\'' +
				", SH300_6_CSI_4=" + SH300_6_CSI_4 +
				", SH300_5_CSI_5=" + SH300_5_CSI_5 +
				", SH905=" + SH905 +
				", SH300_4_CSI_6=" + SH300_4_CSI_6 +
				", GDAXIGI=" + GDAXIGI +
				", H11001CSI=" + H11001CSI +
				", SH906=" + SH906 +
				", queryDate=" + queryDate +
				'}';
	}
}

