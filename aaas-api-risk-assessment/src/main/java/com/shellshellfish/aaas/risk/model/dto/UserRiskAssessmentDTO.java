package com.shellshellfish.aaas.risk.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class UserRiskAssessmentDTO {
	@JsonIgnore
	private String id;
	@JsonIgnore
	private String userUuid;
	private String assessmentResult;
	
	public UserRiskAssessmentDTO() {
		
	}
	
	public UserRiskAssessmentDTO(String userUuid, String assessmentResult) {
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
