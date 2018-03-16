package com.shellshellfish.aaas.finance.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.service.IndexService;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ssf-finance")
public class WareHouseController {

	private final Logger logger = LoggerFactory.getLogger(WareHouseController.class);

	@Autowired
	UserProdChangeLogService userProdChangeLogService;

	@ApiOperation("调仓记录")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = false, value = "用户groupId"),
//		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = false, value = "用户subGroupId"),
//		@ApiImplicitParam(paramType = "query", name = "testResult", dataType = "String", required = false, value = "测评结果")
    })
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=204,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")        
    })	
	@RequestMapping(value = "/product-groups/warehouse-records", method = RequestMethod.GET)
	public ResponseEntity<?> warehouseRecords(
			@RequestParam(value = "prodId",required = false) Long prodId
//			@RequestParam(value = "groupId",required = false) Long groupId
//			@RequestParam(value = "testResult",required = false) String testResult
			) throws Exception {
	  List<UserProdChg> resultList = userProdChangeLogService.getGeneralChangeLogs(prodId);
	  Map<String, Object> result = new HashMap<String, Object>();
	  result.put("result", resultList);
	  return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@ApiOperation("调仓记录-详细页面")
	@ApiImplicitParams({
	  @ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = false, value = "用户groupId"),
	  @ApiImplicitParam(paramType = "query", name = "modifySeq", dataType = "Integer", required = false, value = "选择其对应的Seq"),
//		@ApiImplicitParam(paramType = "query", name = "isTestFlag", dataType = "String", required = false, value = "是否测评"),
//		@ApiImplicitParam(paramType = "query", name = "testResult", dataType = "String", required = false, value = "测评结果")
	})
	@ApiResponses({
	  @ApiResponse(code=200,message="OK"),
	  @ApiResponse(code=204,message="OK"),
	  @ApiResponse(code=400,message="请求参数没填好"),
	  @ApiResponse(code=401,message="未授权用户"),        				
	  @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
	  @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")        
	})	
	@RequestMapping(value = "/product-groups/warehouse-record-details", method = RequestMethod.GET)
	public ResponseEntity<?> warehouseRecordDetails(
	    @RequestParam(value = "prodId",required = false) Long prodId,
	    @RequestParam(value = "modifySeq",required = false) Integer modifySeq
//			@RequestParam(value = "isTestFlag",required = false) String isTestFlag,
//			@RequestParam(value = "testResult",required = false) String testResult
	    ) throws Exception {
	  List<Map> resultList = userProdChangeLogService.getWarehouseRecords(prodId, modifySeq);
	  Map<String,List> result = new HashMap<>();
	  result.put("result", resultList);
	  return new ResponseEntity<>(result, HttpStatus.OK);
	}
}
