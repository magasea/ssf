package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.userinfo.aop.AopPageResources;
import com.shellshellfish.aaas.userinfo.model.dao.Answer;
import com.shellshellfish.aaas.userinfo.model.dao.SurveyResult;
import com.shellshellfish.aaas.userinfo.model.dto.AnswerDTO;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyResultDTO;
import com.shellshellfish.aaas.userinfo.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserRiskAssessmentDTO;
import com.shellshellfish.aaas.userinfo.service.QuestionService;
import com.shellshellfish.aaas.userinfo.service.impl.SurveyResultServiceImpl;
import com.shellshellfish.aaas.userinfo.service.impl.SurveyTemplateServiceImpl;
import com.shellshellfish.aaas.userinfo.utils.Links;
import com.shellshellfish.aaas.userinfo.utils.PageWrapper;
import com.shellshellfish.aaas.userinfo.utils.ResourceWrapper;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 风险测评相关接口
 */
@RestController
@RequestMapping(value = "/api/riskassessments")
public class SurveyController {

    private final Logger logger = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private SurveyTemplateServiceImpl surveyTemplateService;

    @Autowired
    private SurveyResultServiceImpl surveyResultService;


    @Autowired
    private QuestionService questionService;

    private static String URL_HEAD = "/api/riskassessments";

    @ApiOperation("风险测评LIST")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 401, message = "未授权用户"),
            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "bankUuid", dataType = "String", required = true, value = "银行卡的Uuid", defaultValue = "")
    })
    @GetMapping(value = "/banks/{bankUuid}/surveytemplates/latest")
    public ResponseEntity<ResourceWrapper<SurveyTemplateDTO>> getSurveyTemplate(
            @PathVariable String bankUuid) {
        logger.debug("REST request to get a survey template. bank uuid:{}", bankUuid);

        SurveyTemplateDTO surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");

        ResourceWrapper<SurveyTemplateDTO> resource = new ResourceWrapper<>(surveyTemplate);
        Links links = new Links();
        links.setSelf(String.format("/api/riskassessment/banks/%s/surveytemplates/latest", bankUuid));
        links.setDescribedBy(String.format("/api/riskassessment/banks/%s/surveytemplates/latest.json", bankUuid));

        resource.setLinks(links);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @ApiOperation("风险测评 结果")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 401, message = "未授权用户"),
            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "bankUuid", dataType = "String", required = true, value = "银行卡的Uuid", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "userUuid", dataType = "String", required = true, value = "用户的Uuid", defaultValue = ""),
            @ApiImplicitParam(paramType = "body", name = "surveyResult", dataType = "SurveyResult", required = true, value = "测评结果BODY", defaultValue = "")
    })
    @RequestMapping(value = "/banks/{bankUuid}/users/{userUuid}/surveyresults", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveSurveyResult(@PathVariable String bankUuid,
                                                                @PathVariable String userUuid,
                                                                @RequestBody SurveyResult surveyResult) {
        logger.debug("REST request to Insert or Update a SurveyResult.");

        Integer sum = 0;
        for (Answer answer : surveyResult.getAnswers()) {
            sum += answer.getSelectedOption().getScore();
        }

        Map<String, String> map = new HashMap<>();

        if (sum <= 73) {
            map.put("riskLevel", "保守型");
        } else if (sum >= 74 && sum <= 107) {
            map.put("riskLevel", "稳健型");
        } else if (sum >= 108 && sum <= 141) {
            map.put("riskLevel", "平衡型");
        } else if (sum >= 142 && sum <= 175) {
            map.put("riskLevel", "成长型");
        } else if (sum >= 176) {
            map.put("riskLevel", "进取型");
        }

        surveyResult.setUserId(userUuid);
        surveyResult.setRiskLevel(map.get("riskLevel"));
        surveyResultService.save(surveyResult);

        return ResponseEntity.ok().body(map);
    }

    @ApiOperation("风险测评结果")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 401, message = "未授权用户"),
            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams(value = {
            @ApiImplicitParam(paramType = "path", name = "bankUuid", dataType = "String", required = true, value = "银行卡的Uuid", defaultValue = ""),
            @ApiImplicitParam(paramType = "path", name = "userUuid", dataType = "String", value = "用户的Uuid")
    })
    @GetMapping(value = "/banks/{bankUuid}/users/{userUuid}/assessment")
    public ResponseEntity<ResourceWrapper<UserRiskAssessmentDTO>> getRiskAssessment(
            @PathVariable String bankUuid,
            @PathVariable String userUuid) throws Exception {
        logger.debug("REST request to get an assessment results. bank uuid:{}", bankUuid);

        //TODO: get survey template based on bankUuid

        userUuid = "uuid-of-user-xxx";

        List<SurveyResultDTO> surveyResults = surveyResultService.getSurveyResultsByUserId(userUuid);
        SurveyResultDTO surveyResult;
        if (surveyResults != null && surveyResults.size() > 0) {
            surveyResult = surveyResults.get(surveyResults.size() - 1);
        } else {
            logger.error("未找到评测记录");
            throw new Exception("未找到评测记录");
        }

        UserRiskAssessmentDTO userRiskAssessment = new UserRiskAssessmentDTO();
        userRiskAssessment.setUserUuid(userUuid);
        if (surveyResult != null && surveyResult.getAnswers() != null) {
            Double score = 0d;
            for (int i = 0; i < surveyResult.getAnswers().size(); i++) {
                AnswerDTO answer = surveyResult.getAnswers().get(i);
                if (answer.getSelectedOption() != null) {
                    score += answer.getSelectedOption().getScore();
                }
            }

            System.out.println("score:" + score);
        }

        // TODO: determine assessment result based on calculated score

        userRiskAssessment.setAssessmentResult("稳健型");

        Links links = new Links();
        links.setSelf(String.format("/api/risk-assessment/banks/%s/survey-results/assessment?%s", bankUuid, userUuid));
        links.setDescribedBy(String.format("/api/risk-assessment/banks/%s/survey-results/assessment.json?%s", bankUuid, userUuid));
        ResourceWrapper<UserRiskAssessmentDTO> resource = new ResourceWrapper<>(userRiskAssessment);
        resource.setLinks(links);
        resource.setName("风险评测结果");
        //AnnotationHelper.changeResourceAnnotion(resource, "surveyTemplate");

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }


    @ApiOperation("风险测评")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 400, message = "请求参数没填好"),
            @ApiResponse(code = 401, message = "未授权用户"),
            @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
            @ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对")
    })
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "path", name = "bankUuid", dataType = "String", required = true, value = "银行卡的Uuid", defaultValue = ""),
            @ApiImplicitParam(paramType = "query", name = "size", dataType = "int", required = false, value = "每页显示数", defaultValue = "25"),
            @ApiImplicitParam(paramType = "query", name = "page", dataType = "int", required = false, value = "当前显示页", defaultValue = "0"),
            @ApiImplicitParam(paramType = "query", name = "sort", dataType = "String", value = "排序条件", defaultValue = "id")
    })
    @GetMapping(value = "/banks/{bankUuid}/questions")
    @AopPageResources
    public ResponseEntity<PageWrapper<SurveyTemplateDTO>> getAllQuestions(
            @PathVariable String bankUuid,
            Pageable pageable,
            @RequestParam(value = "size", defaultValue = "25") Integer size,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "sort") String sort) {
        logger.debug("REST request to get questions based on page id and bank uuid. bank uuid:{}, "
                + "page:{}", bankUuid, page);
        if (size == null) {
            size = 20;
        }
        //TODO: get survey template based on bankUuid
        Page<SurveyTemplateDTO> pages = null;
        try {
            pages = questionService.findByTitleAndVersion(pageable, "南京银行个人客户风险评估表", "1.0");
        } catch (InstantiationException ex) {
            logger.error("exception:", ex);
        } catch (IllegalAccessException ex) {
            logger.error("exception:", ex);
        }
        Map<String, Object> selfMap = new HashMap<String, Object>();
        Map<String, Object> self = new HashMap<String, Object>();
        selfMap.put("name", "test");
        selfMap.put("href", URL_HEAD + "/banks/" + bankUuid + "/questions");
        selfMap.put("describedBy", URL_HEAD + "/banks/" + bankUuid + "/questions.json");
        self.put("self", selfMap);
        PageWrapper<SurveyTemplateDTO> pageWrapper = new PageWrapper<>(pages);
        pageWrapper.set_links(self);
        pageWrapper.setSort(pageable.getSort());
        pageWrapper.setName("风险测评");
        return new ResponseEntity<>(pageWrapper, HttpStatus.OK);
    }
}
