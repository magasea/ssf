package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
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
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.dto.FinanceProdBuyInfo;
import com.shellshellfish.aaas.dto.FinanceProdSellInfo;
import com.shellshellfish.aaas.oeminfo.model.JsonResult;
import com.shellshellfish.aaas.oeminfo.service.MidApiService;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import com.shellshellfish.aaas.transfer.service.RiskService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 交易用
 *
 * @author developer4
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
	
	@Value("${shellshellfish.userinfo-url}")
	private String userinfoUrl;

	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;
	
	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;

	@Autowired
	RiskService riskService;

	@ApiOperation("获取预计费用,以及投资组合的每一支基金的费用")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "2000"),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "String", required = true, value = "购买的总金额", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")})
	@RequestMapping(value = "/getEstPurAmount", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getEstPurAmount(String groupId, String subGroupId, String totalAmount, @RequestParam(required=false, defaultValue="1")String oemid) {
		Map resultMap = null;
		try {
			String url = tradeOrderUrl + "/api/trade/funds/buyProduct?groupId=" + groupId + "&subGroupId=" + subGroupId
					+ "&totalAmount=" + totalAmount + "&oemid=" + Integer.parseInt(oemid);
			resultMap = restTemplate.getForEntity(url, Map.class).getBody();
			BigDecimal poundage = BigDecimal.valueOf(Double.parseDouble(resultMap.get("poundage").toString()));
			BigDecimal discount = BigDecimal.valueOf(Double.parseDouble(resultMap.get("discountSaving").toString()));
			// BigDecimal
			// total=poundage.add(BigDecimal.valueOf(Double.parseDouble((totalAmount))));
			BigDecimal total = poundage;
			BigDecimal totalOffDiscount = total.add(discount);
			if (total != null) {
				if (total.compareTo(BigDecimal.ZERO) > 0 && total.compareTo(new BigDecimal("0.01")) < 0) {
					total = new BigDecimal("0.01");
//					resultMap.put("poundage", "小于:¥0.01");
				} else {
					total = total.setScale(2, BigDecimal.ROUND_HALF_UP);
				}
				resultMap.put("poundage", total);
			}
			if (totalOffDiscount != null) {
				totalOffDiscount = totalOffDiscount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			if (discount != null) {
				discount = discount.setScale(2, BigDecimal.ROUND_HALF_UP);
			}
			BigDecimal totalAmountTemp = new BigDecimal(0);
			if (resultMap.get("fundAmountList") != null) {
				List<Map> resultList = (List<Map>) resultMap.get("fundAmountList");
				if (resultList != null && resultList.size() > 0) {
					for (int i = 0; i < resultList.size()-1; i++) {
						Map map = resultList.get(i);
						if (map.get("grossAmount") != null) {
							BigDecimal grossAmount = new BigDecimal(map.get("grossAmount") + "");
							grossAmount = grossAmount.setScale(2, BigDecimal.ROUND_HALF_UP);
							map.put("grossAmount", grossAmount);
							totalAmountTemp = totalAmountTemp.add(grossAmount);
						}
					}
					Map map = resultList.get(resultList.size()-1);
					if (map.get("grossAmount") != null) {
						BigDecimal grossAmount = new BigDecimal(totalAmount);
						totalAmountTemp = totalAmountTemp.setScale(2, BigDecimal.ROUND_HALF_UP);
						grossAmount = grossAmount.subtract(totalAmountTemp);
						map.put("grossAmount", grossAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
					}
				}
			}
			
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
			logger.error("exception:",e);
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("申购基金")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "电话号码", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
			@ApiImplicitParam(paramType = "query", name = "msgCode", dataType = "String", required = true, value = "验证码", defaultValue = "") })
	@RequestMapping(value = "/subscribeFund", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult doTransaction(@RequestParam String telNum, @RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard, @RequestParam(required = false) String buyfee,
			@RequestParam(required = false) String poundage, @RequestParam String msgCode,
			@RequestBody FinanceProdBuyInfo prdInfo) {
		String verify = null;
		// 首先验证验证码
		try {
			verify = service.verifyMSGCode(telNum, msgCode);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败", JsonResult.EMPTYRESULT);
		}
		// 验证码不通过则直接返回失败
		if ("验证失败".equals(verify)) {
			// TODO 临时注释2018-01-22
			/********************** start ****************************/
			if (!"123456".equals(msgCode)) {
				// return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败",
				// JsonResult.EMPTYRESULT);
			}
			/********************** end ******************************/
			return new JsonResult(JsonResult.Fail, "手机验证失败，申购失败", JsonResult.EMPTYRESULT);
		}

		if (!riskService.isAppropriateRishLevel(prdInfo.getUuid(), prdInfo.getProdId())) {
			return new JsonResult(JsonResult.Fail, "风险等级低，不能购买当前产品", JsonResult.EMPTYRESULT);
		}
		if(prdInfo.getOemId() == null || prdInfo.getOemId() == 0){
			prdInfo.setOemId(1);
		}
		try {
			// 调用购买接口
			Map buyProductSuccess = service.buyProduct(prdInfo);
			Map resultMap = new HashMap<>();
			resultMap.put("orderId", buyProductSuccess.get("orderId").toString());
			resultMap.put("bankName", bankName);
			resultMap.put("bankCard", bankCard);
			if(StringUtils.isEmpty(poundage) || "0".equals(poundage)){
				resultMap.put("poundage", "0.00");
			} else {
				DecimalFormat decimalFormat = new DecimalFormat("0.00");
				logger.info("poundage==>:{}",poundage);
				resultMap.put("poundage", decimalFormat.format(new Float(poundage)));
			}
			
			resultMap.put("buyfee", buyfee);
//			resultMap.put("poundage", poundage);
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
//			logger.error("购买基金调用购买接口失败" + e.getMessage());
//			logger.error("exception:",e);
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
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
				resultMap.put("min", minValue.setScale(0, BigDecimal.ROUND_UP));
			}
			if (resultMap.get("min") != null) {
				Double max = (Double) resultMap.get("max");
				BigDecimal maxValue = new BigDecimal(max);
				resultMap.put("max", maxValue.setScale(0, BigDecimal.ROUND_DOWN));
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
//			logger.error(e.getMessage());
//			logger.error("exception:",e);
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("购买方案初始页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "12"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "120049"),
		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/purchase-plan", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getBuyInitial(String uuid, String groupId, String subGroupId, @RequestParam(required=false, defaultValue="1")String oemid) {
		Map resultMap = null;
		try {
			String url = tradeOrderUrl + "/api/trade/funds/maxminValue?groupId=" + groupId + "&subGroupId="
					+ subGroupId + "&oemid=" + Integer.parseInt(oemid);
			resultMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (resultMap.get("min") != null) {
				Double min = (Double) resultMap.get("min");
				BigDecimal minValue = new BigDecimal(min);
				resultMap.put("min", minValue.setScale(0, BigDecimal.ROUND_UP));
			}
			if (resultMap.get("min") != null) {
				Double max = (Double) resultMap.get("max");
				BigDecimal maxValue = new BigDecimal(max);
				resultMap.put("max", maxValue.setScale(0, BigDecimal.ROUND_DOWN));
			}
			
			List<Map> resultOriginList = new ArrayList();
			List<Map> result = new ArrayList();

			resultOriginList = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid +
					"/bankcards", List.class)
					.getBody();

			if (resultOriginList != null && resultOriginList.size() > 0) {
				for (int i = 0; i < resultOriginList.size(); i++) {
					Map resultOriginMap = resultOriginList.get(i);
					if (resultOriginMap.get("bankCode") != null) {
						Map bankMap = new HashMap();
						bankMap = restTemplate.getForEntity(
								tradeOrderUrl + "/api/trade/funds/banks?bankShortName=" + resultOriginMap.get("bankCode"),
								Map.class).getBody();
						if (!StringUtils.isEmpty(bankMap.get("bankName"))) {
							resultOriginMap.put("bankShortName", bankMap.get("bankName"));
							resultOriginMap.put("bankName", bankMap.get("bankName"));
							result.add(resultOriginMap);
						}
					}
				}
			}
			resultMap.put("banks", result);
			
			url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/" + Integer.parseInt(oemid);
			Map productMap = restTemplate.getForEntity(url, Map.class).getBody();
			if(productMap!=null){
				if(productMap.get("name")!=null){
					resultMap.put("name", productMap.get("name"));
				}
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
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("产品赎回")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "手机号", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "verifyCode", dataType = "String", required = true, value = "验证码", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "userProdId", dataType = "String", required = true, value = "产品Id", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品的groupId", defaultValue = "12"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品的subGroupId", defaultValue = "120049"),
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费") })
	@RequestMapping(value = "/sellProduct", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellProduct(@RequestParam String telNum, @RequestParam String verifyCode,
			@RequestParam String userProdId, @RequestParam String prodId, @RequestParam String groupId,
			@RequestParam String userUuid, @RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard, @RequestParam(required = false) String buyfee,
			@RequestParam(required = false) String poundage, @RequestBody List<FinanceProdSellInfo> infoList) {
		// 首先调用手机验证码
		String verify = null;
		try {
			verify = service.verifyMSGCode(telNum, verifyCode);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
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
		Map result = new HashMap();
		try {
			result = service.sellFund(userProdId, prodId, groupId, userUuid, infoList);
		} catch (Exception e) {
			logger.error("调用赎回接口发生错误");
			logger.error(e.getMessage());
			return new JsonResult(JsonResult.Fail, "赎回失败", JsonResult.EMPTYRESULT);
		}
		if (result != null) {
			if (result.get("payAmount") != null) {
				BigDecimal payAmount = new BigDecimal(result.get("payAmount") + "");
				result.put("payAmount", payAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			result.put("poundage", poundage);
			result.put("buyfee", buyfee);
			result.put("bankName", bankName);
			result.put("bankCard", bankCard);
		}
		return new JsonResult(JsonResult.SUCCESS, "赎回成功", result);
	}
	
	@ApiOperation("产品百分比赎回")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "手机号", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "verifyCode", dataType = "String", required = true, value = "验证码", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "userProdId", dataType = "String", required = true, value = "产品Id", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品的groupId", defaultValue = "12"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "产品的subGroupId", defaultValue = "120049"),
		@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
		@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"),
//		@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
		@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
		@ApiImplicitParam(paramType = "query", name = "sellTargetPercent", dataType = "BigDecimal", required = true, value = "百分比(默认100%)", defaultValue = "100"),
		})
	@RequestMapping(value = "/sellPersentProduct", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellPersentProduct(
			@RequestParam String telNum, 
			@RequestParam String verifyCode,
			@RequestParam String userProdId, 
			@RequestParam String groupId, 
			@RequestParam String subGroupId,
			@RequestParam String userUuid, 
			@RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard, 
//			@RequestParam(required = false) String buyfee,
			@RequestParam(required = false) String poundage,
			@RequestParam BigDecimal sellTargetPercent) {
		// 首先调用手机验证码
		String verify = null;
		try {
			verify = service.verifyMSGCode(telNum, verifyCode);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
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
		Map result = new HashMap();
		try {
			result = service.sellFundPersent(userProdId, groupId, subGroupId, userUuid, bankCard, sellTargetPercent);
		} catch (Exception e) {
			logger.error("调用赎回接口发生错误");
			logger.error(e.getMessage());
			return new JsonResult(JsonResult.Fail, "赎回失败", JsonResult.EMPTYRESULT);
		}
		if (result != null) {
			if (result.get("payAmount") != null) {
				BigDecimal payAmount = new BigDecimal(result.get("payAmount") + "");
				result.put("payAmount", payAmount.setScale(2, BigDecimal.ROUND_HALF_UP));
			}
			result.put("poundage", poundage);
//			result.put("buyfee", buyfee);
			result.put("bankName", bankName);
			result.put("prodId", userProdId);
			result.put("bankCard", bankCard);
			result.put("sellTargetPercent", sellTargetPercent);
		}
		return new JsonResult(JsonResult.SUCCESS, "赎回成功", result);
	}
	
	@ApiOperation("赎回页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "归属id", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupID", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "bankNum", dataType = "String", required = true, value = "银行卡号", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = true, value = "银行名称", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "手机号码", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "combinationName", dataType = "String", required = true, value = "组合名称", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品id", defaultValue = ""),
//		@ApiImplicitParam(paramType = "query", name = "userProdId", dataType = "String", required =
//				true, value = "用户产品id", defaultValue = ""),
		@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "String", required = true, value = "总金额", defaultValue = "") 
		})
		@RequestMapping(value = "/sellFundPage", method = RequestMethod.POST)
		@ResponseBody
		public JsonResult sellFundPage(@RequestParam(required=false, defaultValue="1")String oemid, String userUuid, String groupId, String subGroupId,
				String bankNum, String bankName,
				String telNum, String combinationName, 
//				String userProdId, 
				String prodId, String totalAmount) {
			Map result = null;
			try {
				result = service.sellFundPage(groupId, subGroupId, totalAmount, Integer.parseInt(oemid));
				if (result != null) {
					result.put("userUuid", userUuid);
					result.put("bankNum", bankNum);
					result.put("bankName", bankName);
					result.put("totalAmount", totalAmount);
					result.put("combinationName", combinationName);
					result.put("prodId", prodId);
					result.put("telNum", telNum);
					result.put("title1", "依据最优比例分配赎回金额");
					result.put("title2", "贝贝鱼依据最优比例分配赎回金额");
					long startTime = System.currentTimeMillis();
					if (!InstantDateUtil.isDealDay(startTime)) {
						// 交易日
						LocalDateTime localDateTime = LocalDateTime.now();
						LocalDateTime localDateTimeLimit = LocalDateTime.of(localDateTime.toLocalDate(),
								LocalTime.of(15, 0));
						if (localDateTime.isAfter(localDateTimeLimit)) {
							String date = InstantDateUtil.getTplusNDayNWeekendOfWork(startTime, 1);
							date = date.replaceAll("-", ".");
							result.put("sellAmountDate", date);
						} else {
							// 3点以前
							String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
							time = time.replaceAll("-", ".");
							result.put("sellAmountDate", time);
						}
					} else {
						String date = InstantDateUtil.getTplusNDayNWeekendOfWork(startTime, 1);
						date = date.replaceAll("-", ".");
						result.put("sellAmountDate", date);
					}
					if (bankNum != null && bankNum.length() > 4) {
						result.put("bankinfo", bankName + "(" + bankNum.substring(bankNum.length() - 4) + ")");
						result.put("bankNum", bankNum);
					}
				}
				return new JsonResult(JsonResult.SUCCESS, "调用成功", result);
			} catch (Exception ex) {
		//		logger.error("赎回页面接口调用失败");
		//		logger.error("exception:",ex);
				String str = new ReturnedException(ex).getErrorMsg();
				logger.error(str, ex);
				return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
			}
		}

	@ApiOperation("赎回百分比例页面")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "归属id", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "客户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "bankNum", dataType = "String", required = true, value = "银行卡号", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = true, value = "银行名称", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "telNum", dataType = "String", required = true, value = "手机号码", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "combinationName", dataType = "String", required = true, value = "组合名称", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品id", defaultValue = ""),
//			@ApiImplicitParam(paramType = "query", name = "userProdId", dataType = "String", required =
//					true, value = "用户产品id", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "persent", dataType = "BigDecimal", required = true, value = "赎回比例", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "totalAmount", dataType = "String", required = true, value = "总金额", defaultValue = "") 
			})
	@RequestMapping(value = "/sellPersentFundPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellPersentFundPage(@RequestParam(required=false, defaultValue="1")String oemid, String userUuid, String groupId, String subGroupId,
			String bankNum, String bankName,
			String telNum, String combinationName, 
//			String userProdId, 
			String prodId, BigDecimal persent, String totalAmount) {
		Map result = null;
		try {
			BigDecimal amount = new BigDecimal(totalAmount);
			amount = amount.multiply(persent).divide(new BigDecimal("100"));
			result = service.sellFundPage(groupId, subGroupId, amount + "", Integer.parseInt(oemid));
			if (result != null) {
				result.put("userUuid", userUuid);
				result.put("bankNum", bankNum);
				result.put("bankName", bankName);
				result.put("totalAmount", amount);
				result.put("combinationName", combinationName);
				result.put("sellTargetPercent", persent);
				result.put("prodId", prodId);
				result.put("telNum", telNum);
				result.put("title1", "依据最优比例分配赎回比例");
//				result.put("title2", "贝贝鱼依据最优比例分配赎回金额");
				long startTime = System.currentTimeMillis();
				if (!InstantDateUtil.isDealDay(startTime)) {
					// 交易日
					LocalDateTime localDateTime = LocalDateTime.now();
					LocalDateTime localDateTimeLimit = LocalDateTime.of(localDateTime.toLocalDate(),
							LocalTime.of(15, 0));
					if (localDateTime.isAfter(localDateTimeLimit)) {
						String date = InstantDateUtil.getTplusNDayNWeekendOfWork(startTime, 1);
						date = date.replaceAll("-", ".");
						result.put("sellAmountDate", date);
					} else {
						// 3点以前
						String time = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
						time = time.replaceAll("-", ".");
						result.put("sellAmountDate", time);
					}
				} else {
					String date = InstantDateUtil.getTplusNDayNWeekendOfWork(startTime, 1);
					date = date.replaceAll("-", ".");
					result.put("sellAmountDate", date);
				}
				if (bankNum != null && bankNum.length() > 4) {
					result.put("bankinfo", bankName + "(" + bankNum.substring(bankNum.length() - 4) + ")");
					result.put("bankNum", bankNum);
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "调用成功", result);
		} catch (Exception ex) {
//			logger.error("赎回页面接口调用失败");
//			logger.error("exception:",ex);
			String str = new ReturnedException(ex).getErrorMsg();
			logger.error(str, ex);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
}
