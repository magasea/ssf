package com.shellshellfish.aaas.risk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.shellshellfish.aaas.risk.model.dao.Answer;
import com.shellshellfish.aaas.risk.model.dao.SurveyResult;
import com.shellshellfish.aaas.risk.model.dto.AnswerDTO;
import com.shellshellfish.aaas.risk.model.dto.OptionItemDTO;
import com.shellshellfish.aaas.risk.model.dto.SurveyResultDTO;
import com.shellshellfish.aaas.risk.repositories.mongo.SurveyResultRepository;
import com.shellshellfish.aaas.risk.service.SurveyResultService;

@Service
public class SurveyResultServiceImpl implements SurveyResultService{
	
	@Autowired
	private SurveyResultRepository surveyResultRepository;
	
	public List<SurveyResult> getSurveyResults(String userId, String surveyTemplateId) {
		return surveyResultRepository.findAllByUserIdAndSurveyTemplateId(userId, surveyTemplateId); 
	}
	
	public List<SurveyResultDTO> getSurveyResultsByUserId(String userId) {
		List<SurveyResult> list= surveyResultRepository.findAllByUserId(userId);
		List<SurveyResultDTO> dtoList = new ArrayList<SurveyResultDTO>();
		if(list!=null&&list.size()>0){
			dtoList = list.stream().map( item -> {return convertToSurveyResultDTO(item);}).collect(Collectors.toList());
		}
		return dtoList;
	}
	
	public List<SurveyResult> getSurveyResultsBySurveyTemplateId(String surveyTemplateId) {
		return surveyResultRepository.findAllBySurveyTemplateId(surveyTemplateId);
	}
		
	public SurveyResult save(SurveyResult surveyResult) {
		return surveyResultRepository.save(surveyResult);
	}
	
	public static SurveyResultDTO convertToSurveyResultDTO(SurveyResult surveyResult) {
		SurveyResultDTO dto = new SurveyResultDTO();
		List<AnswerDTO> answerDTOList = new ArrayList<>();
		List<Answer> answerList = surveyResult.getAnswers();
		if(answerList!=null&&answerList.size()>0){
			answerDTOList = answerList.stream().map( item -> {return convertToAnswerDTO(item);}).collect(Collectors.toList());
		}
 		BeanUtils.copyProperties(surveyResult, dto);
		dto.setAnswers(answerDTOList);
		return dto;
	}
	
	public static AnswerDTO convertToAnswerDTO(Answer answer) {
		AnswerDTO dto = new AnswerDTO();
		OptionItemDTO optionItemDTO = new OptionItemDTO();
 		BeanUtils.copyProperties(answer, dto);
 		BeanUtils.copyProperties(answer.getSelectedOption(), optionItemDTO);
 		dto.setSelectedOption(optionItemDTO);
		return dto;
	}
}
