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

import com.shellshellfish.aaas.risk.model.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.service.SurveyTemplateService;

@RestController
@RequestMapping("/api")
public class QuestionController {

	private final Logger log = LoggerFactory.getLogger(QuestionController.class);
	
	@Autowired
	private SurveyTemplateService surveyTemplateService;
	
	@Autowired
	private QuestionService questionService;
	
	@RequestMapping(value = "/questions", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<QuestionDTO>> getAllQuestions(Pageable pageable, @RequestParam(required=false, name="user-uuid") String userUuid) throws URISyntaxException {
		log.debug("REST request to get questions based on page id and user id. user uuid:{}, page:{}", userUuid, pageable);		
		
		//TODO: get survey template title and version based on userUuid
						
		SurveyTemplate surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");
		List<QuestionDTO> dtoList = questionService.convertToQuestionDTOs(surveyTemplate.getQuestions(), surveyTemplate.getId());
		
		return new ResponseEntity<>(dtoList, HttpStatus.OK);
	}
	
}
