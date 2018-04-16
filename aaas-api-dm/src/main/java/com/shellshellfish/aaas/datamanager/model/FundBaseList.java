package com.shellshellfish.aaas.datamanager.model;

import javax.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@Document(collection = "fundbaselist")
public class FundBaseList {

	@Id
	private String id;
	@Field("code")
	private String code; //基金代码
	@Field("baseName")
	private String baseName; //基金基准

	@Field("baseLine")
	private String baseLine; //基金基准


	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}


	public String getBaseName() {
		return baseName;
	}

	public void setBaseName(String name) {
		this.baseName = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBaseLine() {
		return baseLine;
	}

	public void setBaseLine(String baseLine) {
		this.baseLine = baseLine;
	}

	@Override
	public String toString() {
		return "FundBaseList{" +
				"id='" + id + '\'' +
				", code='" + code + '\'' +
				", basename='" + baseName + '\'' +
				", baseLine='" + baseLine + '\'' +
				'}';
	}
}
