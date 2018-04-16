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
		JsonResult jsonResult = optimizationService.financeFront();
		if (jsonResult != null) {
			logger.info("run OptimizationApiController.financeModule() success..");
			System.out.println("run success");
			return new HttpJsonResult (HttpStatus.OK.value(),"OK", JsonResult.EMPTYRESULT);
		} else {
			return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到产品", JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("获取进入理财页面后的数据")
	@ApiImplicitParams({
      @ApiImplicitParam(paramType = "query", name = "size", dataType = "Integer", required = true, value = "每页显示数（至少大于1）", defaultValue = "15"),
      @ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = true, value = "显示页数（从0开始）", defaultValue = "0"),
    })
	@RequestMapping(value = "/getFinanceFrontPage", method = RequestMethod.GET)
	@ResponseBody
	public JsonResult getFinanceModule(@RequestParam(defaultValue="15") Integer size, @RequestParam(defaultValue="0") Integer pageSize) {
		JsonResult result = optimizationService.getFinanceFront(size, pageSize);
		if (result == null) {
			optimizationService.financeFront();
			result = optimizationService.getFinanceFront(size, pageSize);
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
	  
	  for(int i = 1;i < 16; i++){
	    String groupId = i + "";
	    String subGroupId = i + "0048";
	    jsonResult = optimizationService.checkPrdDetails2(groupId, subGroupId);
	    if (jsonResult != null) {
	      logger.info("groupId:{}-subGroupId:{}-Ok", groupId, subGroupId);
//	      System.out.println("groupId：" + groupId + " , subGroupId:" + subGroupId + " -->OK");
//				return new JsonResult(JsonResult.SUCCESS, "OK", JsonResult.EMPTYRESULT);
	    } else {
	      logger.warn("groupId:{}-subGroupId:{}-no data", groupId, subGroupId);
	      result = false;
	      return new HttpJsonResult(HttpStatus.NOT_FOUND.value(), "NG:没有获取到产品:subGroupId为-->"+subGroupId,
	          JsonResult.EMPTYRESULT);
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
	public JsonResult getPrdDetails(String groupId, String subGroupId) {
		JsonResult result = optimizationService.getPrdDetails(groupId, subGroupId);
		if (result == null) {
			this.prdDetails2();
			result = optimizationService.getPrdDetails(groupId, subGroupId);
		}
		logger.info("getCheckPrdDetails info: groupId:{}-subGroupId:{}-Ok", groupId, subGroupId);
		System.out.println("run success");
		return result;
	}
}
