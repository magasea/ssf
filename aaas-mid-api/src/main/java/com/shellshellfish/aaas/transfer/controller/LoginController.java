package com.shellshellfish.aaas.transfer.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.service.MidApiService;
import com.shellshellfish.aaas.transfer.exception.ReturnedException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/phoneapi-ssf")
@Api("转换相关restapi")
public class LoginController {

	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	//@Autowired
	@Value("${shellshellfish.user-user-info}")
	private String userinfoUrl;
	
	
	@Autowired
	private RestTemplate restTemplate;
	
	private RestTemplate restTemplatePeach = new RestTemplate(new HttpComponentsClientHttpRequestFactory());
	@Autowired
	private MidApiService service;
	
	@ApiOperation("页面登陆")
//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="电话号码",defaultValue="13512345678"),
//		@ApiImplicitParam(paramType="query",name="password",dataType="String",required=true,value="密码",defaultValue="Zaq12wsx")
//	})
	@ApiResponses({
		@ApiResponse(code=100,message="密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合"),
        @ApiResponse(code=101,message="手机号格式不对"),
        @ApiResponse(code=200,message="OK"),
        @ApiResponse(code=204,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
    })
	@RequestMapping(value="/userlogin",method=RequestMethod.POST)
	@ResponseBody
   public JsonResult loginAccount(@RequestParam("telNum") String telNum,
	@RequestParam("password") 	String password){
		Map result=null;
		//HttpClient http = new Ht
		try {
			String str = "{\"password\":\""+password+"\",\"telnum\":\""+telNum+"\"}";	
			String url=loginUrl+"/api/useraccount/login";
			HttpEntity<String> entity =  getHttpEntity(str);
			result=restTemplate.postForObject(loginUrl+"/api/useraccount/login",entity,Map.class);
		    /*}catch(HttpClientErrorException e){
			 String str=e.getResponseBodyAsString();
			 return new JsonResult(JsonResult.Fail,EasyKit.getErrorMessage(str), "");
			}*/
			if(result == null){
				return new JsonResult(JsonResult.Fail,"登录信息获取为空",JsonResult.EMPTYRESULT);
			}
			/**********************添加的测试数据*******************************/
			//result.put("totalAssets", "10,000,000"); //总资产
			//result.put("dailyReturn", "3.8%"); //日收益率
			//result.put("totalRevenue", "10,000"); //累计收益
			String uuid = (String) result.get("uuid");
			
			Map userinfoMap = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/" + uuid + "/count", Map.class)
					.getBody();
			if(userinfoMap == null){
				return new JsonResult(JsonResult.Fail,"登录时，获取userinfo信息为空",JsonResult.EMPTYRESULT);
			}
			result.put("myInvstTotalQty", userinfoMap.get("myInvstTotalQty")); //我的智投组合数量
			result.put("myCardTotalQty", userinfoMap.get("myCardTotalQty")); //我的银行卡数量
			result.put("messageUnread", userinfoMap.get("messageUnread")); //未读消息数量	
			result.put("totalAssets", userinfoMap.get("totalAssets")); //总资产
			result.put("dailyReturn", userinfoMap.get("dailyReturn")); //日收益
			result.put("dailyReturnRate", userinfoMap.get("dailyIncomeRate")); //日收益率
			result.put("totalRevenue", userinfoMap.get("totalIncome")); //累计收益
			result.put("totalIncomeRate", userinfoMap.get("totalIncomeRate")); //累计收益率
			/**********************添加的测试数据*******************************/
			//移除不需要的数据
			result.remove("_links");
			result.remove("self");
		    result.put("telNum",telNum);
		 /*try{*/
		    Map resultCount = restTemplate.getForEntity(userinfoUrl + "/api/userinfo/users/telnums/" + telNum, Map.class)
					.getBody();
		    Map userMap =  (Map) resultCount.get("result");
		    result.put("isTestFlag", userMap.get("isTestFlag"));
		    result.put("testResult", resultCount.get("testResult"));
		    return new JsonResult(JsonResult.SUCCESS,"登陆成功",result);
		} catch (Exception e) {
			result = new HashMap<String, String>();
			String errorMsg=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail,errorMsg,JsonResult.EMPTYRESULT);
		} 
   }

	@ApiOperation("发送验证码")
	@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="手机号码",defaultValue="")
	@RequestMapping(value="/requestVerifyCode",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult sendVerifyCode(@RequestParam String telNum) {
		Map<String, Object> result = new HashMap();
		try {
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			headers.add("content-type", "application/json;charset=utf8");
			MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
			body.add("action", "getVerificationCode");
			HttpEntity<MultiValueMap<String, String>> httpEntity = new HttpEntity<MultiValueMap<String,String>>(body, headers);
			result = restTemplate.postForEntity(loginUrl + "/api/useraccount/telnums/" + telNum + "?action=getVerificationCode",null, Map.class).getBody();
			result.remove("_links");
			result.remove("_schemaVersion");
			return new JsonResult(JsonResult.SUCCESS, "发送成功", result);
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail,str, JsonResult.EMPTYRESULT);
		}
		
	}
	
	/**
	 * 点击注册的页面
	 * @param telNum
	 * @param password
	 * @param verifyCode
	 * @return
	 */
	@ApiOperation("注册")
	@ApiImplicitParams({
	@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="手机号码",defaultValue="13818977524"),
	@ApiImplicitParam(paramType="query",name="password",dataType="String",required=true,value="密码",defaultValue="555444"),
	@ApiImplicitParam(paramType="query",name="verifyCode",dataType="String",required=true,value="验证码",defaultValue="123123"),
	})
	@RequestMapping(value="/registration",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult doRegister(@RequestParam String telNum,@RequestParam String password,@RequestParam String verifyCode){

		//先调用注册手机号接口
		Map result=null;
		Map<String, String> res=new HashMap<String, String>();
		try{
			String url=loginUrl+"/api/useraccount/registrations?action=checkDupTelNum";
			String str="{\"telnum\":\""+telNum+"\",\"identifyingcode\":\""+verifyCode+"\"}";
		    result=restTemplatePeach.postForEntity(url,getHttpEntity(str),Map.class).getBody();
		    if(!"OK".equalsIgnoreCase((String)result.get("result"))){
		    	//如果不能注册，直接返回失败
		    	return new JsonResult(JsonResult.Fail, "注册失败",JsonResult.EMPTYRESULT);
		    }
		    //不报错进入下一个接口进行密码注册(requestMapping=registrations)
		    url=loginUrl+"/api/useraccount/registrations";
		    str="{\"telnum\":\""+telNum+"\",\"identifyingcode\":\""+verifyCode+"\",\"password\":\""+password+"\",\"pwdconfirm\":\""+password+"\"}";
		    res=restTemplatePeach.exchange(url, HttpMethod.PATCH, getHttpEntity(str), Map.class).getBody();
		    res.put("telNum", telNum);
		    /*********************插入注册时数据都为0**********************/
		    res.put("totalAssets", "0.00"); //总资产
		    res.put("dailyReturn", "-"); //日收益率
		    res.put("totalRevenue", "0.00"); //累计收益
		    res.put("myInvstTotalQty", "0"); //我的智投组合数量
		    res.put("myCardTotalQty", "0"); //我的银行卡数量
		    res.put("MessageUnread", "0"); //未读消息数量	
		    /*********************插入注册时数据都为0**********************/
		    String uuid = res.get("uuid");
		    if(StringUtils.isEmpty(uuid)){
		    	return new JsonResult(JsonResult.Fail, "注册失败", "uuid为空");
		    }
		    url=userinfoUrl+"/api/userinfo/users/"+uuid+"?cellphone="+telNum+"&isTestFlag=0";
		    Map fxResult=new HashMap();
		    fxResult=restTemplate.postForEntity(url,null,Map.class).getBody();
		    if("OK".equals(fxResult.get("status"))){
		    	res.put("isTestFlag","F");
		    	return new JsonResult(JsonResult.SUCCESS, "注册成功", res);
		    } else {
		    	return new JsonResult(JsonResult.Fail, "注册时，设置是否测评失败", null);
		    }
		} catch (Exception e) {
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
	}
	
	@ApiOperation("忘记密码")
	@ApiImplicitParams({
	@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="手机号码",defaultValue="13818977524"),
	@ApiImplicitParam(paramType="query",name="password",dataType="String",required=true,value="密码",defaultValue="555444"),
	@ApiImplicitParam(paramType="query",name="verifyCode",dataType="String",required=true,value="验证码",defaultValue="123123"),
	})
	@RequestMapping(value="/forgottenPsw",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult forgottenPsw(@RequestParam String telNum,@RequestParam String password,@RequestParam String verifyCode){
		//先调用注册手机号接口
		Map result=null;
		try{
			String url=loginUrl+"/api/useraccount/registrations";
			
			/*String str="{\"telnum\":\""+telNum+"\",\"identifyingcode\":\""+verifyCode+"\"}";*/
//			MultiValueMap<String, String> entity=new LinkedMultiValueMap<>();
//			entity.add("telnum", telNum);
//			entity.add("identifyingcode", verifyCode);
//			HttpEntity<?> reqEntity=new HttpEntity<>(entity);
//			
//			result  = restTemplatePeach.exchange(url, HttpMethod.PATCH,reqEntity, Map.class).getBody();
			
			url=loginUrl+"/api/useraccount/registrations";
		    String str="{\"telnum\":\""+telNum+"\",\"identifyingcode\":\""+verifyCode+"\",\"password\":\""+password+"\",\"pwdconfirm\":\""+password+"\"}";
		    result=restTemplatePeach.exchange(url, HttpMethod.PATCH, getHttpEntity(str), Map.class).getBody();
		    return new JsonResult(JsonResult.SUCCESS, "OK", result);
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
			 return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		
	}
	
	@ApiOperation("修改密码")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="uuid",dataType="String",required=true,value="用户ID"),
		@ApiImplicitParam(paramType="query",name="oldPWD",dataType="String",required=true,value="旧密码"),
		@ApiImplicitParam(paramType="query",name="newPWD",dataType="String",required=true,value="新密码"),
	})
	@RequestMapping(value="/resetPsw",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult resetPsw(
			@RequestParam String uuid,
			@RequestParam String oldPWD,
			@RequestParam String newPWD){
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			if(oldPWD.equals(newPWD)){
				return new JsonResult(JsonResult.Fail, "新旧密码不能一致", JsonResult.EMPTYRESULT);
			}
			String url=loginUrl+"/api/useraccount/users/"+uuid;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			if(result==null){
				return new JsonResult(JsonResult.Fail, "用户不存在", JsonResult.EMPTYRESULT);
			}
//			Map<String, Object> res = (Map<String, Object>) result.get("result");
//			Object pwd = res.get("passwordHash");
//			String old = MD5.getMD5(oldPWD);
//			if(!old.equals(pwd)){
//				return new JsonResult(JsonResult.Fail, "旧密码输入不正，请重新输入", "");
//			}
			url=loginUrl+"/api/useraccount/users/"+uuid+"/passwords/"+oldPWD+"?newpassword="+newPWD;
			restTemplatePeach.exchange(url, HttpMethod.PATCH, null, Map.class).getBody();
			return new JsonResult(JsonResult.SUCCESS, "更改密码正确", JsonResult.EMPTYRESULT);
		} catch (HttpClientErrorException e) {
			result = new HashMap();
			result.put("errorCode", "400");
			String str = e.getResponseBodyAsString();
			System.out.println(str);
			result.put("error", e.getResponseBodyAsString());
			return new JsonResult(JsonResult.Fail, "修改密码失败", JsonResult.EMPTYRESULT);
		}catch(Exception e){
			return new JsonResult(JsonResult.Fail, "Fail", JsonResult.EMPTYRESULT);
		}
		
	}
	
	@ApiOperation("验证码验证接口")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="msgCode",dataType="String",required=true,value="手机验证码"),
		@ApiImplicitParam(paramType="query",name="telNum",dataType="String",required=true,value="手机号码")
	})
	@RequestMapping(value="/verifyMsgCode",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult verifyMSGCode(String telNum,String msgCode){
		try{
		String result=service.verifyMSGCode(telNum, msgCode);
		return new JsonResult(JsonResult.SUCCESS,result,JsonResult.EMPTYRESULT);
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		
	}
	
	@ApiOperation("用户退出")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="uuid",dataType="String",required=true,value="用户UUID"),
		@ApiImplicitParam(paramType="query",name="token",dataType="String",required=true,value="用户token")
	})
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	public JsonResult logout(String uuid,String token){
		Map<String, Object> result=new HashMap<String, Object>();
		try{
			String url=loginUrl+"/api/useraccount/logout?uuid="+uuid+"&token="+token;
			result = restTemplate.getForEntity(url, Map.class).getBody();
			return new JsonResult(JsonResult.SUCCESS, "用户退出成功", JsonResult.EMPTYRESULT);
		}catch(Exception e){
			String str=new ReturnedException(e).getErrorMsg();
			return new JsonResult(JsonResult.Fail, str, JsonResult.EMPTYRESULT);
		}
		
	}
	
	
	
	
	/**
	 * 通用方法处理post请求带requestbody
	 * @param JsonString
	 * @return
	 */
	protected HttpEntity<String> getHttpEntity(String JsonString){
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.parseMediaType("application/json;UTF-8"));
		headers.add("Accept", MediaType.APPLICATION_JSON.toString());
		HttpEntity<String> strEntity = new HttpEntity<String>(JsonString,headers);
		return strEntity;
	}
	
	

	
	
}
