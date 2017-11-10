package com.shellshellfish.aaas.risk.model.dto;

import com.shellshellfish.aaas.risk.model.Question;

public class QuestionDTO extends Question{
	private String surveyTemplateId;
	
	public String getSurveyTemplateId() {
		return surveyTemplateId;
	}
	public void setSurveyTemplateId(String surveyTemplateId) {
		this.surveyTemplateId = surveyTemplateId;
	}
	
}
