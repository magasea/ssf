package com.shellshellfish.aaas.risk.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shellshellfish.aaas.risk.model.dao.Question;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;

public interface QuestionService {
	public QuestionDTO convertToQuestionDTO(Question question);
	public QuestionDTO convertToQuestionDTO(Question question, String surveyTemplateId);
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions);
	public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions, String surveyTemplateId);
	public List<Question> getQuestionsByPage(Integer page, Integer size, List<Question> originalQuestions);
	Page<SurveyTemplateDTO> findByTitleAndVersion(Pageable page, String title, String version) throws InstantiationException, IllegalAccessException;
}
