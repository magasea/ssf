package com.shellshellfish.aaas.risk.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;

public interface SurveyTemplateService {
	Page<SurveyTemplateDTO> findByTitleAndVersion(Pageable page, String title, String version);
	SurveyTemplateDTO getSurveyTemplate(String title, String version);
}
