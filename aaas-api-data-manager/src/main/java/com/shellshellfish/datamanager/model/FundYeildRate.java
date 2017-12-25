package com.shellshellfish.datamanager.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fund_yieldrate")
public class FundYeildRate {
	
	@Id
	private String id;
	@Field("date")
	private String date; //更新日期
	@Field("code")
	private String code; //基金代码
	@Field("navadjreturnp")
	private String navadjreturnp; //区间复权单位净值增长率
	
	public String getDate() {
	    return date;
	}

	public void setDate(String date) {
	    this.date = date;
	}


	public String getCode() {
	    return code;
	}

	public void setCode(String code) {
	    this.code = code;
	}


	public String getNavadjreturnp() {
	    return navadjreturnp;
	}

	public void setDifferrangep(String navadjreturnp) {
	    this.navadjreturnp = navadjreturnp;
	}

}
