package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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
import org.springframework.util.CollectionUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.dto.FinanceProductCompo;
import com.shellshellfish.aaas.oeminfo.model.JsonResult;
import com.shellshellfish.aaas.oeminfo.service.MidApiService;
import com.shellshellfish.aaas.transfer.aop.AopTimeResources;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import com.shellshellfish.aaas.transfer.service.GrpcOemInfoService;
import com.shellshellfish.aaas.transfer.utils.EasyKit;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class FinanceController {

	Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	// @Autowired
	@Value("${shellshellfish.userinfo-url}")
	private String userinfoUrl;

	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	@Value("${shellshellfish.finance-url}")
	private String financeUrl;

	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;
	
	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private MidApiService service;
	
	@Autowired
	GrpcOemInfoService grpcOemInfoService;
	
	private static final DecimalFormat decimalFormat = new DecimalFormat(".00"); //保留 5 位

	@ApiOperation("1.首页")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID"),
			@ApiImplicitParam(paramType = "query", name = "isTestFlag", dataType = "String", required = false, value = "是否测评（1-已做 0-未做）"),
			@ApiImplicitParam(paramType = "query", name = "testResult", dataType = "String", required = false, value = "测评结果", defaultValue = "平衡型"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid（贝贝鱼:1,兰州银行：2）", defaultValue = "1")
	})
	@RequestMapping(value = "/finance-home", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult financeHome(
			@RequestParam(required = false) String uuid,
			@RequestParam(required = false) String isTestFlag,
			@RequestParam(required = false) String testResult,
			@RequestParam(required = false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		logger.info("mid financeHome method run..");
		try {
			if ("1".equals(isTestFlag)) {
				if (StringUtils.isEmpty(uuid)) {
					return new JsonResult(JsonResult.Fail, "用户ID必须输入", JsonResult.EMPTYRESULT);
				} else if (StringUtils.isEmpty(testResult)) {
					return new JsonResult(JsonResult.Fail, "测评结果必须输入", JsonResult.EMPTYRESULT);
				}
			}
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			uuid = uuid == null ? "" : uuid;
			requestEntity.add("uuid", uuid);
			isTestFlag = isTestFlag == null ? "" : isTestFlag;
			testResult = testResult == null ? "" : testResult;
			result = restTemplate
					.getForEntity(financeUrl + "/api/ssf-finance/product-groups/homepage?uuid=" + uuid
							+ "&isTestFlag=" + isTestFlag + "&testResult=" + testResult + "&oemid=" + Integer.parseInt(oemid), Map.class).getBody();
			if (result == null || result.size() == 0) {
				/*result.put("msg", "获取失败");*/
				return new JsonResult(JsonResult.SUCCESS, "没有获取到产品", JsonResult.EMPTYRESULT);
			} else {
				List bannerList = new ArrayList();
//				if (oemid == null) {
//					oemid = 1L;
//				}
//				oemid = oemid == null ? 1 : oemid;
				Map<String, String> oemInfos = grpcOemInfoService.getOemInfoById(Long.parseLong(oemid + ""));
				logger.info("oemInfos====home_page1:" + oemInfos.get("homePageImgOne"));
				logger.info("oemInfos====home_page2:" + oemInfos.get("homePageImgTwo"));
				logger.info("oemInfos====home_page3:" + oemInfos.get("homePageImgThree"));
				logger.info("oemInfos====home_page4:" + oemInfos.get("homePageImgFour"));
				bannerList.add(oemInfos.get("homePageImgOne"));
				bannerList.add(oemInfos.get("homePageImgTwo"));
				bannerList.add(oemInfos.get("homePageImgThree"));
				bannerList.add(oemInfos.get("homePageImgFour"));
//				if (oemid == null || oemid == 1) {
//					bannerList.add("http://47.96.164.161:81/1.png");
//					bannerList.add("http://47.96.164.161:81/2.png");
//					bannerList.add("http://47.96.164.161:81/3.png");
//					bannerList.add("http://47.96.164.161:81/4.png");
//				} else if(oemid == 2){
//					bannerList.add("http://47.96.164.161/1.png");
//					bannerList.add("http://47.96.164.161/2.png");
//					bannerList.add("http://47.96.164.161/3.png");
//					bannerList.add("http://47.96.164.161/4.png");
//				}
				result.put("banner_list", bannerList);
				for (Object obj : result.values()) {
					if (obj != null && obj instanceof Map) {
						Map objMap = (Map) obj;
						if (objMap.containsKey("income6month")) {
							// 历史年化收益率和历史波动率
							Double historicalYearPerformance = (Double) objMap.get("historicalYearPerformance");
							historicalYearPerformance = EasyKit
									.getDecimal(new BigDecimal(historicalYearPerformance));
							objMap.put("historicalYearPerformance", historicalYearPerformance + EasyKit.PERCENT);
							Double historicalvolatility = (Double) objMap.get("historicalvolatility");
							historicalvolatility = EasyKit.getDecimal(new BigDecimal(historicalvolatility));
							objMap.put("historicalvolatility", historicalvolatility + EasyKit.PERCENT);
							//历史收益率
							if (objMap.get("income6month") != null) {
								Map income6monthMap = (Map) objMap.get("income6month");
								List<Double> maxminList = new ArrayList<Double>();
								if (income6monthMap.get("_items") != null) {
									List itemList = (List) income6monthMap.get("_items");
									for (int i = 0; i < itemList.size(); i++) {
										Map itemMap = (Map) itemList.get(i);
										Double value = (Double) itemMap.get("value");
										value = EasyKit.getDecimal(new BigDecimal(value));
										itemMap.put("value", value);
										maxminList.add(value);
									}
								}
								Map maxminMap = new HashMap();
								maxminMap.put("minValue", Collections.min(maxminList));
								maxminMap.put("maxValue", Collections.max(maxminList));
								income6monthMap.put("maxMinMap", maxminMap);
							}
						} 
						if (objMap.containsKey("product_list")) {
							List productList = (List) objMap.get("product_list");
							if (productList != null && productList.size() > 0) {
								Collections.sort(productList, new Comparator<Map<String, Object>>() {
									public int compare(Map<String, Object> o1, Map<String, Object> o2) {
										int map1value = (int) o1.get("value");
										int map2value = (int) o2.get("value");
										return map2value - map1value;
									}
								});
								Integer count = 0;
								Integer value = 0;
								for (int i = 0; i < productList.size(); i++) {
									Map pMap = (Map) productList.get(i);
									if (pMap.get("value") != null) {
										if (i == productList.size() - 1) {
											value = 100 - count;
										} else {
											value = (Integer) pMap.get("value");
											count = count + value;
										}
										if (value != null) {
											pMap.put("value", value);
										} else {
											pMap.put("value", "0.00");
										}
									}
								}
							}
						}
					}
				}
			}
//			requestEntity.add("productType", productType);
			/*result.put("msg", "获取成功");*/
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("进入理财页面后的数据（暂时不用）")
	//@RequestMapping(value = "/financeFrontPage2", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult financeModule2() {
		Map returnMap = new HashMap<>();
		//BANNER LIST
		List<String> bannerList = new ArrayList<>();
		bannerList.add("http://47.96.164.161/APP-invest-banner01.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner02.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner03.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner04.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner05.png");
		returnMap.put("bannerList", bannerList);

		//先获取全部产品
		String url = assetAlloctionUrl + "/api/asset-allocation/products";
		Map result = null;//中间容器

		Object object = null;
		List<Map<String, Object>> prdList = null; //中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();//结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
		} catch (Exception e) {
			//获取list失败直接返回
				/*result=new HashMap<>();
				String message=e.getMessage();*/
			return new JsonResult(JsonResult.Fail, "获取理财产品调用restTemplate方法发生错误！", JsonResult.EMPTYRESULT);
		}
		//如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		if (result != null) {
			object = result.get("_items");
			if (object instanceof List) {
				//转换成List
				prdList = (List<Map<String, Object>>) object;
				try {
					for (Map<String, Object> productMap : prdList) {
						//获取goupid和subGroupId
						String groupId =
								(productMap.get("groupId")) == null ? null : (productMap.get("groupId")).toString();
						String subGroupId = (productMap.get("subGroupId")) == null ? null
								: (productMap.get("subGroupId")).toString();
						String prdName =
								productMap.get("name") == null ? null : (productMap.get("name")).toString();
						List productCompo = (List) productMap.get("assetsRatios");
						if (productCompo != null && productCompo.size() > 0) {
							Double count = 0D;
							Double value = 0D;
							for (int i = 0; i < productCompo.size(); i++) {
								Map pMap = (Map) productCompo.get(i);
								if (pMap.get("value") != null) {
									if (i == productCompo.size() - 1) {
										value = 100D - count;
										BigDecimal bigValue = new BigDecimal(value);
										value = bigValue.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
									} else {
										value = (Double) pMap.get("value");
										value = EasyKit.getDecimal(new BigDecimal(value));
										count = count + value;
									}
									if (value != null) {
										pMap.put("value", value);
									} else {
										pMap.put("value", "0.00");
									}
								}
							}
						}

						//去另一接口获取历史收益率图表的数据
						Map histYieldRate = getCombYieldRate(groupId, subGroupId, null);
						//去另一个接口获取预期年化，预期最大回撤
						Map expAnnReturn = getExpAnnReturn(groupId, subGroupId, null);
						if (expAnnReturn.containsKey("value")) {
							expAnnReturn.put("value", expAnnReturn.get("value"));
						}
						//Map ExpMaxReturn=getExpMaxReturn(groupId,subGroupId);
						//将结果封装进实体类
						FinanceProductCompo prd = new FinanceProductCompo(groupId, subGroupId, prdName,
								expAnnReturn.size() > 0 ? expAnnReturn.get("value").toString() : null, productCompo,
								histYieldRate);
						resultList.add(prd);
					}
				} catch (Exception e) {
					return new JsonResult(JsonResult.Fail, "获取产品的field属性失败", JsonResult.EMPTYRESULT);
				}
			}
		} else {
			return new JsonResult(JsonResult.Fail, "没有获取到产品", JsonResult.EMPTYRESULT);
		}
		returnMap.put("data", resultList);
		return new JsonResult(JsonResult.SUCCESS, "获取成功", returnMap);
	}
	
//	@ApiOperation("进入理财页面后的数据")
//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "Long", required = false, value = "1")
//	})
//	@RequestMapping(value = "/financeFrontPage", method = RequestMethod.POST)
//	@ResponseBody
//	@AopTimeResources
//	public JsonResult financeModule(@RequestParam(required = false) Long oemid) {
//		// 先获取全部产品
//		JsonResult result = restTemplate
//				.getForEntity(dataManagerUrl + "/api/datamanager/getFinanceFrontPage", JsonResult.class).getBody();
//		Object obj = result.getResult();
//		if(obj!=null){
//			HashMap resultMap = (HashMap) obj;
//			List bannerList = new ArrayList();
//			if (oemid == null || oemid == 1) {
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner01.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner02.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner03.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner04.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner05.png");
//			} else if(oemid == 2){
//				bannerList.add("http://47.96.164.161/APP-invest-banner01.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner02.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner03.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner04.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner05.png");
//			}
//			resultMap.put("bannerList", bannerList);
//		}
//		return result;
//	}
	
	@ApiOperation("进入理财页面后的数据")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid（贝贝鱼:1,兰州银行：2）"),
		@ApiImplicitParam(paramType = "query", name = "size", dataType = "Integer", required = true, value = "每页显示数（至少大于1）", defaultValue = "15"),
		@ApiImplicitParam(paramType = "query", name = "pageSize", dataType = "Integer", required = true, value = "显示页数（从0开始）", defaultValue = "0"),
	})
	@RequestMapping(value = "/financeFrontPage", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult financeModule(@RequestParam(required = false, defaultValue="1") String oemid, @RequestParam(defaultValue="15") Integer size,@RequestParam(defaultValue="0") Integer pageSize) {
		// 先获取全部产品
		JsonResult result = restTemplate
				.getForEntity(dataManagerUrl + "/api/datamanager/getFinanceFrontPage?size=" + size + "&pageSize=" + pageSize + "&oemid=" + oemid, JsonResult.class).getBody();
		Object obj = result.getResult();
		if(obj!=null){
			HashMap resultMap = (HashMap) obj;
			List bannerList = new ArrayList();
//			if (oemid == null) {
//				oemid = 1L;
//			}
			Map<String, String> oemInfos = grpcOemInfoService.getOemInfoById(Long.parseLong(oemid + ""));
			logger.info("oemInfos====combination1:" + oemInfos.get("combinationOne"));
			logger.info("oemInfos====combination2:" + oemInfos.get("combinationTwo"));
			logger.info("oemInfos====combination3:" + oemInfos.get("combinationThree"));
			logger.info("oemInfos====combination4:" + oemInfos.get("combinationFour"));
			logger.info("oemInfos====combination5:" + oemInfos.get("combinationFive"));
			bannerList.add(oemInfos.get("combinationOne"));
			bannerList.add(oemInfos.get("combinationTwo"));
			bannerList.add(oemInfos.get("combinationThree"));
			bannerList.add(oemInfos.get("combinationFour"));
			bannerList.add(oemInfos.get("combinationFive"));
//			if (oemid == null || oemid == 1) {
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner01.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner02.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner03.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner04.png");
//				bannerList.add("http://47.96.164.161:81/APP-invest-banner05.png");
//			} else if(oemid == 2){
//				bannerList.add("http://47.96.164.161/APP-invest-banner01.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner02.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner03.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner04.png");
//				bannerList.add("http://47.96.164.161/APP-invest-banner05.png");
//			}
			resultMap.put("bannerList", bannerList);
			resultMap.put("title1", "组合");
			resultMap.put("title2", "比较基准");
		}
		return result;
	}


	@ApiOperation("理财产品查看详情页面(暂时不用)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "8"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "80048"),
	})
	//@RequestMapping(value = "/checkPrdDetails", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getPrdDetails2(String groupId, String subGroupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = service.getPrdNPVList(groupId, subGroupId, null);
		if (result == null) {
			return new JsonResult(JsonResult.Fail, "获取净值增长值活净值增长率为空", JsonResult.EMPTYRESULT);
		}

		Map expAnnReturn = getExpAnnReturn(groupId, subGroupId, null);
		Map expMaxReturn = getExpMaxReturn(groupId, subGroupId, null);
		Map simulateHistoricalReturn = getSimulateHistoricalReturn(groupId, subGroupId, null);
		result.put("expAnnReturn", expAnnReturn);
		result.put("expMaxDrawDown", expMaxReturn);
		result.put("simulateHistoricalVolatility", simulateHistoricalReturn);
		//饼图（返回单个基金组合产品信息）
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId;
			Map productMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (productMap == null) {
				logger.info("单个基金组合产品信息为空");
			} else {
				if (productMap.get("assetsRatios") != null) {
					List<Map> assetList = (List<Map>) productMap.get("assetsRatios");
					Double count = 0D;
					Double value = 0D;
					for (int i = 0; i < assetList.size(); i++) {
						Map<String, Object> assetMap = assetList.get(i);
						if (assetMap.get("value") != null) {
							if (i == assetList.size() - 1) {
								value = 100D - count;
								BigDecimal bigValue = new BigDecimal(value);
								value = bigValue.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
							} else {
								value = (Double) assetMap.get("value");
								value = EasyKit.getDecimal(new BigDecimal(value));
								count = count + value;
							}
							if (value != null) {
								assetMap.put("value", value);
							} else {
								assetMap.put("value", "0.00");
							}
						}
					}
					result.put("assetsRatios", productMap.get("assetsRatios"));
				}
			}
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "查看理财产品详情成功", result);
	}
	
	@ApiOperation("理财产品查看详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid（贝贝鱼:1,兰州银行：2）"),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "12"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "120048"),
	})
	@RequestMapping(value = "/checkPrdDetails", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getPrdDetails(@RequestParam(required = false, defaultValue="1") String oemid ,@RequestParam(required = true) String groupId,@RequestParam(required = true) String subGroupId) {
		// 先获取全部产品
		JsonResult result = restTemplate
				.getForEntity(dataManagerUrl + "/api/datamanager/getCheckPrdDetails?groupId=" + groupId + "&subGroupId="
						+ subGroupId + "&oemid=" + Integer.parseInt(oemid), JsonResult.class).getBody();
		return result;
	}

	@ApiOperation("历史业绩")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "productName", dataType = "String", required = false, value = "productName", defaultValue = "贝贝鱼1号“御•安守”组合"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/historicalPerformancePage", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getHistoricalPerformance(@RequestParam(required = false) String groupId,
			@RequestParam(required = false) String subGroupId,
			@RequestParam(required = false) String productName,
			@RequestParam(required = false, defaultValue="1") String oemid) {
		long startTime = System.currentTimeMillis();
		logger.info("历史业绩：" + startTime);
		// 先获取全部产品
		String url = assetAlloctionUrl
				+ "/api/asset-allocation/product-groups/historicalPer-formance?fund_group_id=" + groupId
				+ "&subGroupId=" + subGroupId + "&oemId=" + Integer.parseInt(oemid);
		Map<String, Object> result = new HashMap<String, Object>();// 中间容器
		Map<String, Object> title = new HashMap<String, Object>();
		Object object = null;
		List<Map<String, Object>> prdList = null; // 中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();// 结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
			logger.info("历史业绩-第一部分数据获取成功");
			if (result != null) {
				object = result.get("_items");
				if (object != null) {
					title.put("header1", productName);
					logger.info("object获取成功");
					List<Map> perfomaceList = (List<Map>) object;
					for (int i = 0; i < perfomaceList.size(); i++) {
						Map performanceMap = perfomaceList.get(i);
						if (performanceMap.containsKey("value")) {
							Double performance = (Double) performanceMap.get("value");
							int id = (int) performanceMap.get("id");
							if (id < 4) {
								performance = EasyKit.getDecimal(new BigDecimal(performance));
								performanceMap.put("value", performance + EasyKit.PERCENT);
							} else {
								performance = EasyKit.getDecimalNum(new BigDecimal(performance));
								performanceMap.put("value", performance);
							}
						}
					}
					result.put("historicalPerformance", object);
					result.remove("_items");
					result.remove("name");
					result.remove("_schemaVersion");
					result.remove("_serviceId");
				} else {
					logger.info("object获取失败");
				}

			} else {
				return new JsonResult(JsonResult.SUCCESS, "获取失败", resultList);
			}
		} catch (Exception e) {
			// 获取list失败直接返回
			logger.error("获取理财产品调用restTemplate方法发生错误", e);
			String message = e.getMessage();
			result.put("错误原因", message + ",获取理财产品调用restTemplate方法发生错误！");
			return new JsonResult(JsonResult.Fail, "获取理财List数据失败", JsonResult.EMPTYRESULT);
		}

		//收益率走势图
		//http://localhost:10020/api/asset-allocation/product-groups/6/sub-groups/111111/portfolio-yield-week?returnType=income
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
				+ subGroupId + "/portfolio-yield-week?returnType=income&oemId=" + Integer.parseInt(oemid);
		Map<String, Object> incomeResult = new HashMap<String, Object>();
		incomeResult = restTemplate.getForEntity(url, Map.class).getBody();
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		Object obj = null;
		Object maxMinMap = null;
		Object maxMinBenchmarkMap = null;
		if (incomeResult != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj = incomeResult.get("_items");
			maxMinMap = incomeResult.get("maxMinMap");
			maxMinBenchmarkMap = incomeResult.get("maxMinBenchmarkMap");
			if (obj != null) {
				logger.info("obj获取成功");
				List<Map> objList = (List<Map>) obj;
				for (int i = 0; i < objList.size(); i++) {
					Map objMap = objList.get(i);
					List income = new ArrayList();
					if (objMap.containsKey("income") && objMap.get("income") != null) {
						income = (List<Map>) objMap.get("income");
						if (income != null && income.size() > 0) {
							for (int j = 0; j < income.size(); j++) {
								Map incomeMap = (Map) income.get(j);
								if (incomeMap.get("value") != null) {
									Double incomeValue = (Double) incomeMap.get("value");
									incomeValue = EasyKit.getDecimal(new BigDecimal(incomeValue));
									incomeMap.put("value", incomeValue);
								} else {
									incomeMap.put("value", "0.00" + EasyKit.PERCENT);
								}
							}
						}
					}
					if (objMap.containsKey("incomeBenchmark") && objMap.get("incomeBenchmark") != null) {
						income = (List<Map>) objMap.get("incomeBenchmark");
						if (income != null && income.size() > 0) {
							for (int j = 0; j < income.size(); j++) {
								Map incomeMap = (Map) income.get(j);
								if (incomeMap.get("value") != null) {
									Double incomeValue = (Double) incomeMap.get("value");
									incomeValue = EasyKit.getDecimal(new BigDecimal(incomeValue));
									incomeMap.put("value", incomeValue);
								} else {
									incomeMap.put("value", "0.00" + EasyKit.PERCENT);
								}
							}
						}
					}

				}
				result.put("portfolioYield", obj);
			} else {
				logger.info("object获取失败");
			}
			if (!CollectionUtils.isEmpty((Map) maxMinMap)) {
				Double min = (Double) ((Map) maxMinMap).get("minValue");
				Double max = (Double) ((Map) maxMinMap).get("maxValue");
				Double minValue = EasyKit.getDecimal(new BigDecimal(min));
				Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
				((Map) maxMinMap).put("minValue", minValue);
				((Map) maxMinMap).put("maxValue", maxValue);
				logger.info("maxMinIncomeMap获取成功");
				result.put("maxMinMap", maxMinMap);
			} else {
				logger.info("maxMinMap获取失败");
			}
			if (!CollectionUtils.isEmpty((Map) maxMinBenchmarkMap)) {
				logger.info("maxMinBenchmarkMap获取成功");
				Double min = (Double) ((Map) maxMinBenchmarkMap).get("minValue");
				Double max = (Double) ((Map) maxMinBenchmarkMap).get("maxValue");
				Double minValue = EasyKit.getDecimal(new BigDecimal(min));
				Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
				((Map) maxMinBenchmarkMap).put("minValue", minValue);
				((Map) maxMinBenchmarkMap).put("maxValue", maxValue);
				result.put("maxMinIncomeBenchmarkMap", maxMinBenchmarkMap);
			} else {
				logger.info("Income获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", resultList);
		}
		//最大回撤走势图
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
				+ subGroupId + "/portfolio-yield-week?returnType=1&oemId=" + Integer.parseInt(oemid);
		Map<String, Object> incomeResult1 = new HashMap<String, Object>();
		incomeResult1 = restTemplate.getForEntity(url, Map.class).getBody();
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		Object obj1 = null;
		Object maxMinMap2 = null;
		Object maxMinBenchmarkMap2 = null;
		if (incomeResult1 != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj1 = incomeResult1.get("_items");
			maxMinMap2 = incomeResult1.get("maxMinMap");
			maxMinBenchmarkMap2 = incomeResult1.get("maxMinBenchmarkMap");
			if (obj1 != null) {
				List<Map> maxRetreatList = (List<Map>) obj1;
				for (int i = 0; i < maxRetreatList.size(); i++) {
					Map maxRetreatMap = maxRetreatList.get(i);
					if (maxRetreatMap.containsKey("retracement")
							&& maxRetreatMap.get("retracement") != null) {
						List<Map> maxRetreatList2 = (List<Map>) maxRetreatMap.get("retracement");
						if (maxRetreatList2 != null && maxRetreatList2.size() > 0) {
							for (int j = 0; j < maxRetreatList2.size(); j++) {
								Map retracementMap = (Map) maxRetreatList2.get(j);
								if (retracementMap.get("value") != null) {
									Double incomeValue = (Double) retracementMap.get("value");
									incomeValue = EasyKit.getDecimal(new BigDecimal(incomeValue));
									retracementMap.put("value", incomeValue);
								} else {
									retracementMap.put("value", "0.00" + EasyKit.PERCENT);
								}
							}
						}
					}
					if (maxRetreatMap.containsKey("incomeBenchmark")
							&& maxRetreatMap.get("incomeBenchmark") != null) {
						List<Map> incomeBenchmarkList2 = (List<Map>) maxRetreatMap.get("incomeBenchmark");
						if (incomeBenchmarkList2 != null && incomeBenchmarkList2.size() > 0) {
							for (int j = 0; j < incomeBenchmarkList2.size(); j++) {
								Map retracementMap = (Map) incomeBenchmarkList2.get(j);
								if (retracementMap.get("value") != null) {
									Double incomeValue = (Double) retracementMap.get("value");
									incomeValue = EasyKit.getDecimal(new BigDecimal(incomeValue));
									retracementMap.put("value", incomeValue);
								} else {
									retracementMap.put("value", "0.00" + EasyKit.PERCENT);
								}
							}
						}
					}
				}

				logger.info("obj1获取成功");
				result.put("maxRetreat", obj1);
			} else {
				logger.info("object获取失败");
			}
			if (!CollectionUtils.isEmpty((Map)maxMinMap2)) {
				logger.info("maxMinIncomeMap获取成功");
				Double min = (Double) ((Map) maxMinMap2).get("minValue");
				Double max = (Double) ((Map) maxMinMap2).get("maxValue");
				Double minValue = EasyKit.getDecimal(new BigDecimal(min));
				Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
				((Map) maxMinMap2).put("minValue", minValue);
				((Map) maxMinMap2).put("maxValue", maxValue);
				result.put("maxMinRetreatMap", maxMinMap2);
			} else {
				logger.info("maxMinMap2获取失败");
			}
			if (!CollectionUtils.isEmpty((Map)maxMinBenchmarkMap2)) {
				logger.info("maxMinBenchmarkMap获取成功");
				Double min = (Double) ((Map) maxMinBenchmarkMap2).get("minValue");
				Double max = (Double) ((Map) maxMinBenchmarkMap2).get("maxValue");
				Double minValue = EasyKit.getDecimal(new BigDecimal(min));
				Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
				((Map) maxMinBenchmarkMap2).put("minValue", minValue);
				((Map) maxMinBenchmarkMap2).put("maxValue", maxValue);
				result.put("maxMinRetreatBenchmarkMap", maxMinBenchmarkMap2);
			} else {
				logger.info("Income获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", result);
		}
		result.put("title", title);
		
		long endTime = System.currentTimeMillis();
		logger.info("（历史业绩）结束时间：" + endTime);
		logger.info("该方法执行时间为: " + (endTime - startTime));
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

	@ApiOperation("未来预期page")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1"),
	})
	@RequestMapping(value = "/futureExpectationPage", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getFutureExpectation(@RequestParam(required = false) String uuid,
			@RequestParam String groupId, @RequestParam String subGroupId, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 预期平均年化收益率
		Map<String, Object> optMap = new HashMap<String, Object>();
		String url =
				assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/opt/" + Integer.parseInt(oemid) + "?returntype=1";
		optMap = restTemplate.postForEntity(url, null, Map.class).getBody();
		if (optMap != null && !optMap.isEmpty()) {
			logger.info("预期平均年化收益率获取成功");
			Object obj = optMap.get("value");
			String value = "";
			if (obj != null) {
				logger.info("预期平均年化收益率获取成功2");
				value = obj.toString();
				Double doubleValue = EasyKit.getDecimal(new BigDecimal(value));
				result.put("averageAnnualRate", doubleValue + EasyKit.PERCENT);
			} else {
				logger.error("预期平均年化收益率获取失败");
			}
		} else {
			logger.error("预期平均年化收益率获取失败2");
		}

		//未来收益走势图
		Object object = null;
		List<Map<String, Object>> prdList = new ArrayList<Map<String, Object>>(); // 中间容器
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
				+ subGroupId + "/expected-income?oemId=" + Integer.parseInt(oemid);
		try {
			Map<String, Object> expectedIncomeMap = new HashMap<String, Object>();
			expectedIncomeMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (expectedIncomeMap != null && !expectedIncomeMap.isEmpty()) {
				List<Map<String, Object>> expectedIncomeList = (List<Map<String, Object>>) expectedIncomeMap
						.get("_items");
				if (expectedIncomeList != null && expectedIncomeList.size() > 0) {
					for (int i = 0; i < expectedIncomeList.size(); i++) {
						Map incomeMap = expectedIncomeList.get(i);
						if (incomeMap.get("_item") != null) {
							Map<String, Object> itemMap = (Map<String, Object>) incomeMap.get("_item");
							if (itemMap != null && itemMap.size() > 0) {
								for (String key : itemMap.keySet()) {
									Double value = (Double) itemMap.get(key);
									Double doubleValue = EasyKit.getDecimal(new BigDecimal(value));
									itemMap.put(key, doubleValue);
								}
							}
						}
					}
				}

				logger.info("未来收益走势图数据获取成功");
				Object expectedIncomeSizeMap = expectedIncomeMap.get("expectedIncomeSizeMap");
				Object highPercentMaxIncomeSizeMap = expectedIncomeMap.get("highPercentMaxIncomeSizeMap");
				Object highPercentMinIncomeSizeMap = expectedIncomeMap.get("highPercentMinIncomeSizeMap");
				Object lowPercentMaxIncomeSizeMap = expectedIncomeMap.get("lowPercentMaxIncomeSizeMap");
				Object lowPercentMinIncomeSizeMap = expectedIncomeMap.get("lowPercentMinIncomeSizeMap");
				Object obj2 = expectedIncomeMap.get("_items");
				if (obj2 != null) {
					logger.info("未来收益走势图数据获取成功2");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("expectedIncome", obj2);
				} else {
					logger.error("未来收益走势图数据获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) expectedIncomeSizeMap)) {
					logger.info("expectedIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					Double min = (Double) ((Map) expectedIncomeSizeMap).get("minValue");
					Double max = (Double) ((Map) expectedIncomeSizeMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) expectedIncomeSizeMap).put("minValue", minValue);
					((Map) expectedIncomeSizeMap).put("maxValue", maxValue);
					result.put("expectedIncomeSizeMap", expectedIncomeSizeMap);
				} else {
					logger.error("expectedIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) highPercentMaxIncomeSizeMap)) {
					logger.info("highPercentMaxIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					Double min = (Double) ((Map) highPercentMaxIncomeSizeMap).get("minValue");
					Double max = (Double) ((Map) highPercentMaxIncomeSizeMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) highPercentMaxIncomeSizeMap).put("minValue", minValue);
					((Map) highPercentMaxIncomeSizeMap).put("maxValue", maxValue);
					result.put("highPercentMaxIncomeSizeMap", highPercentMaxIncomeSizeMap);
				} else {
					logger.error("highPercentMaxIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) highPercentMinIncomeSizeMap)) {
					logger.info("highPercentMinIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					Double min = (Double) ((Map) highPercentMinIncomeSizeMap).get("minValue");
					Double max = (Double) ((Map) highPercentMinIncomeSizeMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) highPercentMinIncomeSizeMap).put("minValue", minValue);
					((Map) highPercentMinIncomeSizeMap).put("maxValue", maxValue);
					result.put("highPercentMinIncomeSizeMap", highPercentMinIncomeSizeMap);
				} else {
					logger.error("highPercentMinIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) lowPercentMaxIncomeSizeMap)) {
					logger.info("lowPercentMaxIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					Double min = (Double) ((Map) lowPercentMaxIncomeSizeMap).get("minValue");
					Double max = (Double) ((Map) lowPercentMaxIncomeSizeMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) lowPercentMaxIncomeSizeMap).put("minValue", minValue);
					((Map) lowPercentMaxIncomeSizeMap).put("maxValue", maxValue);
					result.put("lowPercentMaxIncomeSizeMap", lowPercentMaxIncomeSizeMap);
				} else {
					logger.error("lowPercentMaxIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) lowPercentMinIncomeSizeMap)) {
					logger.info("lowPercentMinIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					Double min = (Double) ((Map) lowPercentMinIncomeSizeMap).get("minValue");
					Double max = (Double) ((Map) lowPercentMinIncomeSizeMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) lowPercentMinIncomeSizeMap).put("minValue", minValue);
					((Map) lowPercentMinIncomeSizeMap).put("maxValue", maxValue);
					result.put("lowPercentMinIncomeSizeMap", lowPercentMinIncomeSizeMap);
				} else {
					logger.error("lowPercentMinIncomeSizeMap:未来收益走势图数据获取失败");
				}
			} else {
				logger.error("未来收益走势图数据获取失败2");
			}
		} catch (Exception e) {
			// 获取list失败直接返回
//			logger.error("未来收益走势图数据发生错误", e);
			/*String message = e.getMessage();
			result.put("错误原因", message + ",未来收益走势图数据发生错误！"); */
			String str = new ReturnedException(e).getErrorMsg();
			logger.error("未来收益走势图数据发生错误", e);
			return new JsonResult(JsonResult.Fail, "未来收益走势图数据失败", JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

	@ApiOperation("风险控制")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "productName", dataType = "String", required = false, value = "productName", defaultValue = "贝贝鱼1号“御•安守”组合"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/riskMangementPage", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getRiskManagement(@RequestParam(required = false) String uuid,
			@RequestParam String groupId, @RequestParam String subGroupId,
			@RequestParam(required = false) String productName,
			@RequestParam(required = false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> title = new HashMap<String, Object>();
		// 最大回撤走势图
		Map<String, Object> portfolioYieldWeekMap = new HashMap<String, Object>();
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/portfolio-yield-week?returnType=1&oemId=" + Integer.parseInt(oemid);
			portfolioYieldWeekMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (portfolioYieldWeekMap != null && !portfolioYieldWeekMap.isEmpty()) {
				logger.info("最大回撤走势图获取成功");
				Object obj = portfolioYieldWeekMap.get("_items");
				Object maxMinMap = portfolioYieldWeekMap.get("maxMinMap");
				Object maxMinBenchmarkMap = portfolioYieldWeekMap.get("maxMinBenchmarkMap");
				String value = "";
				if (obj != null) {
					logger.info("最大回撤走势图获取成功2");
					// value = obj.toString();
					List list = (List) obj;
					for (int i = 0; i < list.size(); i++) {
						Map map = (Map) list.get(i);
						if (map != null && map.size() > 0) {
							if (map.get("retracement") != null) {
								List retracementList = (List) map.get("retracement");
								if (retracementList != null && retracementList.size() > 0) {
									for (int j = 0; j < retracementList.size(); j++) {
										Map retracementMap = (Map) retracementList.get(j);
										Double doubleValue = (Double) retracementMap.get("value");
										doubleValue = EasyKit.getDecimal(new BigDecimal(doubleValue));
										retracementMap.put("value", doubleValue);
									}
								}
								List incomeBenchmarkList = (List) map.get("incomeBenchmark");
								if (incomeBenchmarkList != null && incomeBenchmarkList.size() > 0) {
									for (int j = 0; j < incomeBenchmarkList.size(); j++) {
										Map incomeBenchmarkMap = (Map) incomeBenchmarkList.get(j);
										Double doubleValue = (Double) incomeBenchmarkMap.get("value");
										doubleValue = EasyKit.getDecimal(new BigDecimal(doubleValue));
										incomeBenchmarkMap.put("value", doubleValue);
									}
								}
							}
						}
					}

					result.put("portfolioYieldWeek", obj);
				} else {
					logger.error("最大回撤走势图获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) maxMinMap)) {
					logger.info("maxMinMap:最大回撤走势图获取成功");
					// value = obj.toString();
					Double min = (Double) ((Map) maxMinMap).get("minValue");
					Double max = (Double) ((Map) maxMinMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) maxMinMap).put("minValue", minValue);
					((Map) maxMinMap).put("maxValue", maxValue);
					result.put("maxMinMap", maxMinMap);
				} else {
					logger.error("maxMinMap:最大回撤走势图获取失败");
				}
				if (!CollectionUtils.isEmpty((Map) maxMinBenchmarkMap)) {
					logger.info("maxMinBenchmarkMap:最大回撤走势图获取成功");
					// value = obj.toString();
					Double min = (Double) ((Map) maxMinBenchmarkMap).get("minValue");
					Double max = (Double) ((Map) maxMinBenchmarkMap).get("maxValue");
					Double minValue = EasyKit.getDecimal(new BigDecimal(min));
					Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
					((Map) maxMinBenchmarkMap).put("minValue", minValue);
					((Map) maxMinBenchmarkMap).put("maxValue", maxValue);
					result.put("maxMinBenchmarkMap", maxMinBenchmarkMap);
				} else {
					logger.error("maxMinBenchmarkMap:最大回撤走势图获取失败");
				}
			} else {
				logger.error("预期平均年化收益率获取失败2");
			}
			title.put("header1", productName);
			//预期最大回撤数
			url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
					+ subGroupId + "/opt/" + Integer.parseInt(oemid) + "?returntype=2";
			Map<String, Object> optResult = (Map) restTemplate.postForEntity(url, null, Map.class)
					.getBody();
			if (optResult != null) {
				if (optResult.get("value") != null) {
					double opt = Double.valueOf(optResult.get("value") + "");
					if (opt <= 0) {
						opt = opt - 0.05;
						opt = EasyKit.getDecimal(new BigDecimal(opt));
						logger.info("预期最大回撤数获取成功");
					} else {
						logger.error("预期最大回撤数获取失败，应为负数，而不是为：" + opt);
					}
					result.put("optValue", opt);
				}
			}

			// 等级风险
			url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
					+ subGroupId
					+ "/risk-controls";
			Map<String, Object> riskMap = new HashMap<String, Object>();
			riskMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (riskMap != null && !riskMap.isEmpty()) {
				logger.info("等级风险数据获取成功");
				Object obj2 = riskMap.get("_items");
				if (obj2 != null) {
					List riskList = (List) obj2;
					for (int i = 0; i < riskList.size(); i++) {
						Map risk2Map = (Map) riskList.get(i);
						if (risk2Map.get("benchmark") != null) {
							Double benchmark = (Double) risk2Map.get("benchmark");
							benchmark = EasyKit.getDecimal(new BigDecimal(benchmark));
							risk2Map.put("benchmark", benchmark + EasyKit.PERCENT);
						}
						if (risk2Map.get("level2RiskControl") != null) {
							Double level2RiskControl = (Double) risk2Map.get("level2RiskControl");
							level2RiskControl = EasyKit.getDecimal(new BigDecimal(level2RiskControl));
							risk2Map.put("level2RiskControl", level2RiskControl + EasyKit.PERCENT);
						}

					}
					logger.info("等级风险数据获取成功2");
					// prdList = (List<Map<String, Object>>) obj2;
					result.put("levelRiskControl", obj2);
				} else {
					logger.info("等级风险数据获取失败");
				}
			} else {
				logger.info("等级风险数据获取失败2");
			}

			// 风险控制手段与通知
			url = assetAlloctionUrl + "/api/asset-allocation/products/" + uuid + "/risk-notifications";
			Map<String, Object> riskNotificationsMap = new HashMap<String, Object>();
			riskNotificationsMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (riskNotificationsMap != null && !riskNotificationsMap.isEmpty()) {
				logger.info("风险控制手段与通知数据获取成功");
				Object obj2 = riskNotificationsMap.get("_items");
				if (obj2 != null) {
					logger.info("风险控制手段与通知获取成功2");
					List<Map<String, Object>> prdList = (List<Map<String, Object>>) obj2;
					for (int i = 0; i < prdList.size(); i++) {
						Map<String, Object> prdMap = prdList.get(i);
						prdMap.remove("content");
					}
					result.put("riskNotifications", prdList);
				} else {
					logger.info("风险控制手段与通知失败");
				}
			} else {
				logger.info("风险控制手段与通知失败2");
			}
			result.put("title", title);
		} catch (Exception e) {
			// 获取list失败直接返回
			logger.error("风险控制数据发生错误", e);
			String message = e.getMessage();
			result.put("错误原因", message + ",风险控制数据发生错误！");
			return new JsonResult(JsonResult.Fail, "风险控制数据失败", result);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

	@ApiOperation("全球配置")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/globalConfigurationPage", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getGlobalConfiguration(@RequestParam(required = false) String uuid,
			@RequestParam String groupId, @RequestParam String subGroupId, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 配置收益贡献
		Map<String, Object> configurationBenefitContributionMap = new HashMap<String, Object>();
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/contributions/" + Integer.parseInt(oemid);
			configurationBenefitContributionMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (configurationBenefitContributionMap != null && !configurationBenefitContributionMap
					.isEmpty()) {
				logger.info("配置收益贡献获取成功");
				Object obj = configurationBenefitContributionMap.get("_items");
				Object category = configurationBenefitContributionMap.get("_total");
				List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();
				if (obj != null && category != null) {
					logger.info("配置收益贡献获取成功2");
					value = (List<Map<String, Object>>) obj;
					Map<String, Object> configMap = value.get(0);
					Object configTime = null;
					if (configMap != null && configMap.size() > 0) {
						logger.info("配置收益贡献时间获取成功");
						configTime = configMap.get("time");
						if (configTime == null) {
							logger.error("配置收益贡献时间获取为空");
						}
					} else {
						logger.error("配置收益贡献时间获取失败");
					}
					List<Map<String, Object>> list = getMaxToMinComparator(value);
					result.put("configurationBenefitContribution", list);
					result.put("categoryQuantity", category);
					result.put("configurationTime", configTime);
				} else {
					logger.error("配置收益贡献获取失败" + category);
				}
			} else {
				logger.error("配置收益贡献获取失败2");
			}
		} catch (Exception e) {
			// 获取list失败直接返回
			logger.error("风险控制数据发生错误", e);
			String message = e.getMessage();
			result.put("错误原因", message + ",风险控制数据发生错误！");
			return new JsonResult(JsonResult.Fail, "风险控制数据失败", JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

	@ApiOperation("查询产品的历史收益率和最大回撤")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/getExpAnnualAndMaxReturn", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getExpAnnualAndMaxReturn(String groupId, String subGroupId, @RequestParam(required=false, defaultValue="1")String oemid) {
		return new JsonResult(JsonResult.SUCCESS, "请求成功",
				service.getExpAnnualAndMaxReturn(groupId, subGroupId, Integer.parseInt(oemid)));
	}


	/**
	 * 获取预期最大回撤（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+2）
	 */
	protected Map<String, Object> getExpMaxReturn(String groupId, String subGroupId, Integer oemid) {
		Map result = null;
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/opt/"+ oemid + "?returntype=" + "2";
//			String str="{\"returnType\":\""+"2"+"\"}";
			result = (Map) restTemplate.postForEntity(url, null, Map.class).getBody();

			if (result.get("value") != null) {
				Double value = (Double) result.get("value");
				if (!StringUtils.isEmpty(value)) {
					value = EasyKit.getDecimal(new BigDecimal(value));
					result.put("value", value + EasyKit.PERCENT);
				}
			}
		} catch (Exception e) {
			result = new HashMap<String, Object>();
			result.put("error", "restTemplate获取预期最大回撤失败");
		}
		return result;
	}


	/**
	 * 获取预期年化收益（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+1）
	 */
	protected Map<String, Object> getExpAnnReturn(String groupId, String subGroupId, Integer oemid) {
		Map<String, Object> result = new HashMap<>();
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/opt/" + oemid + "?returntype=" + "1";
			String str = "{\"returnType\":\"" + "1" + "\"}";
			result = (Map) restTemplate.postForEntity(url, getHttpEntity(str), Map.class).getBody();
			/*result.remove("_total");
			result.remove("_name");
			result.remove("_links");
			result.remove("_serviceId");
			result.remove("_schemaVersion");*/
			if (result.get("value") != null) {
				Double value = (Double) result.get("value");
				if (!StringUtils.isEmpty(value)) {
					value = EasyKit.getDecimal(new BigDecimal(value));
					result.put("value", value + EasyKit.PERCENT);
				}
			}
		} catch (Exception e) {
			result = new HashMap<String, Object>();
			result.put("error", "restTemplate获取预期年化收益失败");
		}
		return result;
	}
	
	/**
	 *模拟历史年化波动率（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+3）
	 */
	protected Map<String, Object> getSimulateHistoricalReturn(String groupId, String subGroupId, Integer oemid) {
		Map<String, Object> result = new HashMap<>();
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/opt/" + oemid + "?returntype=" + "3";
			String str = "{\"returnType\":\"" + "1" + "\"}";
			result = (Map) restTemplate.postForEntity(url, getHttpEntity(str), Map.class).getBody();
			/*result.remove("_total");
			result.remove("_name");
			result.remove("_links");
			result.remove("_serviceId");
			result.remove("_schemaVersion");*/
			if (result.get("value") != null) {
				Double value = (Double) result.get("value");
				if (!StringUtils.isEmpty(value)) {
					value = EasyKit.getDecimal(new BigDecimal(value));
					result.put("value", value + EasyKit.PERCENT);
				}
			}
		} catch (Exception e) {
			result = new HashMap<String, Object>();
			result.put("error", "restTemplate获取预期年化收益失败");
		}
		return result;
	}


	/**
	 * 根据groupid和subgroupid获取产品组合的组合收益率
	 */
	protected Map<String, Object> getCombYieldRate(String groupId, String subgroupId, Integer oemid) {
		Map result = null;
		try {
			//准备调用asset-allocation接口的方法，获取组合组合收益率(最大回撤)走势图-每天
			String url = assetAlloctionUrl
					+ "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-all?returnType=income&oemId=" + oemid;
			result = restTemplate.getForEntity(url, Map.class, groupId, subgroupId).getBody();
			result.remove("_total");
			result.remove("_name");
			result.remove("_links");
			result.remove("_serviceId");
			result.remove("_schemaVersion");
			if (result.get("_items") != null) {
				List item = (List) result.get("_items");
				if (item != null && !item.isEmpty()) {
					for (int i = 0; i < item.size(); i++) {
						Map map = (Map) item.get(i);
						if (map.containsKey("incomeBenchmark")) {
							map.remove("incomeBenchmark");
						}
						if (map.containsKey("income")) {
							List<Map> incomeList = (List<Map>) map.get("income");
							if (incomeList != null && incomeList.size() > 0) {
								for (int j = 0; j < incomeList.size(); j++) {
									Map incomeMap = incomeList.get(j);
									Double value = (Double) incomeMap.get("value");
									value = EasyKit.getDecimal(new BigDecimal(value));
									incomeMap.put("value", value);
								}
							} else {
								if (incomeList == null) {
									map.put("income", new ArrayList<Map>());
								}
							}
						}
					}
				}
				item.remove("incomeBenchmark");

				Map maxminMap = (Map) result.get("maxMinMap");
				if (!CollectionUtils.isEmpty(maxminMap)) {
					if (maxminMap != null && maxminMap.size() > 0) {
						Double min = (Double) maxminMap.get("minValue");
						Double max = (Double) maxminMap.get("maxValue");
						Double minValue = EasyKit.getDecimal(new BigDecimal(min));
						Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
						maxminMap.put("minValue", minValue);
						maxminMap.put("maxValue", maxValue);
						result.put("maxMinMap", maxminMap);
					}
				}
				maxminMap = (Map) result.get("maxMinBenchmarkMap");
				if (!CollectionUtils.isEmpty(maxminMap)) {
					if (maxminMap != null && maxminMap.size() > 0) {
						Double min = (Double) maxminMap.get("minValue");
						Double max = (Double) maxminMap.get("maxValue");
						Double minValue = EasyKit.getDecimal(new BigDecimal(min));
						Double maxValue = EasyKit.getDecimal(new BigDecimal(max));
						maxminMap.put("minValue", minValue);
						maxminMap.put("maxValue", maxValue);
						result.put("maxMinBenchmarkMap", maxminMap);
					}
				}
			}
		} catch (Exception ex) {
			result = new HashMap<String, Object>();
			String str = new ReturnedException(ex).getErrorMsg();
			logger.error("restTemplate获取预期组合收益率走势图失败:{}", str, ex);
			result.put("error", "restTemplate获取预期组合收益率走势图失败:" + str);
		}
		return result;
	}


	/**
	 * 通用方法处理post请求带requestbody
	 */
	protected HttpEntity<String> getHttpEntity(String JsonString) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;UTF-8"));
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString, headers);
		return strEntity;
	}


	@ApiOperation("2.配置收益贡献")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")})
	@RequestMapping(value = "/contributions", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult contributions(@RequestParam String groupId, @RequestParam String subGroupId, @RequestParam(required=false, defaultValue="1") String oemid) {

		String CONTRIBUTIONS_URL = "/api/asset-allocation/product-groups/{0}/sub-groups/{1}/contributions/" + Integer.parseInt(oemid);

		return postConnectFinaceUrl(CONTRIBUTIONS_URL, groupId, subGroupId);
	}


	@ApiOperation("3.组合收益率(最大回撤)走势图-每天")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009")})
	//@RequestMapping(value = "/portfolioYield", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult portfolioYield(@RequestParam String groupId, @RequestParam String subGroupId) {

		String PORFOLIO_YIELD_URL = "/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield";

		return postConnectFinaceUrl(PORFOLIO_YIELD_URL, groupId, subGroupId);
	}

	@ApiOperation("4.组合收益率(最大回撤)走势图-每天(一周以来)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009")})
	//@RequestMapping(value = "/portfolioYieldWeek", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult portfolioYieldWeek(@RequestParam String groupId,
			@RequestParam String subGroupId) {

		String PORFOLIO_YIELD_WEEK_URL = "/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield-week?returnType=1";

		return postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL, groupId, subGroupId);
	}


	@ApiOperation("5.风险控制手段与通知")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "productId", dataType = "String", required = true, value = "产品组ID", defaultValue = "1")})
	//@RequestMapping(value = "/riskNotifications", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult riskNotifications(@RequestParam String productId) {

		String PORFOLIO_YIELD_WEEK_URL = "/api/asset-allocation/products/{0}/risk-notifications";

		return postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL, productId, "");
	}


	@ApiOperation("需求调整")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "invstTerm", dataType = "String", required = true, value = "投资期限", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "riskLevel", dataType = "String", required = true, value = "风险承受级别", defaultValue = "C1"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/optAdjustment", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult getOptAdjustment(String invstTerm, String riskLevel, @RequestParam(required=false, defaultValue="1")String oemid) {
		try {
			return new JsonResult(JsonResult.SUCCESS, "请求成功",
					service.getOptAdjustment(riskLevel, invstTerm, Integer.parseInt(oemid)));
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}

	}


	private JsonResult postConnectFinaceUrl(String url, String groupId, String subGroupId) {
		Map<String, Object> result;
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("groupId", groupId);
		requestEntity.add("subGroupId", subGroupId);
		result = restTemplate
				.getForEntity(assetAlloctionUrl + MessageFormat.format(url, groupId, subGroupId), Map.class)
				.getBody();

		if (result.isEmpty()) {
			result.put("msg", "获取失败");
			return new JsonResult(JsonResult.Fail, "获取失败", JsonResult.EMPTYRESULT);
		}

		result.put("msg", "获取成功");
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}

	public static List<Map<String, Object>> getMaxToMinComparator(List<Map<String, Object>> list) {
//        List<Map<String, Object>> reuslt = new ArrayList<Map<String, Object>>();
		//排序前
		for (Map<String, Object> map : list) {
			if (map.get("value") != null) {
				Double doubleValue = (Double) map.get("value");
				doubleValue = EasyKit.getDecimal(new BigDecimal(doubleValue));
				map.put("value", doubleValue);
			}
			System.out.println(map.get("value"));
		}
		Collections.sort(list, new Comparator<Map<String, Object>>() {
			public int compare(Map<String, Object> o1, Map<String, Object> o2) {
				Double name1 = Double.valueOf(o1.get("value").toString());//name1是从你list里面拿出来的一个
				Double name2 = Double.valueOf(o2.get("value").toString()); //name1是从你list里面拿出来的第二个name
				return name2.compareTo(name1);
			}
		});
		//排序后
		System.out.println("-------------------");
		for (Map<String, Object> map : list) {
			System.out.println(map.get("value"));
			map.put("value", map.get("value") + EasyKit.PERCENT);
		}
		return list;
	}


	@ApiOperation("1.我选好了")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "riskPointValue", dataType = "String", required = false, value = "风险率", defaultValue = "0.0213"),
			@ApiImplicitParam(paramType = "query", name = "incomePointValue", dataType = "String", required = false, value = "收益率", defaultValue = "0.0451"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/optimizations", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult Optimizations(@RequestParam String groupId, @RequestParam String riskPointValue,
			@RequestParam String incomePointValue, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("riskValue", riskPointValue);
			requestEntity.add("returnValue", incomePointValue);
			requestEntity.add("oemid", oemid);
			result = restTemplate
					.postForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId
							+ "/optimizations", requestEntity, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("2.获取（预期收益率调整）有多少个点")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/inComeSlidebarPoints", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult inComeSlidebarPoints(@RequestParam String groupId, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId
							+ "/slidebar-points/" + Integer.parseInt(oemid) + "?slidebarType=income_num", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("3.获取（风险率调整）有多少个点")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/riskSlidebarPoints", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult riskSlidebarPoints(@RequestParam String groupId, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId
							+ "/slidebar-points/" + Integer.parseInt(oemid) + "?slidebarType=risk_num", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("3.获取（最优组合）")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/effectiveFrontierPoints", method = RequestMethod.POST)
	@ResponseBody
	@AopTimeResources
	public JsonResult effectiveFrontierPoints(@RequestParam String groupId, @RequestParam(required=false, defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/products/" + groupId
							+ "/effective-frontier-points/" + Integer.parseInt(oemid), Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
	
	
	@ApiOperation("调仓记录")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
//			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "80048"),
	})
	@RequestMapping(value = "/warehouse-records", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getwarehouseRecords(String groupId, @RequestParam(required=false,defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String url = financeUrl + "/api/ssf-finance/product-groups/warehouse-records?prodId=" + groupId;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if (result == null) {
				logger.error("调仓记录为空");
			} else {
				if (result != null && result.size() > 0) {
					List<Map> resultList = new ArrayList<>();
					List<Map> resultListBak = new ArrayList<>();
					resultList = (List<Map>) result.get("result");
					for (int i = resultList.size()-1; i > 0; i--) {
						Object userProdChg = resultList.get(i);
						if (userProdChg != null) {
							Map userProdChgMap = (HashMap) userProdChg;
							if (userProdChgMap.get("modifySeq") != null && userProdChgMap.get("modifyType") != null) {
								if (userProdChgMap.get("modifyTime") != null) {
									Long modifyTime = (Long) userProdChgMap.get("modifyTime");
									String time = TradeUtil.getReadableDateTime(modifyTime);
									userProdChgMap.put("modifyTime", time.split("T")[0]);
									userProdChgMap.remove("id");
//									userProdChgMap.remove("modifySeq");
									userProdChgMap.remove("userProdId");
									userProdChgMap.remove("modifyType");
									userProdChgMap.remove("createTime");
									resultListBak.add(userProdChgMap);
								}
							}
						}
					}
					result.put("result", resultListBak);
				}
				
//				url = assetAlloctionUrl + "/api/asset-allocation/products";
				Map<String, Object> prodcutResult = new HashMap<String, Object>();
				result.put("name", "贝贝鱼调仓记录详情");
				if(!StringUtils.isEmpty(groupId)){
					String subGroupId = groupId + "0048";
					url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/" + oemid;
					Map productMap = restTemplate.getForEntity(url, Map.class).getBody();
					if(productMap!=null){
						if(productMap.get("name")!=null){
							result.put("name", productMap.get("name"));
						}
					}
				}
				
			}
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "调仓记录成功", result);
	}
	
	@ApiOperation("调仓记录-详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "Long", required = false, value = "groupId", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "modifySeq", dataType = "Integer", required = false, value = "modifySeq", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "oemid", dataType = "String", required = false, value = "oemid", defaultValue = "1")
	})
	@RequestMapping(value = "/warehouse-record-details", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getwarehouseRecordDetails(Long groupId, Integer modifySeq, @RequestParam(required=false,defaultValue="1") String oemid) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> topResult = new HashMap<String, Object>();
		List<Map<String, Object>> topList = new ArrayList();
		try {
			String url = financeUrl + "/api/ssf-finance/product-groups/warehouse-record-details?prodId=" + groupId
					+ "&modifySeq=" + modifySeq;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if (result == null) {
				logger.error("调仓记录详情为空");
			} else {
				if (result != null && result.size() > 0) {
					List<Map<String, Object>> resultList = new ArrayList<>();
					resultList = (List<Map<String, Object>>) result.get("result");
					Collections.sort(resultList, new Comparator<Map<String, Object>>() {
						public int compare(Map<String, Object> o1, Map<String, Object> o2) {
							Float map1value = Float.parseFloat((o1.get("adjustAfter") + "").replaceAll("%", ""));
							Float map2value = Float.parseFloat((o2.get("adjustAfter") + "").replaceAll("%", ""));
							return map2value.compareTo(map1value);
						}
					});
					
					for (int i = 0; i < resultList.size(); i++) {
						topResult = new HashMap<String, Object>();
						Object obj = resultList.get(i);
						if (obj != null) {
							Map resultMap = (Map) obj;
							if (resultMap.get("adjustBefore") != null) {
								String value = resultMap.get("adjustBefore") + "";
								Float adjustBefore = Float.valueOf(value);
								String adjustBeforeStr = "";
								if (adjustBefore == 0) {
									adjustBeforeStr = "0.00%";
								} else {
									adjustBefore = adjustBefore / 100;
									adjustBeforeStr = decimalFormat.format(adjustBefore) + "%";
								}
								resultMap.put("adjustBefore", adjustBeforeStr);
							}
							if (resultMap.get("adjustAfter") != null) {
								String value = resultMap.get("adjustAfter") + "";
								Float adjustAfter = Float.valueOf(value);
								String adjustAfterStr = "";
								if (adjustAfter == 0) {
									adjustAfterStr = "0.00%";
								} else {
									adjustAfter = adjustAfter / 100;
									adjustAfterStr = decimalFormat.format(adjustAfter) + "%";
								}
								resultMap.put("adjustAfter", adjustAfterStr);
								
								topResult.put("adjustAfter", adjustAfterStr);
								topResult.put("fundTypeName", resultMap.get("fundTypeName"));
								topResult.put("fundType", resultMap.get("fundType"));
								topList.add(topResult);
							}
							if (resultMap.get("fundList") != null) {
								List fundList = (List) resultMap.get("fundList");
								if (fundList != null && fundList.size() > 0) {
									for (int j = 0; j < fundList.size(); j++) {
										Map fundMap = (Map) fundList.get(j);
										if (fundMap.get("percentBefore") != null) {
											String value = fundMap.get("percentBefore") + "";
											Float percentBefore = Float.valueOf(value);
											String percentBeforeStr = "";
											if (percentBefore == 0) {
												percentBeforeStr = "0.00%";
											} else {
												percentBefore = percentBefore / 100;
												percentBeforeStr = decimalFormat.format(percentBefore) + "%";
											}
											fundMap.put("percentBefore", percentBeforeStr);
										}
										if (fundMap.get("percentAfter") != null) {
											String value = fundMap.get("percentAfter") + "";
											Float percentAfter = Float.valueOf(value);
											String percentAfterStr = "";
											if (percentAfter == 0) {
												percentAfterStr = "0.00%";
											} else {
												percentAfter = percentAfter / 100;
												percentAfterStr = decimalFormat.format(percentAfter) + "%";
											}
											fundMap.put("percentAfter", percentAfterStr);
										}
									}
								}
							}

						}
					}
					result.put("detailData", result.get("result"));
					result.put("topData", topList);
					result.remove("result");
					
//					url = assetAlloctionUrl + "/api/asset-allocation/products";
					Map<String, Object> prodcutResult = new HashMap<String, Object>();
					result.put("name", "贝贝鱼调仓记录详情");
					if(!StringUtils.isEmpty(groupId)){
						String subGroupId = groupId + "0048";
						url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/" + oemid;
						Map productMap = restTemplate.getForEntity(url, Map.class).getBody();
						if(productMap!=null){
							if(productMap.get("name")!=null){
								result.put("name", productMap.get("name"));
							}
						}
					}
				}
			}
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			logger.error(str, e);
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "调仓记录成功", result);
	}
}
