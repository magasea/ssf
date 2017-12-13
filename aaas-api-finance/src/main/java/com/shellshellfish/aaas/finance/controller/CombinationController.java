package com.shellshellfish.aaas.finance.controller;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.Null;

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

import com.shellshellfish.aaas.finance.model.dao.Fundresources;
import com.shellshellfish.aaas.finance.service.CombinationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ssf-finance")
public class CombinationController {

	private final Logger logger = LoggerFactory.getLogger(CombinationController.class);

	@Autowired
	CombinationService combinationService;

	@ApiOperation("基金详情")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="subGroupId",dataType="String",required=true,value="基金组合SubID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue="")
    })
	@ApiResponses({
		 @ApiResponse(code=200,message="OK"),
	     @ApiResponse(code=204,message="OK"),
	     @ApiResponse(code=400,message="请求参数没填好"),
	     @ApiResponse(code=401,message="未授权用户"),        				
	     @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
	     @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
    })	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/combinations/{code}/fund-details", method = RequestMethod.GET)
	public ResponseEntity<?> getTrades(
			@Valid @PathVariable(value = "groupId") String groupId,
			@Valid @PathVariable(value = "subGroupId") String subGroupId,
			@Valid @PathVariable(value = "code") String code
			) {
		logger.debug("REST request to get details.");
		Map<String, Object> resultMap = combinationService.getCombinationServices(groupId,subGroupId,code);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("涨幅详情")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="subGroupId",dataType="String",required=true,value="基金组合SubID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/combinations/{code}/fund-increase-infos", method = RequestMethod.GET)
	public ResponseEntity<?> fundUpDown(
			@Valid @PathVariable(value = "groupId") String groupId,
			@Valid @PathVariable(value = "subGroupId") String subGroupId,
			@Valid @PathVariable(value = "code") String code
			) {
		logger.debug("REST request to get updown.");
		Map<String, Object> resultMap = combinationService.fundUpDown(groupId,subGroupId,code);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("收益走势")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="subGroupId",dataType="String",required=true,value="基金组合SubID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="reportDate",dataType="String",required=true,value="报告期",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="span",dataType="String",required=true,value="报告期净值数据项",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/combinations/{code}/fund-navbenchreturnstmts", method = RequestMethod.GET)
	public ResponseEntity<?> navbenchreturnstmts(
			@Valid @PathVariable(value = "groupId") String groupId,
			@Valid @PathVariable(value = "subGroupId") String subGroupId,
			@Valid @PathVariable(value = "code") String code,
			@Valid @RequestParam(value = "reportDate") String reportDate,
			@Valid @RequestParam(value = "span") String span
			) {
		logger.debug("REST request to get navbenchreturnstmts.");
		Map<String, Object> resultMap = combinationService.navbenchreturnstmt(groupId,subGroupId,code,reportDate,span);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("历史净值")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="subGroupId",dataType="String",required=true,value="基金组合SubID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/combinations/{code}/fund-navhistories", method = RequestMethod.GET)
	public ResponseEntity<?> navhistories(
			@Valid @PathVariable(value = "groupId") String groupId,
			@Valid @PathVariable(value = "subGroupId") String subGroupId,
			@Valid @PathVariable(value = "code") String code
			) {
		logger.debug("REST request to get navhistories.");
		Map<String, Object> resultMap = combinationService.navhistories(groupId,subGroupId,code);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("历史业绩")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="groupId",dataType="String",required=true,value="基金组合ID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="subGroupId",dataType="String",required=true,value="基金组合SubID",defaultValue="1"),
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/combinations/{code}/fund-achievementhistories", method = RequestMethod.GET)
	public ResponseEntity<?> achievementhistories(
			@Valid @PathVariable(value = "groupId") String groupId,
			@Valid @PathVariable(value = "subGroupId") String subGroupId,
			@Valid @PathVariable(value = "code") String code
			) {
		logger.debug("REST request to get achievementhistories.");
		Map<String, Object> resultMap = combinationService.achievementhistories(groupId,subGroupId,code);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("交易须知")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="基金代码"),
		@ApiImplicitParam(paramType="query",name="businflag",dataType="String",required=false,value="业务类型",defaultValue="")
//		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="代码",defaultValue="")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/tradenotices/{code}", method = RequestMethod.GET)
	public ResponseEntity<?> tradenotices(
			@Valid @PathVariable(value = "code") String code,
			@Valid @RequestParam(value = "businflag",defaultValue="") String falg
			) {
		logger.debug("REST request to get achievementhistories.");
		Map<String, Object> resultMap = combinationService.getTradeLimits(code,falg);
		
		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("基金经理")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="code",dataType="String",required=true,value="基金代码")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=204,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
	})	
	@RequestMapping(value = "/product-groups/mgrlongestyears/{code}", method = RequestMethod.GET)
	public ResponseEntity<?> getMgrlongestyears(
			@Valid @PathVariable(value = "code") String code
			) {
		logger.debug("REST request to get achievementhistories.");
		Fundresources fundResources = combinationService.getMgrlongestyears(code);
		
		return new ResponseEntity<>("", HttpStatus.OK);
	}
}
