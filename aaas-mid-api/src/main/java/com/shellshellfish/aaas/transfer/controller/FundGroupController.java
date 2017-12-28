package com.shellshellfish.aaas.transfer.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);

	
	@ApiOperation("1.首页")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = "1") })
	@RequestMapping(value = "/getProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getProductDetail(@RequestParam String prodId) {
		return "{\n" +
				"  \"investDays\":\"5\",\n" +
				"  \"investDate\":\"2017-12-22\",\n" +
				"  \"amonut\":\"12000.00\",\n" +
				"  \"fundShareHolders\":\"500\",\n" +
				"  \"accruedIncome\":\"50.00\",\n" +
				"  \"todayIncome\":\"10\",\n" +
				"  \"cumulativeReturns\":\"10.00\",\n" +
				"  \"dividendsWay\":\"1\",\n" +
				"  \"everydayCumulativeReturns\":[\n" +
				"    {\n" +
				"      \"date\":\"2017-12-20\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-21\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-22\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-23\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-24\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-25\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-26\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    },\n" +
				"    {\n" +
				"      \"date\":\"2017-12-27\",\n" +
				"      \"value\":\"10.00\"\n" +
				"    }\n" +
				"  ],\n" +
				"  \"partIncomeRate\":{\n" +
				"    \"1\":\"2.00\",\n" +
				"    \"2\":\"2.00\",\n" +
				"    \"3\":\"2.00\",\n" +
				"    \"4\":\"2.00\",\n" +
				"    \"5:\":\"2.00\",\n" +
				"  }\n" +
				"}";
	}

}
