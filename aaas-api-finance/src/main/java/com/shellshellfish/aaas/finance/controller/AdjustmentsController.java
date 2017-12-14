package com.shellshellfish.aaas.finance.controller;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.finance.service.AdjustmentsService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ssf-finance")
public class AdjustmentsController {

	private final Logger logger = LoggerFactory.getLogger(AdjustmentsController.class);

	@Autowired
	AdjustmentsService adjustmentsService;

	@ApiOperation("调整方案")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1")
    })
	@ApiResponses({
		 @ApiResponse(code=200,message="OK"),
	     @ApiResponse(code=204,message="OK"),
	     @ApiResponse(code=400,message="请求参数没填好"),
	     @ApiResponse(code=401,message="未授权用户"),        				
	     @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
	     @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
    })	
	@RequestMapping(value = "/product-groups/adjustments/{groupId}", method = RequestMethod.GET)
	public ResponseEntity<?> getAdjustments(
			@Valid @NotNull(message="groupId不能为空") @PathVariable(value = "groupId") String groupId
			) {
		logger.debug("REST request to get adjustments.");
		Map<String, Object> resultMap = adjustmentsService.getAdjustments(groupId);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("风险承受级别")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="investmentHorizonId",dataType="String",required=true,value="投资年限ID",defaultValue="1"),
		@ApiImplicitParam(paramType="query",name="custRiskId",dataType="String",required=true,value="风险ID",defaultValue="C1")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/investment-horizons/{investmentHorizonId}", method = RequestMethod.GET)
	public ResponseEntity<?> getCustRisk(
			@Valid @NotNull(message="groupId不能为空") @PathVariable(value = "groupId") String groupId,
			@Valid @NotNull(message="investmentHorizonId不能为空") @PathVariable(value = "investmentHorizonId") String investmentHorizonId,
			@Valid @NotNull(message="custRiskId不能为空") @RequestParam(value = "custRiskId") String custRiskId
			) {
		logger.debug("REST request to get adjustments.");
		Map<String, Object> resultMap = adjustmentsService.getPerformanceVolatility(groupId,custRiskId,investmentHorizonId,2);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("大致投资年限")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="custRiskId",dataType="String",required=true,value="风险ID",defaultValue="C1"),
		@ApiImplicitParam(paramType="query",name="investmentHorizonId",dataType="String",required=true,value="投资年限ID",defaultValue="1")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/cust-risks/{custRiskId}", method = RequestMethod.GET)
	public ResponseEntity<?> getInvestmentHorizon(
			@Valid @NotNull(message="groupId不能为空") @PathVariable(value = "groupId") String groupId,
			@Valid @NotNull(message="custRiskId不能为空") @PathVariable(value = "custRiskId") String custRiskId,
			@Valid @NotNull(message="investmentHorizonId不能为空") @RequestParam(value = "investmentHorizonId") String investmentHorizonId
			) {
		logger.debug("REST request to get adjustments.");
		Map<String, Object> resultMap = adjustmentsService.getPerformanceVolatility(groupId,custRiskId,investmentHorizonId,1);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
}
