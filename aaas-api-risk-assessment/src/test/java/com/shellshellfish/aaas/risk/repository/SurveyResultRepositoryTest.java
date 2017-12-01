package com.shellshellfish.aaas.risk.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.shellshellfish.aaas.risk.repositories.mongo.SurveyResultRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class SurveyResultRepositoryTest {
	
	@Autowired
	private SurveyResultRepository surveyResultRepository;
	
	@Test
	public void test() {
		surveyResultRepository.findOneByUserIdAndSurveyTemplateId("uuid-of-user-xxx", "5a04267df3f60343ac267ee0");
		surveyResultRepository.findAllByUserId("uuid-of-user-xxx");
	}
}
