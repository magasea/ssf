package com.shellshellfish.aaas.tools.fundcheck.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundrate")
public class FundRate {
	
	@Id
	private String id;
	
	@Field("querydate")
	private String querydate; //查询日期
	
	@Field("code")
	private String code; //基金代码
	
	@Field("shstockstar3ycomrat")
	private String shstockstar3ycomrat; //上海证券3年评级(综合评级)
	
	public String getQuerydate() {
	    return querydate;
	}

	public void setQuerydate(String date) {
	    this.querydate = date;
	}


	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}


	public String getShstockstar3ycomrat() {
	    return shstockstar3ycomrat;
	}

	public void setShstockstar3ycomrat(String rate) {
	    this.shstockstar3ycomrat = rate;
	}

}
