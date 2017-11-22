package com.shellshellfish.aaas.risk.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class UserRiskAssessment {
	@JsonIgnore
	@Id
	private String id;
	private String userUuid;
	private String assessmentResult;
	
	public UserRiskAssessment() {
		
	}
	
	public UserRiskAssessment(String userUuid, String assessmentResult) {
		super();
		this.userUuid = userUuid;
		this.assessmentResult = assessmentResult;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserUuid() {
		return userUuid;
	}
	public void setUserUuid(String userUuid) {
		this.userUuid = userUuid;
	}
	public String getAssessmentResult() {
		return assessmentResult;
	}
	public void setAssessmentResult(String assessmentResult) {
		this.assessmentResult = assessmentResult;
	}
	
}
