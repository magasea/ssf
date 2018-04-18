package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.dao.Question;
import com.shellshellfish.aaas.userinfo.model.dto.QuestionDTO;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyTemplateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QuestionService {
    QuestionDTO convertToQuestionDTO(Question question);

    QuestionDTO convertToQuestionDTO(Question question, String surveyTemplateId);

    List<QuestionDTO> convertToQuestionDTOs(List<Question> questions);

    List<QuestionDTO> convertToQuestionDTOs(List<Question> questions, String surveyTemplateId);

    List<Question> getQuestionsByPage(Integer page, Integer size, List<Question> originalQuestions);

    Page<SurveyTemplateDTO> findByTitleAndVersion(Pageable page, String title, String version) throws InstantiationException, IllegalAccessException;
}
