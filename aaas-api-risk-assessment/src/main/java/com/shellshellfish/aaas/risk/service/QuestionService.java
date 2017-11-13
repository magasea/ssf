package com.shellshellfish.aaas.risk.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.Question;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;

@Service
public class QuestionService {
	
	public QuestionDTO convertToQuestionDTO(Question question) {
		QuestionDTO dto = new QuestionDTO();
		BeanUtils.copyProperties(question, dto);
		
		return dto;
	}
	
	public QuestionDTO convertToQuestionDTO(Question question, String surveyTemplateId) {
		QuestionDTO dto = new QuestionDTO();
		BeanUtils.copyProperties(question, dto);
		
		dto.setSurveyTemplateId(surveyTemplateId);		
		return dto;
	}
	
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions) {
		return questions.stream().map(question -> {
		
			return convertToQuestionDTO(question);
		
		}).collect(Collectors.toList());		
	}	
	
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions, String surveyTemplateId) {
		return questions.stream().map(question -> {
		
			return convertToQuestionDTO(question, surveyTemplateId);
		
		}).collect(Collectors.toList());		
	}
	
	public List<Question> getQuestionsByPage(Integer page, Integer size, List<Question> originalQuestions){
		
		int fromIndex = page * size; 
		if (fromIndex > originalQuestions.size() - 1)
			return new ArrayList<Question>();
		
		int toIndex = fromIndex + size;
		if (toIndex > originalQuestions.size()) {
			toIndex = originalQuestions.size() - 1;
		}
		
		return originalQuestions.subList(fromIndex, toIndex);
	}
}
