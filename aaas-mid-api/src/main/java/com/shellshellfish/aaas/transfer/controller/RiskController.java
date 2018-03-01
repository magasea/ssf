package com.shellshellfish.aaas.transfer.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.shellshellfish.aaas.dto.SurveyResult;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("phoneapi-ssf/")
@Api("转换相关restapi")
public class RiskController {
	
	Logger logger = LoggerFactory.getLogger(UserInfoController.class);
	
	@Value("${shellshellfish.risk-assessment-url}")
	private String url;
	
	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;
	
	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;
	
	@Autowired
	private RestTemplate restTemplate;

	private RestTemplate restTemplatePeach = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	
	@ApiOperation("风险测评-试题")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "bankId", dataType = "String", required = false, value = "银行卡ID",defaultValue="1") })
	@ApiResponses({ 
		@ApiResponse(code = 100, message = "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合"),
		@ApiResponse(code = 101, message = "手机号格式不对"), @ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "OK"), @ApiResponse(code = 400, message = "请求参数没填好"),
		@ApiResponse(code = 401, message = "未授权用户"), @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") 
		})
	@RequestMapping(value = "/surveytemplates/latest", method = RequestMethod.POST)
	@ResponseBody
	public JsonResult getSurveyTemplate(@RequestParam String bankId) {
		Map<String, Object> result = null;
		try {
			result = restTemplate.getForEntity(url + "/api/riskassessments/banks/" + bankId + "/surveytemplates/latest", Map.class)
					.getBody();
			List questionList=  (List) result.get("questions");
			for(int i=0;i<questionList.size();i++){
				Map optionItem = (Map) questionList.get(i);
				if(optionItem.get("optionItems")!=null){
					List item = (List) optionItem.get("optionItems");
					for(int j=0;j<item.size();j++){
						Map itemMap = (Map) item.get(j);
						if(itemMap.get("content")!=null){
							itemMap.put("text", itemMap.get("content"));
							itemMap.remove("content");
						}
					}
				}
			}
 			result.remove("_links");
			result.remove("_schemaVersion");
			result.put("title", "尊敬的客户您好！欢迎来到贝贝鱼风险测评...");
			return new JsonResult(JsonResult.SUCCESS, "风险测评成功", result);
		} catch (Exception e) {
			/*result = new HashMap<>();
			result.put("errorCode", "400");
			result.put("error", "");*/
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("风险测评-结果")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "bankUuid", dataType = "String", required = true, value = "银行卡ID"),
			@ApiImplicitParam(paramType = "query", name = "userUuid", dataType = "String", required = true, value = "用户ID"),
			@ApiImplicitParam(paramType="body",name="surveyResult",dataType="SurveyResult",required=true,value="测评结果BODY(选项、分数)",defaultValue="")
	})
	@ApiResponses({ 
		@ApiResponse(code = 100, message = "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合"),
		@ApiResponse(code = 101, message = "手机号格式不对"), @ApiResponse(code = 200, message = "OK"),
		@ApiResponse(code = 204, message = "OK"), @ApiResponse(code = 400, message = "请求参数没填好"),
		@ApiResponse(code = 401, message = "未授权用户"), @ApiResponse(code = 403, message = "服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code = 404, message = "请求路径没有或页面跳转路径不对") 
		})
	@RequestMapping(value = "/surveyresults", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public JsonResult saveSurveyResult(
			@RequestParam(value = "bankUuid") String bankUuid,
			@RequestParam(value = "userUuid") String userUuid,
			@RequestBody SurveyResult surveyResult 
			) {
		Map<String, Object> result = null;
		try {
			result = restTemplate.postForEntity(url + "/api/riskassessments/banks/" + bankUuid + "/users/"+userUuid+"/surveyresults",surveyResult, Map.class)
					.getBody();
			result.put("msg", "风险测评-结果成功");
			result.remove("_links");
			result.remove("_schemaVersion");
			Object obj = result.get("riskLevel");
			String riskLevel = "";
			if(obj!=null){
				logger.info("风险测评-结果成功");
				riskLevel = (String) obj;
			} else{
				logger.error("风险测评-结果失败");
			}
			//String telNum = "13573143909";
			//获取uid
			logger.info("获取uuid==="+userUuid);
			String urlUid=userinfoUrl+"/api/userinfo/users/"+userUuid;
			Map uidMap = restTemplate.getForEntity(urlUid,Map.class).getBody();
			logger.info("获取uuid成功");
			Map resultMap = (Map) uidMap.get("userBaseInfo");
			String telNum = (String) resultMap.get("cellPhone");
			
			String url=userinfoUrl+"/api/userinfo/users/"+telNum+"?isTestFlag=1&riskLevel="+riskLevel;
			Map fxResult=new HashMap();
			//String isTestFlag = "T";
			//fxResult=restTemplate.postForEntity(url,isTestFlag,Map.class).getBody();
			//String str="{\"isTestFlag\":\""+isTestFlag +"\"}";
			fxResult = restTemplatePeach.exchange(url, HttpMethod.PATCH, null, Map.class).getBody();
			if ("OK".equals(fxResult.get("status"))) {
				// res.put("isTestFlag","F");
				// return new JsonResult(JsonResult.SUCCESS, "风险测评成功", result);
			} else {
				return new JsonResult(JsonResult.Fail, "风险测评失败", JsonResult.EMPTYRESULT);
			}
			return new JsonResult(JsonResult.SUCCESS, "风险测评成功", result);
		} catch (Exception ex) {
			result = new HashMap<>();
			result.put("errorCode", "400");
			result.put("error", "");
			logger.error("exception:",ex);
			logger.error(ex.getMessage());
			String str = new ReturnedException(ex).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
	
	

	/**
	 * 通用方法处理post请求
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

}
