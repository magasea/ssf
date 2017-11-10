package com.shellshellfish.aaas.risk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.SurveyTemplate;
import com.shellshellfish.aaas.risk.repository.SurveyTemplateRepository;

@Service
public class SurveyTemplateService {

	@Autowired
	private SurveyTemplateRepository surveyTemplateRepository;
	
	public SurveyTemplate getSurveyTemplate(String title, String version) {
		return surveyTemplateRepository.findOneByTitleAndVersion(title, version);
	}
}
