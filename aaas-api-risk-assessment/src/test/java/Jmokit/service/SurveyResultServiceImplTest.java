package Jmokit.service;

import com.shellshellfish.aaas.risk.model.dao.Answer;
import com.shellshellfish.aaas.risk.model.dao.OptionItem;
import com.shellshellfish.aaas.risk.model.dao.SurveyResult;
import com.shellshellfish.aaas.risk.model.dto.AnswerDTO;
import com.shellshellfish.aaas.risk.model.dto.SurveyResultDTO;
import com.shellshellfish.aaas.risk.repositories.mongo.SurveyResultRepository;
import com.shellshellfish.aaas.risk.service.impl.SurveyResultServiceImpl;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;

import  static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.List;

@RunWith(JMockit.class)
public class SurveyResultServiceImplTest {
    @Tested
    SurveyResultServiceImpl service;


    SurveyResultRepository surveyResultRepository;


    public  boolean verifyResult(List<SurveyResult> results,List<SurveyResultDTO>  resultDTOS){
        for (int i=0;i<results.size();i++){
            assertEquals(results.get(i).getUserId(),resultDTOS.get(i).getUserId());
            assertEquals(results.get(i).getSurveyTemplateId(),resultDTOS.get(i).getSurveyTemplateId());
        }
        return  true;
    }
    @Test
    public  void testGetSurveyResultsByUserId(@Injectable SurveyResultRepository surveyResultRepository ){
        List<SurveyResult> Allresults=new ArrayList<>();
        SurveyResult surveyResult = new SurveyResult("1", "1");
        Allresults.add(surveyResult);
        new NonStrictExpectations(){{
                surveyResultRepository.findAllByUserId("2");
                result =Allresults;
        }};
        List<SurveyResultDTO> resultsDto = service.getSurveyResultsByUserId("2");
        new Verifications(){{
            surveyResultRepository.findAllByUserId("2");
            times=1;
        }};
        verifyResult(Allresults,resultsDto);
    }

    @Test
    public  void testconvertToSurveyResultDTO(){
        SurveyResult surveyResult=new SurveyResult("1","1");
        SurveyResultDTO resultDTO = SurveyResultServiceImpl.convertToSurveyResultDTO(surveyResult);
        assertEquals(surveyResult.getUserId(),resultDTO.getUserId());
        assertEquals(surveyResult.getSurveyTemplateId(),resultDTO.getSurveyTemplateId());
    }
    @Test
    public void testConvertToAnswerDTO(){
        OptionItem optionItem=new OptionItem(1,"wp","123",100);
        Answer answer = new Answer(1,optionItem);
        AnswerDTO answerDTO = SurveyResultServiceImpl.convertToAnswerDTO(answer);
        assertEquals(answer.getQuestionOrdinal(),answerDTO.getQuestionOrdinal());
        assertEquals(answer.getSelectedOption().getScore(),answerDTO.getSelectedOption().getScore());
    }
}



