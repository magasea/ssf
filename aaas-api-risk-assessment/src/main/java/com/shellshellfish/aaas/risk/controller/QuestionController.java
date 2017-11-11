package com.shellshellfish.aaas.risk.controller;

import java.net.URISyntaxException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.shellshellfish.aaas.risk.model.Question;
import com.shellshellfish.aaas.risk.model.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.service.SurveyTemplateService;
import com.shellshellfish.aaas.risk.util.AnnotationHelper;
import com.shellshellfish.aaas.risk.util.Links;
import com.shellshellfish.aaas.risk.util.ResourceWrapper;

@RestController
@RequestMapping("/api")
public class QuestionController {

	private final Logger log = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private SurveyTemplateService surveyTemplateService;
	
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(value = "/questions", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceWrapper<List<QuestionDTO>>> getAllQuestions(@RequestParam(required=false)Integer page,
																			  @RequestParam(required=false, defaultValue="20")Integer size,					
																			  @RequestParam(required=false, name="user-uuid") String userUuid) throws URISyntaxException {
		log.debug("REST request to get questions based on page id and user id. user uuid:{}, page:{}", userUuid, page);		
		
		//TODO: get survey template title and version based on userUuid
						
		SurveyTemplate surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");		
		
		List<Question> questions;
		if (page != null) {
			questions = questionService.getQuestionsByPage(page, size, surveyTemplate.getQuestions());
			
		} else {
			questions = surveyTemplate.getQuestions();
		}
		
		List<QuestionDTO> dtoList = questionService.convertToQuestionDTOs(questions, surveyTemplate.getId());
		
		ResourceWrapper<List<QuestionDTO>> resource = new ResourceWrapper<>(dtoList);
		Links links = new Links();
		links.setSelf("/api/questions?page=1&size=2");
		links.setNext("/api/questions?page=2&size=2");
		
		resource.setLinks(links);
		
	//	AnnotationHelper.alterAnnotationOn(ResourceWrapper.class, ResourceWrapper.class.getAnnotations()[0], "questions");
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
}
