package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.utils.SSFDateUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.datamanager.commons.EasyKit;
import com.shellshellfish.datamanager.exception.ReturnedException;
import com.shellshellfish.datamanager.model.FinanceProductCompo;
import com.shellshellfish.datamanager.model.FundNAVInfo;
import com.shellshellfish.datamanager.model.JsonResult;
import com.shellshellfish.datamanager.model.MonetaryFund;
import com.shellshellfish.datamanager.model.MongoFinanceAll;
import com.shellshellfish.datamanager.model.MongoFinanceDetail;
import com.shellshellfish.datamanager.repositories.MongoFinanceDetailRepository;
import com.shellshellfish.datamanager.repositories.mongo.MongoFinanceALLRepository;
import com.shellshellfish.datamanager.service.OptimizationService;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
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


@Service
public class OptimizationServiceImpl implements OptimizationService {

	@Autowired
	MongoFinanceALLRepository mongoFinanceALLRepository;
	@Autowired
	MongoFinanceDetailRepository mongoFinanceDetailRepository;

	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;
	
	@Value("${shellshellfish.userinfo-url}")
	private String userInfoUrl;

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
		if(returnMap !=null && !returnMap.isEmpty() ){
			MongoFinanceAll mongoFinanceAll = new MongoFinanceAll();
			Long utcTime = TradeUtil.getUTCTime();
			String dateTime = TradeUtil.getReadableDateTime(utcTime);
			String date = dateTime.split("T")[0].replaceAll("-", "");
			mongoFinanceAll.setDate(date);
			System.out.println("---\n" + date);
//			mongoFinanceAll.setHead(jsonResult.getHead());
//			mongoFinanceAll.setResult(jsonResult.getResult());
			mongoFinanceAll.setResult(returnMap);
			mongoFinanceAll.setLastModifiedBy(utcTime + "");
			mongoFinanceALLRepository.deleteAll();
//			MongoFinanceAll mongoFinanceCount = mongoFinanceALLRepository.findAllByDate(date);
//			if(mongoFinanceCount!=null){
//				logger.info("已存在，删除后重新插入");
//				mongoFinanceALLRepository.deleteAllByDate(date);
////				mongoFinanceALLRepository.deleteAll();
//			}
//			mongoFinanceALLRepository.deleteAll();
			
			mongoFinanceALLRepository.save(mongoFinanceAll);

			System.out.println(date + "--数据插入成功");
			logger.info("run com.shellshellfish.datamanager.service.OptimizationServiceImpl.financeFront() success..");

		} else {
			logger.error("run com.shellshellfish.datamanager.service.impl.OptimizationServiceImpl.financeFront() fail..\n");
			logger.error("jsonResult 结果为空");
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
//			logger.error("exception:",e);
//			logger.error(e.getMessage());
			String str = new ReturnedException(e).getErrorMsg();
			logger.error("restTemplate获取预期组合收益率走势图失败:{}", str, e);
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
		JsonResult jsonResult = null;
		Long utcTime = TradeUtil.getUTCTime();
		String dateTime = TradeUtil.getReadableDateTime(utcTime);
		String date = dateTime.split("T")[0].replaceAll("-", "");
		MongoFinanceAll mongoFinanceAll = mongoFinanceALLRepository.findAllByDate(date);
//		List<MongoFinanceAll> mongoFinanceCountList = mongoFinanceALLRepository.findAll();
		if (mongoFinanceAll != null) {
			jsonResult = new JsonResult(JsonResult.SUCCESS, "获取成功", mongoFinanceAll.getResult());
		} else {
			logger.error("com.shellshellfish.datamanager.service.impl.OptimizationServiceImpl.getFinanceFront() 数据获取为空");
		}
		return jsonResult;
	}

	@Override
	public JsonResult checkPrdDetails(String groupId, String subGroupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		result = this.getPrdNPVList(groupId, subGroupId);
		if (result == null) {
			return new JsonResult(JsonResult.Fail, "获取净值增长值活净值增长率为空", JsonResult.EMPTYRESULT);
		}

		Map expAnnReturn = getExpAnnReturn(groupId, subGroupId);
		Map expMaxReturn = getExpMaxReturn(groupId, subGroupId);
		Map simulateHistoricalReturn = getSimulateHistoricalReturn(groupId, subGroupId);
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
		
		JsonResult jsonResult = new JsonResult(JsonResult.SUCCESS, "查看理财产品详情成功", result);
		if(result !=null && !result.isEmpty()){
			MongoFinanceDetail mongoFinanceDetail = new MongoFinanceDetail();
			Long utcTime = TradeUtil.getUTCTime();
			String dateTime = TradeUtil.getReadableDateTime(utcTime);
			String date = dateTime.split("T")[0].replaceAll("-", "");
			mongoFinanceDetail.setDate(date);
			mongoFinanceDetail.setGroupId(groupId);
			mongoFinanceDetail.setSubGroupId(subGroupId);
			System.out.println("---\n" + date);
//			mongoFinanceDetail.setHead(jsonResult.getHead());
			mongoFinanceDetail.setResult(result);
			System.out.println("---\n" + jsonResult.getResult().toString());
			mongoFinanceDetail.setLastModifiedBy(utcTime + "");
//			mongoFinanceDetailRepository.deleteAll();
//			MongoFinanceDetail mongoFinanceCount = mongoFinanceDetailRepository.findAllByDateAndGroupIdAndSubGroupId(date, groupId, subGroupId);
//			if (mongoFinanceCount != null) {
//				logger.info("已存在，删除后重新插入");
//				mongoFinanceDetailRepository.deleteAllByDate(date);
////				mongoFinanceDetailRepository.deleteAll();
//			}
//			mongoFinanceDetailRepository.deleteAll();
			
			mongoFinanceDetailRepository.save(mongoFinanceDetail);
			logger.info(date + "--数据插入成功，groupId:{},subGroupId:{}",groupId,subGroupId);
			logger.info("run com.shellshellfish.datamanager.service.OptimizationServiceImpl.checkPrdDetails() success..");
		} else {
			logger.error("run com.shellshellfish.datamanager.service.OptimizationServiceImpl.checkPrdDetails() fail..\n");
			logger.error("jsonResult 结果为空");
		}
		return jsonResult;
	}
	
	/**
	 * 获取预期最大回撤（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+2）
	 */
	protected Map<String, Object> getExpMaxReturn(String groupId, String subGroupId) {
		Map result = null;
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/opt?returntype=" + "2";
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
	 *模拟历史年化波动率（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+3）
	 */
	protected Map<String, Object> getSimulateHistoricalReturn(String groupId, String subGroupId) {
		Map<String, Object> result = new HashMap<>();
		try {
			String url =
					assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
							+ subGroupId + "/opt?returntype=" + "3";
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
	
	public Map<String, Object> getPrdNPVList(String groupId, String subGroupId) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<FundNAVInfo> resultList = new ArrayList<>();
		//获取所有产品净值增长值的list
		List<FundNAVInfo> listA = getNPVIncrement(groupId, subGroupId);
		//获取所有产品净值增长率的list
		List<FundNAVInfo> listB = getNPVIncrementRate(groupId, subGroupId);
		//遍历每一个对象进行对比
		if (listA == null || listB == null) {
			logger.error("获取净值增长值活净值增长率为null");
			return null;
		}
		for (FundNAVInfo infoA : listA) {
			for (FundNAVInfo infoB : listB) {
				//对象进行比较
				FundNAVInfo info = FundNAVInfo.mergeIntoOne(infoA, infoB);
				if (info != null) {
					getGrowthOfMonetaryFunds(info);
					
					List<Map> yieldof7days = info.getYieldof7days();
					Map<String,Object> maxMinValue = new HashMap();
					if(yieldof7days!=null&&yieldof7days.size()>0){
						List<Double> maxMinValueList = new ArrayList<Double>();
						for (int i = 0; i < yieldof7days.size(); i++) {
							Map yieldof7daysMap = yieldof7days.get(i);
							if(yieldof7daysMap.get("value")!=null){
								maxMinValueList.add(Double.parseDouble(yieldof7daysMap.get("value")+""));
							}
						}
						maxMinValue.put("maxValue", Collections.max(maxMinValueList));
						maxMinValue.put("minValue", Collections.min(maxMinValueList));
					}
					info.setYieldof7daysMap(maxMinValue);
					
					List<Map> tenKiloUnitYieldList = info.getTenKiloUnitYield();
					maxMinValue = new HashMap();
					if(tenKiloUnitYieldList!=null&&tenKiloUnitYieldList.size()>0){
						List<Double> maxMinValueList = new ArrayList<Double>();
						for (int i = 0; i < tenKiloUnitYieldList.size(); i++) {
							Map tenKiloUnitYieldMap = tenKiloUnitYieldList.get(i);
							if(tenKiloUnitYieldMap.get("value")!=null){
								maxMinValueList.add(Double.parseDouble(tenKiloUnitYieldMap.get("value")+""));
							}
						}
						maxMinValue.put("maxValue", Collections.max(maxMinValueList));
						maxMinValue.put("minValue", Collections.min(maxMinValueList));
					}
					info.setTenKiloUnitYieldMap(maxMinValue);
					resultList.add(info);
				}
			}
		}
		resultMap.put("list", resultList);
		resultMap.put("total", resultList.size());
		return resultMap;
	}
	
	/**
	 * 获取全部产品的NPV增值
	 *
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	private List<FundNAVInfo> getNPVIncrement(String groupId, String subGroupId) {
		FundNAVInfo info = new FundNAVInfo();
		Map result = new HashMap<>();
		List<FundNAVInfo> resultList = new ArrayList<FundNAVInfo>();
		try {
			//调用组合各种类型净值收益，参数为1，获取净值走势
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/fund-navadj?returnType=1&id=" + groupId + "&subGroupId=" + subGroupId;
			result = restTemplate.getForEntity(url, Map.class).getBody();
		} catch (Exception e) {
			logger.error("调用restTemplate查询净值增长数据获取失败", e.getMessage());
			return null;
		}
		//判断非空
		if (result.size() == 0) {
			logger.error("查询净值增长数据为空值", "数据获取失败");
			return null;
		}
		//判断结果是否有数据
		if ((int) result.get("_total") == 0) {
			logger.error("查询净值增长数据结果为0", "数据获取失败");
			return null;
		}
		//转成list
		List prdList = null;
		try {
			prdList = (List) result.get("_items");
		} catch (Exception e) {
			logger.error("解析转换_items为List失败", e.getMessage());
			return null;
		}
		int count = 0;
		Double total = 0D;
		for (Object prd : prdList) {
			count++;
			//创建对象
			Map mapItem = null;
			if (!(prd instanceof Map)) {
				logger.error("_item List转换为Map失败", "数据获取失败");
				return null;
			}
			mapItem = (Map) prd;
			FundNAVInfo infoA = mapToFundNAVInfo(mapItem, "1");//增长值

			resultList.add(infoA);
		}
		return resultList;
	}
	
	/**
	 * 获取全部产品的NPV增长率
	 *
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	private List<FundNAVInfo> getNPVIncrementRate(String groupId, String subGroupId) {
		FundNAVInfo info = new FundNAVInfo();
		Map result = new HashMap<>();
		List<FundNAVInfo> resultList = new ArrayList<FundNAVInfo>();
		try {
			//调用组合各种类型净值收益，参数为1，获取净值走势
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/fund-navadj?returnType=2&id=" + groupId + "&subGroupId=" + subGroupId;
			result = restTemplate.getForEntity(url, Map.class).getBody();
		} catch (Exception e) {
			logger.error("调用restTemplate查询净值增长数据获取失败", e.getMessage());
			return null;
		}
		//判断非空
		if (result.size() == 0) {
			logger.error("查询净值增长数据为空值", "数据获取失败");
			return null;
		}
		//判断结果是否有数据
		if ((int) result.get("_total") == 0) {
			logger.error("查询净值增长数据结果为0", "数据获取失败");
			return null;
		}
		//转成list
		List prdList = null;
		try {
			prdList = (List) result.get("_items");
		} catch (Exception e) {
			logger.error("解析转换_items为List失败", e.getMessage());
			return null;
		}
		int count = 0;
		Double total = 0D;
		for (Object prd : prdList) {
			count++;
			//创建对象
			Map mapItem = null;
			if (!(prd instanceof Map)) {
				logger.error("_item List转换为Map失败", "数据获取失败");
				return null;
			}
			mapItem = (Map) prd;
			FundNAVInfo infoA = mapToFundNAVInfo(mapItem, "2");
			if (count == prdList.size()) {
				Double last = (new Double(100)) - total;
				last = (new BigDecimal("100")).subtract(new BigDecimal(total)).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
				infoA.setAvgIncreRate(last + EasyKit.PERCENT);
			} else {
				String rate = infoA.getAvgIncreRate();
				if (!StringUtils.isEmpty(rate)) {
					total = total + Double.parseDouble(rate.replace(EasyKit.PERCENT, ""));
				}
			}
			resultList.add(infoA);
		}
		return resultList;
	}
	
	/**
	 * 将Map丢进去，转换成FundNAVInfo实体
	 *
	 * @param map
	 * @return
	 */
	private FundNAVInfo mapToFundNAVInfo(Map map, String flag) {
		String fundCode = null;
		String name = null;
		String fundType = null;
		String avgIncreRate = null;
		if (map == null) {
			logger.info("mapToFundNAVInfo:map集合为空");
			return null;
		}
		FundNAVInfo info = new FundNAVInfo();
		try {
			fundCode = map.get("fund_code").toString(); //获取产品代码code
			name = map.get("name").toString();//产品名称
			avgIncreRate = map.get("type_value").toString();
			if (!StringUtils.isEmpty(avgIncreRate)) {
				avgIncreRate = EasyKit.getDecimal(new BigDecimal(avgIncreRate)) + "";
			}
			fundType = map.get("fund_type_two").toString();//基金类型
		} catch (Exception e) {
			logger.error("获取map中的参数出错," + e.getMessage());
		}
		List npvIncrement = null;
		try {
			npvIncrement = (List) map.get("navadj");//净值增长值

			if (npvIncrement != null && npvIncrement.size() > 0) {
				for (int i = 0; i < npvIncrement.size(); i++) {
					Map<String, Object> obj = (Map<String, Object>) npvIncrement.get(i);
					if (obj.get("value") != null) {
						BigDecimal value = new BigDecimal(0);
						if ("2".equals(flag)) {
							Double decimal = EasyKit.getDecimal(new BigDecimal(obj.get("value") + ""));
							obj.put("value", decimal);
						} else {
							value = new BigDecimal(obj.get("value") + "");
							obj.put("value", value.setScale(4, BigDecimal.ROUND_HALF_UP).doubleValue());
						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("净值增长值或净值增长率转换为List时出错");
		}
		info.setFundCode(fundCode);
		info.setName(name);
		info.setFundType(fundType);
		info.setAvgIncreRate(avgIncreRate + EasyKit.PERCENT);
		if ("1".equals(flag)) {//净值增长值
			info.setNPVIncrement(npvIncrement);
			info.setIncrementMinMaxValueMap(npvIncrement);
		}
		if ("2".equals(flag)) {//净值增长率
			info.setNPVIncreRate(npvIncrement);
			info.setIncrementRateMinMaxValueMap(npvIncrement);
		}
		return info;
	}
	
	/**
	 * @param info
	 */
	private void getGrowthOfMonetaryFunds(FundNAVInfo info) {

		Long endDate = new Date().getTime();
		Long startDate = SSFDateUtils.getLongTimeOfThreeYearsBefore();


		String fundCode = info.getFundCode();

		if (!MonetaryFundEnum.containsCode(info.getFundCode())) {
			info.setIsMonetaryFund(0);
			return;
		}

		info.setIsMonetaryFund(1);

		String methodUrl = "/api/userinfo/getGrowthRateOfMonetaryFundsList";

		Map<String, String> params = new HashMap(3);
		params.put("code", fundCode);
		params.put("startDate", String.valueOf(startDate));
		params.put("endDate", String.valueOf(endDate));

		MonetaryFund[] originResult = restTemplate.postForObject(URLutils.prepareParameters(userInfoUrl + methodUrl, params), null, MonetaryFund[].class);

		if (originResult == null || originResult.length <= 0) {
			logger.error("没有查找到基金‘{code:{},name:{}}’的七日年化收益和万份收益", fundCode, info.getName());
			return;
		}

		List<MonetaryFund> result = Arrays.asList(originResult);
		info.setYieldof7days(result);
		info.setTenKiloUnitYield(result);


	}

	@Override
	public JsonResult getPrdDetails(String groupId, String subGroupId) {
		JsonResult jsonResult = null;
		Long utcTime = TradeUtil.getUTCTime();
		String dateTime = TradeUtil.getReadableDateTime(utcTime);
		String date = dateTime.split("T")[0].replaceAll("-", "");
		MongoFinanceDetail mongoFinanceDetail = mongoFinanceDetailRepository.findAllByDateAndGroupIdAndSubGroupId(date, groupId, subGroupId);
//		MongoFinanceDetail mongoFinanceDetail = mongoFinanceDetailRepository.findAllByGroupIdAndSubGroupId(groupId, subGroupId);
		if(mongoFinanceDetail!=null){
			jsonResult = new JsonResult(JsonResult.SUCCESS, "查看理财产品详情成功", mongoFinanceDetail.getResult());
			logger.info("获取信息成功");
		} else {
			logger.error("com.shellshellfish.datamanager.service.OptimizationServiceImpl.getPrdDetails() 数据获取为空");
		}
		return jsonResult;
	}
}

