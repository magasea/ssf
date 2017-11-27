package com.shellshellfish.aaas.risk.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.dao.SurveyResult;
import com.shellshellfish.aaas.risk.repository.mongo.SurveyResultRepository;
import com.shellshellfish.aaas.risk.service.SurveyResultService;

@Service
public class SurveyResultServiceImpl implements SurveyResultService{
	
	@Autowired
	private SurveyResultRepository surveyResultRepository;
	
	public List<SurveyResult> getSurveyResults(String userId, String surveyTemplateId) {
		return surveyResultRepository.findAllByUserIdAndSurveyTemplateId(userId, surveyTemplateId); 
	}
	
	public List<SurveyResult> getSurveyResultsByUserId(String userId) {
		return surveyResultRepository.findAllByUserId(userId);
	}
	
	public List<SurveyResult> getSurveyResultsBySurveyTemplateId(String surveyTemplateId) {
		return surveyResultRepository.findAllBySurveyTemplateId(surveyTemplateId);
	}
		
	public SurveyResult save(SurveyResult surveyResult) {
		return surveyResultRepository.save(surveyResult);
	}
}
