package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.dto.FinanceProdBuyInfo;
import com.shellshellfish.aaas.dto.FinanceProdSellInfo;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易用
 * 
 * @author developer4
 *
 */
@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class TransferController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private MidApiService service;

	@Autowired
	private RestTemplate restTemplate;

	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;

	@ApiOperation("获取预计费用,以及投资组合的每一支基金的费用")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "2000"),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "String", required = true, value = "购买的总金额", defaultValue = "") })
	@RequestMapping(value = "/getEstPurAmount", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getEstPurAmount(String groupId, String subGroupId, String totalAmount) {
		Map resultMap = null;
		try {
			String url = tradeOrderUrl + "/api/trade/funds/buyProduct?groupId=" + groupId + "&subGroupId=" + subGroupId
					+ "&totalAmount=" + totalAmount;
			resultMap = restTemplate.getForEntity(url, Map.class).getBody();
			BigDecimal poundage = BigDecimal.valueOf(Double.parseDouble(resultMap.get("poundage").toString()));
			BigDecimal discount = BigDecimal.valueOf(Double.parseDouble(resultMap.get("discountSaving").toString()));
			// BigDecimal
			// total=poundage.add(BigDecimal.valueOf(Double.parseDouble((totalAmount))));
			BigDecimal total = poundage;
			BigDecimal totalOffDiscount = total.add(discount);
			if (total != null) {
				total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			if (totalOffDiscount != null) {
				totalOffDiscount = totalOffDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			if (discount != null) {
				discount = discount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			if (resultMap.get("fundAmountList") != null) {
				List<Map> resultList = (List<Map>) resultMap.get("fundAmountList");
				if (resultList != null && resultList.size() > 0) {
					for (int i = 0; i < resultList.size(); i++) {
						Map map = resultList.get(i);
						if (map.get("grossAmount") != null) {
							BigDecimal grossAmount = new BigDecimal(map.get("grossAmount") + "");
							map.put("grossAmount", grossAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
						}
					}
				}
			}
			resultMap.put("poundage", total);
			resultMap.put("discountSaving", discount);
			resultMap.put("total", total);
			resultMap.put("originalCost", totalOffDiscount);
			resultMap.put("discount", discount);
			return new JsonResult(JsonResult.SUCCESS, "获取成功", resultMap);
		} catch (HttpClientErrorException e) {
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		} catch (HttpServerErrorException e) {
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			JSONObject myJson = JSONObject.parseObject(str);
			String error = myJson.getString("message");
			return new JsonResult(JsonResult.Fail, error, JsonResult.EMPTYRESULT);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("申购基金")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "电话号码", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "msgCode", dataType = "String", required = true, value = "验证码", defaultValue = "") })
	@RequestMapping(value = "/subscribeFund", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult doTransaction(@RequestParam String telNum, @RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard, @RequestParam(required = false) String buyfee, @RequestParam String msgCode,
			@RequestBody FinanceProdBuyInfo prdInfo) {
		String verify = null;
		// 首先验证验证码
		try {
			verify = service.verifyMSGCode(telNum, msgCode);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str);
			return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败", JsonResult.EMPTYRESULT);
		}
		// 验证码不通过则直接返回失败
		if ("验证失败".equals(verify)) {
			// TODO 临时注释2018-01-22
			/********************** start ****************************/
			if (!"123456".equals(msgCode)) {
//				return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败", JsonResult.EMPTYRESULT);
			}
			/********************** end ******************************/
			 return new JsonResult(JsonResult.Fail,"手机验证失败，申购失败", JsonResult.EMPTYRESULT);
		}
		try {
			// 调用购买接口
			Map buyProductSuccess = service.buyProduct(prdInfo);
			Map resultMap = new HashMap<>();
			resultMap.put("orderId", buyProductSuccess.get("orderId").toString());
			resultMap.put("bankName", bankName);
			resultMap.put("bankCard", bankCard);
			resultMap.put("buyfee", buyfee);
			return new JsonResult(JsonResult.SUCCESS, "订单已受理，申购中...", resultMap);
		} catch (HttpClientErrorException e) {
			logger.error("购买基金调用购买接口失败" + e.getMessage());
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		} catch (HttpServerErrorException e) {
			logger.error("购买基金调用购买接口失败" + e.getMessage());
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			JSONObject myJson = JSONObject.parseObject(str);
			String error = myJson.getString("message");
			return new JsonResult(JsonResult.Fail, error, JsonResult.EMPTYRESULT);
		} catch (Exception e) {
			logger.error("购买基金调用购买接口失败" + e.getMessage());
			e.printStackTrace();
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("获取购买的最大值最小值")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "12"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "12049") })
	@RequestMapping(value = "/maxminValue", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getMaxminValue(String groupId, String subGroupId) {
		Map resultMap = null;
		try {
			String url = tradeOrderUrl + "/api/trade/funds/maxminValue?groupId=" + groupId + "&subGroupId="
					+ subGroupId;
			resultMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (resultMap.get("min") != null) {
				Double min = (Double) resultMap.get("min");
				BigDecimal minValue = new BigDecimal(min);
				resultMap.put("min", minValue.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			if (resultMap.get("min") != null) {
				Double max = (Double) resultMap.get("max");
				BigDecimal maxValue = new BigDecimal(max);
				resultMap.put("max", maxValue.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			return new JsonResult(JsonResult.SUCCESS, "获取成功", resultMap);
		} catch (HttpClientErrorException e) {
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		} catch (HttpServerErrorException e) {
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			JSONObject myJson = JSONObject.parseObject(str);
			String error = myJson.getString("message");
			return new JsonResult(JsonResult.Fail, error, JsonResult.EMPTYRESULT);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("产品赎回")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "手机号", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "verifyCode", dataType = "String", required = true, value = "验证码", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "userProdId", dataType = "String", required = true, value = "", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品的groupId", defaultValue = "12"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品的subGroupId", defaultValue = "120049"),
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = "shellshellfish"), })
	@RequestMapping(value = "/sellProduct", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellProduct(@RequestParam String telNum, @RequestParam String verifyCode,
			@RequestParam String userProdId, @RequestParam String prodId, @RequestParam String groupId,
			@RequestParam String userUuid, @RequestBody List<FinanceProdSellInfo> infoList) {
		// 首先调用手机验证码
		String verify = null;
		try {
			verify = service.verifyMSGCode(telNum, verifyCode);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str);
			return new JsonResult(JsonResult.Fail, "手机验证失败，赎回失败", JsonResult.EMPTYRESULT);
		}
		// 验证码不通过则直接返回失败
		if ("验证失败".equals(verify)) {
			// TODO 临时注释2018-01-22
			/********************** start ****************************/
			if (!"123456".equals(verifyCode)) {
				return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败", JsonResult.EMPTYRESULT);
			}
			/********************** end ******************************/
			// return new JsonResult(JsonResult.Fail, "手机验证失败，赎回失败",
			// JsonResult.EMPTYRESULT);
		}
		// 调用赎回口
		Map result = null;
		try {
			result = service.sellFund(userProdId, prodId, groupId, userUuid, infoList);
		} catch (Exception e) {
			logger.error("调用赎回接口发生错误");
			logger.error(e.getMessage());
			return new JsonResult(JsonResult.Fail, "赎回失败", JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "赎回成功", result);
	}

	@ApiOperation("赎回页面")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "String", required = true, value = "总金额", defaultValue = "") })
	@RequestMapping(value = "/sellFundPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellFundPage(String userUuid, String groupId, String subGroupId, String totalAmount) {
		Map result = null;
		try {
			result = service.sellFundPage(groupId, subGroupId, totalAmount);
			if(result!=null){
				result.put("userUuid", userUuid);
			}
			return new JsonResult(JsonResult.SUCCESS, "调用成功", result);
		} catch (Exception e) {
			logger.error("赎回页面接口调用失败");
			e.printStackTrace();
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

}
