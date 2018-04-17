package com.shellshellfish.aaas.userinfo.repositories.mongo;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.model.dao.SurveyResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author pierre
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongDailyAmountRepositoryTest {


    @Autowired
    private SurveyResultRepository surveyResultRepository;

    @Test
    public void testSurveyResultSave() {

        SurveyResult surveyResult = new SurveyResult();
        surveyResult.setUserId("1");
        surveyResult.setRiskLevel("test");
        surveyResult.setSurveyTemplateId("test");

        surveyResultRepository.save(surveyResult);
    }

}


