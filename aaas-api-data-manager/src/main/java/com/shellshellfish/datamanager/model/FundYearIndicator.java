package com.shellshellfish.datamanager.model;
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
	private long querydate; //查询日期
	
	@Field("navaccumreturnp")
	private String navaccumreturnp; //区间累计单位净值增长率
	
	@Field("navreturnrankingp")//同类基金区间收益排名
	private String navreturnrankingp;
	
	@Field("navunit")
	private String navunit; //单位净值
	
	@Field("navaccum")
	private String navaccum; //累计单位净值
	
	
	public String getStdate() {
	    return stdate;
	}

	public void setStdate(String date) {
	    this.stdate = date;
	}

	public String getEnddate() {
	    return enddate;
	}

	public String getNavunit() {
	    return navunit;
	}

	public void setNavunit(String navunit) {
	    this.navunit =navunit;
	}
	
	public String getNavaccum() {
	    return navaccum;
	}

	public void setNavaccum(String navaccum) {
	    this.navaccum =navaccum;
	}

	public void setEnddate(String date) {
	    this.enddate = date;
	}

	public long getQuerydate() {
	    return querydate;
	}

	public void setQuerydate(long  date) {
	    this.querydate = date;
	}


	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}


	public String getNavaccumreturnp() {
	    return navaccumreturnp;
	}

	public void setNavaccumreturnp(String navaccumreturnp) {
	    this.navaccumreturnp = navaccumreturnp;
	}

	public String getNavreturnrankingp() {
	    return navreturnrankingp;
	}

	public void setNavreturnrankingp(String navreturnrankingp) {
	    this.navreturnrankingp = navreturnrankingp;
	}
}
