package com.shellshellfish.aaas.risk.controller;

import com.shellshellfish.aaas.risk.aop.AopPageResources;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.utils.PageWrapper;
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
import java.util.Map;

@RestController
@RequestMapping("/api/riskassessments")
public class QuestionController {

    private final Logger logger = LoggerFactory.getLogger(QuestionController.class);


    @Autowired
    private QuestionService questionService;

    private static String URL_HEAD = "/api/riskassessments";

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
//    @RequestMapping(value = "/banks/{bankUuid}/questions", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
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
