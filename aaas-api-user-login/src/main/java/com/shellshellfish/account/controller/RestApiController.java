package com.shellshellfish.account.controller;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
//import com.shellshellfish.account.Validation.Max;

import com.shellshellfish.account.commons.MD5;
import com.shellshellfish.account.exception.UserException;
import com.shellshellfish.account.model.PageSchema;
import com.shellshellfish.account.repositories.SmsVerificationRepositoryCustom;
import com.shellshellfish.account.repositories.SmsVerificationRepositoryCustomImpl;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.account.service.AccountService;
import com.shellshellfish.account.service.AccountServiceImpl;
import com.shellshellfish.account.service.ResourceManager;
import com.shellshellfish.account.service.SchemaManager;


@RestController
@RequestMapping("api")
@Validated
public class RestApiController {
  //public static final Logger logger = LoggerFactory.getLogger(RestApiController.class);

	@Autowired
	AccountService accountService;
	
	@Autowired
	ResourceManager resourceManager;
	
	@Autowired
	SchemaManager schemaManager;
		
	@Bean
	public ResourceManager resourceManager() {
		return new ResourceManager();
	}
	
	@Bean
	public SchemaManager schemaManager() {
		return new SchemaManager();
	}
	
	@Bean
	public SmsVerificationRepositoryCustom SmsVerificationRepositoryCustomImpl() {
		return new SmsVerificationRepositoryCustomImpl();
	}
	
	@Bean
	public AccountService accountService() {
		return new AccountServiceImpl();
	}
	
	/*
	@RequestMapping(value = "/register.json", method = RequestMethod.POST)
	public ResponseEntity<PageSchema> registerschema(
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		   PageSchema pageschema= schemaManager.getSchemafile("register");
		  //System.out.println(pagestr);
		  return new ResponseEntity<PageSchema>(pageschema, HttpStatus.OK);	
    }*/
	
	@RequestMapping(value = "/register", method = RequestMethod.GET)
	public ResponseEntity<Map> registerres( //register resource
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") String bankid
			@RequestParam(value = "telnum") String telnum
			){
		   
		  HashMap<String ,Object> rsmap= resourceManager.response("register",new String[]{telnum});
		  //System.out.println(pagestr);
		  return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }
	/**
	 * 密码设置 初始页面
	 * 
	 * @param telnum
	 * @return
	 */
	@RequestMapping(value = "/pwdsetting", method = RequestMethod.POST)
	public ResponseEntity<Map> pwdsettingres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号格式不对") @RequestParam(value = "telnum") String telnum) {
		String tel[] = new String[] { telnum };
		HashMap<String, Object> rsmap = resourceManager.response("pwdsetting", tel);
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
	@RequestMapping(value = "/pwdconfirm", method = RequestMethod.POST)
	public ResponseEntity<String> pwdconfirm(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum,
			@Valid @NotNull(message = "密码不能为空") @Size(min = 8, max = 16, message = "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合") @RequestParam(value = "pwdsetting") String pwdsetting,
			@Valid @NotNull(message = "密码不能为空") @Size(min = 8, max = 16, message = "密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合") @RequestParam(value = "pwdconfirm") String pwdconfirm) {
		if (accountService.isSettingPWD(telnum, pwdsetting, pwdconfirm)) { // 密码修正正确
			return new ResponseEntity<String>("/api/login?telnum=" + telnum, HttpStatus.OK);
		}
		return new ResponseEntity<String>("/api/login?telnum=" + telnum, HttpStatus.UNAUTHORIZED);// 未授权用户
	}
	
	//忘记密码下的获取验证码
	
	@RequestMapping(value = "/verifycodeget", method = RequestMethod.GET)
	public ResponseEntity<HttpStatus> verifycodeget(
			@Valid @NotNull(message="电话不能为空")  @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum
			){			
			
		  //nedd alisms interface to get sms
		  //send  sms to telphone 
		return new ResponseEntity<HttpStatus>(HttpStatus.OK);	
    }


	//忘记密码下的密码设置
	@RequestMapping(value = "/topwdsetting", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> topwdsetting(
			@Valid @NotNull(message="电话不能为空") @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum,
			@RequestParam(value = "verfiedcode") String verfiedcode)
			{
		
		if (accountService.isSmsVerified(telnum,verfiedcode))
			return new ResponseEntity<HttpStatus>(HttpStatus.OK);
		
		return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);
	}
	
	@RequestMapping(value = "/forgottenpwd", method = RequestMethod.GET)
	public ResponseEntity<Map> forgottenpwdres(@RequestParam(value = "telnum") String telnum){			
			
		   
		  HashMap<String ,Object> rsmap= resourceManager.response("forgottenpwd",new String[]{telnum});
		  
		  return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }

	/*
	// login schema
	@RequestMapping(value = "/login.json", method = RequestMethod.GET)
	public ResponseEntity<PageSchema> loginschema(
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") Integer bankid
			){
		   
		  PageSchema ps= schemaManager.getSchemafile("login");
		  //System.out.println(pagestr);
		  return new ResponseEntity<PageSchema>(ps, HttpStatus.OK);	
    }
   */
	
	@RequestMapping(value = "/loginverify", method = RequestMethod.POST)
	public ResponseEntity<HttpStatus> loginveirfy(
			//@Valid @NotNull(message="电话不能为空") @Max(value=20) @Min(value=1) @RequestParam(value = "id") String bankid
			@Valid @NotNull(message="电话不能为空") @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum,
			@Valid @NotNull(message="密码不能为空") @Size(min = 8, max = 16,message="密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合")  @RequestParam(value = "password") String pwd
			){
		   
		    Pattern p = Pattern.compile(  
		            "^(?![A-Za-z]+$)(?![A-Z\\d]+$)(?![A-Z\\W]+$)(?![a-z\\d]+$)(?![a-z\\W]+$)(?![\\d\\W]+$)\\S{8,20}$");  
		    Matcher m = p.matcher(pwd);  
		    if (m.find()) { //need pwd check  
		        //int kk=1;  
		    } else {
		    	throw new UserException("100","密码长度至少8位,至多16位，必须是字母 大写、字母小写、数字、特殊字符中任意三种组合");
		    }
		    
		    String telRegExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
			Pattern telPattern = Pattern.compile(telRegExp);
			Matcher telMatcher = telPattern.matcher(telnum);
			if (!telMatcher.find()) {
				throw new UserException("101", "手机号格式不对");
			}
		   
	         //passwd:abccd4djsN-999
		    //CellPhone:13611442221
	        
	      //  User targetuser = userRepository.findByCellPhoneAndPasswordHash(user.getCellPhone(),user.getPasswordHash());
	        if (accountService.isRegisteredUser(telnum, MD5.getMD5(pwd))) { // 是已登记的用户
		       return new ResponseEntity<HttpStatus>(HttpStatus.OK);
	        }
		    
	        return new ResponseEntity<HttpStatus>(HttpStatus.UNAUTHORIZED);//未授权用户
	       	
    }
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public ResponseEntity<Map> loginres(@RequestParam(value = "telnum") String telnum)
	{
		   
		    HashMap<String ,Object> rsmap= resourceManager.response("login",new String[]{telnum});
		    return new ResponseEntity<Map>(rsmap, HttpStatus.OK);	
    }
	
	@RequestMapping(value = "/tosmsverification", method = RequestMethod.POST)
	public ResponseEntity<String> tosmsverification(			
			@Valid @NotNull(message="电话不能为空") @Size(min = 11, max = 11,message="电话长度必须是11位的数字")  @RequestParam(value = "telnum") String telnum
			){
	         //passwd:abccd4djsN-999
		    //CellPhone:13611442221
	        
	       boolean flag= accountService.isRegisterredTel(telnum);
	       if (flag==true) {
	    	   throw new UserException("100","抱歉，此电话号码已注册");
	       }
	       
	       return new ResponseEntity<String>("/smsverification?telnum="+telnum,HttpStatus.OK);//注册OK,return target url
	       	
    }

	/**
	 * 短信验证 初始页面
	 * 
	 * @param telnum手机号码
	 * @return
	 */
	@RequestMapping(value = "/smsverification", method = RequestMethod.POST)
	public ResponseEntity<Map> smsverificationres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "Mobile") String telnum) {
		String tel[] = new String[] { telnum };
		HashMap<String, Object> rsmap = resourceManager.response("smsverification", tel);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}

	/**
	 * 短信验证 确认
	 * 
	 * @param telnum
	 * @param identifyingcode
	 * @return
	 */
	@RequestMapping(value = "/smsverconfirm", method = RequestMethod.POST)
	public ResponseEntity<String> smsverconfirm(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum,
			@Valid @NotNull(message = "电话不能为空") @Size(max = 6, min = 6, message = "验证码长度必须为6位") @RequestParam(value = "verificationcode") String identifyingcode) {
		String args[] = new String[] { telnum, identifyingcode };

		// HashMap<String, Object> rsmap =
		// resourceManager.response("smsverification",args);
		if (!"123456".equals(identifyingcode)) {
			throw new UserException("101", "输入验证码不正确");
		}

		return new ResponseEntity<String>("/api/pwdsetting?telnum=" + telnum, HttpStatus.OK);
	}

	/**
	 * 添加银行卡 初始页面
	 * 
	 * @param telnum
	 * @return
	 */
	@RequestMapping(value = "/addbankcard", method = RequestMethod.POST)
	public ResponseEntity<Map> addbankcardres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum) {
		String args[] = new String[] { telnum };
		HashMap<String, Object> rsmap = resourceManager.response("addbankcard", args);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}

	/**
	 * 查看
	 * 
	 * @return
	 */
	@RequestMapping(value = "/viewbklist", method = RequestMethod.POST)
	public ResponseEntity<String> checkbklistres() {
		return new ResponseEntity<String>("/api/bklist", HttpStatus.OK);
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
	@RequestMapping(value = "/bkverrify", method = RequestMethod.POST)
	public ResponseEntity<String> addbankcardnextres(
			@Valid @NotNull(message = "电话不能为空") @Size(max = 11, min = 11, message = "手机号长度必须是11位的数字") @RequestParam(value = "telnum") String telnum,
			@Valid @NotNull(message = "银行卡不能为空") @Size(max = 20, min = 15) @RequestParam(value = "bankcardno") String bkcardnum,
			@Valid @NotNull(message = "银行名不能为空") @Size(max = 20, min = 4) @RequestParam(value = "bankname") String bkname,
			@Valid @NotNull(message = "用户名不能为空") @Size(max = 20, min = 2) @RequestParam(value = "name") String name) {
		String args[] = new String[] { telnum, bkcardnum, bkname, name };
		if (accountService.addBankCard(args)) {
			return new ResponseEntity<String>("/api/bknext?telnum=" + telnum, HttpStatus.OK);
		}
		return new ResponseEntity<String>("/api/bknext?telnum=" + telnum, HttpStatus.UNAUTHORIZED);// 未授权用户
	}

	/**
	 * 支持银行列表
	 * 
	 * @return
	 */
	@RequestMapping(value = "/bklist", method = RequestMethod.POST)
	public ResponseEntity<Map> viewbklistres() {
		HashMap<String, Object> rsmap = resourceManager.response("bklist", null);
		return new ResponseEntity<Map>(rsmap, HttpStatus.OK);
	}
	
	
	
}
