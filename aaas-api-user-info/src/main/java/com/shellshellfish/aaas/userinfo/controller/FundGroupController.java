package com.shellshellfish.aaas.userinfo.controller;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;
import com.shellshellfish.aaas.userinfo.model.MonetaryFund;
import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.MonetaryFundsService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.utils.DateUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @Author pierre
 * 17-12-28
 */

@RestController
@RequestMapping("/api/userinfo")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);


	@Autowired
	FundGroupService fundGroupService;

	@Autowired
	UserFinanceProdCalcService userFinanceProdCalcService;

	@Autowired
	MonetaryFundsService monetaryFundsService;

	@ApiOperation("获取我的产品详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = "shellshellfish"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = true, value = "产品ID", defaultValue = "41")})
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map> getProductDetail(@RequestParam @NotNull String uuid, @RequestParam @NotNull Long prodId) {
		List resultList = new ArrayList();
		int days = 6; //计算6天的值
		Map result = fundGroupService.getGroupDetails(uuid, prodId);
		//遍历赋值
		for (int i = 0; i < days; i++) {
			Map dateValueMap = new HashMap<>();
			String selectDate = DateUtil.getSystemDatesAgo(-i);
			String dayBeforeSelectDate = DateUtil.getSystemDatesAgo(-i - 1);
			dateValueMap.put("time", selectDate);
			//调用对应的service
			BigDecimal rate = userFinanceProdCalcService.calcYieldRate(uuid, prodId, dayBeforeSelectDate, selectDate);
			dateValueMap.put("value", rate);
			resultList.add(dateValueMap);
		}
		result.put("accumulationIncomes", resultList);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}


	@ApiOperation("获取货币型基金的7天年化收益和万分收益")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金代码", defaultValue = "400005.OF"),
			@ApiImplicitParam(paramType = "query", name = "startDate", dataType = "Long", required = true, value = "开始时间", defaultValue = "1420387200"),
			@ApiImplicitParam(paramType = "query", name = "endDate", dataType = "Long", required = true, value = "结束时间", defaultValue = "1516276950940")
	})
	@RequestMapping(value = "/getGrowthRateOfMonetaryFundsList", method = RequestMethod.POST)
	public ResponseEntity<List<MonetaryFund>> getGrowthRateOfMonetaryFundsList(@RequestParam @NotNull String code, @RequestParam @NotNull Long startDate, @RequestParam Long endDate) {

		List<GrowthRateOfMonetaryFund> growthRateOfMonetaryFunds = monetaryFundsService.getGrowthRateOfMonetaryFundsList(code, startDate, endDate);

		List<MonetaryFund> result = new ArrayList<>(growthRateOfMonetaryFunds.size());
		copyProperties(growthRateOfMonetaryFunds, result);

		return new ResponseEntity<>(result, HttpStatus.OK);
	}


	private void copyProperties(List<GrowthRateOfMonetaryFund> src, List<MonetaryFund> dest) {
		for (GrowthRateOfMonetaryFund growthRateOfMonetaryFund : src) {
			MonetaryFund monetaryFund = new MonetaryFund();
			monetaryFund.setCode(growthRateOfMonetaryFund.getCode());
			monetaryFund.setQueryDate(growthRateOfMonetaryFund.getQueryDate());
			monetaryFund.setQueryDateStr(growthRateOfMonetaryFund.getQueryDateStr());
			monetaryFund.setTenKiloUnitYield(growthRateOfMonetaryFund.getTenKiloUnitYield());
			monetaryFund.setUpdate(growthRateOfMonetaryFund.getUpdate());
			monetaryFund.setYieldof7days(growthRateOfMonetaryFund.getYieldof7Days());

			dest.add(monetaryFund);
		}


	}
}