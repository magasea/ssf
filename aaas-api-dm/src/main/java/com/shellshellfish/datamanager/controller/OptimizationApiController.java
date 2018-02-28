package com.shellshellfish.datamanager.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.datamanager.model.JsonResult;
import com.shellshellfish.datamanager.service.OptimizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Configuration
@RestController
@RequestMapping("api/datamanager")
@Validated
@Api("基金数据管理相关restapi")
public class OptimizationApiController {

	Logger logger = LoggerFactory.getLogger(OptimizationApiController.class);

	@Autowired
	OptimizationService optimizationService;

	@ApiOperation("进入理财页面后的数据")
	@RequestMapping(value = "/financeFrontPage", method = RequestMethod.POST)
	@ResponseBody
	public void financeModule() {
		optimizationService.financeFront();
		logger.info(
				"run com.shellshellfish.datamanager.controller.OptimizationApiController.financeModule() success..");
		System.out.println("run success");
	}

	@ApiOperation("进入理财页面后的数据")
	@RequestMapping(value = "/getFinanceFrontPage", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getFinanceModule() {
		JsonResult result = optimizationService.getFinanceFront();
		if (result != null) {
			logger.info(
					"run com.shellshellfish.datamanager.controller.OptimizationApiController.getFinanceModule() success..");
		} else {
			logger.info(
					"run com.shellshellfish.datamanager.controller.OptimizationApiController.getFinanceModule() fail..");
		}
		System.out.println("run success");
		return result;
	}
}
