package com.shellshellfish.aaas.risk.controller;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.shellshellfish.aaas.risk.model.Question;
import com.shellshellfish.aaas.risk.model.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.service.SurveyTemplateService;
import com.shellshellfish.aaas.risk.util.AnnotationHelper;
import com.shellshellfish.aaas.risk.util.CollectionResourceWrapper;
import com.shellshellfish.aaas.risk.util.Links;
import com.shellshellfish.aaas.risk.util.ResourceWrapper;

import antlr.StringUtils;

@RestController
@RequestMapping("/api/risk-assessment")
public class QuestionController {

	private final Logger log = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private SurveyTemplateService surveyTemplateService;
	
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(value = "/banks/{bankUuid}/questions", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<QuestionDTO>>> getAllQuestions(@RequestParam(required=false)Integer page,
																			  @RequestParam(required=false)Integer size,					
																			  @PathVariable String bankUuid) throws Exception {
		log.debug("REST request to get questions based on page id and bank uuid. bank uuid:{}, page:{}", bankUuid, page);		
		if (size == null) {
			size = 20;
		}
		//TODO: get survey template based on bankUuid
						
		SurveyTemplate surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");		
		
		List<Question> questions;
		if (page != null) {
			questions = questionService.getQuestionsByPage(page, size, surveyTemplate.getQuestions());
			
		} else {
			questions = surveyTemplate.getQuestions();
		}
		
		List<QuestionDTO> dtoList = questionService.convertToQuestionDTOs(questions, surveyTemplate.getId());
		
		CollectionResourceWrapper<List<QuestionDTO>> resource = new CollectionResourceWrapper<>(dtoList);
		Links links = new Links();
		if (page != null) {
			links.setSelf(String.format("/api/risk-assessment/banks/%s/questions?page=%d&size=%d", bankUuid, page, size));
			links.setNext(String.format("/api/risk-assessment/banks/%s/questions?page=%d&size=%d", bankUuid, page + 1, size));
			if (page > 0) {
				links.setPrev(String.format("/api/risk-assessment/banks/%s/questions?page=%d&size=%d", bankUuid, page - 1, size));
			}
		} else {
			links.setSelf(String.format("/api/risk-assessment/banks/%s/questions", bankUuid));
		}		
		
		resource.setLinks(links);	
		resource.setTotal(surveyTemplate.getQuestions().size());
		resource.setName("风险评估题目"); 
	//    AnnotationHelper.changeResourceAnnotion(resource, "_items"); 
	    
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
}
