package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.model.dto.FundCompany;
import com.shellshellfish.aaas.finance.model.dto.FundManager;
import com.shellshellfish.aaas.finance.model.dto.HistoryList;
import com.shellshellfish.aaas.finance.service.GroupDetailsService;
import com.shellshellfish.aaas.finance.trade.model.FundIncome;
import com.shellshellfish.aaas.finance.trade.service.impl.OneFundApiService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
	public FundManager getFundManager(@RequestParam() @NotNull String name) {

		String fundManagerUrl = "/api/datamanager/getFundManager";

		Map params = new HashMap();
		params.put("name", name);
		return groupDetailsService.getFundManager(fundManagerUrl, params);

	}

	@ApiOperation("组合详情 基金公司")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "name", defaultValue = "天弘基金管理有限公司")
	})
	@RequestMapping(value = "/getFundCompany", method = {RequestMethod.GET})
	public FundCompany getFundCompany(@RequestParam() @NotNull String name) {

		String detailFundCompanyUrl = "/api/datamanager/getFundCompany";
		Map params = new HashMap();
		params.put("name", name);
		return groupDetailsService.getFundCompany(detailFundCompanyUrl, params);
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
		return groupDetailsService.getHistoryList(getFundInfoUrl, params);
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

	@ApiOperation("获取基金日收益")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "用户UUID", defaultValue = "shellshellfish"),
			@ApiImplicitParam(paramType = "query", name = "fundCode", dataType = "String", required = true, value = "基金编码", defaultValue = "002163")
	})
	@RequestMapping(value = "/getFundIncome", method = {RequestMethod.GET})
	public FundIncome getFundIncome(@RequestParam() @NotNull String userUuid, @RequestParam() @NotNull String fundCode) {

		try {
			return oneFundApiService.getFundIncome(userUuid,fundCode);
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
		return null;
	}


}
