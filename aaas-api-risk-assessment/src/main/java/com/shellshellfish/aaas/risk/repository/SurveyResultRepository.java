package com.shellshellfish.aaas.risk.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.risk.model.Answer;
import com.shellshellfish.aaas.risk.model.SurveyResult;



public interface SurveyResultRepository extends MongoRepository<SurveyResult, String> {
	List<SurveyResult> findAllByUserIdAndSurveyTemplateId(String userId, String surveyTemplateId); 
}
