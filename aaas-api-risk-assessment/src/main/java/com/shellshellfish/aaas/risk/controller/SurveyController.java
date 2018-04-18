package com.shellshellfish.aaas.risk.controller;

import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.shellshellfish.aaas.risk.model.dao.Answer;
import com.shellshellfish.aaas.risk.model.dao.Question;
import com.shellshellfish.aaas.risk.model.dto.QuestionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.aaas.risk.model.dao.SurveyResult;
import com.shellshellfish.aaas.risk.model.dao.SurveyTemplate;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.service.impl.SurveyResultServiceImpl;
import com.shellshellfish.aaas.risk.service.impl.SurveyTemplateServiceImpl;
import com.shellshellfish.aaas.risk.utils.Links;
import com.shellshellfish.aaas.risk.utils.ResourceWrapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/riskassessments")
public class SurveyController {

    private final Logger log = LoggerFactory.getLogger(SurveyController.class);

    @Autowired
    private SurveyTemplateServiceImpl surveyTemplateService;

    @Autowired
    private SurveyResultServiceImpl surveyResultService;

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
//    @RequestMapping(value = "/banks/{bankUuid}/surveytemplates/latest", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ResourceWrapper<SurveyTemplateDTO>> getSurveyTemplate(
            @PathVariable String bankUuid) throws URISyntaxException {
        log.debug("REST request to get a survey template. bank uuid:{}", bankUuid);

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
//    @RequestMapping(value = "/banks/{bankUuid}/users/{userUuid}/surveyresults", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, String>> saveSurveyResult(@PathVariable String bankUuid,
                                                                @PathVariable String userUuid,
                                                                @RequestBody SurveyResult surveyResult) throws URISyntaxException, Exception {
        log.debug("REST request to Insert or Update a SurveyResult.");

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
}
