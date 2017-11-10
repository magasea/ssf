package com.shellshellfish.aaas.risk.model;

import java.util.List;


public class Question {	
	private Integer ordinal;
	private String title;
	private List<OptionItem> optionItems;
	private Double weight;
	
	public Question() {
		
	}
	
	public Integer getOrdinal() {
		return ordinal;
	}
	public void setOrdinal(Integer ordinal) {
		this.ordinal = ordinal;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<OptionItem> getOptionItems() {
		return optionItems;
	}
	public void setOptionItems(List<OptionItem> optionItems) {
		this.optionItems = optionItems;
	}
	public Double getWeight() {
		return weight;
	}
	public void setWeight(Double weight) {
		this.weight = weight;
	}
	
	
}
