package com.shellshellfish.aaas.account.service;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceManager {
	public static String URL_HEAD="/api/useraccount";
	public ResourceManager(){;}
	
	public HashMap<String ,Object> response(String pagename,String[] argv){
	    
		HashMap<String, Object> map = null;
		if (pagename.equals("login")) {
			map = logindesc();
		} else if (pagename.equals("register")) {
			String telnum=argv[0];
			map = registerdesc(telnum);
		} else if (pagename.equals("forgottenpwd")) {
			String telnum=argv[0];
			map = forgottenpwddesc(telnum);
		} else if (pagename.equals("pwdsettings")) {
			map = pwdSettingDesc(argv);
		} else if (pagename.equals("smsverifications")) {
			map = smsVerificationDesc(argv);
		} else if (pagename.equals("bankcards")) {
			map = addBankCardDesc(argv);
		} else if (pagename.equals("supportbanks")) {
			map = bkListDesc();
		}
		return map;
	}
	
	//for forgottenpwd url
	public HashMap<String,Object> forgottenpwddesc(String telnum){
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","forgottenpwd");
		
		if (telnum==null)
			rsmap.put("telnum","");
		else
			rsmap.put("telnum",telnum);
			
		rsmap.put("verificationcode","");
		rsmap.put("verifycodeget","");
		rsmap.put("pwdsetting",""); //for 密码设置
		
		HashMap<String,Object> linkitemmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", URL_HEAD+"/pwdforgettingpage?telnum="+telnum);
		selfitemmap.put("describedBy", URL_HEAD+"/pwdforgettingpage.json?telnum="+telnum);
		linkitemmap.put("self",selfitemmap);
		
		HashMap<String,Object>[] relateditem1map=new HashMap[1];
		relateditem1map[0]=new HashMap<String,Object>();
		relateditem1map[0].put("href", URL_HEAD+"/verifycode?telnum="+telnum);	
		relateditem1map[0].put("name", "verifycode?telnum="+telnum);
		linkitemmap.put("related", relateditem1map);
		
		//for pwd setting button 
		HashMap<String,Object>[] registeritemmap=new HashMap[1];
		registeritemmap[0]=new HashMap<String,Object>();
		registeritemmap[0].put("href", URL_HEAD+"/registrations?registrationBody={\"identifyingcode\":\"\",\"telnum\":\"\"}"); // for password setting button
		registeritemmap[0].put("describedBy", URL_HEAD+"/registrations.json");
		registeritemmap[0].put("method", "POST");
		registeritemmap[0].put("name", "pwdsettingpage");
		linkitemmap.put("execute", registeritemmap);// post
		
		rsmap.put("_links", linkitemmap);
		return rsmap;
	}
	
	//for register url
	public HashMap<String,Object> registerdesc(String telnum){
		HashMap<String,Object> rsMap= new HashMap<String,Object>();
		HashMap<String, Object>[] mapArray = new HashMap[2];
		mapArray[0] = new HashMap<String,Object>();
		mapArray[0].put("name","register");
		//rsmap.put("title","注册");
		mapArray[0].put("telsuffix","");
		
		if (telnum==null)
		   mapArray[0].put("telnum","");
		else
		   mapArray[0].put("telnum",telnum);
		
		mapArray[0].put("agreement","");
		
		HashMap<String,Object> linkitemmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", URL_HEAD+"/registrationpage?telnum="+telnum);
		selfitemmap.put("describedBy", URL_HEAD+"/registrationpage.json?telnum="+telnum);
		linkitemmap.put("self",selfitemmap);
		
		HashMap<String,Object>[] relateditemmap=new HashMap[1];
		relateditemmap[0] = new HashMap<String,Object>();
		relateditemmap[0].put("href",  URL_HEAD+"/agreement");
		relateditemmap[0].put("name", "agreement");
		linkitemmap.put("related", relateditemmap);
		
		HashMap<String,Object>[] registeritemmap=new HashMap[1];
		registeritemmap[0] = new HashMap<String,Object>();
		registeritemmap[0].put("href", URL_HEAD+"/registrations?action='checkDupTelNum' & registrationBody={\"telnum\":\""+telnum+"\"}");
		registeritemmap[0].put("describedBy", URL_HEAD+"/registrations.json");
		registeritemmap[0].put("method", "POST");
		registeritemmap[0].put("name", "register");
		 
		linkitemmap.put("execute", registeritemmap);// post
		
		mapArray[0].put("_links", linkitemmap);
		/////////////////////////////////////////
		mapArray[1] = new HashMap<String,Object>();
		mapArray[1].put("name","register");
		//rsmap.put("title","注册");
		mapArray[1].put("telsuffix","");
		
		if (telnum==null)
		   mapArray[1].put("telnum","");
		else
		   mapArray[1].put("telnum",telnum);
		
		mapArray[1].put("agreement","");
		
		linkitemmap=new HashMap<String,Object>();
		selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", URL_HEAD+"/registrationpage?telnum="+telnum);
		selfitemmap.put("describedBy", URL_HEAD+"/registrationpage.json?telnum="+telnum);
		linkitemmap.put("self",selfitemmap);
		
		HashMap<String,Object>[] relateditemmap2=new HashMap[1];
		relateditemmap2[0] = new HashMap<String,Object>();
		relateditemmap2[0].put("href", URL_HEAD+"/agreement");
		relateditemmap2[0].put("name", "agreement");
		linkitemmap.put("related", relateditemmap2);
		
		HashMap<String,Object>[] registeritemmap2=new HashMap[1];
		registeritemmap2[0] = new HashMap<String,Object>();
		registeritemmap2[0].put("href", URL_HEAD+"/registrations?action='checkDupTelNum' & registrationBody={\"telnum\":\""+telnum+"\"}");
		registeritemmap2[0].put("describedBy", URL_HEAD+"/registrations.json");
		registeritemmap2[0].put("method", "POST");
		registeritemmap2[0].put("name", "register");
		linkitemmap.put("execute", registeritemmap2);// post
		mapArray[1].put("_links", linkitemmap);
		
		rsMap.put("items", mapArray);
		rsMap.put("_total", "2");
	    return rsMap;
	}
	
	//for login url
	public HashMap<String,Object> logindesc(){
		//login resource
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","login");
		//rsmap.put("title","登录");
		
		rsmap.put("telnum","");
			
		rsmap.put("password","");
		//rsmap.put("describedby", "/api/login.json");
		
		HashMap<String,Object> linkitemmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", URL_HEAD+"/loginpage");
		selfitemmap.put("describedBy", URL_HEAD+"/loginpage.json");
		selfitemmap.put("name", "login");
		linkitemmap.put("self",selfitemmap);
	    
		HashMap<String,Object>[] relateditemmap=new HashMap[2];
		relateditemmap[0]=new HashMap<String,Object>();
		relateditemmap[0].put("name", "fastRegistration");
		relateditemmap[0].put("href", URL_HEAD+"/registrationpage?telnum="+"''");
		
		relateditemmap[1]=new HashMap<String,Object>();
		relateditemmap[1].put("name", "forgottenPwd");
		relateditemmap[1].put("href", URL_HEAD+"/pwdsettingpage?telnum="+"''");
		linkitemmap.put("related", relateditemmap);
		
		HashMap<String,Object>[] loginitemmap=new HashMap[1];
		loginitemmap[0]=new HashMap<String,Object>();
		loginitemmap[0].put("name", "login");
		loginitemmap[0].put("href", URL_HEAD+"/loginverify");
		loginitemmap[0].put("describedBy", URL_HEAD+"/loginverify.json");
		loginitemmap[0].put("method", "POST");
		//executemap.put("execute",loginmap); 
		linkitemmap.put("execute", loginitemmap);// post
		
		rsmap.put("_links", linkitemmap);
		return rsmap;
		/*
	     //editor "tel,pwd"
	    HashMap<String,Object> editormap=new HashMap<String,Object>();
	    HashMap<String,Object>[] emap= new HashMap[2];
	    HashMap<String,Object> emap1= new HashMap<String,Object>();
	    emap1.put("name", "telnum");
	    HashMap<String,Object> emap2= new HashMap<String,Object>();
	    emap2.put("name", "password");
	    emap[0]=emap1;
	    emap[1]=emap2;
	    //editormap.put("name", "telphone");	    
	    rsmap.put("telnum", emap[0]);
	    rsmap.put("password", emap[1]);
	    //link reg,forgottnpwd
	    HashMap<String,Object> linkmap=new HashMap<String,Object>();
	    HashMap<String,Object>[] hmap= new HashMap[2];
	    HashMap<String,Object> hmap1= new HashMap<String,Object>();
	    
	    hmap1.put("name", "registration");
	    hmap1.put("href", "/api/register");
	    hmap1.put("describedby", "/api/registration.json");
	    
	    HashMap<String,Object> hmap2= new HashMap<String,Object>();
	    
	    hmap2.put("name", "forgottenpwd");
	    hmap2.put("href", "/api/forgottenpwd");
	    hmap2.put("describedby", "/api/pwd/forgottenpwd.json");
	    
	    hmap[0]=hmap1;
	    hmap[1]=hmap2;	    
	    //linkmap.put("name", "[快速注册,忘记密码 ?]");
	    rsmap.put("registration", hmap[0]);
	    rsmap.put("forgottenpwd", hmap[1]);
	    
	    //button register
	    HashMap<String,Object> buttonmap=new HashMap<String,Object>();
	    buttonmap.put("name", "loginclick");
	    rsmap.put("loginclick", buttonmap);
	   */
	    
	    
	}
	
	// for pwdsetting url
	public HashMap<String, Object> pwdSettingDesc(String[] telnum) {
		HashMap<String, Object> rsmap = new HashMap<String, Object>();
		Pattern regExp = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
		Matcher m = regExp.matcher(telnum[0]);
		if (!m.find()) {
			rsmap.put("status", "手机号格式不正确");
			return rsmap;
		}
		// pwdsetting resource
		rsmap.put("name", "pwdsettings");
		// rsmap.put("title", "密码设置");
		rsmap.put("pwdsetting", "");
		rsmap.put("pwdconfirm", "");
		rsmap.put("telnum", telnum[0]);

		HashMap<String, Object> linkitemmap = new HashMap<String, Object>();
		HashMap<String, Object> selfitemmap = new HashMap<String, Object>();
		selfitemmap.put("href", URL_HEAD+"/pwdsettingpage?telnum="+telnum[0]);
		selfitemmap.put("describedBy", URL_HEAD+"/pwdsettingpage.json?telnum="+telnum[0]);
		//selfitemmap.put("name", "pwdsettings");
		linkitemmap.put("self", selfitemmap);

		HashMap<String, Object>[] loginitemmap = new HashMap[1];
		loginitemmap[0] = new HashMap<String, Object>();
		loginitemmap[0].put("href", URL_HEAD+"/registrations");
		loginitemmap[0].put("describedBy", URL_HEAD+"/registrations?registrationBody={\"identifyingcode\":\"\",\"telnum\":\"\"}"); // for password setting button
		loginitemmap[0].put("method", "POST");
		loginitemmap[0].put("name","pwdsetting");
		linkitemmap.put("execute", loginitemmap);// post

		rsmap.put("_links", linkitemmap);
		return rsmap;
	}

	// for smsverification url
	public HashMap<String, Object> smsVerificationDesc(String[] telnum) {
		// smsverification resource
		HashMap<String, Object> rsmap = new HashMap<String, Object>();
		/// =====start======///
		Pattern regExp = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
		Matcher m = regExp.matcher(telnum[0]);
		if (!m.find()) {
			rsmap.put("status", "手机号格式不正确");
			return rsmap;
		}
		/// =====end=======///

		rsmap.put("name", "smsverifications");
		// rsmap.put("title", "短信验证");
		rsmap.put("smssuffix", "");
		rsmap.put("telnum", telnum[0]);
		rsmap.put("identifyingcode", "");

		HashMap<String, Object> linkitemmap = new HashMap<String, Object>();
		HashMap<String, Object> selfitemmap = new HashMap<String, Object>();
		selfitemmap.put("href", URL_HEAD+"/smsverificationpage?telnum="+telnum[0]);
		selfitemmap.put("describedBy", URL_HEAD+"/smsverificationpage.json?telnum="+telnum[0]);
		linkitemmap.put("self", selfitemmap);

		HashMap<String, Object> relatedmap = new HashMap<String, Object>();
		HashMap<String, Object>[] relateditem1map = new HashMap[1];
		relateditem1map[0] = new HashMap<String, Object>();
		relateditem1map[0].put("href", URL_HEAD+"/resendmsg?telnum="+telnum[0]);
		relateditem1map[0].put("name", "resendidentifycode");
		linkitemmap.put("related", relateditem1map);

		HashMap<String, Object>[] smsitemmap = new HashMap[1];
		smsitemmap[0] = new HashMap<String, Object>();
		smsitemmap[0].put("href", URL_HEAD+"/smsverifications?verificationBody={\"identifyingcode\":\"\",\"telnum\":\""+telnum[0]+"\"}");
		smsitemmap[0].put("describedBy", URL_HEAD+"/smsverifications.json");
		smsitemmap[0].put("method", "POST");
		smsitemmap[0].put("name", "conform");
		linkitemmap.put("execute", smsitemmap);// post

		rsmap.put("_links", linkitemmap);
		return rsmap;
	}

	// for addbankcard url
	public HashMap<String, Object> addBankCardDesc(String[] args) {
		// addbankcard resource
		HashMap<String, Object> rsmap = new HashMap<String, Object>();

		/// =====start======///
		Pattern regExp = Pattern.compile("^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$");
		Matcher m = regExp.matcher(args[1]);
		if (!m.find()) {
			rsmap.put("status", "手机号格式不正确");
			return rsmap;
		}
		/// =====end=======///

		rsmap.put("name", "bankcards");
		// rsmap.put("title", "短信验证");
		rsmap.put("bankcardnumber", "");
		rsmap.put("supportedcard ", "");
		rsmap.put("telnum ", args[1]);

		HashMap<String, Object> linkitemmap = new HashMap<String, Object>();
		HashMap<String, Object> selfitemmap = new HashMap<String, Object>();
		selfitemmap.put("href", URL_HEAD + "/registerations/" + args[0] + "/bankcards?telnum=" + args[1]);
		selfitemmap.put("describedBy", URL_HEAD + "/registerations/" + args[0] + "/bankcards.json?telnum=" + args[1]);
		linkitemmap.put("self", selfitemmap);

		HashMap<String, Object>[] relateditem1map = new HashMap[1];
		relateditem1map[0] = new HashMap<String, Object>();
		relateditem1map[0].put("href", URL_HEAD+"/selectbanks");
		relateditem1map[0].put("name", "view");
		linkitemmap.put("related", relateditem1map);

		HashMap<String, Object>[] smsitemmap = new HashMap[1];
		smsitemmap[0] = new HashMap<String, Object>();
		smsitemmap[0].put("href", URL_HEAD+"/bankcardverification");
		smsitemmap[0].put("describedBy", URL_HEAD+"/bankcardverification.json");
		smsitemmap[0].put("method", "GET");
		smsitemmap[0].put("name", "next");
		linkitemmap.put("execute", smsitemmap);// post

		rsmap.put("_links", linkitemmap);
		return rsmap;
	}

	// for bklist url
	public HashMap<String, Object> bkListDesc() {
		// bklist resource
		HashMap<String, Object> rsmap = new HashMap<String, Object>();
		HashMap<String, Object>[] arrayMap = new HashMap[2];
		arrayMap[0] = new HashMap<String, Object>();
		arrayMap[0].put("bank", "工商银行");
		
		arrayMap[1] = new HashMap<String, Object>();
		arrayMap[1].put("bank", "广发银行");
		rsmap.put("name", "bklist");
		rsmap.put("bank", arrayMap);
		rsmap.put("_total", "2");
		return rsmap;
	}
	
}
