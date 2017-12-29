package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_yieldrate")
public class FundYeildRate {

	@Field("code")
	String code;
	@Field("_id")
	String id;
	@Field("navaccum")
	Double navaccum;
	@Field("navadj")
	Double navadj;
	@Field("navlatestdate")
	Long navlatestdate;
	@Field("navreturnrankingp")
	String navreturnrankingp;
	@Field("navreturnrankingpctp")
	String navreturnrankingpctp;
	@Field("navsimiavgreturnp")
	Double navsimiavgreturnp;
	@Field("navunit")
	Double navunit;
	@Field("querydate")
	@Indexed(direction = IndexDirection.ASCENDING)
	Long querydate;
	@Field("update")
	Long update;


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getNavaccum() {
		return navaccum;
	}

	public void setNavaccum(Double navaccum) {
		this.navaccum = navaccum;
	}

	public Double getNavadj() {
		return navadj;
	}

	public void setNavadj(Double navadj) {
		this.navadj = navadj;
	}

	public Long getNavlatestdate() {
		return navlatestdate;
	}

	public void setNavlatestdate(Long navlatestdate) {
		this.navlatestdate = navlatestdate;
	}

	public String getNavreturnrankingp() {
		return navreturnrankingp;
	}

	public void setNavreturnrankingp(String navreturnrankingp) {
		this.navreturnrankingp = navreturnrankingp;
	}

	public String getNavreturnrankingpctp() {
		return navreturnrankingpctp;
	}

	public void setNavreturnrankingpctp(String navreturnrankingpctp) {
		this.navreturnrankingpctp = navreturnrankingpctp;
	}

	public Double getNavsimiavgreturnp() {
		return navsimiavgreturnp;
	}

	public void setNavsimiavgreturnp(Double navsimiavgreturnp) {
		this.navsimiavgreturnp = navsimiavgreturnp;
	}

	public Double getNavunit() {
		return navunit;
	}

	public void setNavunit(Double navunit) {
		this.navunit = navunit;
	}

	public Long getQuerydate() {
		return querydate;
	}

	public void setQuerydate(Long querydate) {
		this.querydate = querydate;
	}

	public Long getUpdate() {
		return update;
	}

	public void setUpdate(Long update) {
		this.update = update;
	}
}
