package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundresources")
public class FundResources {
	
	@Id
	private String id;
	
	@Field("querystdate")
	private String querystdate; //查询开始日期
	
	@Field("queryenddate")
	private String queryenddate; //查询开始日期
	
	@Field("code")
	private String code; //基金代码
	
	@Field("fname")
	private String fname; //基金名称
	
	@Field("risklevel")
	private String risklevel; //风险等级
	
	public String getQuerystdate() {
	    return querystdate;
	}

	public void setQuerystdate(String date) {
	    this.querystdate = date;
	}


	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}


	public String getFname() {
	    return fname;
	}

	public void setFname(String name) {
	    this.fname = name;
	}

	public String getRisklevel() {
	    return risklevel;
	}

	public void setRisklevel(String risklevel) {
	    this.risklevel = risklevel;
	}

}
