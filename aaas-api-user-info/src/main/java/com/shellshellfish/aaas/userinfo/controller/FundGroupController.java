package com.shellshellfish.aaas.userinfo.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;


/**
 * @Author pierre
 * 17-12-28
 */

@RestController
@RequestMapping("/api/userinfo")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);


	@Autowired
	FundGroupService fundGroupService;
	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;
	

	@ApiOperation("获取我的产品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = "shellshellfish"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = true, value = "产品ID", defaultValue = "41")})
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map> getProductDetail(@RequestParam @NotNull String uuid, @RequestParam @NotNull Long prodId) {
		List resultList=new ArrayList();
		String[] datesSelected=new String[6];
		Map result = fundGroupService.getGroupDetails(uuid, prodId);
		//遍历赋值
		for(int i=0;i<datesSelected.length;i++){
			Map dateValueMap=new HashMap<>();
			datesSelected[i]=DateUtil.getSystemDatesAgo(-i);
			dateValueMap.put("time",datesSelected[i]);
		//调用对应的service
		BigDecimal rate=userFinanceProdCalcService.calcYieldRate(uuid, prodId, datesSelected[i]	, datesSelected[i]);
		dateValueMap.put("value",rate);
		resultList.add(dateValueMap);
		}
		result.put("accumulationIncomes",resultList);
		return new ResponseEntity<Map>(result,HttpStatus.OK);
	}
}