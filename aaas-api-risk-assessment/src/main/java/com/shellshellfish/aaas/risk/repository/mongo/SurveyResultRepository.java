package com.shellshellfish.aaas.risk.repository.mongo;

import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.risk.model.dao.SurveyResult;

public interface SurveyResultRepository extends MongoRepository<SurveyResult, String> {
	SurveyResult findOneByUserIdAndSurveyTemplateId(String userId, String surveyTemplateId); 
	List<SurveyResult> findAllByUserIdAndSurveyTemplateId(String userId, String surveyTemplateId);
	List<SurveyResult> findAllByUserId(String userId);
	List<SurveyResult> findAllBySurveyTemplateId(String surveyTemplateId);
	
}
