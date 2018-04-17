package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.dao.Answer;
import com.shellshellfish.aaas.userinfo.model.dao.SurveyResult;
import com.shellshellfish.aaas.userinfo.model.dto.AnswerDTO;
import com.shellshellfish.aaas.userinfo.model.dto.OptionItemDTO;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyResultDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.SurveyResultRepository;
import com.shellshellfish.aaas.userinfo.service.SurveyResultService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyResultServiceImpl implements SurveyResultService {

    @Autowired
    private SurveyResultRepository surveyResultRepository;


    public List<SurveyResultDTO> getSurveyResultsByUserId(String userId) {
        List<SurveyResult> list = surveyResultRepository.findAllByUserId(userId);
        List<SurveyResultDTO> dtoList = new ArrayList<SurveyResultDTO>();
        if (list != null && list.size() > 0) {
            dtoList = list.stream().map(item -> convertToSurveyResultDTO(item)).collect(Collectors.toList());
        }
        return dtoList;
    }


    public SurveyResult save(SurveyResult surveyResult) {
        return surveyResultRepository.save(surveyResult);
    }

    public static SurveyResultDTO convertToSurveyResultDTO(SurveyResult surveyResult) {
        SurveyResultDTO dto = new SurveyResultDTO();
        List<AnswerDTO> answerDTOList = new ArrayList<>();
        List<Answer> answerList = surveyResult.getAnswers();
        if (answerList != null && answerList.size() > 0) {
            answerDTOList = answerList.stream().map(item -> convertToAnswerDTO(item)).collect(Collectors.toList());
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
