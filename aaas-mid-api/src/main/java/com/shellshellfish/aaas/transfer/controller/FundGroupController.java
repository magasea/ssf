package com.shellshellfish.aaas.transfer.controller;

import com.shellshellfish.aaas.common.utils.URLutils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);

	@Autowired
	RestTemplate restTemplate;


	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;


	@ApiOperation("我的智投组合详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = "1")})
	@RequestMapping(value = "/getProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public String getProductDetail(@RequestParam String prodId) {

		String methodUrl = "/api/userinfo/getProductDetail";
		Map<String, String> params = new HashMap(1);
		params.put("prodId", prodId);
		ResponseEntity<String> entity = restTemplate.postForEntity(URLutils.prepareParameters(userinfoUrl + methodUrl, params),HttpEntity.EMPTY, String.class,params);
		if (HttpStatus.OK.equals(entity.getStatusCode())) {
			return entity.getBody();
		}

		logger.error("errorCode:" + entity.getStatusCode() + "\nbody:" + entity.getBody());
		return null;
	}

}
