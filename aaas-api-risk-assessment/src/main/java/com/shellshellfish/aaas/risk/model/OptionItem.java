package com.shellshellfish.aaas.risk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

public class OptionItem {

	private Integer ordinal;
	private String name;
	private String content;
	private Integer score;
	
	public OptionItem() {
		
	}
	
	public OptionItem(Integer ordinal, String name, String content, Integer score) {
		super();
		this.ordinal = ordinal;
		this.name = name;
		this.content = content;
		this.score = score;
	}
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer id) {
		this.ordinal = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getScore() {
		return score;
	}
	public void setScore(Integer score) {
		this.score = score;
	}

}
