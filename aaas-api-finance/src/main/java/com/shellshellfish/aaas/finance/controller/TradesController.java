package com.shellshellfish.aaas.finance.controller;

import java.util.Map;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.finance.model.dto.TradesBodyDTO;
import com.shellshellfish.aaas.finance.service.TradesService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/ssf-finance")
public class TradesController {

	private final Logger logger = LoggerFactory.getLogger(TradesController.class);

	@Autowired
	TradesService tradesService;

	@ApiOperation("购买页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="body",name="tradesBodyDTO",dataType="TradesBodyDTO",required=true,value="购买信息")
    })
	@ApiResponses({
		 @ApiResponse(code=200,message="OK"),
	     @ApiResponse(code=204,message="OK"),
	     @ApiResponse(code=400,message="请求参数没填好"),
	     @ApiResponse(code=401,message="未授权用户"),        				
	     @ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
	     @ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")  
    })	
	@RequestMapping(value = "/product-groups/{groupId}/sub-groups/{subGroupId}/trades", method = RequestMethod.POST)
	public ResponseEntity<?> getTrades(
			@Valid @NotNull(message="groupId不能为空") @PathVariable("groupId") String groupId,
			@Valid @NotNull(message="subGroupId不能为空") @PathVariable("subGroupId") String subGroupId,
			@Valid @RequestBody TradesBodyDTO tradesBodyDTO
			) {
		logger.debug("REST request to get trades.");
		Map<String, Object> resultMap = tradesService.getTrades(groupId,subGroupId,tradesBodyDTO);

		return new ResponseEntity<>(resultMap, HttpStatus.OK);
	}
}
