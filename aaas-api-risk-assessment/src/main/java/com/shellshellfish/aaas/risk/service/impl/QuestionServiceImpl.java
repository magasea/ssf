package com.shellshellfish.aaas.risk.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.risk.model.dao.Question;
import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.repositories.mongo.SurveyTemplateRepository;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.utils.MyBeanUtils;

@Service 
public class QuestionServiceImpl implements QuestionService{
	@Autowired
	private SurveyTemplateRepository surveyTemplateRepository;
	
	
	@Override
	public QuestionDTO convertToQuestionDTO(Question question) throws RuntimeException{
		QuestionDTO dto = new QuestionDTO();
		BeanUtils.copyProperties(question, dto);
		
		return dto;
	}
	
	@Override
	public QuestionDTO convertToQuestionDTO(Question question, String surveyTemplateId) throws RuntimeException{
		QuestionDTO dto = new QuestionDTO();
		BeanUtils.copyProperties(question, dto);
		
		dto.setSurveyTemplateId(surveyTemplateId);		
		return dto;
	}
	
	@Override
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions) throws RuntimeException{
		return questions.stream().map(question -> {
		
			return convertToQuestionDTO(question);
		
		}).collect(Collectors.toList());		
	}	
	
	@Override
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions, String surveyTemplateId) throws RuntimeException{
		return questions.stream().map(question -> {
		
			return convertToQuestionDTO(question, surveyTemplateId);
		
		}).collect(Collectors.toList());		
	}
	
	@Override
	public List<Question> getQuestionsByPage(Integer page, Integer size, List<Question> originalQuestions) throws RuntimeException{
		
		int fromIndex = page * size; 
		if (fromIndex > originalQuestions.size() - 1)
			return new ArrayList<Question>();
		
		int toIndex = fromIndex + size;
		if (toIndex > originalQuestions.size()) {
			toIndex = originalQuestions.size();
		}
		
		return originalQuestions.subList(fromIndex, toIndex);
	}

	@Override
	public Page<SurveyTemplateDTO> findByTitleAndVersion(Pageable page, String title, String version) throws InstantiationException, IllegalAccessException, RuntimeException {
		Page<SurveyTemplate> questionPage = surveyTemplateRepository.findByTitleAndVersion(title,version, page);
		Page<SurveyTemplateDTO> questionPageDTO = MyBeanUtils.convertPageDTOs(page, questionPage);
		return questionPageDTO;
	}
}
