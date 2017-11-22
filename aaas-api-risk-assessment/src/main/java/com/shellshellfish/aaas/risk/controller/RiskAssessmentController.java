package com.shellshellfish.aaas.risk.controller;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.risk.model.Answer;
import com.shellshellfish.aaas.risk.model.SurveyResult;
import com.shellshellfish.aaas.risk.model.UserRiskAssessment;
import com.shellshellfish.aaas.risk.service.SurveyResultService;
import com.shellshellfish.aaas.risk.util.Links;
import com.shellshellfish.aaas.risk.util.ResourceWrapper;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping(value = "/api/riskassessments")
public class RiskAssessmentController {

	private final Logger log = LoggerFactory.getLogger(RiskAssessmentController.class);
	
	@Autowired
	private SurveyResultService surveyResultService;
	
	@ApiOperation("风险测评结果")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="bankUuid",dataType="String",required=true,value="银行卡的Uuid",defaultValue=""),
		@ApiImplicitParam(paramType="path",name="userUuid",dataType="String",required=false,value="用户的Uuid",defaultValue="")
	})
	@RequestMapping(value = "/banks/{bankUuid}/users/{userUuid}/assessment", method = {RequestMethod.GET, RequestMethod.HEAD}, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ResourceWrapper<UserRiskAssessment>> getRiskAssessment(
			@PathVariable String bankUuid,
			@PathVariable String userUuid) throws Exception {
		log.debug("REST request to get an assessment results. bank uuid:{}", bankUuid);		
		
		//TODO: get survey template based on bankUuid
		
		userUuid = "uuid-of-user-xxx";
		
		List<SurveyResult> surveyResults = surveyResultService.getSurveyResultsByUserId(userUuid);
		SurveyResult surveyResult;		
		if (surveyResults.size() > 0) {
			 surveyResult = surveyResults.get(surveyResults.size() - 1);
		} else {
			throw new Exception("未找到评测记录");
		}
		
		UserRiskAssessment userRiskAssessment = new UserRiskAssessment();
		userRiskAssessment.setUserUuid(userUuid);
		if (surveyResult != null && surveyResult.getAnswers() != null) {
			Double score = 0d;
			for (int i = 0; i < surveyResult.getAnswers().size(); i++) {
				Answer answer = surveyResult.getAnswers().get(i);
				score += answer.getSelectedOption().getScore();
			}
			
			System.out.println("score:" + score);
		}
		
		// TODO: determine assessment result based on calculated score
		
		userRiskAssessment.setAssessmentResult("稳健型");		

		Links links = new Links();
		links.setSelf(String.format("/api/risk-assessment/banks/%s/survey-results/assessment?%s", bankUuid, userUuid));
		links.setDescribedBy(String.format("/api/risk-assessment/banks/%s/survey-results/assessment.json?%s", bankUuid, userUuid));
		ResourceWrapper<UserRiskAssessment> resource = new ResourceWrapper<>(userRiskAssessment);		
		resource.setLinks(links);
		resource.setName("风险评测结果");
		//AnnotationHelper.changeResourceAnnotion(resource, "surveyTemplate");
		 
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
}
