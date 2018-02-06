package com.shellshellfish.aaas.userinfo.controller;

import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;
import com.shellshellfish.aaas.userinfo.model.MonetaryFund;
import com.shellshellfish.aaas.userinfo.service.FundGroupService;
import com.shellshellfish.aaas.userinfo.service.MonetaryFundsService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author pierre 17-12-28
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
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户UUID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "buyDate", dataType = "String", required = false, value = "买入时间"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "Long", required = true, value = "产品ID", defaultValue = "") })
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public ResponseEntity<Map> getProductDetail(@RequestParam @NotNull String uuid,
			@RequestParam(required = false) String buyDate, @RequestParam @NotNull Long prodId) throws ParseException {
		Map result = fundGroupService.getGroupDetails(uuid, prodId, buyDate);
		result.put("prodId", prodId);
		return new ResponseEntity<Map>(result, HttpStatus.OK);
	}

	@ApiOperation("获取货币型基金的7天年化收益和万分收益")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金代码", defaultValue = "400005.OF"),
			@ApiImplicitParam(paramType = "query", name = "startDate", dataType = "Long", required = true, value = "开始时间", defaultValue = "1420387200"),
			@ApiImplicitParam(paramType = "query", name = "endDate", dataType = "Long", required = true, value = "结束时间", defaultValue = "1516276950940")
	})
	@RequestMapping(value = "/getGrowthRateOfMonetaryFundsList", method = RequestMethod.POST)
	public ResponseEntity<List<MonetaryFund>> getGrowthRateOfMonetaryFundsList(
			@RequestParam @NotNull String code, @RequestParam @NotNull Long startDate,
			@RequestParam Long endDate) {

		List<GrowthRateOfMonetaryFund> growthRateOfMonetaryFunds = monetaryFundsService
				.getGrowthRateOfMonetaryFundsList(code, startDate, endDate);

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