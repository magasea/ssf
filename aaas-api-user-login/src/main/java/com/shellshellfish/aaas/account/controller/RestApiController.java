package com.shellshellfish.aaas.account.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.shellshellfish.aaas.account.aop.AopPageResources;
import com.shellshellfish.aaas.account.exception.UserException;
import com.shellshellfish.aaas.account.model.dao.User;
import com.shellshellfish.aaas.account.model.dto.BankCardDTO;
import com.shellshellfish.aaas.account.model.dto.LoginBodyDTO;
import com.shellshellfish.aaas.account.model.dto.PwdSettingBodyDTO;
import com.shellshellfish.aaas.account.model.dto.RegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UpdateRegistrationBodyDTO;
import com.shellshellfish.aaas.account.model.dto.UserDTO;
import com.shellshellfish.aaas.account.model.dto.VerificationBodyDTO;
import com.shellshellfish.aaas.account.service.AccountService;
import com.shellshellfish.aaas.account.service.BankCardService;
import com.shellshellfish.aaas.account.service.ResourceManagerService;
import com.shellshellfish.aaas.account.service.SchemaManager;
import com.shellshellfish.aaas.account.utils.MD5;
import com.shellshellfish.aaas.account.utils.PageWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("api/useraccount")
@Validated
@Api("用户登录相关restapi")
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	private static final Class<?> UserException = null;

	@Autowired
	AccountService accountService;
	
	@Autowired
	ResourceManagerService resourceManagerService;
	
	@Autowired
	SchemaManager schemaManager;
	
	@Autowired
	BankCardService bankCardService;
	
	
	
	public static String URL_HEAD="/api/useraccount";
	
//	@Bean
//	public BankCardService bankCardService() {
//		return new BankCardServiceImpl();
//	}
//	
//	@Bean
//	public AccountService accountService() {
//		return new AccountServiceImpl();
//	}
//		
//	@Bean
//	public ResourceManager resourceManager() {
//		return new ResourceManager();
//	}
//	
//	@Bean
//	public SchemaManager schemaManager() {
//		return new SchemaManager();
//	}
//	
//	@Bean
//	public SmsVerificationRepositoryCustom SmsVerificationRepositoryCustomImpl() {
//		return new SmsVerificationRepositoryCustomImpl();
//	}
	
	/*
	@RequestMapping(value = "/register.json", method = RequestMethod.POST)
	public ResponseEntity<PageSchema> registerschema(
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		   PageSchema pageschema= schemaManager.getSchemafile("register");
		  //System.out.println(pagestr);
		  return new ResponseEntity<PageSchema>(pageschema, HttpStatus.OK);	
    }*/
	
	@ApiOperation("注册 首页")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="电话号码",defaultValue="")
    })
	@ApiResponses({
        @ApiResponse(code=200,message="OK")        
    })					
	@RequestMapping(value = "/registrationpage", method = RequestMethod.GET)
	public ResponseEntity<Map> registrationpage( 
			@Valid @NotNull(message="电话不能为空") @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum){
		   
		  HashMap<String ,Object> rsmap= resourceManagerService.response("register",new String[]{telnum});
		  //System.out.println(pagestr);
		  return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }
	
	//忘记密码下的获取验证码
	//忘记密码
//	@ApiOperation("获取验证码")
//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType="query",name="action",dataType="String",required=true,value="动作",defaultValue="coderequest"),
//		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="电话号码",defaultValue="")
//       
//    })
//	@ApiResponses({
//        @ApiResponse(code=200,message="OK")        
//    })					
//	@RequestMapping(value = "/pwdforgettings/request", method = RequestMethod.GET)
//	public ResponseEntity<HttpStatus> verifycodeget(
//			@RequestParam(value = "action",required=true,defaultValue="coderequest") String action,
//			@Valid @NotNull(message="电话不能为空")  @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum
//			){			
//			
//		  //nedd alisms interface to get sms
//		  //send  sms to telphone 
//		return new ResponseEntity<HttpStatus>(HttpStatus.OK);	
//    }

	
	//
	@ApiOperation("密码设置")
	@ApiImplicitParams({
		@ApiImplicitParam(name="updateregistrationBody", value ="用户数据 电话号码/验证码",required=true,paramType="body",dataType="UpdateRegistrationBodyDTO")
    })
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=401,message="未授权用户")        				
    })		
	@RequestMapping(value = "/registrations", method = RequestMethod.PATCH)
	public ResponseEntity<HttpStatus> updateregistrationsId(
			@Valid @RequestBody UpdateRegistrationBodyDTO updateregistrationBody
			){
		if (accountService.isSmsVerified(updateregistrationBody))
			return new ResponseEntity<HttpStatus>(HttpStatus.RESET_CONTENT);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
	}
	
	//忘记密码 初始化页面
	@ApiOperation("忘记密码资源页")
	@ApiImplicitParams({
			@ApiImplicitParam(paramType = "query", name = "telnum", dataType = "String", required = true, value = "电话号码", defaultValue = "")

	})
	@ApiResponses({ @ApiResponse(code = 200, message = "OK") })
	@RequestMapping(value = "/pwdforgettingpage", method = RequestMethod.GET)
	public ResponseEntity<Map> forgottenpwdres(
			@Valid @NotNull(message = "手机号码不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum) {
		String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern telPattern = Pattern.compile(telRegExp);
		Matcher telMatcher = telPattern.matcher(telnum);
		if (!telMatcher.find()) {
			throw new UserException("102", "手机号格式不对");
		}
		HashMap<String, Object> rsmap = resourceManagerService.response("forgottenpwd", new String[] { telnum });

		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}

	//忘记密码页面 获取验证码
	@ApiOperation("忘记密码页面的获取验证码")
	@ApiImplicitParams({
			@ApiImplicitParam(name="action", value ="action:getVerificationCode",required=true,paramType="query",dataType="String"),
			@ApiImplicitParam(name="id", value ="手机号码",required=true,paramType="path",dataType="String")
	})
	@ApiResponses({ @ApiResponse(code = 200, message = "OK"), @ApiResponse(code = 500, message = "服务器内部程序错误") })
	@RequestMapping(value = "/telnums/{id}", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> codeRequest(
			@Valid @NotNull(message = "id不能为空") @PathVariable("id") String id,
			@Valid @NotNull(message = "action不能为空") @RequestParam(value = "action") String action) {
		if ("getVerificationCode".equals(action)) {
			String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
			Pattern telPattern = Pattern.compile(telRegExp);
			Matcher telMatcher = telPattern.matcher(id);
			if (!telMatcher.find()) {
				throw new UserException("102", "手机号格式不对");
			}
			if (!accountService.sendSmsMessage(id)) {
				return new ResponseEntity<HttpStatus>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
			
		}
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	}


	
	@ApiOperation("用户登录资源页")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")   
    })		
	@RequestMapping(value = "/loginpage", method = RequestMethod.GET)
	public ResponseEntity<Map> loginpage(){
		    HashMap<String ,Object> rsmap= resourceManagerService.response("login",null);
		    return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }
	
	@ApiOperation("用户登录验证")
	@ApiImplicitParam(name="loginBodyDTO", value ="用户数据 手机号码/密码",required=true,paramType="body",dataType="LoginBodyDTO")
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
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<?> login(@Valid @RequestBody LoginBodyDTO loginBodyDTO) {
		String telnum = loginBodyDTO.getTelnum();
		String password = loginBodyDTO.getPassword();
		if (telnum.length() != 11) {
			throw new UserException("100", "电话长度必须是11位的数字");
		}
		Pattern p = Pattern.compile(
				"^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");
		Matcher m = p.matcher(password);
		if (m.find()) { // need pwd check
			// int kk=1;
		} else {
			throw new UserException("101", "密码长度至少8位,至多20位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
		}

		String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern telPattern = Pattern.compile(telRegExp);
		Matcher telMatcher = telPattern.matcher(telnum);
		if (!telMatcher.find()) {
			throw new UserException("102", "手机号格式不对");
		}

		// passwd:abccd4djsN-999
		// CellPhone:13611442221

		// User targetuser =
		// userRepository.findByCellPhoneAndPasswordHash(user.getCellPhone(),user.getPasswordHash());
		loginBodyDTO.setPassword(MD5.getMD5(password));
		List<User> result = accountService.isRegisteredUser(loginBodyDTO);
		if (result != null && result.size() > 0) { // 是已登记的用户
			User user = result.get(0);
			String uuid = user.getUuid();
			String token = "Token-XXXXXXXX";
			String obj[] = new String[] { uuid, token };
			HashMap<String, Object> rsmap = resourceManagerService.response("loginhome", obj);
			return new ResponseEntity<Object>(rsmap, HttpStatus.CREATED);
		}

		return new ResponseEntity<Object>("/login", HttpStatus.NO_CONTENT);// 未授权用户

	}
	
	
	//短信验证
	@ApiOperation("注册页的确认")
	@ApiImplicitParams({
		@ApiImplicitParam(name="action", value ="action:checkDupTelNum",required=true,paramType="query",dataType="String"),
		@ApiImplicitParam(name="registrationBody", value ="用户数据 电话号码",required=true,paramType="body",dataType="RegistrationBodyDTO")
    })
	@ApiResponses({
		@ApiResponse(code=103,message="抱歉，此电话号码已注册"),
        @ApiResponse(code=200,message="OK")        
    })			
	@RequestMapping(value = "/registrations", method = RequestMethod.POST)
	public ResponseEntity<String> registrationpageId(@RequestParam String action,
			@Valid @RequestBody RegistrationBodyDTO registrationBody) {
		// passwd:abccd4djsN-999
		// CellPhone:13611442221
		String telnum = registrationBody.getTelnum();
		if ("checkDupTelNum".equals(action)) {
			List<UserDTO> userList = accountService.isRegisterredTel(registrationBody);
			if (userList != null && userList.size() > 0) {
				throw new UserException("103", "抱歉，此电话号码已注册");
			}
		} else {
			throw new UserException("104", "不支持此动作");
		}

		return new ResponseEntity<String>("/smsverificationpage?telnum=" + telnum, HttpStatus.CREATED);
	}

    //---code seprated by chenyuan----//
	
	/**
	 * 短信验证 初始页面
	 * 
	 * @param telnum手机号码
	 * @return
	 */
	@ApiOperation("短信验证 初始页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="用户的手机号码")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/smsverificationpage", method = RequestMethod.GET)
	public ResponseEntity<Map> smsverificationsres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum) {
		String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern telPattern = Pattern.compile(telRegExp);
		Matcher telMatcher = telPattern.matcher(telnum);
		if (!telMatcher.find()) {
			throw new UserException("102", "手机号格式不对");
		}
		String tel[] = new String[] { telnum };
		HashMap<String, Object> rsmap = resourceManagerService.response("smsverifications", tel);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}

	/**
	 * 短信验证 确认
	 * 
	 * @param telnum
	 * @param identifyingcode
	 * @return
	 */
	@ApiOperation("短信验证 确认按钮")
	@ApiImplicitParams({
		@ApiImplicitParam(name="verificationBody", value ="用户数据 手机号码/验证码",required=true,paramType="body",dataType="VerificationBodyDTO")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/smsverifications", method = RequestMethod.POST)
	public ResponseEntity<String> smsverificationpageId(
			@Valid @RequestBody VerificationBodyDTO verificationBody) {
		String telnum = verificationBody.getTelnum();
		String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
		Pattern telPattern = Pattern.compile(telRegExp);
		Matcher telMatcher = telPattern.matcher(telnum);
		if (!telMatcher.find()) {
			throw new UserException("102", "手机号格式不对");
		}
		String identifyingcode = verificationBody.getIdentifyingcode();
		String args[] = new String[] { telnum, identifyingcode };
		// 进行短信验证
		if (!accountService.doSmsVerification(verificationBody)) {
			throw new UserException("101", "输入验证码不正确");
		}

		return new ResponseEntity<String>(URL_HEAD + "/pwdsettingpage?telnum=" + telnum, HttpStatus.OK);
	}

	/**
	 * 添加银行卡 初始页面
	 * 
	 * @param telnum
	 * @return
	 */
	@ApiOperation("添加银行卡 初始页面")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="id",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="用户的手机号码")
	})
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/registrations/{id}/bankcards", method = RequestMethod.GET)
	public ResponseEntity<Map> bankcardsresId(
			@Valid @NotNull(message = "id不能为空") @PathVariable("id") String id,
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum) {
		String args[] = new String[] { id,telnum };
		HashMap<String, Object> rsmap = resourceManagerService.response("bankcards", args);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}

	/**
	 * 查看
	 * 
	 * @return
	 */
	@ApiOperation("银行卡查看")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/selectbanks", method = RequestMethod.GET)
	public ResponseEntity<String> selectbanks() {
		return new ResponseEntity<String>(URL_HEAD+"/banks?action='banks'", HttpStatus.OK);
	}

	/**
	 * 添加银行卡 下一步
	 * 
	 * @param telnum手机号
	 * @param bkcardnum银行卡号
	 * @param bkname银行名称
	 * @param name用户名
	 * @return
	 */
//	@ApiOperation("添加银行卡 下一步按钮")
//	@ApiImplicitParams({
//		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="用户的手机号码"),
//		@ApiImplicitParam(paramType="query",name="bankcardno",dataType="String",required=true,value="用户的银行卡号"),
//		@ApiImplicitParam(paramType="query",name="bankname",dataType="String",required=true,value="用户的银行名称"),
//		@ApiImplicitParam(paramType="query",name="name",dataType="String",required=true,value="用户名")
//	})
//	@ApiResponses({
//		@ApiResponse(code=200,message="OK"),
//		@ApiResponse(code=400,message="请求参数没填好"),
//		@ApiResponse(code=401,message="当前请求需要用户验证"),
//		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
//		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
//	})
//	@RequestMapping(value = "/bankcardverification", method = RequestMethod.GET)
//	public ResponseEntity<String> bankcardverification(
//			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum,
//			@Valid @NotNull(message = "银行卡不能为空") @Size(max = 20, min = 15) @RequestParam(value = "bankcardno") String bkcardnum,
//			@Valid @NotNull(message = "银行名不能为空") @Size(max = 20, min = 4) @RequestParam(value = "bankname") String bkname,
//			@Valid @NotNull(message = "用户名不能为空") @Size(max = 20, min = 2) @RequestParam(value = "name") String name) {
//		String args[] = new String[] { telnum, bkcardnum, bkname, name };
//		if (accountService.addBankCard(args)) {
//			return new ResponseEntity<String>(URL_HEAD+"/bankcardnext?action='bankcardnext'&telnum=" + telnum, HttpStatus.OK);
//		}
//		return new ResponseEntity<String>(URL_HEAD+"/bankcardverification?action='bankcardverification'&telnum=" + telnum, HttpStatus.UNAUTHORIZED);// 未授权用户
//	}

	/**
	 * 支持银行列表
	 * 
	 * @return
	 */
	@ApiOperation("支持银行列表")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/supportbanks", method = RequestMethod.GET)
	public ResponseEntity<Map> banksres() {
		HashMap<String, Object> rsmap = resourceManagerService.response("supportbanks", null);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}
	
	/**
	 * 密码设置 初始页面
	 * 
	 * @param telnum
	 * @return
	 */
	@ApiOperation("密码设置 初始页面")
	@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="用户的手机号码")
	@ApiResponses({
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	@RequestMapping(value = "/pwdsettingpage", method = RequestMethod.GET)
	public ResponseEntity<Map> pwdsettingres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号格式不对") @RequestParam(value = "telnum") String telnum) {
		String tel[] = new String[] { telnum };
		HashMap<String, Object> rsmap = resourceManagerService.response("pwdsettings", tel);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}
	
	/**
	 * 密码设置 确认
	 * 
	 * @param telnum
	 * @param pwdsetting
	 * @param pwdconfirm
	 * @return
	 */
	@ApiOperation("密码设置 确认按钮")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="path",name="id",dataType="String",required=true,value="id",defaultValue=""),
		@ApiImplicitParam(name="pwdSettingBody", value ="用户数据 手机号码/初始密码/确认密码",required=true,paramType="body",dataType="PwdSettingBodyDTO")
//		@ApiImplicitParam(paramType="query",name="telnum",dataType="String",required=true,value="用户的手机号码"),
//		@ApiImplicitParam(paramType="query",name="pwdsetting",dataType="String",required=true,value="用户的密码"),
//		@ApiImplicitParam(paramType="query",name="pwdconfirm",dataType="String",required=true,value="用户的确认密码")
	})
	@ApiResponses({
		@ApiResponse(code=101,message="手机号格式不对"),
		@ApiResponse(code=102,message="密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合"),
		@ApiResponse(code=103,message="两次输入密码需要一致"),
		@ApiResponse(code=200,message="OK"),
		@ApiResponse(code=400,message="请求参数没填好"),
		@ApiResponse(code=401,message="当前请求需要用户验证"),
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
	})
	//@RequestMapping(value = "/pwdsettingpage/{id}", method = RequestMethod.PATCH)
	public ResponseEntity<String> pwdsettingpageId(
			@Valid @NotNull(message="id不能为空") @PathVariable("id") String id,
			@Valid @RequestBody PwdSettingBodyDTO pwdSettingBody
//			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum,
//			@Valid @NotNull(message = "密码不能为空") @Size(min = 8, max = 16, message = "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合") @RequestParam(value = "pwdsetting") String pwdsetting,
//			@Valid @NotNull(message = "确认密码不能为空") @Size(min = 8, max = 16, message = "确认密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合") @RequestParam(value = "pwdconfirm") String pwdconfirm
			) {
		String telnum = pwdSettingBody.getTelnum();
		String pwdsetting = pwdSettingBody.getPassword();
		String pwdconfirm = pwdSettingBody.getPwdconfirm();
		if (accountService.isSettingPWD(pwdSettingBody)) { // 密码修正正确
			return new ResponseEntity<String>(URL_HEAD+"/loginpage?action='loginpage'&telnum=" + telnum, HttpStatus.OK);
		}
		return new ResponseEntity<String>(URL_HEAD+"/loginpage?action='loginpage'&telnum=" + telnum, HttpStatus.UNAUTHORIZED);// 未授权用户
	}
	
	
	@ApiImplicitParams({
		@ApiImplicitParam(paramType="query",name="size",dataType="Long",value="每页显示记录数",defaultValue="25"),
		@ApiImplicitParam(paramType="query",name="page",dataType="Long",value="显示页数（默认第0页开始）",defaultValue="0"),
		@ApiImplicitParam(paramType="query",name="sort",dataType="String",value="排序条件",defaultValue="id")
	})
	@RequestMapping(value = "/test", method = RequestMethod.GET)
	@AopPageResources
	public ResponseEntity<PageWrapper<BankCardDTO>> selectbankinfos(
			Pageable pageable,
			@RequestParam(value = "size") Long size,
			@RequestParam(value = "page",defaultValue="0") Long page,
			@RequestParam(value = "sort") String sort) {
		//page 默认第一页从0开始
		Page<BankCardDTO> pages = null;
		try {
			pages = bankCardService.selectBankCardById(pageable, 3L);
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}

		Map<String, Object> selfMap = new HashMap<String, Object>();
		Map<String, Object> self = new HashMap<String, Object>();
		selfMap.put("name", "test");
		selfMap.put("href", URL_HEAD + "/test");
		selfMap.put("describedBy", URL_HEAD + "/test.json");
		self.put("self", selfMap);

		Map<String, Object> executeMap = new HashMap<String, Object>();
		List<Map<String, Object>> executeList = new ArrayList<Map<String, Object>>();
		Map<String, Object> execute = new HashMap<String, Object>();
		executeMap.put("method", "POST");
		executeMap.put("name", "login");
		executeMap.put("href", URL_HEAD + "/loginpage");
		executeMap.put("describedBy", URL_HEAD + "/loginpage.json");
		executeList.add(executeMap);
		execute.put("related", executeList);
		if(pages==null){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		PageWrapper<BankCardDTO> pageWrapper = new PageWrapper<>(pages);
		pageWrapper.set_links(self);
		pageWrapper.set_links(execute);
		pageWrapper.setSort(pageable.getSort());
		return new ResponseEntity<>(pageWrapper,HttpStatus.OK);
	}
	
	@ApiOperation("用户退出")
	@ApiImplicitParam(name="loginBody", value ="用户数据 手机号码/密码",required=true,paramType="body",dataType="LoginBody")
	@ApiResponses({
		@ApiResponse(code=100,message="密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合"),
        @ApiResponse(code=101,message="手机号格式不对"),
        @ApiResponse(code=200,message="OK"),
        @ApiResponse(code=400,message="请求参数没填好"),
        @ApiResponse(code=401,message="未授权用户"),        				
		@ApiResponse(code=403,message="服务器已经理解请求，但是拒绝执行它"),
		@ApiResponse(code=404,message="请求路径没有或页面跳转路径不对")
        
    })
	@RequestMapping(value = "/logout", method = RequestMethod.POST)
	public ResponseEntity<String> logout(@Valid @RequestBody LoginBodyDTO loginBody){
			String telnum = loginBody.getTelnum();
			String password = loginBody.getPassword();
			if(telnum.length()!=11) {
				throw new UserException("100","电话长度必须是11位的数字");
			}
		    Pattern p = Pattern.compile(  
		            "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");  
		    Matcher m = p.matcher(password);  
		    if (m.find()) { //need pwd check  
		        //int kk=1;  
		    } else {
		    	throw new UserException("101","密码长度至少8位,至多20位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
		    }
		    
		    String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
			Pattern telPattern = Pattern.compile(telRegExp);
			Matcher telMatcher = telPattern.matcher(telnum);
			if (!telMatcher.find()) {
				throw new UserException("102", "手机号格式不对");
			}
		   
	         //passwd:abccd4djsN-999
		    //CellPhone:13611442221
	        
	      //  User targetuser = userRepository.findByCellPhoneAndPasswordHash(user.getCellPhone(),user.getPasswordHash());
			accountService.doLogout(loginBody);
		    
	        return new ResponseEntity<String>("/login",HttpStatus.NO_CONTENT);//未授权用户
	       	
    }
}