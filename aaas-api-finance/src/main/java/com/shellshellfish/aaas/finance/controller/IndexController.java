package com.shellshellfish.aaas.finance.controller;

import java.util.Map;
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
import com.shellshellfish.aaas.finance.model.ChartResource;
import com.shellshellfish.aaas.finance.service.IndexService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ssf-finance")
public class IndexController {

	private final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	IndexService indexService;

	@ApiOperation("理财产品 首页")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID"),
		@ApiImplicitParam(paramType = "query", name = "isTestFlag", dataType = "String", required = false, value = "是否测评"),
		@ApiImplicitParam(paramType = "query", name = "testResult", dataType = "String", required = false, value = "测评结果")
    })
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=204,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")        
    })	
	@RequestMapping(value = "/product-groups/homepage", method = RequestMethod.GET)
	public ResponseEntity<?> homepage(
			@RequestParam(value = "uuid",required = false) String uuid,
			@RequestParam(value = "isTestFlag",required = false) String isTestFlag,
			@RequestParam(value = "testResult",required = false) String testResult
//			@RequestParam(value = "productType") String productType
			) throws Exception {
		// FundReturn fundReturn =
		// assetAllocationService.selectById(groupId,subGroupId);
		Map<String, Object> resultMap = indexService.homepage(uuid, isTestFlag, testResult);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("理财产品 类型")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="risktype",dataType="String",required=true,value="风险类型",defaultValue="C1"),
//		@ApiImplicitParam(paramType="query",name="subGroupId",dataType="String",required=true,value="risk_income_interval_id",defaultValue="1")
    })
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=204,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")        
    })	
	@RequestMapping(value = "/product-groups/risktypes/{risktype}", method = RequestMethod.GET)
	public ResponseEntity<Map> getRiskInfo(
			@PathVariable String risktype
			) {
		Map<String, Object> resultMap = indexService.getRiskInfo(risktype);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
	
	@ApiOperation("理财产品 历史收益图")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=204,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")        
    })	
	//@RequestMapping(value = "/product-groups/homepage/charts/1", method = RequestMethod.GET)
	public ResponseEntity<ChartResource> getYieldChart() {
		ChartResource chartResource = indexService.getChart();
		return new ResponseEntity<>(chartResource, HttpStatus.OK);
	}

}
