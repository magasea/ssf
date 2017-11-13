package com.shellshellfish.aaas.risk.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.SurveyResult;
import com.shellshellfish.aaas.risk.repository.SurveyResultRepository;

@Service
public class SurveyResultService {
	
	@Autowired
	private SurveyResultRepository surveyResultRepository;
	
	public List<SurveyResult> getSurveyResults(String userId, String surveyTemplateId) {
		return surveyResultRepository.findAllByUserIdAndSurveyTemplateId(userId, surveyTemplateId); 
	}
	
	public SurveyResult save(SurveyResult surveyResult) {
		return surveyResultRepository.save(surveyResult);
	}
}
