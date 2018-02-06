package com.shellshellfish.aaas.transfer.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import com.shellshellfish.aaas.transfer.utils.CalculatorFunctions;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("基金详情restapi")
public class FundDetailedController {

	Logger logger = LoggerFactory.getLogger(UserInfoController.class);

	private final static String TRADENOTICE = "交易流程，费率";

	private final static String FUNDNOTICE = "招募说明书、分红通知";

	@Value("${shellshellfish.finance-url}")
	private String financeUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;
	
	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;

	@Autowired
	private RestTemplate restTemplate;


	@ApiOperation("基金详情")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金编码", defaultValue = "000216.OF"),
			@ApiImplicitParam(paramType = "query", name = "date", dataType = "String", required = false, value = "日期", defaultValue = "2017-12-22")
	})
	@RequestMapping(value = "/getFundDetails", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundInfoByCodes(@RequestParam String code, @RequestParam(required = false) String date) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			//近一年涨幅, 当日涨幅, 当日净值,获取历史页面
			String param = StringUtils.isNotBlank(date) ? "&date=" + date : "";
//			result = restTemplate
//					.getForEntity(financeUrl + "/api/ssf-finance/getFundValueInfo?fundCode=" + code + param, Map.class)
//					.getBody();
//			if (result == null || result.size() == 0) {
//				result.put("msg", "获取失败");
//				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
//			}

			//获取类型 风险等级	评级
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundValueInfo?code=" + code + param, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				logger.error("获取类型 风险等级	评级为空");
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
//			//加入到返回值
//			result.put("rate", dataResult.get("rate"));
//			result.put("classtype", dataResult.get("classtype"));
//			result.put("net", dataResult.get("net") == null ? 0 : dataResult.get("net"));

			//获取基金经理、基金公司
			Map<String, Object> companyResult = new HashMap<String, Object>();
			companyResult = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundInfoBycode?code=" + code, Map.class)
					.getBody();
			if (companyResult == null || companyResult.size() == 0) {
				logger.error("获取基金经理、基金公司为空");
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			} else {
				// 基金名称
				result.put("fundname", companyResult.get("fundname"));
				// 基金经理
				result.put("manager", companyResult.get("manager"));
				// 基金公司
				result.put("fundcompany", companyResult.get("fundcompany"));
				// 基金概况
				Object createdate = companyResult.get("createdate");
				int years = 0;
				if (createdate != null) {
					years = CalculatorFunctions.getAgeFromBirthTime(createdate + "");
				}
				result.put("fundsurvey", "成立" + years + "年，" + companyResult.get("scale") + "亿");
			}
			// 交易须知
			result.put("tradenotice", TRADENOTICE);
			// 基金公告
			result.put("fundnotice", FUNDNOTICE);

			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("收益走势&历史净值")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金代码", defaultValue = "000614.OF"),
			@ApiImplicitParam(paramType = "query", name = "type", dataType = "String", required = true, value = "类型(1: 3month,2: 6month,3: 1year,4: 3year);", defaultValue = "1"),
			@ApiImplicitParam(paramType = "query", name = "date", dataType = "String", required = false, value = "日期", defaultValue = "2017-12-28")
	})
	@RequestMapping(value = "/getHistoryNetvalue", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getHistoryNetvalue(@RequestParam String code, @RequestParam String type, @RequestParam(required = false) String date) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			String url = "";
			if(StringUtils.isEmpty(date)){
				url = dataManagerUrl + "/api/datamanager/getHistoryNetvalue?code=" + code + "&type=" + type ;
			} else {
				url = dataManagerUrl + "/api/datamanager/getHistoryNetvalue?code=" + code + "&type=" + type + "&date=" + date;
			}
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.Fail, "获取失败", JsonResult.EMPTYRESULT);
//				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			
			if (MonetaryFundEnum.containsCode(code)) {
				if (result.get("yieldOf7DaysAndTenKiloUnitYield") != null) {
					List<Map> yieldOf7DaysAndTenKiloUnitYieldList = (List<Map>) result
							.get("yieldOf7DaysAndTenKiloUnitYield");
					if (yieldOf7DaysAndTenKiloUnitYieldList != null && yieldOf7DaysAndTenKiloUnitYieldList.size() > 0) {
						Map yieldOf7DaysAndTenKiloUnitYieldMap = yieldOf7DaysAndTenKiloUnitYieldList.get(yieldOf7DaysAndTenKiloUnitYieldList.size()-1);
						yieldOf7DaysAndTenKiloUnitYieldList.remove(yieldOf7DaysAndTenKiloUnitYieldList.size()-1);
						Map<String,Object> yieldOfTenKiloUnitYieldMaxMin = new HashMap<String,Object>();
						Map<String,Object> yieldOf7DaysMaxMin = new HashMap<String,Object>();
						if (yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOfTenKiloUnitYieldMin") != null) {
							yieldOfTenKiloUnitYieldMaxMin.put("yieldOfTenKiloUnitYieldMin",
									yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOfTenKiloUnitYieldMin"));
							if (yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOfTenKiloUnitYieldMax") != null) {
								yieldOfTenKiloUnitYieldMaxMin.put("yieldOfTenKiloUnitYieldMax",
										yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOfTenKiloUnitYieldMax"));
								result.put("yieldOfTenKiloUnitYieldMaxMin", yieldOfTenKiloUnitYieldMaxMin);
							}
						}

						if (yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOf7DaysMax") != null) {
							yieldOf7DaysMaxMin.put("yieldOf7DaysMax",
									yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOf7DaysMax"));
							if (yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOf7DaysMin") != null) {
								yieldOf7DaysMaxMin.put("yieldOf7DaysMin",
										yieldOf7DaysAndTenKiloUnitYieldMap.get("yieldOf7DaysMin"));
								result.put("yieldOf7DaysMaxMin", yieldOf7DaysMaxMin);
							}
						}
						
						List<Map> yieldOf7DaysList = new ArrayList<Map>();
						List<Map> yieldOfTenKiloUnitYieldList = new ArrayList<Map>();
						for(int i=0;i<yieldOf7DaysAndTenKiloUnitYieldList.size();i++){
							Map<String,Object> yieldOf7DaysMap = new HashMap<String,Object>();
							Map<String,Object> yieldOfTenKiloUnitYieldMap = new HashMap<String,Object>();
							Map yieldMap = yieldOf7DaysAndTenKiloUnitYieldList.get(i);
							yieldOf7DaysMap.put("date", yieldMap.get("date"));
							yieldOf7DaysMap.put("yieldOf7Days", yieldMap.get("yieldOf7Days"));
							yieldOfTenKiloUnitYieldMap.put("date", yieldMap.get("date"));
							yieldOfTenKiloUnitYieldMap.put("tenKiloUnitYield", yieldMap.get("tenKiloUnitYield"));
							yieldOf7DaysList.add(yieldOf7DaysMap);
							yieldOfTenKiloUnitYieldList.add(yieldOfTenKiloUnitYieldMap);
						}
						Collections.reverse(yieldOf7DaysAndTenKiloUnitYieldList);
						result.put("yieldOf7DaysList", yieldOf7DaysList);
						result.put("yieldOfTenKiloUnitYieldList", yieldOfTenKiloUnitYieldList);
						result.put("yieldOf7DaysAndTenKiloUnitYield", yieldOf7DaysAndTenKiloUnitYieldList);
					}
				}
			} else {
				if(result.get("baselinehistoryprofitlist")!=null){
					List<Map> baselinehistoryprofitlist = (List<Map>) result.get("baselinehistoryprofitlist");
					Map<String,Object> baselinehistoryprofitMaxMix = new HashMap();
					if(baselinehistoryprofitlist!=null&&baselinehistoryprofitlist.size()>0){
						List<Double> maxMinValueList = new ArrayList<Double>();
						for(int i=0;i<baselinehistoryprofitlist.size();i++){
							Map baselinehistoryprofitMap = baselinehistoryprofitlist.get(i);
							String dayup = baselinehistoryprofitMap.get("dayup")+"";
							dayup = dayup.replace("%", "");
							maxMinValueList.add(Double.parseDouble(dayup));
						}
						baselinehistoryprofitMaxMix.put("maxValue", Collections.max(maxMinValueList));
						baselinehistoryprofitMaxMix.put("minValue", Collections.min(maxMinValueList));
						result.put("baselinehistoryprofitMaxMix", baselinehistoryprofitMaxMix);
					}
				}
				if(result.get("historyprofitlist")!=null){
					List<Map> historyprofitlist = (List<Map>) result.get("historyprofitlist");
					Map<String,Object> historyprofitMaxMix = new HashMap();
					if(historyprofitlist!=null&&historyprofitlist.size()>0){
						List<Double> maxMinValueList = new ArrayList<Double>();
						for(int i=0;i<historyprofitlist.size();i++){
							Map historyprofitMap = historyprofitlist.get(i);
							String profit = historyprofitMap.get("profit")+"";
							maxMinValueList.add(Double.parseDouble(profit));
						}
						historyprofitMaxMix.put("maxValue", Collections.max(maxMinValueList));
						historyprofitMaxMix.put("minValue", Collections.min(maxMinValueList));
						result.put("historyprofitMaxMix", historyprofitMaxMix);
					}
				}
				if(result.get("historynetlist")!=null){
					List<Map> historynetlist = (List<Map>) result.get("historynetlist");
					if(historynetlist!=null&&historynetlist.size()>0){
						List<Map> historynetlistBak = new ArrayList<Map>();
						for (int i = historynetlist.size() - 1; i >= 0; i--) {
							Map historyprofitMap = historynetlist.get(i);
							historynetlistBak.add(historyprofitMap);
						}
						result.put("historynetlist", historynetlistBak);
					}
				}
			}
			
			if(result.get("basename")!=null){
				String basename = result.get("basename") + "";
				StringBuffer base = new StringBuffer();
				base.append("基准（");
				base.append(basename);
				base.append("）");
				result.put("basename", base);
			}
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
//			return new JsonResult(JsonResult.Fail, "获取失败", map);
		}
	}


	@ApiOperation("交易须知")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@ApiIgnore
	@RequestMapping(value = "/getFundTradeNotices", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundTradeNotices(@RequestParam String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(financeUrl + "/api/ssf-finance/product-groups/tradenotices/" + code, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			Map<String, Object> map = new HashMap();
			map.put("errorCode", "400");
			return new JsonResult(JsonResult.Fail, "获取失败", map);
		}
	}

	@ApiOperation("基金概况")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000216.OF")
	})
	@RequestMapping(value = "/getFundInfoBycode", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundInfo(@RequestParam String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundInfoBycode?code=" + code, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				logger.error("基金概况获取为空");
				result.put("msg", "基金概况获取为空");
				return new JsonResult(JsonResult.Fail, "基金概况获取为空", JsonResult.EMPTYRESULT);
			}
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}


	@ApiOperation("基金经理")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "基金经理名称", defaultValue = "董阳阳")
	})
	@RequestMapping(value = "/getFundManager", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundManager(@RequestParam String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundManager?name=" + name, Map.class)
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
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("基金公司ByName")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "name", dataType = "String", required = true, value = "基金公司名称", defaultValue = "天弘基金管理有限公司")
	})
	@RequestMapping(value = "/getFundCompanyDetailInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundCompanyDetailInfo(@RequestParam String name) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundCompanyDetailInfo?name=" + name, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.Fail, "获取成功", result);
			}
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			logger.error(e.getMessage());
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}

	}

	@ApiOperation("基金公司ByCode")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金公司名称", defaultValue = "000009.OF")
	})
	//@RequestMapping(value = "/getFundCompany", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundCompany(@RequestParam String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundCompany?code=" + code, Map.class)
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
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}

	@ApiOperation("基金公告")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundNotices", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundNotices(@RequestParam String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Object> list = restTemplate
					.getForEntity(financeUrl + "/api/ssf-finance/getFundNotices?fundCode=" + code, List.class)
					.getBody();
			if (list.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			result.put("notices", list);
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str = new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
}
