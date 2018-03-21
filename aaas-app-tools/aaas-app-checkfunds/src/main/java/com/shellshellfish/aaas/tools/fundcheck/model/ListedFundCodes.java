package com.shellshellfish.aaas.tools.fundcheck.model;
import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "listedfundcodes")
public class ListedFundCodes {
	
	@Id
	private String id;
	@Field("date")
	private String date; //更新日期
	@Field("code")
	private String code; //基金代码
	@Field("name")
	private String name; //基金名称
	
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


	public String getName() {
	    return name;
	}

	public void setName(String name) {
	    this.name = name;
	}

}
