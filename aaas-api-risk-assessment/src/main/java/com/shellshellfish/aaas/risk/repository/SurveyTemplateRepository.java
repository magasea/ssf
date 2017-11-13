package com.shellshellfish.aaas.risk.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.risk.model.SurveyTemplate;



public interface SurveyTemplateRepository extends MongoRepository<SurveyTemplate, String> {
	SurveyTemplate findOneByTitleAndVersion(String title, String version);
}
