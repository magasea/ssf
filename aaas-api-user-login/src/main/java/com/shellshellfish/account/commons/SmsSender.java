package com.shellshellfish.account.commons;


import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;

public class SmsSender {
	  public static final Logger logger = LoggerFactory.getLogger(SmsSender.class);

	public static String sendVcode(String telnum) {
		RestTemplate restTemplate = new RestTemplate();
		
		String url = "http://222.73.117.156/msg/HttpBatchSendSM?";// 应用地址
		String account = "shanjian_zntg";// 账号
		String pswd = "Zhinengtougu123";// 密码
		String mobile = telnum;// 手机号码，多个号码使用","分割
		String msg = "您好，您的验证码是123456";// 短信内容
		
		url=url+setParamString(account,pswd,mobile,msg,null);
		String responsemsg = restTemplate.getForObject(url, String.class);
	    return responsemsg; 
	}
	
	public static String setParamString(String account,String password,String mobile,String msg,String extrano) {
		String paramstr="";
		paramstr=paramstr+"account="+account+'&';
		paramstr=paramstr+"pswd="+password+'&';
		paramstr=paramstr+"mobile="+mobile+'&';
		paramstr=paramstr+"needstatus=true&";
		String xmlString="";
		try {
		    
			xmlString = new String(msg.toString().getBytes("UTF-8"));
		}catch (UnsupportedEncodingException  e) {
			String logstr="'"+msg+"'"+"UTF-8编码失败";
			logger.debug(logstr);
			
			
		}
		paramstr=paramstr+"msg="+xmlString+'&';
		paramstr=paramstr+"extrano=";
		return paramstr;
	}
}
