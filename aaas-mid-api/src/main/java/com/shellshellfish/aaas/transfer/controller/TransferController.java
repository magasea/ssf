package com.shellshellfish.aaas.transfer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.model.JsonResult;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易用
 * @author developer4
 *
 */
@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class TransferController {
	
	@Autowired
	private RestTemplate restTemplate;
	
	
	
	@ApiOperation("获取预计费用")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="电话号码",defaultValue="")
	})
	@RequestMapping("/getEstPurAmount")
	@ResponseBody
	public JsonResult getEstPurAmount(String telNum){
	   
		
		
		return new JsonResult(JsonResult.SUCCESS, "请求成功", null);
	}
	
	
	
	@ApiOperation("获取组合的购买金额")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="",defaultValue="")
	})
	@RequestMapping("/getSingleAmountInPortfolio")
	@ResponseBody
	public JsonResult getSingleAmountInPortfolio(String telNum){
	   
		
		
		
		return new JsonResult(JsonResult.SUCCESS, "请求成功", null);
	}
	
	
}
