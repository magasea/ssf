package com.shellshellfish.aaas.risk.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.repository.mongo.SurveyTemplateRepository;
import com.shellshellfish.aaas.risk.service.SurveyTemplateService;

@Service
public class SurveyTemplateServiceImpl implements SurveyTemplateService{

	@Autowired
	private SurveyTemplateRepository surveyTemplateRepository;
	
	public SurveyTemplate getSurveyTemplate(String title, String version) {
		return surveyTemplateRepository.findOneByTitleAndVersion(title, version);
	}
}
