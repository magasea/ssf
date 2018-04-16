package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.Question;
import com.shellshellfish.aaas.userinfo.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.userinfo.model.dto.QuestionDTO;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.SurveyTemplateRepository;
import com.shellshellfish.aaas.userinfo.service.QuestionService;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {
    @Autowired
    private SurveyTemplateRepository surveyTemplateRepository;


    @Override
    public QuestionDTO convertToQuestionDTO(Question question) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);

        return dto;
    }

    @Override
    public QuestionDTO convertToQuestionDTO(Question question, String surveyTemplateId) {
        QuestionDTO dto = new QuestionDTO();
        BeanUtils.copyProperties(question, dto);

        dto.setSurveyTemplateId(surveyTemplateId);
        return dto;
    }

    @Override
    public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions) {
        return questions.stream().map(question -> {

            return convertToQuestionDTO(question);

        }).collect(Collectors.toList());
    }

    @Override
    public List<QuestionDTO> convertToQuestionDTOs(List<Question> questions, String surveyTemplateId) {
        return questions.stream().map(question -> {

            return convertToQuestionDTO(question, surveyTemplateId);

        }).collect(Collectors.toList());
    }

    @Override
    public List<Question> getQuestionsByPage(Integer page, Integer size, List<Question> originalQuestions) {

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
    public Page<SurveyTemplateDTO> findByTitleAndVersion(Pageable page, String title, String version) throws InstantiationException, IllegalAccessException {
        Page<SurveyTemplate> questionPage = surveyTemplateRepository.findByTitleAndVersion(title, version, page);
        Page<SurveyTemplateDTO> questionPageDTO = MyBeanUtils.convertPageDTOs(page, questionPage);
        return questionPageDTO;
    }
}
