package com.shellshellfish.aaas.risk.model;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class SurveyResult {
	@Id
	private String id;	
	private String userId;
	private String surveyTemplateId;
	private List<Answer> answers;
	
	public SurveyResult() {
		
	}
	
	public SurveyResult(String userId, String surveyTemplateId) {
		super();
		this.userId = userId;
		this.surveyTemplateId = surveyTemplateId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSurveyTemplateId() {
		return surveyTemplateId;
	}

	public void setSurveyTemplateId(String surveyTemplateId) {
		this.surveyTemplateId = surveyTemplateId;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
	
}
