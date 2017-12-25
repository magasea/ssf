package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_yieldrate")
public class FundYearIndicator {
	
	@Id
	private String id;
	@Field("querystdate")
	private String stdate; //开始日期
	@Field("queryenddate")
	private String enddate; //开始日期
	
	@Field("code")
	private String code; //基金代码
	
	@Field("navaccumreturnp")
	private String navaccumreturnp; //区间累计单位净值增长率
	
	
	public String getStdate() {
	    return stdate;
	}

	public void setStdate(String date) {
	    this.stdate = date;
	}

	public String getEnddate() {
	    return enddate;
	}

	public void setEnddate(String date) {
	    this.enddate = date;
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

}
