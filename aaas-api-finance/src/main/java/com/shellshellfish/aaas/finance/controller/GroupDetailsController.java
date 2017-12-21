package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.trade.service.impl.OneFundApiService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author pierre
 * 17-12-21
 */

@RestController
@RequestMapping("/api/ssf-finance")
public class GroupDetailsController {

	@Value("${api-data-manager-url}")
	private String dataManagerBaseUrl;


	@Autowired
	RestTemplate restTemplate;


	@Autowired
	OneFundApiService oneFundApiService;


	private  static  final Logger logger =  LoggerFactory.getLogger(GroupDetailsController.class);


	@ApiOperation("组合详情 基金经理")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "name", defaultValue = "董阳阳")
	})
	@RequestMapping(value = "/getFundManager", method = {RequestMethod.GET})
	public String getFundManager(@RequestParam() @NotNull String name) {

		String fundManagerUrl = "/api/datamanager/getFundManager";

		Map params = new HashMap();
		params.put("name", name);
		return connectDataManager(fundManagerUrl, params);

	}

	@ApiOperation("组合详情 基金公司")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "name", defaultValue = "董阳阳")
	})
	@RequestMapping(value = "/getFundCompany", method = {RequestMethod.GET})
	public String getFundCompany(@RequestParam() @NotNull String name) {

		String detailFundCompanyUrl = "/api/datamanager/getFundCompany";
		Map params = new HashMap();
		params.put("name", name);
		return connectDataManager(detailFundCompanyUrl, params);
	}

	@ApiOperation("组合详情 基金概述")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundInfo", method = {RequestMethod.GET})
	public String getFundInfo(@RequestParam() @NotNull String code) {
		String getFundInfoUrl = "/api/datamanager/getFundInfo";

		Map params = new HashMap();
		params.put("codes", code);
		return connectDataManager(getFundInfoUrl, params);
	}

	//@TODO
	@ApiOperation("组合详情 基金公告")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "fundCode", dataType = "String", required = true, value = "fundCode", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundNotices", method = {RequestMethod.GET})
	public List getFundNotices(@RequestParam() @NotNull String fundCode){
		try {
			return oneFundApiService.getFundNotices(fundCode);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}
	}

	private String  connectDataManager(String methodUrl, Map params) {
		ResponseEntity<String> entity =
				restTemplate.getForEntity(prepareParameters(dataManagerBaseUrl +methodUrl,params), String.class);
		if (entity != null && entity.getStatusCode().equals(HttpStatus.OK))
			return entity.getBody();

		return null;
	}
	private static String prepareParameters(String url ,Map<String,String> params){
		url+="?";
		for (Map.Entry<String, String> entry : params.entrySet()) {
			url+= entry.getKey() + "=" + entry.getValue()+"&";
		}
		return url.substring(0,url.lastIndexOf("&"));

	}

	public static void main(String[] args) {
		String rul = "123";
		Map map = new HashMap();
		map.put("key","valeu");
		map.put("hah","jh");
		System.out.println(prepareParameters(rul,map));
	}

}
