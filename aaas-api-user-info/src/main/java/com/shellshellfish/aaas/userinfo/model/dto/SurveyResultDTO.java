package com.shellshellfish.aaas.userinfo.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

public class SurveyResultDTO {
	@JsonIgnore
	private String id;
	@JsonIgnore
	private String userId;
	@JsonIgnore
	private String surveyTemplateId;
	private List<AnswerDTO> answers;

	public SurveyResultDTO() {

	}

	public SurveyResultDTO(String userId, String surveyTemplateId) {
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

	public List<AnswerDTO> getAnswers() {
		return answers;
	}

	public void setAnswers(List<AnswerDTO> answers) {
		this.answers = answers;
	}
}
