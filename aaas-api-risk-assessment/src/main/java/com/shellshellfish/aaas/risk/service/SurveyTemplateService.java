package com.shellshellfish.aaas.risk.service;

import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;

public interface SurveyTemplateService {
	SurveyTemplateDTO getSurveyTemplate(String title, String version);
}
