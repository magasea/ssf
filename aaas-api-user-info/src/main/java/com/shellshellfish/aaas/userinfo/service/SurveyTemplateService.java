package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dto.SurveyTemplateDTO;

public interface SurveyTemplateService {
	SurveyTemplateDTO getSurveyTemplate(String title, String version);
}
