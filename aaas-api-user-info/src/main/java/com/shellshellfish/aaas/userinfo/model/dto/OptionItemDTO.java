package com.shellshellfish.aaas.userinfo.model.dto;

public class OptionItemDTO {

	private Integer ordinal;
	private String name;
	private String content;
	private Integer score;
	
	public OptionItemDTO() {
		
	}
	
	public OptionItemDTO(Integer ordinal, String name, String content, Integer score) {
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
