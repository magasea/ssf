package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.utils.EasyKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("智投组合相关接口")
public class FundGroupController {

	Logger logger = LoggerFactory.getLogger(FundGroupController.class);

	@Autowired
	RestTemplate restTemplate;

	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;

	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@ApiOperation("我的智投组合详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "12"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "120049"),
			@ApiImplicitParam(paramType = "query", name = "buyDate", dataType = "String", required = false, value = "买入时间"),
			@ApiImplicitParam(paramType = "query", name = "totals", dataType = "String", required = false, value = "组合资产"),
			@ApiImplicitParam(paramType = "query", name = "totalIncome", dataType = "String", required = false, value = "累计收益"),
			@ApiImplicitParam(paramType = "query", name = "totalIncomeRate", dataType = "String", required = false, value = "累计收益率") })
	@RequestMapping(value = "/getMyProductDetail", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getProductDetail(@RequestParam String uuid, @RequestParam String prodId,
			@RequestParam String groupId, @RequestParam String subGroupId,
			@RequestParam(required = false) String buyDate, @RequestParam(required = false) String totals,
			@RequestParam(required = false) String totalIncome,
			@RequestParam(required = false) String totalIncomeRate) {

		Map result = null;

		String methodUrl = "/api/userinfo/getMyProductDetail";
		Map<String, String> params = new HashMap(2);

		params.put("uuid", uuid);
		params.put("buyDate", buyDate);
		params.put("prodId", prodId);

		ResponseEntity<Map> entity = restTemplate.postForEntity(
				URLutils.prepareParameters(userinfoUrl + methodUrl, params), HttpEntity.EMPTY, Map.class, params);
		if (HttpStatus.OK.equals(entity.getStatusCode())) {
			result = entity.getBody();
			result.put("groupId", groupId);
			result.put("subGroupId", subGroupId);
			result.put("buyDate", buyDate == null ? "" : buyDate);
			result.put("totals", totals == null ? "" : totals);
			result.put("totalIncome", totalIncome == null ? "" : totalIncome);
			result.put("totalIncomeRate", totalIncomeRate == null ? "" : totalIncomeRate);
			Map bankNumResult = restTemplate
					.getForEntity(tradeOrderUrl + "/api/trade/funds/banknums/" + uuid + "?prodId=" + prodId, Map.class)
					.getBody();
			if (bankNumResult.get("bankNum") != null) {
				String bankNum = bankNumResult.get("bankNum") + "";
				String bankName = "";
				String bankShortNum = "";
				String telNum = "";
				List bankList = restTemplate
						.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/bankcards", List.class).getBody();
				if (bankList != null) {
					for (int i = 0; i < bankList.size(); i++) {
						Map bankMap = (Map) bankList.get(i);
						if (bankNum.equals(bankMap.get("bankcardNum"))) {
							if (bankMap.get("bankShortName") != null) {
								bankName = bankMap.get("bankShortName") + "";
								// String bankcardSecurity[] =
								// (bankMap.get("bankcardSecurity").toString()).split("
								// ");
								// bankNum =
								// bankcardSecurity[bankcardSecurity.length-1];
								// bankNum =
								// bankNum.substring(bankNum.length()-4);
								bankShortNum = bankNum.substring(bankNum.length() - 4);
								telNum = bankMap.get("cellphone") + "";
								break;
							}
						}
					}
					result.put("bankNum", bankNum);
					result.put("telNum", telNum);
					result.put("bankName", bankName);
				}
			}

			if (result.get("accumulationIncomes") != null) {
				List<Map> accumulationIncomesList = (List<Map>) result.get("accumulationIncomes");
				if (accumulationIncomesList != null) {
					List<Double> maxMinValueList = new ArrayList<Double>();
					for (int i = 0; i < accumulationIncomesList.size(); i++) {
						Map accumulationIncomesMap = accumulationIncomesList.get(i);
						if (accumulationIncomesMap.get("value") != null) {
							BigDecimal value = new BigDecimal(accumulationIncomesMap.get("value")+"");
							accumulationIncomesMap.put("value", EasyKit.getDecimal(value));
							maxMinValueList.add(Double.parseDouble(accumulationIncomesMap.get("value") + ""));
						}
					}
					if (maxMinValueList != null && maxMinValueList.size() > 0) {
						result.put("maxValue", Collections.max(maxMinValueList));
						result.put("minValue", Collections.min(maxMinValueList));
					}
				}
			}

		} else {
			logger.error("error code : {} ; error message :{}", entity.getStatusCode(), entity.getBody());
			return new JsonResult(JsonResult.Fail, "获取失败", JsonResult.EMPTYRESULT);
		}

		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

}
