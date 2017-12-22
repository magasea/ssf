package com.shellshellfish.aaas.transfer.controller;

import java.text.MessageFormat;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.dto.FinanceProductCompo;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
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
			@ApiImplicitParam(paramType = "query", name = "uuid", dataType = "String", required = false, value = "用户ID", defaultValue = "1") })
	@RequestMapping(value = "/finance-home", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult financeHome(@RequestParam String uuid) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();
			requestEntity.add("uuid", uuid);
//			requestEntity.add("productType", productType);
			result = restTemplate
					.getForEntity(financeUrl + "/api/ssf-finance/product-groups/homepage?uuid=" + uuid, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			result.put("msg", "获取成功");
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap();
			map.put("errorCode", "400");
			return new JsonResult(JsonResult.Fail, "获取失败", map);
		}
	}


	@ApiOperation("6.进入理财页面后的数据")
    @RequestMapping(value="/financeFrontPage",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult financeModule(){
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
				result=new HashMap<>();
				String message=e.getMessage();
				result.put("错误原因", message+",获取理财产品调用restTemplate方法发生错误！");
				return new JsonResult(JsonResult.Fail, "获取理财List数据失败",result);
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
				      Map productCompo=(Map) productMap.get("assetsRatios");
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
					   result.clear();
					   result.put("error","获取产品的field属性失败");
					   return new JsonResult(JsonResult.Fail, "获取失败", result);
				   }
			                             }
			                  }else{
			return new JsonResult(JsonResult.Fail, "没有获取到产品", null);
			                  }
			return new JsonResult(JsonResult.SUCCESS, "获取成功", resultList);
	}

	
	@ApiOperation("理财产品查看详情页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "6"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "111111"),
	})
	@RequestMapping(value="/checkPrdDetails",method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getPrdDetails(String groupId,String subGroupId){
		Map result=null;
		try{
			result=service.getPrdNPVList(groupId, subGroupId);
		}catch(Exception e){
			result=new HashMap<>();
			result.put("错误原因", e.getMessage()+",restTemplate调用组合各种类型净值收益失败");
			return new JsonResult(JsonResult.Fail, "查看理财产品详情失败", result);
		}
		return new JsonResult(JsonResult.Fail, "查看理财产品详情成功", result);
	}
	
	
	

	@ApiOperation("历史业绩")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "6"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "111135"),
	})
	@RequestMapping(value = "/historicalPerformancePage", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getHistoricalPerformance(@RequestParam String groupId, @RequestParam String subGroupId) {
		// 先获取全部产品
		String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/historicalPer-formance?fund_group_id=" + groupId
				+ "&subGroupId=" + subGroupId;
		Map<String,Object> result = new HashMap<String,Object>();// 中间容器
		Object object = null;
		List<Map<String, Object>> prdList = null; // 中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();// 结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
			logger.info("历史业绩-第一部分数据获取成功");
			if (result != null) {
				object = result.get("_items");
				if (object != null) {
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
			return new JsonResult(JsonResult.Fail, "获取理财List数据失败", result);
		}
		
		//收益率走势图
		//http://localhost:10020/api/asset-allocation/product-groups/6/sub-groups/111111/portfolio-yield-week?returnType=income
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId +"/sub-groups/"+subGroupId+"/portfolio-yield-week?returnType=income";
		Map<String,Object> incomeResult = new HashMap<String,Object>();
		incomeResult = restTemplate.getForEntity(url, Map.class).getBody();
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		Object obj = null;
		if (incomeResult != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj = incomeResult.get("_items");
			if (obj != null) {
				logger.info("obj获取成功");
				result.put("portfolioYield",obj);
			} else {
				logger.info("object获取失败");
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
		if (incomeResult1 != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj1 = incomeResult1.get("_items");
			if (obj1 != null) {
				logger.info("obj1获取成功");
				result.put("maxRetreat",obj1);
			} else {
				logger.info("object获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", result);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}
	
	@ApiOperation("未来预期page")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "6"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "111135"),
	})
	@RequestMapping(value = "/futureExpectation", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFutureExpectation(@RequestParam String groupId, @RequestParam String subGroupId) {
		// 预期平均年化收益
		String url = assetAlloctionUrl + "/api/asset-allocation/product-groups/historicalPer-formance?fund_group_id=" + groupId
				+ "&subGroupId=" + subGroupId;
		Map<String,Object> result = new HashMap<String,Object>();// 中间容器
		Object object = null;
		List<Map<String, Object>> prdList = null; // 中间容器
		List<FinanceProductCompo> resultList = new ArrayList<FinanceProductCompo>();// 结果集
		try {
			result = restTemplate.getForEntity(url, Map.class).getBody();
			logger.info("历史业绩-第一部分数据获取成功");
			if (result != null) {
				object = result.get("_items");
				if (object != null) {
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
			return new JsonResult(JsonResult.Fail, "获取理财List数据失败", result);
		}
		
		//收益率走势图
		//http://localhost:10020/api/asset-allocation/product-groups/6/sub-groups/111111/portfolio-yield-week?returnType=income
		url = assetAlloctionUrl + "/api/asset-allocation/product-groups/" + groupId +"/sub-groups/"+subGroupId+"/portfolio-yield-week?returnType=income";
		Map<String,Object> incomeResult = new HashMap<String,Object>();
		incomeResult = restTemplate.getForEntity(url, Map.class).getBody();
		// 如果成功获取内部值，再遍历获取每一个产品的年化收益(进入service)
		Object obj = null;
		if (incomeResult != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj = incomeResult.get("_items");
			if (obj != null) {
				logger.info("obj获取成功");
				result.put("portfolioYield",obj);
			} else {
				logger.info("object获取失败");
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
		if (incomeResult1 != null) {
			logger.info("历史业绩-第一部分数据获取成功");
			obj1 = incomeResult1.get("_items");
			if (obj1 != null) {
				logger.info("obj1获取成功");
				result.put("maxRetreat",obj1);
			} else {
				logger.info("object获取失败");
			}
		} else {
			logger.error("获取收益率失败");
			return new JsonResult(JsonResult.SUCCESS, "获取收益率失败", result);
		}
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}
	
	
	
	@ApiOperation("查询产品的历史收益率和最大回撤")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = false, value = "groupId", defaultValue = "6"),
		@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = false, value = "subGroupId", defaultValue = "111111")
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
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt";
			String str="{\"returnType\":\""+"1"+"\"}";
			result=(Map) restTemplate.postForEntity(url,getHttpEntity(str),Map.class).getBody();
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
		Map result=null;
		try{
			String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/"+groupId+"/sub-groups/"+subGroupId+"/opt?returnType="+"1";
			String str="{\"returnType\":\""+"1"+"\"}";
			result=(Map) restTemplate.postForEntity(url,getHttpEntity(str),Map.class).getBody();
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
		String url=assetAlloctionUrl+"/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-week";
 		result=restTemplate.getForEntity(url, Map.class,groupId,subgroupId).getBody();
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
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "6"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "111111") })
	@RequestMapping(value = "/contributions", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult contributions(@RequestParam String groupId, @RequestParam String subGroupId) {

		String CONTRIBUTIONS_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/contributions";

		return  postConnectFinaceUrl(CONTRIBUTIONS_URL,groupId,subGroupId);
	}


	@ApiOperation("3.组合收益率(最大回撤)走势图-每天")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "6"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "111111") })
	@RequestMapping(value = "/portfolioYield", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult portfolioYield(@RequestParam String groupId, @RequestParam String subGroupId) {

		String PORFOLIO_YIELD_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_URL,groupId,subGroupId);
	}

	@ApiOperation("4.组合收益率(最大回撤)走势图-每天(一周以来)")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "groupId", dataType = "String", required = true, value = "产品组ID", defaultValue = "6"),
			@ApiImplicitParam(paramType = "query", name = "subGroupId", dataType = "String", required = true, value = "子产品组ID", defaultValue = "111111") })
	@RequestMapping(value = "/portfolioYieldWeek", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult portfolioYieldWeek(@RequestParam String groupId, @RequestParam String subGroupId) {

		String PORFOLIO_YIELD_WEEK_URL ="/api/asset-allocation/product-groups/{0}/sub-groups/{1}/portfolio-yield-week?returnType=1";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL,groupId,subGroupId);
	}


	@ApiOperation("5.风险控制手段与通知")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "productId", dataType = "String", required = true, value = "产品组ID", defaultValue = "1") })
	@RequestMapping(value = "/riskNotifications", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult riskNotifications(@RequestParam String productId) {

		String PORFOLIO_YIELD_WEEK_URL ="/api/asset-allocation/products/{0}/risk-notifications";

		return  postConnectFinaceUrl(PORFOLIO_YIELD_WEEK_URL,productId,"");
	}


	
	@ApiOperation("调整方案")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="riskLevel",dataType="String",required = true,value="风险承受级别",defaultValue="C1"),
		@ApiImplicitParam(paramType="query",name="invstTerm",dataType="String",required = true,value="投资期限",defaultValue="1")
	})
	@RequestMapping(value="/optAdjustment",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult getOptAdjustment(String riskLevel,String invstTerm){
		try{
		return new JsonResult(JsonResult.SUCCESS, "请求成功",service.getOptAdjustment(riskLevel, invstTerm));
		}catch(Exception e){
	    return new JsonResult(JsonResult.Fail, "请求失败",service.getOptAdjustment(riskLevel, invstTerm));
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
			return new JsonResult(JsonResult.Fail, "获取失败", result);
		}

		result.put("msg", "获取成功");
		return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
	}


}
