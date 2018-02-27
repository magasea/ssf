package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundcompanys")
public class FundCompanys {
	
	@Id
	private String id;
	
	@Field("基金公司")
	private String companyname; //基金公司
	
	@Field("基金类型")
	private String fundtype; //基金类型
	
	@Field("名称")
	private String fundname; //基金名称
	
	@Field("代码")
	private String code; //代码
	
	@Field("最新资产净值(亿元)")
	private String scale; //最新资产净值(亿元)
	
	@Field("最新基金份额(亿份)")
	private String fundscale; //最新基金份额(亿份)
	
	@Field("基金成立日")
	private String  createdate; //基金成立日
	
	@Field("基金经理")
	private String  manager; //基金经理
	
	//@Field("托管银行")
	//private String  manager; //基金经理
	
	public String getCompanyname() {
	    return companyname;
	}

	public void setCompanyname(String name) {
	    this.companyname = name;
	}


	public String getCreatedate() {
	    return createdate;
	}

	public void setCreatedate(String date) {
	    this.createdate = date;
	}
	
	public String getManager() {
	    return manager;
	}

	public void setManager(String manager) {
	    this.manager = manager;
	}
	

	
	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}
	

	public String getFundtype() {
	    return fundtype;
	}

	public void setFundtype(String type) {
	    this.fundtype = type;
	}


	public String getScale() {
	    return scale;
	}

	public void setScale(String scale) {
	    this.scale = scale;
	}
	
	public String getFundScale() {
	    return fundscale;
	}

	public void setFundScale(String scale) {
	    this.fundscale = scale;
	}
	
	public String getFundname() {
	    return fundname;
	}

	public void setFundname(String name) {
	    this.fundname = name;
	}

}
