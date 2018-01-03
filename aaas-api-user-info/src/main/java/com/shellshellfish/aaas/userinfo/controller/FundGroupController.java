package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


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

	@ApiOperation("获取我的产品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = "shellshellfish"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = true, value = "产品ID", defaultValue = "41")})
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public Map getProductDetail(@RequestParam @NotNull String uuid, @RequestParam @NotNull Long prodId) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		String dateNow = df.format(new Date());
		
		Map result = fundGroupService.getGroupDetails(uuid, prodId);
		Map accumulationIncomes = new HashMap();
		accumulationIncomes.put("2011-01-09", 1.0);
		accumulationIncomes.put("2011-01-10", 1.0);
		accumulationIncomes.put("2011-01-11", 1.0);
		accumulationIncomes.put("2011-01-12", 1.0);
		accumulationIncomes.put("2011-01-13", 1.0);
		accumulationIncomes.put("2011-01-14", 1.0);
		accumulationIncomes.put("2011-01-15", 1.0);
		accumulationIncomes.put("2011-01-16", 1.0);

		result.put("accumulationIncomes", accumulationIncomes);
		return result;
	}
	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(df.format(new Date()));
	}
}