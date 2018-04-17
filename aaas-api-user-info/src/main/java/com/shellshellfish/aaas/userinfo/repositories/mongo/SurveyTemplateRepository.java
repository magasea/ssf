package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.SurveyTemplate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SurveyTemplateRepository extends MongoRepository<SurveyTemplate, String> {
    SurveyTemplate findOneByTitleAndVersion(String title, String version);

    Page<SurveyTemplate> findByTitleAndVersion(String title, String version, Pageable pageable);
}
