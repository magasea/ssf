package com.shellshellfish.aaas.transfer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("基金详情restapi")
public class FundDetailedController {

	@Value("${shellshellfish.finance-url}")
	private String financeUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;

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
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
//			//加入到返回值
//			result.put("rate", dataResult.get("rate"));
//			result.put("classtype", dataResult.get("classtype"));
//			result.put("net", dataResult.get("net") == null ? 0 : dataResult.get("net"));
			
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
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
	public JsonResult getHistoryNetvalue(@RequestParam String code, @RequestParam String type, @RequestParam(required=false) String date) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			result = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getHistoryNetvalue?code="+code+"&type="+ type + "&date=" + date, Map.class)
					.getBody();
			if (result == null || result.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.Fail,"获取失败",JsonResult.EMPTYRESULT);
//				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
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
			@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "code", defaultValue = "000001.OF")
	})
	@RequestMapping(value = "/getFundInfo", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getFundInfo(@RequestParam String code) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			List<Object> list = restTemplate
					.getForEntity(dataManagerUrl + "/api/datamanager/getFundInfo?codes=" + code, List.class)
					.getBody();
			if (list.size() == 0) {
				result.put("msg", "获取失败");
				return new JsonResult(JsonResult.SUCCESS, "获取成功", result);
			}
			result.remove("_links");
			result.remove("_links");
			return new JsonResult(JsonResult.SUCCESS, "获取成功", list.get(0));
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
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
					.getForEntity(financeUrl + "/api/ssf-finance/getFundManager?name=" + name, Map.class)
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
	@ApiOperation("基金公司ByCode")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "code", dataType = "String", required = true, value = "基金公司名称", defaultValue = "000009.OF")
	})
	@RequestMapping(value = "/getFundCompany", method = RequestMethod.POST)
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
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
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
			String str=new ReturnedException(e).getErrorMsg();
		    return new JsonResult(JsonResult.Fail, str,JsonResult.EMPTYRESULT);
		}
	}
}
