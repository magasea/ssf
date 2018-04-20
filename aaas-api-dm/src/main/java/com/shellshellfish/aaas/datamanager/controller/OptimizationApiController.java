package com.shellshellfish.aaas.datamanager.controller;

import com.shellshellfish.aaas.common.http.HttpJsonResult;
import com.shellshellfish.aaas.datamanager.repositories.MongoFinanceDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.datamanager.model.JsonResult;
import com.shellshellfish.aaas.datamanager.service.OptimizationService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
	@GetMapping(value = "/financeFrontPage")
	@ResponseBody
	public HttpJsonResult financeModule() {
		JsonResult jsonResult1 = optimizationService.financeFront(1);
		if (jsonResult1 != null) {
			logger.info("run OptimizationApiController.financeModule() success1..");
			System.out.println("run success");
			logger.info("贝贝鱼数据：OK");
		} else {
			return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到贝贝鱼数据产品", JsonResult.EMPTYRESULT);
		}
		
		jsonResult1 = optimizationService.financeFront(2);
		if (jsonResult1 != null) {
			logger.info("run OptimizationApiController.financeModule() success2..");
			System.out.println("run success");
			logger.info("兰州银行数据：OK");
		} else {
			return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到兰州银行数据产品", JsonResult.EMPTYRESULT);
		}
		return new HttpJsonResult (HttpStatus.OK.value(),"OK", JsonResult.EMPTYRESULT);
	}

	@ApiOperation("获取进入理财页面后的数据")
	@ApiImplicitParams({
      @ApiImplicitParam(paramType = "query", name = "size", dataType = "Integer", required = true, value = "每页显示数（至少大于1）", defaultValue = "15"),
      @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = true, value = "显示页数（从0开始）", defaultValue = "0"),
      @ApiImplicitParam(paramType = "query", name = "oemid", dataType = "oemid", required = true, value = "oemid", defaultValue = "1"),
    })
	@RequestMapping(value = "/getFinanceFrontPage", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getFinanceModule(@RequestParam(defaultValue="15") Integer size, @RequestParam(defaultValue="0") Integer pageSize, @RequestParam(defaultValue="1") Integer oemid) {
		JsonResult result = optimizationService.getFinanceFront(size, pageSize, oemid);
		if (result == null) {
			optimizationService.financeFront(oemid);
			result = optimizationService.getFinanceFront(size, pageSize, oemid);
		}
		logger.info("getFinanceFrontPage info: size:{}-pageSize:{}-Ok", size, pageSize);
		System.out.println("run success");
		return result;
	}
	
//	@ApiOperation("理财产品详情页面")
//	@GetMapping(value = "/checkPrdDetails")
//	@ResponseBody
	public HttpJsonResult prdDetails() {
		JsonResult jsonResult = null;
		Boolean result = true;
		mongoFinanceDetailRepository.deleteAll();
		
		for(int i = 1;i < 16; i++){
			String groupId = i + "";
			String subGroupId = i + "0048";
			jsonResult = optimizationService.checkPrdDetails(groupId, subGroupId);
			if (jsonResult != null) {
				logger.info(
						"run OptimizationApiController.getPrdDetails() success..");
				System.out.println("groupId：" + groupId + " , subGroupId:" + subGroupId + " -->OK");
//				return new JsonResult(JsonResult.SUCCESS, "OK", JsonResult.EMPTYRESULT);
			} else {
				result = false;
				return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到产品:subGroupId为-->"+subGroupId,
						JsonResult.EMPTYRESULT);
			}
		}
		if (result) {
			return new HttpJsonResult(HttpStatus.OK.value(), "OK", JsonResult.EMPTYRESULT);
		} else {
			return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到产品", JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("理财产品详情页面")
	@GetMapping(value = "/checkPrdDetails")
	@ResponseBody
	public HttpJsonResult prdDetails2() {
	  JsonResult jsonResult = null;
	  Boolean result = true;
	  mongoFinanceDetailRepository.deleteAll();
	  
	  for(int oemid = 1 ; oemid < 3;oemid++){
		  for(int i = 1;i < 16; i++){
			  String groupId = i + "";
			  String subGroupId = i + "0048";
			  jsonResult = optimizationService.checkPrdDetails2(groupId, subGroupId, oemid);
			  if(oemid == 1){
				  if (jsonResult != null) {
					  logger.info("贝贝鱼：groupId:{}-subGroupId:{}-Ok", groupId, subGroupId);
				  } else {
					  logger.warn("贝贝鱼：groupId:{}-subGroupId:{}-no data", groupId, subGroupId);
					  result = false;
					  break;
//					  return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:贝贝鱼没有获取到产品:subGroupId为-->"+subGroupId,
//							  JsonResult.EMPTYRESULT);
				  }
			  } else if(oemid == 2) {
				  if (jsonResult != null) {
					  logger.info("兰州银行：groupId:{}-subGroupId:{}-Ok", groupId, subGroupId);
				  } else {
					  logger.warn("兰州银行：groupId:{}-subGroupId:{}-no data", groupId, subGroupId);
					  result = false;
					  break;
//					  return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:兰州银行没有获取到产品:subGroupId为-->"+subGroupId,
//							  JsonResult.EMPTYRESULT);
				  }
			  }
		  }
	  }
	  
	  if (result) {
//	    return new HttpJsonResult(HttpStatus.OK.value(), "OK", jsonResult);
	    return new HttpJsonResult(HttpStatus.OK.value(), "OK", JsonResult.EMPTYRESULT);
	  } else {
	    return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到产品", JsonResult.EMPTYRESULT);
	  }
	}
	
	@ApiOperation("获取理财产品详情页面的数据")
	@RequestMapping(value = "/getCheckPrdDetails", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getPrdDetails(String groupId, String subGroupId, Integer oemid) {
		JsonResult result = optimizationService.getPrdDetails(groupId, subGroupId, oemid);
		if (result == null) {
			this.prdDetails();
			result = optimizationService.getPrdDetails(groupId, subGroupId, oemid);
		}
		logger.info("getCheckPrdDetails info: groupId:{}-subGroupId:{}-Ok", groupId, subGroupId);
		System.out.println("run success");
		return result;
	}
}
