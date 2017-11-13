package com.shellshellfish.aaas.risk.model;

import java.util.Arrays;

import org.bson.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.shellshellfish.aaas.risk.configuration.MongoConfiguration;
import com.shellshellfish.aaas.risk.repository.SurveyTemplateRepository;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = MongoConfiguration.class)
public class SurveyResultTest {
	
	@Autowired
	private SurveyTemplateRepository surveyRepository;
	
	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private MongoClient mongoClient;
	
	@Before
	public void setUp() {
		SurveyTemplate surveyTemplate = new SurveyTemplate();
		surveyTemplate.setTitle("南京银行个人客户风险评估表");
		surveyTemplate.setVersion("1.0");
				
		Question question1 = new Question();
		question1.setTitle("您的家庭年收入为？");
		question1.setOrdinal(1);
		OptionItem optionItem1 = new OptionItem(1, "A", "5万元以下", 5);
		OptionItem optionItem2 = new OptionItem(2, "B", "5-20万元", 10);
		OptionItem optionItem3 = new OptionItem(3, "C", "20-50万元", 15);
		OptionItem optionItem4 = new OptionItem(4, "D", "50-100万元", 20);		
		question1.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3, optionItem4));
		
		Question question2 = new Question();
		question2.setTitle("您的投资目的是？");
		question2.setOrdinal(2);
		optionItem1 = new OptionItem(1, "A", "子女教育费，退休计划", 5);
		optionItem2 = new OptionItem(2, "B", "个人目标（如置业、购车）", 10);
		optionItem3 = new OptionItem(3, "C", "让财富保值增值", 15);
		question2.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3));
		
		Question question3 = new Question();
		question3.setTitle("您期望的投资理财回报是？ （附注：高回报附带高风险）");
		question3.setOrdinal(3);
		optionItem1 = new OptionItem(1, "A", "跟银行存款利率大体相同", 0);
		optionItem2 = new OptionItem(2, "B", "比定期存款利率稍高", 10);
		optionItem3 = new OptionItem(3, "C", "远超过定期存款利率", 20);	
		question3.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3));		
		
		Question question4 = new Question();
		question4.setTitle("您的年龄是？");
		question4.setOrdinal(4);
		optionItem1 = new OptionItem(1, "A", "18-30", 25);
		optionItem2 = new OptionItem(2, "B", "31-40", 20);
		optionItem3 = new OptionItem(3, "C", "41-50", 15);	
		optionItem4 = new OptionItem(4, "D", "51-60", 10);	
		OptionItem optionItem5 = new OptionItem(5, "E", "高于61岁", 5);	
		question4.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3, optionItem4, optionItem5));		
		
		surveyTemplate.setQuestions(Arrays.asList(question1, question2, question3, question4));
		mongoTemplate.save(surveyTemplate);
	}
	
	@Test	
	@Rollback(false)
	public void testSurveyResult() {
		SurveyTemplate surveyTemplate = surveyRepository.findOneByTitleAndVersion("南京银行个人客户风险评估表", "1.0");
		assertThat(surveyTemplate, notNullValue());
		
		Question question = surveyTemplate.getQuestions().get(0);
		OptionItem optionItem = question.getOptionItems().get(0);

		Answer answer = new Answer(question.getOrdinal(), optionItem);
		
		SurveyResult surveyResult = new SurveyResult("uuid-of-user-xxx", surveyTemplate.getId());	
		surveyResult.setAnswers(Arrays.asList(answer));
		mongoTemplate.save(surveyResult);		
		
	}
}
