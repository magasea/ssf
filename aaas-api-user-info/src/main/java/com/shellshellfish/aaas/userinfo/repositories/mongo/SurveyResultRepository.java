package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.model.dao.SurveyResult;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SurveyResultRepository extends MongoRepository<SurveyResult, String> {

    List<SurveyResult> findAllByUserId(String userId);
}
