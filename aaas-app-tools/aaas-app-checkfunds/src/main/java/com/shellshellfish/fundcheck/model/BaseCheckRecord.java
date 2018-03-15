package com.shellshellfish.fundcheck.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "base_check_record")
public class BaseCheckRecord {
	
	@Id
	private String id;
	@Field("date")
	private String date; //更新日期
	@Field("code")
	private String code; //基金代码
	@Field("name")
	private String name; //基准名称
	@Field("CLOSE")
	private String close; //区间复权单位净值增长率


	@Field("result")
	private int result;// < 0 -> diff = 0 -> equal


	@Field("CSVCLOSE")
	private String csvClose; //区间复权单位净值增长率

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

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

	public String getClose() {
		return close;
	}

	public void setClose(String close) {
		this.close = close;
	}

	public int getResult() {
		return result;
	}

	public void setResult(int result) {
		this.result = result;
	}

	public String getCsvClose() {
		return csvClose;
	}

	public void setCsvClose(String csvClose) {
		this.csvClose = csvClose;
	}
}
