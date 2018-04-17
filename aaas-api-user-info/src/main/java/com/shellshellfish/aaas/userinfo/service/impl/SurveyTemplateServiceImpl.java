package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.SurveyTemplateRepository;
import com.shellshellfish.aaas.userinfo.service.SurveyTemplateService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SurveyTemplateServiceImpl implements SurveyTemplateService {

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
