package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.datamanager.service.OptimizationService;
import java.math.BigDecimal;
import java.util.ArrayList;
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
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.datamanager.commons.EasyKit;
import com.shellshellfish.datamanager.exception.ReturnedException;
import com.shellshellfish.datamanager.model.FinanceProductCompo;
import com.shellshellfish.datamanager.model.JsonResult;
import com.shellshellfish.datamanager.model.MongoFinanceAll;
import com.shellshellfish.datamanager.repositories.mongo.MongoFinanceALLRepository;

@Service
public class OptimizationServiceImpl implements OptimizationService {

	@Autowired
	MongoFinanceALLRepository mongoFinanceALLRepository;

	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;

	@Autowired
	private RestTemplate restTemplate;

	private Logger logger = LoggerFactory.getLogger(OptimizationServiceImpl.class);

	public JsonResult financeFront() {
		Map returnMap = new HashMap<>();
		// BANNER LIST
		List<String> bannerList = new ArrayList<>();
		bannerList.add("http://47.96.164.161/APP-invest-banner01.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner02.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner03.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner04.png");
		bannerList.add("http://47.96.164.161/APP-invest-banner05.png");
		returnMap.put("bannerList", bannerList);

		// 先获取全部产品
		String url = assetAlloctionUrl + "/api/asset-allocation/products";
		Map result = null;// 中间容器

		Object object = null;
		List<Map<String, Object>> prdList = null; // 中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();// 结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
		} catch (Exception e) {
			// 获取list失败直接返回
			/*
			 * result=new HashMap<>(); String message=e.getMessage();
			 */
			return new JsonResult(JsonResult.Fail, "获取理财产品调用restTemplate方法发生错误！", JsonResult.EMPTYRESULT);
		}
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		if (result != null) {
			object = result.get("_items");
			if (object instanceof List) {
				// 转换成List
				prdList = (List<Map<String, Object>>) object;
				try {
					for (Map<String, Object> productMap : prdList) {
						// 获取goupid和subGroupId
						String groupId = (productMap.get("groupId")) == null ? null
								: (productMap.get("groupId")).toString();
						String subGroupId = (productMap.get("subGroupId")) == null ? null
								: (productMap.get("subGroupId")).toString();
						String prdName = productMap.get("name") == null ? null : (productMap.get("name")).toString();
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

						// 去另一接口获取历史收益率图表的数据
						Map histYieldRate = getCombYieldRate(groupId, subGroupId);
						// 去另一个接口获取预期年化，预期最大回撤
						Map expAnnReturn = getExpAnnReturn(groupId, subGroupId);
						if (expAnnReturn.containsKey("value")) {
							expAnnReturn.put("value", expAnnReturn.get("value"));
						}
						// Map ExpMaxReturn=getExpMaxReturn(groupId,subGroupId);
						// 将结果封装进实体类
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
		JsonResult jsonResult = new JsonResult(JsonResult.SUCCESS, "获取成功", returnMap);
		if(jsonResult !=null ){
			MongoFinanceAll mongoFinanceAll = new MongoFinanceAll();
			Long utcTime = TradeUtil.getUTCTime();
			String dateTime = TradeUtil.getReadableDateTime(utcTime);
			String date = dateTime.split("T")[0].replaceAll("-", "");
			mongoFinanceAll.setDate(date);
			System.out.println("---\n" + date);
			mongoFinanceAll.setHead(jsonResult.getHead());
			mongoFinanceAll.setResult(jsonResult.getResult());
			System.out.println("---\n" + jsonResult.getResult().toString());
			mongoFinanceAll.setLastModifiedBy(utcTime + "");
			MongoFinanceAll mongoFinanceCount = mongoFinanceALLRepository.findAllByDate(date);
			if(mongoFinanceCount!=null){
				logger.info("已存在，删除后重新插入");
				mongoFinanceALLRepository.deleteAllByDate(date);
			}
			mongoFinanceALLRepository.save(mongoFinanceAll);
			System.out.println(date + "--数据插入成功，"+jsonResult.getResult().toString());
			logger.info("run com.shellshellfish.datamanager.service.impl.OptimizationServiceImpl.financeFront() success..");
		} else {
			logger.info("run com.shellshellfish.datamanager.service.impl.OptimizationServiceImpl.financeFront() fail..\n");
			logger.info("jsonResult 结果为空");
		}
		return jsonResult;
	}

	/**
	 * 根据groupid和subgroupid获取产品组合的组合收益率
	 */
	protected Map<String, Object> getCombYieldRate(String groupId, String subgroupId) {
		Map result = null;
		try {
			// 准备调用asset-allocation接口的方法，获取组合组合收益率(最大回撤)走势图-每天
			String url = assetAlloctionUrl
					+ "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-all?returnType=income";
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
		} catch (Exception e) {
			result = new HashMap<String, Object>();
			logger.error("exception:",e);
			logger.error(e.getMessage());
			String str = new ReturnedException(e).getErrorMsg();
			result.put("error", "restTemplate获取预期组合收益率走势图失败:" + str);
		}
		return result;
	}

	/**
	 * 获取预期年化收益（/api/asset-allocation/product-groups/{groupId}/sub-groups/{
	 * subGroupId}/opt，参数+1）
	 */
	protected Map<String, Object> getExpAnnReturn(String groupId, String subGroupId) {
		Map<String, Object> result = new HashMap<>();
		try {
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
					+ subGroupId + "/opt?returntype=" + "1";
			String str = "{\"returnType\":\"" + "1" + "\"}";
			result = (Map) restTemplate.postForEntity(url, getHttpEntity(str), Map.class).getBody();
			/*
			 * result.remove("_total"); result.remove("_name");
			 * result.remove("_links"); result.remove("_serviceId");
			 * result.remove("_schemaVersion");
			 */
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
	 * 通用方法处理post请求带requestbody
	 */
	protected HttpEntity<String> getHttpEntity(String JsonString) {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;UTF-8"));
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString, headers);
		return strEntity;
	}

	@Override
	public JsonResult getFinanceFront() {
		JsonResult result = new JsonResult();
		Long utcTime = TradeUtil.getUTCTime();
		String dateTime = TradeUtil.getReadableDateTime(utcTime);
		String date = dateTime.split("T")[0].replaceAll("-", "");
		MongoFinanceAll mongoFinanceAll = mongoFinanceALLRepository.findAllByDate(date);
		if(mongoFinanceAll!=null){
			result.setHead(mongoFinanceAll.getHead());
			result.setResult(mongoFinanceAll.getResult());
		} else {
			logger.error("com.shellshellfish.datamanager.service.impl.OptimizationServiceImpl.getFinanceFront() 数据获取为空");
		}
		return result;
	}
}

