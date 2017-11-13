package com.shellshellfish.aaas.risk.model;

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
	
	
}
