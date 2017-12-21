package com.shellshellfish.aaas.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class OptionItem {

	private Integer ordinal;
	private String name;
	@JsonIgnore
	private String content;
	private String text;
	private Integer score;

	public OptionItem() {

	}

	public OptionItem(Integer ordinal, String name, String content, String text, Integer score) {
		super();
		this.ordinal = ordinal;
		this.name = name;
		this.score = score;
		this.content = content;
		this.text = text;
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

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
