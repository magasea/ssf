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
import com.shellshellfish.datamanager.repositories.MongoFinanceDetailRepository;
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
	
	@Autowired
	MongoFinanceDetailRepository mongoFinanceDetailRepository;

	@ApiOperation("进入理财页面后的数据")
	@RequestMapping(value = "/financeFrontPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult financeModule() {
		JsonResult jsonResult = optimizationService.financeFront();
		if (jsonResult != null) {
			logger.info(
					"run com.shellshellfish.datamanager.controller.OptimizationApiController.financeModule() success..");
			System.out.println("run success");
			return new JsonResult(JsonResult.SUCCESS, "OK", JsonResult.EMPTYRESULT);
		} else {
			return new JsonResult(JsonResult.Fail, "NG:没有获取到产品", JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("获取进入理财页面后的数据")
	@RequestMapping(value = "/getFinanceFrontPage", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getFinanceModule() {
		JsonResult result = optimizationService.getFinanceFront();
		if (result == null) {
			optimizationService.financeFront();
			result = optimizationService.getFinanceFront();
			logger.info(
					"run com.shellshellfish.datamanager.controller.OptimizationApiController.getFinanceModule() success..");
		} else {
			logger.info(
					"run com.shellshellfish.datamanager.controller.OptimizationApiController.getFinanceModule() fail..");
		}
		System.out.println("run success");
		return result;
	}
	
	@ApiOperation("理财产品详情页面")
	@RequestMapping(value = "/checkPrdDetails", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult prdDetails() {
		JsonResult jsonResult = null;
		Boolean result = true;
//		mongoFinanceDetailRepository.deleteAll();
		for(int i = 1;i < 16; i++){
			String groupId = i + "";
			String subGroupId = i + "0048";
			jsonResult = optimizationService.checkPrdDetails(groupId, subGroupId);
			if (jsonResult != null) {
				logger.info(
						"run com.shellshellfish.datamanager.controller.OptimizationApiController.getPrdDetails() success..");
				System.out.println("groupId：" + groupId + " , subGroupId:" + subGroupId + " -->OK");
//				return new JsonResult(JsonResult.SUCCESS, "OK", JsonResult.EMPTYRESULT);
			} else {
				result = false;
				return new JsonResult(JsonResult.Fail, "NG:没有获取到产品:subGroupId为-->"+subGroupId, JsonResult.EMPTYRESULT);
			}
		}
		if (result) {
			return new JsonResult(JsonResult.SUCCESS, "OK", JsonResult.EMPTYRESULT);
		} else {
			return new JsonResult(JsonResult.Fail, "NG:没有获取到产品", JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("获取理财产品详情页面的数据")
	@RequestMapping(value = "/getCheckPrdDetails", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getPrdDetails(String groupId, String subGroupId) {
		JsonResult result = optimizationService.getPrdDetails(groupId, subGroupId);
		if (result == null) {
			this.prdDetails();
			result = optimizationService.getPrdDetails(groupId, subGroupId);
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
