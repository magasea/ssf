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
	private String fname; //基金全名称
	
	@Field("name")
	private String name; //基金名称
	
	@Field("risklevel")
	private String risklevel; //风险等级
	
	@Field("firstinvesttype")
	private String firstinvesttype; //投资风格
	
	@Field("custodianbank")
	private String custodianbank; //投资风格
	
	
	public String getQuerystdate() {
	    return querystdate;
	}

	public void setQuerystdate(String date) {
	    this.querystdate = date;
	}

	public String getCustodianbank() {
	    return custodianbank;
	}

	public void setCustodianbank(String bank) {
	    this.custodianbank = bank;
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

	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}
	
	public String getRisklevel() {
	    return risklevel;
	}

	public void setRisklevel(String risklevel) {
	    this.risklevel = risklevel;
	}
	
	public String getFirstinvesttype() {
	    return firstinvesttype;
	}

	public void setFirstinvesttype(String type) {
	    this.firstinvesttype = type;
	}
	

}
