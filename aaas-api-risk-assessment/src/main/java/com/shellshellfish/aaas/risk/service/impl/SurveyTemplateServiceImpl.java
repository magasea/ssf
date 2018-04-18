package com.shellshellfish.aaas.risk.service.impl;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.repositories.mongo.SurveyTemplateRepository;
import com.shellshellfish.aaas.risk.service.SurveyTemplateService;

@Service
public class SurveyTemplateServiceImpl implements SurveyTemplateService{

	@Autowired
	private SurveyTemplateRepository surveyTemplateRepository;
	
	@Override
	public SurveyTemplateDTO getSurveyTemplate(String title, String version) {
		SurveyTemplate surveyTemplate = surveyTemplateRepository.findOneByTitleAndVersion(title, version);
		SurveyTemplateDTO surveyTemplateDTO = new SurveyTemplateDTO();
		BeanUtils.copyProperties(surveyTemplate, surveyTemplateDTO);
		return surveyTemplateDTO;
	}

}
