package com.shellshellfish.aaas.transfer.controller;

import java.math.BigDecimal;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.dto.FinanceProductCompo;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
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
	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;

	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	@Value("${shellshellfish.finance-url}")
	private String financeUrl;

	@Value("${shellshellfish.asset-alloction-url}")
	private String assetAlloctionUrl;

	@Autowired
	private RestTemplate restTemplate;
   
	@Autowired
	private MidApiService service;
	
	@ApiOperation("1.首页")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID"),
			@ApiImplicitParam(paramType = "query", name = "isTestFlag", dataType = "String", required = false, value = "是否测评（1-已做 0-未做）"),
			@ApiImplicitParam(paramType = "query", name = "testResult", dataType = "String", required = false, value = "测评结果",defaultValue="平衡型")
			})
	@RequestMapping(value = "/finance-home", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult financeHome(
			@RequestParam(required=false) String uuid,
			@RequestParam(required=false) String isTestFlag,
			@RequestParam(required=false) String testResult) {
		Map<String, Object> result = new HashMap<String, Object>();
		logger.info("mid financeHome method run..");
		try {
			if("1".equals(isTestFlag)){
				if(StringUtils.isEmpty(uuid)){
					return new JsonResult(JsonResult.Fail, "用户ID必须输入",JsonResult.EMPTYRESULT );
				} else if(StringUtils.isEmpty(testResult)){
					return new JsonResult(JsonResult.Fail, "测评结果必须输入", JsonResult.EMPTYRESULT);
				}
			}
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			uuid = uuid == null ? "" : uuid;
			requestEntity.add("uuid", uuid);
			isTestFlag = isTestFlag == null ? "" : isTestFlag;
			testResult = testResult == null ? "" : testResult;
			result = restTemplate.getForEntity(financeUrl + "/api/ssf-finance/product-groups/homepage?uuid=" + uuid
					+ "&isTestFlag=" + isTestFlag + "&testResult=" + testResult, Map.class).getBody();
			if (result == null || result.size() == 0) {
				/*result.put("msg", "获取失败");*/
				return new JsonResult(JsonResult.SUCCESS, "没有获取到产品", JsonResult.EMPTYRESULT);
			}
//			requestEntity.add("productType", productType);
			/*result.put("msg", "获取成功");*/
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail,str,JsonResult.EMPTYRESULT);
		}
	}


	@ApiOperation("6.进入理财页面后的数据")
    @RequestMapping(value="/financeFrontPage",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult financeModule(){
		Map returnMap=new HashMap<>();
		//BANNER LIST
		List<String> bannerList=new ArrayList<>();
		bannerList.add("http://47.96.164.161/1.png");
		bannerList.add("http://47.96.164.161/2.png");
		bannerList.add("http://47.96.164.161/3.png");
		bannerList.add("http://47.96.164.161/4.png");
		
		returnMap.put("bannerList", bannerList);
		
		//先获取全部产品
		String url=assetAlloctionUrl+"/api/asset-allocation/products";
		Map result=null;//中间容器
		
		Object object=null;
		List<Map<String,Object>> prdList=null; //中间容器
		List<FinanceProductCompo> resultList=new ArrayList<FinanceProductCompo>();//结果集
			try{
			result=restTemplate.getForEntity(url, Map.class).getBody();
			}catch(Exception e){
				//获取list失败直接返回
				/*result=new HashMap<>();
				String message=e.getMessage();*/
				return new JsonResult(JsonResult.Fail, "获取理财产品调用restTemplate方法发生错误！",JsonResult.EMPTYRESULT);
			}
			//如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
			if(result!=null){		
				object=result.get("_items");
			   if (object instanceof List){
				   //转换成List
				   prdList=(List<Map<String,Object>>)object;
				try{
				   for (Map<String,Object> productMap:prdList){
					   //获取goupid和subGroupId
					  String groupId= (productMap.get("groupId"))==null?null:(productMap.get("groupId")).toString();
					  String subGroupId= (productMap.get("subGroupId"))==null?null:(productMap.get("subGroupId")).toString();
					  String prdName=productMap.get("name")==null?null:(productMap.get("name")).toString();
				      List productCompo=(List) productMap.get("assetsRatios");
					   //去另一接口获取历史收益率图表的数据
					  Map histYieldRate = getCombYieldRate(groupId,subGroupId);
					  //去另一个接口获取预期年化，预期最大回撤
					  Map ExpAnnReturn= getExpAnnReturn(groupId,subGroupId);
					  Map ExpMaxReturn=getExpMaxReturn(groupId,subGroupId);
					  //将结果封装进实体类
					  FinanceProductCompo prd=new FinanceProductCompo(groupId, subGroupId, prdName, ExpMaxReturn.size()>0?ExpAnnReturn.get("value").toString():null, ExpMaxReturn.size()>0?ExpAnnReturn.get("value").toString():null, productCompo, histYieldRate);
					  resultList.add(prd);			  				                                             
					  }
				   }catch (Exception e){
					   return new JsonResult(JsonResult.Fail, "获取产品的field属性失败", JsonResult.EMPTYRESULT);
				   }
			                             }
			                  }else{
			return new JsonResult(JsonResult.Fail, "没有获取到产品", JsonResult.EMPTYRESULT);
			                  }	
			returnMap.put("data",resultList);
			return new JsonResult(JsonResult.SUCCESS, "获取成功", returnMap);
	}

	
	@ApiOperation("理财产品查看详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "4"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "4009"),
	})
	@RequestMapping(value="/checkPrdDetails",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getPrdDetails(String groupId,String subGroupId){
		Map<String,Object> result=new HashMap<String,Object>();
		try{
			result=service.getPrdNPVList(groupId, subGroupId);
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		Map ExpAnnReturn= getExpAnnReturn(groupId,subGroupId);
		Map ExpMaxReturn=getExpMaxReturn(groupId,subGroupId);
		result.put("expAnnReturn", ExpAnnReturn);
		result.put("expMaxDrawDown", ExpMaxReturn);
		//饼图（返回单个基金组合产品信息）
		try{
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId;
			Map productMap = restTemplate.getForEntity(url,Map.class).getBody();
			if(productMap==null){
				logger.info("单个基金组合产品信息为空");
			} else {
				if(productMap.get("assetsRatios")!=null){
					List<Map> assetList = (List<Map>) productMap.get("assetsRatios");
					Double count = 0D;
					Double value = 0D;
					for(int i=0;i<assetList.size();i++) {
						Map<String, Object> assetMap = assetList.get(i);
						if(assetMap.get("value")!=null){
							if(i == assetList.size()-1){
								value = 100D - count; 
							}else {
								value = (Double) assetMap.get("value");
								value = EasyKit.getDecimal(new BigDecimal(value));
								count = count + value;
							}
							assetMap.put("value", value);
						}
					}
					result.put("assetsRatios", productMap.get("assetsRatios"));
				}
			}
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "查看理财产品详情成功", result);
	}

	@ApiOperation("历史业绩")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "4"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "4009"),
		@ApiImplicitParam(paramType = "query", name = "productName", dataType = "String", required = false, value = "productName", defaultValue = "贝贝鱼1号“御•安守”组合")
	})
	@RequestMapping(value = "/historicalPerformancePage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getHistoricalPerformance(@RequestParam(required=false) String groupId, @RequestParam(required=false) String subGroupId,@RequestParam(required=false) String productName) {
		// 先获取全部产品
		String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/historicalPer-formance?fund_group_id=" + groupId
				+ "&subGroupId=" + subGroupId;
		Map<String,Object> result = new HashMap<String,Object>();// 中间容器
		Map<String,Object> title = new HashMap<String,Object>();
		Object object = null;
		List<Map<String, Object>> prdList = null; // 中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();// 结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
			logger.info("历史业绩-第一部分数据获取成功");
			if (result != null) {
				object = result.get("_items");
				if (object != null) {
					title.put("header1",productName);
					logger.info("object获取成功");
					result.put("historicalPerformance",object);
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
			logger.error("获取理财产品调用restTemplate方法发生错误",e);
			String message = e.getMessage();
			result.put("错误原因", message + ",获取理财产品调用restTemplate方法发生错误！");
			return new JsonResult(JsonResult.Fail, "获取理财List数据失败", JsonResult.EMPTYRESULT);
		}
		
		//收益率走势图
		//http://localhost:10020/api/asset-allocation/product-groups/6/sub-groups/111111/portfolio-yield-week?returnType=income
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId +"/sub-groups/"+subGroupId+"/portfolio-yield-week?returnType=income";
		Map<String,Object> incomeResult = new HashMap<String,Object>();
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
				result.put("portfolioYield",obj);
			} else {
				logger.info("object获取失败");
			}
			if (maxMinMap != null) {
				logger.info("maxMinIncomeMap获取成功");
				result.put("maxMinMap",maxMinMap);
			} else {
				logger.info("maxMinMap获取失败");
			}
			if (maxMinBenchmarkMap != null) {
				logger.info("maxMinBenchmarkMap获取成功");
				result.put("maxMinIncomeBenchmarkMap",maxMinBenchmarkMap);
			} else {
				logger.info("Income获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", resultList);
		}
		//最大回撤走势图
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId +"/sub-groups/"+subGroupId+"/portfolio-yield-week?returnType=1";
		Map<String,Object> incomeResult1 = new HashMap<String,Object>();
		incomeResult1 = restTemplate.getForEntity(url, Map.class).getBody();
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		Object obj1 = null;
		Object maxMinMap2 = null;
		Object maxMinBenchmarkMap2 = null;
		if (incomeResult1 != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj1 = incomeResult1.get("_items");
			maxMinMap2 = incomeResult1.get("maxMinMap");
			maxMinBenchmarkMap2 = incomeResult.get("maxMinBenchmarkMap");
			if (obj1 != null) {
				logger.info("obj1获取成功");
				result.put("maxRetreat",obj1);
			} else {
				logger.info("object获取失败");
			}
			if (maxMinMap2 != null) {
				logger.info("maxMinIncomeMap获取成功");
				result.put("maxMinRetreatMap",maxMinMap2);
			} else {
				logger.info("maxMinMap2获取失败");
			}
			if (maxMinBenchmarkMap2 != null) {
				logger.info("maxMinBenchmarkMap获取成功");
				result.put("maxMinRetreatBenchmarkMap",maxMinBenchmarkMap2);
			} else {
				logger.info("Income获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", result);
		}
		result.put("title", title);
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}
	
	@ApiOperation("未来预期page")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "4"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "4009"),
	})
	@RequestMapping(value = "/futureExpectationPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFutureExpectation(@RequestParam(required=false) String groupId, @RequestParam String subGroupId) {
		Map<String,Object> result = new HashMap<String,Object>();
		// 预期平均年化收益率
		Map<String,Object> optMap = new HashMap<String,Object>();
		String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returnType=1";
		optMap= restTemplate.postForEntity(url,null,Map.class).getBody();
		if (optMap != null && !optMap.isEmpty()) {
			logger.info("预期平均年化收益率获取成功");
			Object obj = optMap.get("value");
			String value = "";
			if (obj != null) {
				logger.info("预期平均年化收益率获取成功2");
				value = obj.toString();
				result.put("averageAnnualRate", value);
			} else {
				logger.error("预期平均年化收益率获取失败");
			}
		} else {
			logger.error("预期平均年化收益率获取失败2");
		}
		
		//未来收益走势图
		Object object = null;
		List<Map<String, Object>> prdList = new ArrayList<Map<String, Object>>(); // 中间容器
		url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/expected-income";
		try {
			Map<String,Object> expectedIncomeMap = new HashMap<String,Object>();
			expectedIncomeMap = restTemplate.getForEntity(url, Map.class).getBody();
			if(expectedIncomeMap!=null&&!expectedIncomeMap.isEmpty()){
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
				if (expectedIncomeSizeMap != null) {
					logger.info("expectedIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("expectedIncomeSizeMap", expectedIncomeSizeMap);
				} else {
					logger.error("expectedIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (highPercentMaxIncomeSizeMap != null) {
					logger.info("highPercentMaxIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("highPercentMaxIncomeSizeMap", highPercentMaxIncomeSizeMap);
				} else {
					logger.error("highPercentMaxIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (highPercentMinIncomeSizeMap != null) {
					logger.info("highPercentMinIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("highPercentMinIncomeSizeMap", highPercentMinIncomeSizeMap);
				} else {
					logger.error("highPercentMinIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (lowPercentMaxIncomeSizeMap != null) {
					logger.info("lowPercentMaxIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("lowPercentMaxIncomeSizeMap", lowPercentMaxIncomeSizeMap);
				} else {
					logger.error("lowPercentMaxIncomeSizeMap:未来收益走势图数据获取失败");
				}
				if (lowPercentMinIncomeSizeMap != null) {
					logger.info("lowPercentMinIncomeSizeMap:未来收益走势图数据获取成功");
					//prdList = (List<Map<String, Object>>) obj2;
					result.put("lowPercentMinIncomeSizeMap", lowPercentMinIncomeSizeMap);
				} else {
					logger.error("lowPercentMinIncomeSizeMap:未来收益走势图数据获取失败");
				}
			} else {
				logger.error("未来收益走势图数据获取失败2");
			}
		} catch (Exception e) {
			// 获取list失败直接返回
			logger.error("未来收益走势图数据发生错误",e);
			/*String message = e.getMessage();
			result.put("错误原因", message + ",未来收益走势图数据发生错误！"); */
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, "未来收益走势图数据失败", JsonResult.EMPTYRESULT);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}
	
	@ApiOperation("风险控制")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = true, value = "用户ID", defaultValue = "1"),
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "4"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "subGroupId", defaultValue = "4009"),
		@ApiImplicitParam(paramType = "query", name = "productName", dataType = "String", required = false, value = "productName", defaultValue = "贝贝鱼1号“御•安守”组合")
	})
	@RequestMapping(value = "/riskMangementPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getRiskManagement(@RequestParam String uuid, @RequestParam String groupId, @RequestParam String subGroupId, @RequestParam(required=false) String productName) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String,Object> title = new HashMap<String,Object>();
		// 最大回撤走势图
		Map<String, Object> portfolioYieldWeekMap = new HashMap<String, Object>();
		try {
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/"
					+ subGroupId + "/portfolio-yield-week?returnType=1";
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
					result.put("portfolioYieldWeek", obj);
				} else {
					logger.error("最大回撤走势图获取失败");
				}
				if (maxMinMap != null) {
					logger.info("maxMinMap:最大回撤走势图获取成功");
					// value = obj.toString();
					result.put("maxMinMap", maxMinMap);
				} else {
					logger.error("maxMinMap:最大回撤走势图获取失败");
				}
				if (maxMinBenchmarkMap != null) {
					logger.info("maxMinBenchmarkMap:最大回撤走势图获取成功");
					// value = obj.toString();
					result.put("maxMinBenchmarkMap", maxMinBenchmarkMap);
				} else {
					logger.error("maxMinBenchmarkMap:最大回撤走势图获取失败");
				}
			} else {
				logger.error("预期平均年化收益率获取失败2");
			}
			title.put("header1",productName);
			//预期最大回撤数
			url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returntype=2";
			Map<String, Object> optResult = (Map) restTemplate.postForEntity(url,null,Map.class).getBody();
			if(optResult!=null){
				if(optResult.get("value")!=null){
					double opt = Double.valueOf(optResult.get("value")+"");
					if(opt<=0){
						opt = opt - 0.05;
						logger.info("预期最大回撤数获取成功");
					} else {
						logger.error("预期最大回撤数获取失败，应为负数，而不是为："+opt);
					}
					result.put("optValue", opt);
				}
			}
			
			// 等级风险
			url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId
					+ "/risk-controls";
			Map<String, Object> riskMap = new HashMap<String, Object>();
			riskMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (riskMap != null && !riskMap.isEmpty()) {
				logger.info("等级风险数据获取成功");
				Object obj2 = riskMap.get("_items");
				if (obj2 != null) {
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
					for(int i=0;i<prdList.size();i++){
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
	})
	@RequestMapping(value = "/globalConfigurationPage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getGlobalConfiguration(@RequestParam(required=false) String uuid,@RequestParam String groupId, @RequestParam String subGroupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		// 配置收益贡献
		Map<String, Object> configurationBenefitContributionMap = new HashMap<String, Object>();
		try {
			String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId + "/contributions";
			configurationBenefitContributionMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (configurationBenefitContributionMap != null && !configurationBenefitContributionMap.isEmpty()) {
				logger.info("配置收益贡献获取成功");
				Object obj = configurationBenefitContributionMap.get("_items");
				Object category = configurationBenefitContributionMap.get("_total");
				List<Map<String, Object>> value = new ArrayList<Map<String, Object>>();
				if (obj != null && category != null) {
					logger.info("配置收益贡献获取成功2");
					value = (List<Map<String, Object>>) obj;
					Map<String,Object> configMap = value.get(0);
					Object configTime = null;
					if(configMap!=null&&configMap.size()>0){
						logger.info("配置收益贡献时间获取成功");
						configTime = configMap.get("time");
						if(configTime==null){
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
					logger.error("配置收益贡献获取失败"+category);
				}
			} else {
				logger.error("配置收益贡献获取失败2");
			}
			
			// 等级风险
			url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId + "/sub-groups/" + subGroupId
					+ "/risk-controls";
			Map<String, Object> riskMap = new HashMap<String, Object>();
			riskMap = restTemplate.getForEntity(url, Map.class).getBody();
			if (riskMap != null && !riskMap.isEmpty()) {
				logger.info("等级风险数据获取成功");
				Object obj2 = riskMap.get("_items");
				if (obj2 != null) {
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
					for(int i=0;i<prdList.size();i++){
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
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "4009")
	})
	@RequestMapping(value="/getExpAnnualAndMaxReturn",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult getExpAnnualAndMaxReturn(String groupId,String subGroupId){
		return new JsonResult(JsonResult.SUCCESS,"请求成功",service.getExpAnnualAndMaxReturn(groupId, subGroupId));
	}
	
	
	

	/**
	 * 获取预期最大回撤（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+2）
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	protected Map<String,Object> getExpMaxReturn(String groupId,String subGroupId){
		Map result=null;
		try{
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returntype="+"2";
//			String str="{\"returnType\":\""+"2"+"\"}";
			result=(Map) restTemplate.postForEntity(url,null,Map.class).getBody();
			
			if(result.get("value")!=null){
				Double value = (Double)result.get("value");
				if(!StringUtils.isEmpty(value)){
					value = EasyKit.getDecimal(new BigDecimal(value));
					result.put("value", value);
				}
			}
		}catch(Exception e){
			result=new HashMap<String,Object>();
			result.put("error", "restTemplate获取预期最大回撤失败");
		}
		return result;
	}


	
	
	

	/**
	 * 获取预期年化收益（/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt，参数+1）
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	protected Map<String,Object> getExpAnnReturn(String groupId,String subGroupId){
		Map<String, Object> result=new HashMap<>();
		try{
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returntype="+"1";
			String str="{\"returnType\":\""+"1"+"\"}";
			result=(Map) restTemplate.postForEntity(url,getHttpEntity(str),Map.class).getBody();
			/*result.remove("_total");
			result.remove("_name");
			result.remove("_links");
			result.remove("_serviceId");
			result.remove("_schemaVersion");*/
			if(result.get("value")!=null){
				Double value = (Double)result.get("value");
				if(!StringUtils.isEmpty(value)){
					value = EasyKit.getDecimal(new BigDecimal(value));
					result.put("value", value);
				}
			}
		}catch(Exception e){
			result=new HashMap<String,Object>();
			result.put("error", "restTemplate获取预期年化收益失败");
		}
		return result;
	}



	/**
	 * 根据groupid和subgroupid获取产品组合的组合收益率
	 * @param groupId
	 * @param subGroupId
	 * @return
	 */
	protected Map<String,Object> getCombYieldRate(String groupId,String subgroupId){
		Map result=null;
		try{
			//准备调用asset-allocation接口的方法，获取组合组合收益率(最大回撤)走势图-每天
		String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-week?returnType=income";
 		result=restTemplate.getForEntity(url, Map.class,groupId,subgroupId).getBody();
 		result.remove("_total");
		result.remove("_name");
		result.remove("_links");
		result.remove("_serviceId");
		result.remove("_schemaVersion");
		if(result.get("_items")!= null){
			List item = (List) result.get("_items");
			if(item!=null&&!item.isEmpty()){
				for(int i=0;i<item.size();i++){
					Map map = (Map) item.get(i);
					if(map.containsKey("incomeBenchmark")){
						map.remove("incomeBenchmark");
					}
				}
			}
			item.remove("incomeBenchmark");
		}
		}catch(Exception e){
			result=new HashMap<String,Object>();
			result.put("error", "restTemplate获取预期组合收益率走势图失败");
		}
		return result;
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





	@ApiOperation("2.配置收益贡献")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009") })
	@RequestMapping(value = "/contributions", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult contributions(@RequestParam String groupId, @RequestParam String subGroupId) {

		String CONTRIBUTIONS_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/contributions";

		return  postConnectFinaceUrl(CONTRIBUTIONS_URL,groupId,subGroupId);
	}


	@ApiOperation("3.组合收益率(最大回撤)走势图-每天")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009") })
	//@RequestMapping(value = "/portfolioYield", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult portfolioYield(@RequestParam String groupId, @RequestParam String subGroupId) {

		String PORFOLIO_YIELD_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_URL,groupId,subGroupId);
	}

	@ApiOperation("4.组合收益率(最大回撤)走势图-每天(一周以来)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "4"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "4009") })
	//@RequestMapping(value = "/portfolioYieldWeek", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult portfolioYieldWeek(@RequestParam String groupId, @RequestParam String subGroupId) {

		String PORFOLIO_YIELD_WEEK_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield-week?returnType=1";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL,groupId,subGroupId);
	}


	@ApiOperation("5.风险控制手段与通知")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "productId", dataType = "String", required = true, value = "产品组ID", defaultValue = "1") })
	//@RequestMapping(value = "/riskNotifications", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult riskNotifications(@RequestParam String productId) {

		String PORFOLIO_YIELD_WEEK_URL ="/api/asset-allocation/products/{0}/risk-notifications";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL,productId,"");
	}


	
	@ApiOperation("需求调整")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="invstTerm",dataType="String",required = true,value="投资期限",defaultValue="1"),
		@ApiImplicitParam(paramType="query",name="riskLevel",dataType="String",required = true,value="风险承受级别",defaultValue="C1")
	})
	@RequestMapping(value="/optAdjustment",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult getOptAdjustment(String invstTerm,String riskLevel){
		try{
		return new JsonResult(JsonResult.SUCCESS, "请求成功",service.getOptAdjustment(riskLevel, invstTerm));
		}catch(Exception e){
		String str=new ReturnedException(e).getErrorMsg();
	    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
		
	}
	
	
	

	private JsonResult postConnectFinaceUrl(String url,String groupId ,String subGroupId){
		Map<String,Object> result;
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
		requestEntity.add("groupId", groupId);
		requestEntity.add("subGroupId", subGroupId);
		result = restTemplate.getForEntity(assetAlloctionUrl+ MessageFormat.format(url,groupId,subGroupId),Map.class).getBody();

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
            System.out.println(map.get("value"));
        }
        Collections.sort(list, new Comparator<Map<String, Object>>() {
            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                Double name1 = Double.valueOf(o1.get("value").toString()) ;//name1是从你list里面拿出来的一个 
                Double name2 = Double.valueOf(o2.get("value").toString()) ; //name1是从你list里面拿出来的第二个name
                return name2.compareTo(name1);
            }
        });
        //排序后 
        System.out.println("-------------------");
        for (Map<String, Object> map : list) {
            System.out.println(map.get("value"));
        }
        return list;
    }
	
	
	@ApiOperation("1.我选好了")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "riskPointValue", dataType = "String", required = false, value = "风险率", defaultValue = "0.0213"),
			@ApiImplicitParam(paramType = "query", name = "incomePointValue", dataType = "String", required = false, value = "收益率", defaultValue = "0.0451")
	})
	@RequestMapping(value = "/optimizations", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult Optimizations(@RequestParam String groupId, @RequestParam String riskPointValue, @RequestParam String incomePointValue) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("riskValue", riskPointValue);
			requestEntity.add("returnValue", incomePointValue);
			result = restTemplate
					.postForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/"+groupId+"/optimizations", requestEntity, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("2.获取（预期收益率调整）有多少个点")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
	})
	@RequestMapping(value = "/inComeSlidebarPoints", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult inComeSlidebarPoints(@RequestParam String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/"+groupId+"/slidebar-points?slidebarType=income_num", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("3.获取（风险率调整）有多少个点")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
	})
	@RequestMapping(value = "/riskSlidebarPoints", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult riskSlidebarPoints(@RequestParam String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/product-groups/"+groupId+"/slidebar-points?slidebarType=risk_num", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("3.获取（最优组合）")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "groupId", defaultValue = "2"),
	})
	@RequestMapping(value = "/effectiveFrontierPoints", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult effectiveFrontierPoints(@RequestParam String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(assetAlloctionUrl + "/api/asset-allocation/products/"+groupId+"/effective-frontier-points", Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}

			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
	}
}
