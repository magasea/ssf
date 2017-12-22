package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.model.dto.HistoryList;
import com.shellshellfish.aaas.finance.service.GroupDetailsService;
import com.shellshellfish.aaas.finance.service.impl.GroupDetailsServiceImpl;
import com.shellshellfish.aaas.finance.trade.service.impl.OneFundApiService;
import io.swagger.annotations.*;
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


	@Autowired
	OneFundApiService oneFundApiService;

	@Autowired
	GroupDetailsService groupDetailsService;


	private static final Logger logger = LoggerFactory.getLogger(GroupDetailsController.class);


	@ApiOperation("组合详情 基金经理")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "name", defaultValue = "董阳阳")
	})
	@RequestMapping(value = "/getFundManager", method = {RequestMethod.GET})
	public String getFundManager(@RequestParam() @NotNull String name) {

		String fundManagerUrl = "/api/datamanager/getFundManager";

		Map params = new HashMap();
		params.put("name", name);
		return groupDetailsService.connectDataManager(fundManagerUrl,params);

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
		return groupDetailsService.connectDataManager(detailFundCompanyUrl, params);
	}

	@ApiOperation("组合详情 基金概况")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundInfo", method = {RequestMethod.GET})
	public String getFundInfo(@RequestParam() @NotNull String code) {
		String getFundInfoUrl = "/api/datamanager/getFundInfo";

		Map params = new HashMap();
		params.put("codes", code);
		return groupDetailsService.connectDataManager(getFundInfoUrl, params);
	}

	@ApiOperation("组合详情 基金公告")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "fundCode", dataType = "String", required = true, value = "fundCode", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundNotices", method = {RequestMethod.GET})
	public List getFundNotices(@RequestParam() @NotNull String fundCode) {
		try {
			return oneFundApiService.getFundNotices(fundCode);
		} catch (Exception e) {
			logger.error(e.getLocalizedMessage());
			return null;
		}
	}



	@ApiOperation("组合详情 历史净值")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getHistoryNetvalue", method = {RequestMethod.GET})
	public List<HistoryList> getHistoryNetvalue(@RequestParam() @NotNull String code, @RequestParam(defaultValue = "1") String period) {
		String getFundInfoUrl = "/api/datamanager/getHistoryNetvalue";

		Map params = new HashMap();
		params.put("code", code);
		params.put("period", period);
		return groupDetailsService.getHistoryList(getFundInfoUrl,params);
	}


	@ApiOperation("组合详情 基金详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "fundCode", dataType = "String", required = true, value = "组合详情，基金详情", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundValueInfo", method = {RequestMethod.GET})
	public String getFundValueInfo(@RequestParam() @NotNull String fundCode) {
		String getFundInfoUrl = "/api/datamanager/getFundValueInfo";

		Map params = new HashMap();
		params.put("code", fundCode);
		return groupDetailsService.connectDataManager(getFundInfoUrl, params);
	}


}
