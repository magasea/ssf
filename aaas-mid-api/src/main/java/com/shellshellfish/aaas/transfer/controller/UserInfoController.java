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
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.common.utils.BankUtil;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import com.shellshellfish.aaas.transfer.utils.CalculatorFunctions;
import com.shellshellfish.aaas.transfer.utils.EasyKit;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class UserInfoController {

	Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	private final static String FUND_TYPE_TWO = "QDII基金";
	// @Autowired
	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;

	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;

	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;

	@Autowired
	private RestTemplate restTemplate;

	private RestTemplate restTemplatePeach = new RestTemplate(new HttpComponentsClientHttpRequestFactory());

	@ApiOperation("添加银行卡")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "用户名称", defaultValue = "zhangsan"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = true, value = "银行卡号", defaultValue = "6228480402564890010"),
			@ApiImplicitParam(paramType = "query", name = "idcard", dataType = "String", required = true, value = "身份证号", defaultValue = "11022619850127211X"),
			@ApiImplicitParam(paramType = "query", name = "mobile", dataType = "String", required = true, value = "手机号", defaultValue = "13511111111"),
			@ApiImplicitParam(paramType = "query", name = "verifyCode", dataType = "String", required = true, value = "验证码", defaultValue = "1234") })
	@RequestMapping(value = "/addBankCards", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult addBankCards(@RequestParam String uuid, @RequestParam String name, @RequestParam String bankCard,
			@RequestParam String idcard, @RequestParam String mobile, @RequestParam String verifyCode) {
		Map<String, Object> verifyReult;
		Map<String, Object> result;
		try {

			verifyReult = restTemplate
					.postForEntity(loginUrl + "/api/useraccount/telnums/" + mobile + "?action=getVerificationCode2",
							null, Map.class)
					.getBody();
			if (verifyReult == null || verifyReult.size() == 0) {
				logger.info("获取验证码验证是否正确");
				/* result.put("msg", "添加失败"); */
				// TODO 临时注释2018-01-22
				/********************** start ****************************/
				if (!"123456".equals(verifyCode)) {
					return new JsonResult(JsonResult.Fail, "添加银行卡失败，验证码不正确", JsonResult.EMPTYRESULT);
				}
				/********************** end ******************************/
				// return new JsonResult(JsonResult.Fail, "添加银行卡失败，验证码不正确",
				// JsonResult.EMPTYRESULT);
			} else if (!verifyReult.get("identifyingCode").equals(verifyCode)) {
				/* result.put("msg", "添加失败"); */
				// TODO 临时注释2018-01-22
				/********************** start ****************************/
				if (!"123456".equals(verifyCode)) {
					return new JsonResult(JsonResult.Fail, "添加银行卡失败，验证码不正确", JsonResult.EMPTYRESULT);
				}
				/********************** end ******************************/
				// return new JsonResult(JsonResult.Fail, "添加银行卡失败，验证码不正确",
				// JsonResult.EMPTYRESULT);
			}

			String url = userinfoUrl + "/api/userinfo/users/" + uuid + "/bankcards";
			String str = "{\"cardNumber\":\"" + bankCard + "\",\"cardUserName\":\"" + name + "\",\"cardCellphone\":\""
					+ mobile + "\",\"cardUserPid\":\"" + idcard + "\",\"cardUuId\":\"" + uuid + "\"}";
			logger.info("urlUid==" + str);
			logger.info("str==" + str);
			result = restTemplate.postForEntity(url, getHttpEntitySecond(str), Map.class).getBody();
			if (result == null) {
				logger.info("添加银行卡失败");
				return new JsonResult(JsonResult.Fail, "添加银行卡失败", result);
			} else {
				logger.info("添加银行卡成功");
				return new JsonResult(JsonResult.SUCCESS, "添加银行卡成功", result);
			}
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("获取银行卡列表")
	@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "")
	@RequestMapping(value = "/selectbanks", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getUserBanks(@RequestParam String uuid) {
		List<Map> resultOrigin = new ArrayList();
		List<Map> result = new ArrayList();
		try {
			// result = restTemplate.getForEntity(userinfoUrl +
			// "/api/userinfo/selectbanks?uuid=" + uid, List.class)
			// .getBody();
			resultOrigin = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid +
					"/bankcards", List.class)
					.getBody();

			if (resultOrigin != null && resultOrigin.size() > 0) {
				for (int i = 0; i < resultOrigin.size(); i++) {
					Map resultMap = resultOrigin.get(i);
					if (resultMap.get("bankCode") != null) {
						Map bankMap = new HashMap();
						bankMap = restTemplate.getForEntity(
								tradeOrderUrl + "/api/trade/funds/banks?bankShortName=" + resultMap.get("bankCode"),
								Map.class).getBody();
						if (!StringUtils.isEmpty(bankMap.get("bankName"))) {
							resultMap.put("bankShortName", bankMap.get("bankName"));
							resultMap.put("bankName", bankMap.get("bankName"));
							result.add(resultMap);
						}
					}
				}
			}
			if (CollectionUtils.isEmpty(result )) {
				return new JsonResult(JsonResult.SUCCESS, "获取银行卡为空", new ArrayList());
			} else {
				return new JsonResult(JsonResult.SUCCESS, "获取银行卡成功", result);
			}
		} catch (Exception e) {
			/*
			 * Map<String, Object> map = new HashMap(); map.put("errorCode",
			 * "400"); result.add(map);
			 */
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("获取银行名称")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "bankNum", dataType = "String", required = true, value = "银行卡号", defaultValue = "6210986802084484920"), })
	@RequestMapping(value = "/banks", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getBanks(@RequestParam String bankNum) {
		Map result = new HashMap();
		try {
			result = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/bankcards/" + bankNum + "/banks", Map.class)
					.getBody();
			if (result != null && result.get("bankName") != null) {
				Map bankMap = new HashMap();
				bankMap = restTemplate
						.getForEntity(tradeOrderUrl + "/api/trade/funds/banks?bankShortName=" + result.get("bankCode"),
								Map.class)
						.getBody();
				if (bankMap.get("bankName") != null) {
					result.put("bankName", bankMap.get("bankName"));
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "获取银行名称成功", result);
		} catch (Exception e) {
			/*
			 * Map<String, Object> map = new HashMap(); map.put("errorCode",
			 * "400");
			 */
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	/**
	 * 进入个人信息页面获取手机号，所属行业，和修改密码的link
	 *
	 * @param uuid
	 *            客户id
	 * @return
	 */
	@ApiOperation("个人信息数据")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "客户id号", defaultValue = "1") })
	@RequestMapping(value = "/personalInformation", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getCustInfo(@RequestParam String uuid) {
		String url = userinfoUrl + "api/userinfo/users/{uuid}";
		Map result = null;

		// 先调用个人信息
		try {
			result = restTemplate.getForEntity(url, Map.class, uuid).getBody();
			result.remove("_links");
			result.remove("uuid");// 移除uuid这个key
			result.put("uuid", uuid);// 改名为uid
			return new JsonResult(JsonResult.SUCCESS, "获取个人信息成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("智投推送-我的消息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "1") })
	@RequestMapping(value = "/invationFriends", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFriendsInvationLinks(@RequestParam String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// MultiValueMap<String, String> requestEntity = new
		// LinkedMultiValueMap<>();
		// requestEntity.add("bankId", "1");
		try {
			result = restTemplate
					.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/investmentmessages", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				return new JsonResult(JsonResult.Fail, "获取不到推送信息", JsonResult.EMPTYRESULT);
			}
			result.remove("_links");
			result.put("uuid", uuid);
			result.remove("userUuid");
			List items = (ArrayList) result.get("_items");
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					Map item = (Map) items.get(i);
					Map<String, Object> listMap = new HashMap<String, Object>();
					listMap.put("content", item.get("content"));
					listMap.put("title", item.get("msgTitle"));
					list.add(listMap);
				}
			}
			result.put("_items", list);
			result.remove("_total");
			return new JsonResult(JsonResult.SUCCESS, "智投推送成功", result);
		} catch (Exception e) {
			/*
			 * Map<String, Object> map = new HashMap<String, Object>();
			 * map.put("errorCode", "400");
			 */
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("系统消息-我的消息")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "1") })
	@RequestMapping(value = "/systemMsg", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getSystemMsg(@RequestParam String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		// MultiValueMap<String, String> requestEntity = new
		// LinkedMultiValueMap<>();
		// requestEntity.add("bankId", "1");
		try {
			result = restTemplate
					.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/systemmessages", Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.info("系统消息获取失败");
				return new JsonResult(JsonResult.Fail, "系统消息获取失败", JsonResult.EMPTYRESULT);
			}
			result.remove("_links");
			result.put("uuid", uuid);
			// result.remove("userUuid");
			List items = (ArrayList) result.get("_items");
			if (items != null) {
				for (int i = 0; i < items.size(); i++) {
					Map item = (Map) items.get(i);
					Map<String, Object> listMap = new HashMap<String, Object>();
					listMap.put("content", item.get("content"));
					if (item.get("createdDate") != null) {
						Long date = (Long) item.get("createdDate");
						listMap.put("date", CalculatorFunctions.getDateType(date));
					} else {
						listMap.put("date", "");
					}
					list.add(listMap);
				}
				logger.info("系统消息获取成功");
			} else {
				logger.info("系统消息为空");
			}
			result.put("_items", list);
			result.remove("_total");
			return new JsonResult(JsonResult.SUCCESS, "系统消息获取成功", result);
		} catch (Exception e) {
			/*
			 * Map<String, Object> map = new HashMap<String, Object>();
			 * map.put("errorCode", "400");
			 */
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("解绑银行卡")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "cardno", dataType = "String", required = true, value = "银行卡ID", defaultValue = ""), })
	@RequestMapping(value = "/unbundlingBankCards", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult unbundlingBankCards(@RequestParam String uuid, @RequestParam String cardno) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			// result = restTemplate.getForEntity(userinfoUrl +
			// "/api/userinfo/users/"+uid+"/unbundlingBankCards/"+cardno,
			// Map.class)
			// .getBody();
			restTemplate.delete(userinfoUrl + "/api/userinfo/users/" + uuid + "/unbundlingBankCards/" + cardno);
			result.put("status", "1");
			result.put("msg", "解绑成功");
			return new JsonResult(JsonResult.SUCCESS, "解绑银行卡成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("交易记录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "type", dataType = "Integer", required = false, value = "类型（1：购买，2：赎回）") })
	@RequestMapping(value = "/traderecords", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult tradeLogsOfUser(@RequestParam String uuid, @RequestParam(required = false) Integer type) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			result = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/traderecords", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				logger.error("系统消息获取失败");
				return new JsonResult(JsonResult.Fail, "交易记录获取失败", JsonResult.EMPTYRESULT);
			} else {
				type = type == null ? 0 : type;
				if (type > 4) {
					logger.error("输入类型:" + type + "不正确，请重新输入");
					return new JsonResult(JsonResult.Fail, "输入类型不正确，请重新输入", JsonResult.EMPTYRESULT);
				}
				List<Map> detailBak = new ArrayList<Map>();
				List detail = (List) result.get("tradeLogs");
				if (detail != null || detail.size() != 0) {
					for (int i = 0; i < detail.size(); i++) {
						if (detail.get(i) != null) {
							Map map = (Map) detail.get(i);
							int operationsStatus = (int) map.get("operationsStatus");
							if (map.get("prodId") != null) {
								Integer prodId = (Integer) map.get("prodId");
								Map orderResult = restTemplate.getForEntity(
										tradeOrderUrl + "/api/trade/funds/banknums/" + uuid + "?prodId=" + prodId,
										Map.class).getBody();
								if (orderResult.get("orderId") != null) {
									String orderId = orderResult.get("orderId") + "";
									if(!StringUtils.isEmpty(orderId)){
										orderId = orderId.trim();
										orderId = orderId.replaceAll("-", "");
									}
									String bankName = "";
									String bankcardNum = "";
									if (orderResult.get("bankNum") != null) {
										bankcardNum = orderResult.get("bankNum") + "";
										bankName = BankUtil.getNameOfBank(bankcardNum);
										bankName = bankName.substring(0, bankName.indexOf("·"));
									}
									map.put("orderId", orderId);
									map.put("poundage", orderResult.get("buyFee"));
									map.put("bankName", bankName);
									map.put("bankcardNum", bankcardNum);
									map.put("bankinfo", bankName + "(" + bankcardNum.substring(bankcardNum.length() - 4) + ")");
									if (type == operationsStatus || type == 0) {
										detailBak.add(map);
									}
								}
							}
						}
					}
					result.put("tradeLogs", detailBak);
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "交易记录成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("我的智投组合")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = "") })
	@RequestMapping(value = "/chicombination", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult chicombination(@RequestParam String uuid) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			result = restTemplate
					.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/chicombination", Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.error("我的智投组合获取失败");
				return new JsonResult(JsonResult.Fail, "我的智投组合为空", JsonResult.EMPTYRESULT);
			} else {
				List<Map<String, Object>> resultList = new ArrayList<Map<String,Object>>();
				if (result.get("result") != null) {
					resultList = (List<Map<String, Object>>) result.get("result");
					for (int i = 0; i < resultList.size(); i++) {
						Map<String, Object> resultMap = resultList.get(i);
						if (resultMap.get("totalIncomeRate") != null) {
							resultMap.put("totalIncomeRate", EasyKit.getStringValue(BigDecimal.valueOf((Double) resultMap.get("totalIncomeRate"))));
						}
					}
					Collections.reverse(resultList);
				}
				return new JsonResult(JsonResult.SUCCESS, "我的智投组合成功", resultList);
			}
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("资产总览")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "totalAssets", dataType = "BigDecimal", required = true, value = "总资产", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "dailyReturn", dataType = "BigDecimal", required = true, value = "日收益", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "totalRevenue", dataType = "BigDecimal", required = true, value = "累计收益", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "totalRevenueRate", dataType = "String", required = true, value = "累计收益率", defaultValue = "") })
	@RequestMapping(value = "/asset", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult assetView(@RequestParam String uuid, @RequestParam("totalAssets") BigDecimal totalAssets,
			@RequestParam("dailyReturn") BigDecimal dailyReturn, @RequestParam("totalRevenue") BigDecimal totalRevenue,
			@RequestParam("totalRevenueRate") String totalRevenueRate) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			result = restTemplate.getForEntity(
					userinfoUrl + "/api/userinfo/users/" + uuid + "/asset?totalAssets=" + totalAssets + "&dailyReturn="
							+ dailyReturn + "&totalRevenue=" + totalRevenue + "&totalRevenueRate=" + totalRevenueRate,
					Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.error("资产总览获取失败");
				return new JsonResult(JsonResult.Fail, "资产总览获取失败", JsonResult.EMPTYRESULT);
			} else {
				if (result.get("trendYield") != null) {
					List trendYieldList = (List) result.get("trendYield");
					List<Double> maxMinValueList = new ArrayList<Double>();
					for (int i = 0; i < trendYieldList.size(); i++) {
						Map trendYieldMap = (Map) trendYieldList.get(i);
						if (trendYieldMap.get("value") != null) {
							String trendYield = trendYieldMap.get("value") + "";
							if ("0".equals(trendYield)) {
								trendYieldMap.put("value", "0");
							} else {
								trendYieldMap.put("value", trendYield);
							}
							maxMinValueList.add(Double.parseDouble(trendYield));
						} else {
							trendYieldMap.put("value", "0");
							maxMinValueList.add(0D);
						}
						if (trendYieldMap.get("date") != null) {
							String dateStr1 = (String) trendYieldMap.get("date");
							String dateStr2 = String.format("%s-%s-%s", dateStr1.substring(0,4), dateStr1.substring(4,6), dateStr1.substring(6,8));
							trendYieldMap.put("date", dateStr2);
						}
					}
					if (maxMinValueList != null && maxMinValueList.size() > 0) {
						result.put("maxValue", Collections.max(maxMinValueList));
						result.put("minValue", Collections.min(maxMinValueList));
					}
				}
				if (result.get("totalRevenue") != null) {
					Double totalRevenue2 = Double.valueOf(result.get("totalRevenue") + "");
					result.put("totalRevenue", String.format("%.2f", totalRevenue2));
				} else {
					result.put("totalRevenue", "0.00");
				}
				if (result.get("totalAssets") != null) {
					Double totalAssets2 = Double.valueOf(result.get("totalAssets") + "");
					result.put("totalAssets", String.format("%.2f", totalAssets2));
				} else {
					result.put("totalAssets", "0.00");
				}
				if (result.get("dailyReturn") != null) {
					Double dailyReturn2 = Double.valueOf(result.get("dailyReturn") + "");
					result.put("dailyReturn", String.format("%.2f", dailyReturn2));
				} else {
					result.put("dailyReturn", "0.00");
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "资产总览成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("交易结果 购买")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = false, value = "订单编号"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"), })
	@RequestMapping(value = "/traderesult", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getRecords(@RequestParam String uuid, @RequestParam String prodId,
			@RequestParam(required = false) String orderId, @RequestParam(required = false) String buyfee,
			@RequestParam(required = false) String poundage, @RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			String url = userinfoUrl + "/api/userinfo/users/" + uuid + "/orders/" + prodId + "/buy-records?buyfee="
					+ buyfee + "&bankName=" + bankName + "&bankCard=" + bankCard;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.error("交易结果获取失败");
				return new JsonResult(JsonResult.Fail, "交易结果获取失败", JsonResult.EMPTYRESULT);
			} else {
				result.put("uuid", uuid);
				result.put("prodId", prodId);
				result.put("orderId", orderId == null ? "" : orderId);
				result.put("buyfee", buyfee == null ? "" : buyfee);
				result.put("bankName", bankName == null ? "" : bankName);
				result.put("bankCard", bankCard == null ? "" : bankCard);
				result.put("poundage", poundage == null ? "" : poundage);
			}
			return new JsonResult(JsonResult.SUCCESS, "交易结果成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("交易结果 赎回")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "prodId", dataType = "String", required = true, value = "产品ID", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = false, value = "订单编号"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号"), })
	@RequestMapping(value = "/sellresult", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getSellRecords(@RequestParam String uuid, @RequestParam String prodId,
			@RequestParam(required = false) String orderId, @RequestParam(required = false) String buyfee,
			@RequestParam(required = false) String poundage, @RequestParam(required = false) String bankName,
			@RequestParam(required = false) String bankCard) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			String url = userinfoUrl + "/api/userinfo/users/" + uuid + "/orders/" + prodId + "/sell-records?buyfee="
					+ buyfee + "&bankName=" + bankName + "&bankCard=" + bankCard;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if (result == null || result.size() == 0) {
				logger.error("交易结果获取失败");
				return new JsonResult(JsonResult.Fail, "交易结果获取失败", JsonResult.EMPTYRESULT);
			} else {
				result.put("uuid", uuid);
				result.put("prodId", prodId);
				result.put("orderId", orderId == null ? "" : orderId);
				result.put("buyfee", buyfee == null ? "" : buyfee);
				result.put("bankName", bankName == null ? "" : bankName);
				result.put("bankCard", bankCard == null ? "" : bankCard);
				result.put("poundage", poundage == null ? "" : poundage);

				if (result.get("date1") != null) {
					String date1 = result.get("date1") + "";
					if (result.get("date1") != null) {
						String date2 = result.get("date2") + "";
						String title1 = "预计" + date1 + " ~ " + date2 + "期间到账。";
						String title2 = "预计" + date1 + " ~ " + date2 + "期间到账，赎回基金中包含投资海外资产的QDII类型基金，确认后需要4-15个工作日到账。";
						// 获取产品组合信息
						String url2 = userinfoUrl + "/api/userinfo/product/" + prodId;
						Map productResult = restTemplate.getForEntity(url2, Map.class).getBody();
						if (productResult != null) {
							if (productResult.get("groupId") != null) {
								String groupId = productResult.get("groupId") + "";
								if (productResult.get("subGroupId") != null) {
									String subGroupId = productResult.get("subGroupId") + "";
									// 获取二级分类
									String url3 = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId
											+ "/sub-groups/" + subGroupId;
									Map productMap = restTemplate.getForEntity(url3, Map.class).getBody();
									if (productMap == null) {
										logger.info("单个基金组合产品信息为空");
									} else {
										if (productMap.get("assetsRatios") != null) {
											List<Map> assetsList = (List<Map>) productMap.get("assetsRatios");
											for (int i = 0; i < assetsList.size(); i++) {
												Map assetsMap = assetsList.get(i);
												if (assetsMap != null) {
													if (FUND_TYPE_TWO.equals(assetsMap.get("type"))) {
														result.put("title", title2);
														break;
													} else {
														result.put("title", title1);
													}
													result.remove("date1");
													result.remove("date2");
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "交易结果成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("理财产品 产品详情页面(购买)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = "1231230001000001513657092497"),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号") })
	@RequestMapping(value = "/buyDetails", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult buyDetails(@RequestParam String userUuid, @RequestParam String orderId,
			@RequestParam(required = false) String buyfee, @RequestParam(required = false) String poundage,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) String bankCard) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			result = restTemplate.getForEntity(tradeOrderUrl + "/api/trade/funds/buyDetails/" + orderId, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				logger.error("产品详情-result-获取失败");
				return new JsonResult(JsonResult.Fail, "产品详情获取失败", JsonResult.EMPTYRESULT);
			} else {
				result.put("buyfee", buyfee == null ? "" : buyfee);
				poundage = poundage == null ? "" : poundage;
				bankName = bankName == null ? "" : bankName;
				bankCard = bankCard == null ? "" : bankCard;
				result.put("poundage", poundage);
				result.put("bankName", bankName);
				result.put("bankCard", bankCard);
			}
			if (result.get("detailList") == null) {
				logger.error("产品详情-detailList-获取失败");
				// return new JsonResult(JsonResult.Fail, "产品详情获取失败",
				// JsonResult.EMPTYRESULT);
			} else {

				List detail = (List) result.get("detailList");
				if (detail != null || detail.size() != 0) {
					for (int i = 0; i < detail.size(); i++) {
						if (detail.get(i) != null) {
							Map map = (Map) detail.get(i);
							String fundCode = (String) map.get("fundCode");
							if (!StringUtils.isEmpty(fundCode)) {
								Map fundMap = new HashMap();
								fundMap = restTemplate.getForEntity(
										dataManagerUrl + "/api/datamanager/getFundInfoBycode?code=" + fundCode,
										Map.class).getBody();
								if (fundMap == null || fundMap.size() == 0) {
									logger.error("基金CODE:" + fundCode + "不存在");
								} else {
									String fundName = (String) (fundMap.get("fundname"));
									map.put("fundName", fundName);
									map.remove("fundCode");
								}
							}
						}
					}
				}
				// if(detailList!=null && detailList){
				// statusList
				// }
				String prodId = "";
				if (result.get("prodId") != null) {
					prodId = result.get("prodId") + "";
				}
				String url = userinfoUrl + "/api/userinfo/users/" + userUuid + "/orders/" + prodId + "/status";
				Map resultStatus = restTemplate.getForEntity(url, Map.class).getBody();
				if (resultStatus != null) {
					result.put("statusList", resultStatus.get("result"));
				} else {
					result.put("statusList", new ArrayList());
				}
				result.put("bankinfo", bankName + "(" + bankCard + ")");
				// 获取产品组合信息
				String url2 = userinfoUrl + "/api/userinfo/product/" + prodId;
				Map productResult = restTemplate.getForEntity(url2, Map.class).getBody();
				if (productResult != null) {
					result.put("title", productResult.get("prodName"));
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "产品详情页面成功", result);
		} catch (Exception ex) {
			logger.error("产品详情页面接口失败");
			logger.error("exception:",ex);
			String str = new ReturnedException(ex).getErrorMsg();
			return new JsonResult(JsonResult.Fail, "产品详情页面失败", JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("理财产品 产品详情页面(赎回)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "用户uuid", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "orderId", dataType = "String", required = true, value = "订单编号", defaultValue = ""),
			@ApiImplicitParam(paramType = "query", name = "buyfee", dataType = "String", required = false, value = "预计费用"),
			@ApiImplicitParam(paramType = "query", name = "poundage", dataType = "String", required = false, value = "手续费"),
			@ApiImplicitParam(paramType = "query", name = "bankName", dataType = "String", required = false, value = "银行名称"),
			@ApiImplicitParam(paramType = "query", name = "bankCard", dataType = "String", required = false, value = "银行卡号") })
	@RequestMapping(value = "/sellDetails", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult sellDetails(@RequestParam String userUuid, @RequestParam String orderId,
			@RequestParam(required = false) String buyfee, @RequestParam(required = false) String poundage,
			@RequestParam(required = false) String bankName, @RequestParam(required = false) String bankCard) {
		Map<Object, Object> result = new HashMap<Object, Object>();
		try {
			result = restTemplate.getForEntity(tradeOrderUrl + "/api/trade/funds/sellDetails/" + orderId, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				logger.error("产品详情-result-获取失败");
				return new JsonResult(JsonResult.Fail, "产品详情获取失败", JsonResult.EMPTYRESULT);
			} else {
				result.put("buyfee", buyfee == null ? "" : buyfee);
				result.put("poundage", poundage == null ? "" : poundage);
				result.put("bankName", bankName == null ? "" : bankName);
				result.put("bankCard", bankCard == null ? "" : bankCard);
			}
			if (result.get("detailList") == null) {
				logger.error("产品详情-detailList-获取失败");
				// return new JsonResult(JsonResult.Fail, "产品详情获取失败",
				// JsonResult.EMPTYRESULT);
			} else {

				List detail = (List) result.get("detailList");
				if (detail != null || detail.size() != 0) {
					for (int i = 0; i < detail.size(); i++) {
						if (detail.get(i) != null) {
							Map map = (Map) detail.get(i);
							String fundCode = (String) map.get("fundCode");
							if (!StringUtils.isEmpty(fundCode)) {
								Map fundMap = new HashMap();
								fundMap = restTemplate.getForEntity(
										dataManagerUrl + "/api/datamanager/getFundInfoBycode?code=" + fundCode,
										Map.class).getBody();
								if (fundMap == null || fundMap.size() == 0) {
									logger.error("基金CODE:" + fundCode + "不存在");
								} else {
									String fundName = (String) (fundMap.get("fundname"));
									map.put("fundName", fundName);
									map.remove("fundCode");
								}
							}
						}
					}
				}
				// if(detailList!=null && detailList){
				// statusList
				// }
				String prodId = "";
				if (result.get("prodId") != null) {
					prodId = result.get("prodId") + "";
				}
				String url = userinfoUrl + "/api/userinfo/users/" + userUuid + "/orders/" + prodId + "/status";
				Map resultStatus = restTemplate.getForEntity(url, Map.class).getBody();
				if (resultStatus != null) {
					result.put("statusList", resultStatus.get("result"));
				} else {
					result.put("statusList", new ArrayList());
				}
				result.put("bankinfo", bankName + "(" + bankCard + ")");
				// 获取产品组合信息
				String url2 = userinfoUrl + "/api/userinfo/product/" + prodId;
				Map productResult = restTemplate.getForEntity(url2, Map.class).getBody();
				if (productResult != null) {
					result.put("title", productResult.get("prodName"));
				}
			}
			return new JsonResult(JsonResult.SUCCESS, "产品详情页面成功", result);
		} catch (Exception ex) {
			logger.error("产品详情页面接口失败");
			logger.error("exception:",ex);
			String str = new ReturnedException(ex).getErrorMsg();
			return new JsonResult(JsonResult.Fail, "产品详情页面失败", JsonResult.EMPTYRESULT);
		}
	}

	/**
	 * 通用方法处理post请求带requestbody
	 *
	 * @param JsonString
	 * @return
	 */
	protected HttpEntity<String> getHttpEntity(String JsonString) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;UTF-8"));
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString, headers);
		return strEntity;
	}

	protected HttpEntity<String> getHttpEntitySecond(String JsonString) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;charset=UTF-8"));
		headers.add("Accept", "application/json;charset=UTF-8");
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString, headers);
		return strEntity;
	}

}
