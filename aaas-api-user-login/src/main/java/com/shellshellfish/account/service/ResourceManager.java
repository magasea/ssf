package com.shellshellfish.account.service;

import java.util.HashMap;
import java.util.Map;

import javax.naming.ldap.Rdn;

import com.shellshellfish.account.model.InputItem;

//import com.shellshellfish.account.model.PageSite;

public class ResourceManager {

	public ResourceManager(){;}
	
	public HashMap<String ,Object> response(String pagename,String[] argv){
	    
		HashMap<String,Object> map=null;
	    if (pagename.equals("login")) {
	        String bankid=argv[0];
	    	map=logindesc();
	    }
	    else if (pagename.equals("register"))
	      map=registerdesc();
	    else if (pagename.equals("forgottenpwd"))
	      map=forgottenpwddesc();	
	    return map;
	}
	
	//for forgottenpwd url
	public HashMap<String,Object> forgottenpwddesc(){
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","forgottenpwd");
		
		rsmap.put("telnum","");
		rsmap.put("verificationcode","");
		rsmap.put("verifycodeget","");
		rsmap.put("pwdsetting",""); //for 密码设置
		
		HashMap<String,Object> linkitemmap=new HashMap<String,Object>();
		HashMap<String,Object> selfmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", "/api/forgottenpwd");
		selfitemmap.put("describedBy", "/api/forgottenpwd.json");
		linkitemmap.put("self",selfitemmap);
		
		HashMap<String,Object> relatedmap=new HashMap<String,Object>();
		HashMap<String,Object> relateditem1map=new HashMap<String,Object>();
		relateditem1map.put("href", "/api/verifycodeget");		
		relatedmap.put("verifycodeget",relateditem1map);
		linkitemmap.put("related", relatedmap);
		
		//for pwd setting button 
		
		HashMap<String,Object> registermap=new HashMap<String,Object>();
		HashMap<String,Object> registeritemmap=new HashMap<String,Object>();
		registeritemmap.put("href", "/api/pwdsetting");
		registeritemmap.put("describedBy", "/api/pwdsetting.json");
		registeritemmap.put("method", "POST");
		registermap.put("pwdsetting", registeritemmap);		 
		linkitemmap.put("execute", registermap);// post
		
		rsmap.put("_links", linkitemmap);
		return rsmap;
		
		
	}
	
	//for register url
	public HashMap<String,Object> registerdesc(){
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","register");
		//rsmap.put("title","注册");
		rsmap.put("telsuffix","");
		rsmap.put("telnum","");
		rsmap.put("agreement","");
		
		HashMap<String,Object> linkitemmap=new HashMap<String,Object>();
		HashMap<String,Object> selfmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", "/api/register");
		selfitemmap.put("describedBy", "/api/register.json");
		linkitemmap.put("self",selfitemmap);
		
		HashMap<String,Object> relatedmap=new HashMap<String,Object>();
		HashMap<String,Object> relateditem1map=new HashMap<String,Object>();
		relateditem1map.put("href", "/api/register");		
		relatedmap.put("agreement",relateditem1map);
		
		HashMap<String,Object> relateditem2map=new HashMap<String,Object>();
		relateditem2map.put("href", "/api/agreement");
		relatedmap.put("agreement",relateditem2map);		
		linkitemmap.put("related", relatedmap);
		
		HashMap<String,Object> executemap=new HashMap<String,Object>();
		HashMap<String,Object> registermap=new HashMap<String,Object>();
		HashMap<String,Object> registeritemmap=new HashMap<String,Object>();
		registeritemmap.put("href", "/api/smsverification");
		registeritemmap.put("describedBy", "/api/smsverification.json");
		registeritemmap.put("method", "POST");
		registermap.put("register", registeritemmap);
		 
		linkitemmap.put("execute", registermap);// post
		
		rsmap.put("_links", linkitemmap);
		
	    return rsmap;
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
		HashMap<String,Object> selfmap=new HashMap<String,Object>();
		HashMap<String,Object> selfitemmap=new HashMap<String,Object>();
		selfitemmap.put("href", "/api/login");
		selfitemmap.put("describedBy", "/api/login.json");
		linkitemmap.put("self",selfitemmap);
		
	    
	    HashMap<String,Object> relatedmap=new HashMap<String,Object>();
		HashMap<String,Object> relateditem1map=new HashMap<String,Object>();
		relateditem1map.put("href", "/api/register");
		//HashMap<String,Object> fastregismap=new HashMap<String,Object>();
		
		relatedmap.put("fastRegistration",relateditem1map);
		
		HashMap<String,Object> relateditem2map=new HashMap<String,Object>();
		relateditem2map.put("href", "/api/forgottenpwd");
		relatedmap.put("forgottenPwd",relateditem2map);		
		linkitemmap.put("related", relatedmap);
		
		
		HashMap<String,Object> loginmap=new HashMap<String,Object>();
		HashMap<String,Object> loginitemmap=new HashMap<String,Object>();
		loginitemmap.put("href", "/api/loginverify");
		loginitemmap.put("describedBy", "/api/loginverify.json");
		loginitemmap.put("method", "POST");
		loginmap.put("login", loginitemmap);
		//executemap.put("execute",loginmap); 
		linkitemmap.put("execute", loginmap);// post
		
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
	
	
}
