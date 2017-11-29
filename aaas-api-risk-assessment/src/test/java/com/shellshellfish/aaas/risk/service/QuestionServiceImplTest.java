package com.shellshellfish.aaas.risk.service;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.risk.model.dao.OptionItem;
import com.shellshellfish.aaas.risk.model.dao.Question;
import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.service.impl.QuestionServiceImpl;

import org.springframework.data.domain.AbstractPageRequest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class QuestionServiceImplTest {

	private Question question1;
	private Question question2;
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Autowired
	private QuestionServiceImpl questionService;
	
	@Before
	public void setUp() {
		question1 = new Question();
		question1.setTitle("您的家庭年收入为？");
		question1.setOrdinal(1);
		OptionItem optionItem1 = new OptionItem(1, "A", "5万元以下", 5);
		OptionItem optionItem2 = new OptionItem(2, "B", "5-20万元", 10);
		OptionItem optionItem3 = new OptionItem(3, "C", "20-50万元", 15);
		OptionItem optionItem4 = new OptionItem(4, "D", "50-100万元", 20);		
		question1.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3, optionItem4));
		
		question2 = new Question();
		question2.setTitle("您的投资目的是？");
		question2.setOrdinal(2);
		optionItem1 = new OptionItem(1, "A", "子女教育费，退休计划", 5);
		optionItem2 = new OptionItem(2, "B", "个人目标（如置业、购车）", 10);
		optionItem3 = new OptionItem(3, "C", "让财富保值增值", 15);
		question2.setOptionItems(Arrays.asList(optionItem1, optionItem2, optionItem3));
		
	}

	@Test
	public void testConvertToQuestionDTO() throws JsonProcessingException {
		QuestionDTO dto = questionService.convertToQuestionDTO(question1);
		assertTrue(dto.getTitle().equals(question1.getTitle()));
		assertThat(dto.getOrdinal().equals(question1.getOrdinal()));
		assertThat(dto.getOptionItems().equals(question1.getOptionItems()));
		
		System.out.println(objectMapper.writeValueAsString(dto));
	}

	@Test
	public void testConvertToQuestionDTOWithSurveyTemplateId() throws JsonProcessingException {
		QuestionDTO dto = questionService.convertToQuestionDTO(question1, "dummy-survey-template-id");
		assertTrue(dto.getTitle().equals(question1.getTitle()));
		assertTrue(dto.getOrdinal().equals(question1.getOrdinal()));
		assertTrue(dto.getOptionItems().equals(question1.getOptionItems()));
		assertTrue(dto.getSurveyTemplateId().equals("dummy-survey-template-id"));
		
		System.out.println(objectMapper.writeValueAsString(dto));
	}

	@Test
	public void testConvertToQuestionDTOs() throws JsonProcessingException {
		List<Question> questions = Arrays.asList(question1, question2);
		List<QuestionDTO> dtoList = questionService.convertToQuestionDTOs(questions);
		assertThat(dtoList.size(), is(questions.size()));
		
		QuestionDTO dto1 = dtoList.get(0);
		assertTrue(dto1.getTitle().equals(question1.getTitle()));
		assertTrue(dto1.getOrdinal().equals(question1.getOrdinal()));
		assertTrue(dto1.getOptionItems().equals(question1.getOptionItems()));
		
		QuestionDTO dto2 = dtoList.get(1);
		assertTrue(dto2.getTitle().equals(question2.getTitle()));
		assertTrue(dto2.getOrdinal().equals(question2.getOrdinal()));
		assertTrue(dto2.getOptionItems().equals(question2.getOptionItems()));
		
		System.out.println(objectMapper.writeValueAsString(dtoList));
	}

	@Test
	public void testConvertToQuestionDTOsWithSurveyTemplateId() throws JsonProcessingException {
		List<Question> questions = Arrays.asList(question1, question2);
		List<QuestionDTO> dtoList = questionService.convertToQuestionDTOs(questions, "dummy-survey-template-id");
		assertThat(dtoList.size(), is(questions.size()));
		
		QuestionDTO dto1 = dtoList.get(0);
		assertTrue(dto1.getTitle().equals(question1.getTitle()));
		assertTrue(dto1.getOrdinal().equals(question1.getOrdinal()));
		assertTrue(dto1.getOptionItems().equals(question1.getOptionItems()));
		assertTrue(dto1.getSurveyTemplateId().equals("dummy-survey-template-id"));
		
		QuestionDTO dto2 = dtoList.get(1);
		assertTrue(dto2.getTitle().equals(question2.getTitle()));
		assertTrue(dto2.getOrdinal().equals(question2.getOrdinal()));
		assertTrue(dto2.getOptionItems().equals(question2.getOptionItems()));
		assertTrue(dto2.getSurveyTemplateId().equals("dummy-survey-template-id"));
		
		System.out.println(objectMapper.writeValueAsString(dtoList));
	}
	
	@Test
	public void testGetQuestionsByPage() throws JsonProcessingException {
		
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
		

		List<Question> questionsOfPage1 = questionService.getQuestionsByPage(1, 2, surveyTemplate.getQuestions());
		
		assertTrue(questionsOfPage1.size() == 2);
		System.out.println(objectMapper.writeValueAsString(questionsOfPage1));
	}

}
