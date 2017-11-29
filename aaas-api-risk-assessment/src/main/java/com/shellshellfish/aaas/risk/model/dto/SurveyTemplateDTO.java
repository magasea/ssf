package com.shellshellfish.aaas.risk.model.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class SurveyTemplateDTO {

	@JsonIgnore
	private String id;
	@JsonIgnore
	private String bankUuid;
	@JsonIgnore
	private String title;
	private List<QuestionDTO> questions;
	private String version;
//	private Integer _total;
//	private List<QuestionDTO> _items;

	public SurveyTemplateDTO() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBankUuid() {
		return bankUuid;
	}

	public void setBankUuid(String bankUuid) {
		this.bankUuid = bankUuid;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<QuestionDTO> getQuestions() {
		return questions;
	}

	public void setQuestions(List<QuestionDTO> questions) {
		this.questions = questions;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

//	public Integer get_total() {
//		return _total;
//	}
//
//	public void set_total(Integer _total) {
//		this._total = _total;
//	}
//
//	public List<QuestionDTO> get_items() {
//		return _items;
//	}
//
//	public void set_items(List<QuestionDTO> _items) {
//		this._items = _items;
//	}

}
