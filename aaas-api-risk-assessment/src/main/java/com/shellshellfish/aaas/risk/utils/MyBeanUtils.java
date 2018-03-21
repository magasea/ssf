package com.shellshellfish.aaas.risk.utils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import com.shellshellfish.aaas.risk.model.dao.OptionItem;
import com.shellshellfish.aaas.risk.model.dao.Question;
import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.OptionItemDTO;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;

public class MyBeanUtils {
	private static final Logger logger = LoggerFactory.getLogger(MyBeanUtils.class);
	public static <A, B> List<B> convertList(List<A> sourceList, Class<B> targetClass)
			throws IllegalAccessException, InstantiationException {
		List<B> targetList = new ArrayList<>();
		for (A item : sourceList) {
			B targetItem = targetClass.newInstance();
			BeanUtils.copyProperties(item, targetItem);
			targetList.add(targetItem);
		}
		return targetList;
	}

	public static <A, B> Page<B> convertPageDTO(Pageable pageable, Page<A> pageA, Class<B> targetClassB)
			throws InstantiationException, IllegalAccessException {
		List<B> dtoList = new ArrayList<>();
		List<A> daoList = pageA.getContent();
		B targetItemB = targetClassB.newInstance();
		daoList.forEach(e -> dtoList.add((B) mapEntityIntoDTO(e, targetItemB)));
		Page<B> dtoPage = new PageImpl<B>(((List<B>) dtoList), pageable, pageA.getTotalElements());
		return dtoPage;
	}

	public static <A, B> Object mapEntityIntoDTO(A targetItemA, B targetItemB) {
		BeanUtils.copyProperties(targetItemA, targetItemB);
		return targetItemB;
	}

	public static Page<SurveyTemplateDTO> convertPageDTOs(Pageable pageable, Page<SurveyTemplate> questionPage)
			throws InstantiationException, IllegalAccessException {
		List<SurveyTemplateDTO> dtoList = new ArrayList<>();
		List<SurveyTemplate> daoList = questionPage.getContent();
		daoList.forEach(e -> dtoList.add(mapEntityIntoSurveyTemplateDTO(e, new SurveyTemplateDTO())));
		Page<SurveyTemplateDTO> dtoPage = new PageImpl<SurveyTemplateDTO>((dtoList), pageable,
				questionPage.getTotalElements());
		return dtoPage;
	}

	public static SurveyTemplateDTO mapEntityIntoSurveyTemplateDTO(SurveyTemplate surveyTemplate,
			SurveyTemplateDTO surveyTemplateDTO) {
		BeanUtils.copyProperties(surveyTemplate, surveyTemplateDTO);
		List<Question> questionList = surveyTemplate.getQuestions();
		List<QuestionDTO> questionDtoList = new ArrayList<QuestionDTO>();
		try {
			if (questionList != null && questionList.size() > 0) {
				questionDtoList = convertList(questionList, QuestionDTO.class);
			}
			surveyTemplateDTO.setQuestions(questionDtoList);
			for (int i=0;i<questionList.size();i++) {
				Question question= questionList.get(i);
				QuestionDTO questionDTO= questionDtoList.get(i);
				List<OptionItem> optionItemList = question.getOptionItems();
				List<OptionItemDTO> optionItemDtoList = new ArrayList<OptionItemDTO>();
				if (optionItemList != null && optionItemList.size() > 0) {
					optionItemDtoList = convertList(optionItemList, OptionItemDTO.class);
				}
				questionDTO.setOptionItems(optionItemDtoList);
			}
		} catch (IllegalAccessException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		} catch (InstantiationException e) {
			logger.error(e.getMessage(), e);
			e.printStackTrace();
		}

		return surveyTemplateDTO;
	}

	public static SurveyTemplateDTO mapEntityIntoDTOs(SurveyTemplate surveyTemplate,
			SurveyTemplateDTO surveyTemplateDTO) {
		BeanUtils.copyProperties(surveyTemplate, surveyTemplateDTO);
		return surveyTemplateDTO;
	}

}
