package com.shellshellfish.aaas.risk.model.dao;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SurveyTemplate {

	@Id
	private String id;
	private String bankUuid;
	private String title;
	private List<Question> questions;
	private String version;	
	private Integer _total;
	private List<Question> _items;
	public SurveyTemplate() {
		
	}
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTitle() {
		return this.title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public List<Question> getQuestions() {
		return questions;
	}
	public void setQuestions(List<Question> questions) {
		this.questions = questions;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public String getBankUuid() {
		return bankUuid;
	}

	public void setBankUuid(String bankUuid) {
		this.bankUuid = bankUuid;
	}

	public Integer get_total() {
		return _total;
	}

	public void set_total(Integer _total) {
		this._total = _total;
	}

	public List<Question> get_items() {
		return _items;
	}

	public void set_items(List<Question> _items) {
		this._items = _items;
	}
}
