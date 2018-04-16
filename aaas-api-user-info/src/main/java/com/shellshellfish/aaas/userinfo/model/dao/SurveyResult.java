package com.shellshellfish.aaas.userinfo.model.dao;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "surveyResult")
public class SurveyResult {
    @Id
    private String id;

    @Indexed
    private String userId;
    private String surveyTemplateId;
    private List<Answer> answers;
    private String riskLevel;

    public String getRiskLevel() {
        return riskLevel;
    }

    public void setRiskLevel(String riskLevel) {
        this.riskLevel = riskLevel;
    }

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
