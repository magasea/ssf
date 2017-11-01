package com.shellshellfish.account.service;

import java.util.HashMap;
import java.util.Map;

import com.shellshellfish.account.model.EditorItem;

//import com.shellshellfish.account.model.PageSite;

public class ResourceManager {

	public ResourceManager(){;}
	
	public HashMap<String ,Object> describe(String pagename){
	    
		HashMap<String,Object> map=null;
	    if (pagename.equals("login"))
	      map=logindesc();
	    else if (pagename.equals("register"))
	      map=registerdesc();
	        
	    return map;
	}
	
	//for register url
	public HashMap<String,Object> registerdesc(){
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","register");
		rsmap.put("href","/api/register");
		rsmap.put("describedby", "/api/register.json");
		
		
		//label "registration,tel"
	    
	    HashMap<String,Object>[] emap= new HashMap[2];
	    HashMap<String,Object> emap1= new HashMap<String,Object>();
	    emap1.put("name", "registration");
	    emap1.put("value","注册");
	    
	    HashMap<String,Object> emap2= new HashMap<String,Object>();
	    emap2.put("name", "telsuffix");
	    //emap2.put("value","中国大陆 +86");
	    
	    /*
	    HashMap<String,Object> emap3= new HashMap<String,Object>();
	    emap3.put("name", "agreement");
	    emap3.put("value","我已阅读并同意<贝贝鱼用户协议>");
	    */
	    emap[0]=emap1;
	    emap[1]=emap2;
	    //emap[2]=emap3;
	    //editormap.put("name", "telphone");	    
	    rsmap.put("registration", emap[0]);
	    rsmap.put("telsuffix", emap[1]);
	    
	    //link agreement
	    HashMap<String,Object> linkmap=new HashMap<String,Object>();
	    
	    linkmap.put("name", "agreement");
	    //linkmap.put("value","我已阅读并同意<贝贝鱼用户协议>");
	    linkmap.put("href", "/agreement.json");
	    //linkmap.put("describedby", "/api/registration.json");
	    rsmap.put("agreement", linkmap);
	    
	    //editor 
	    HashMap<String,Object> editormap=new HashMap<String,Object>();
	    editormap.put("name", "telnum");
	    rsmap.put("telnum", editormap);
	    
	    //register link
	    HashMap<String,Object> buttonmap=new HashMap<String,Object>();
	    buttonmap.put("name", "register");
	    buttonmap.put("href", "/api/regaction?");
	    buttonmap.put("describedby", "/api/afterregister.json");
	    
	    rsmap.put("register", buttonmap);
	    
	    return rsmap;
	}
	
	//for login url
	public HashMap<String,Object> logindesc(){
		//caption
		HashMap<String,Object> rsmap= new HashMap<String,Object>();
		rsmap.put("name","login");
		rsmap.put("href","/api/login");
		rsmap.put("describedby", "/api/login.json");
		
		//label "login"
		HashMap<String,Object> labelmap=new HashMap<String,Object>();
	    labelmap.put("name", "login");
	    rsmap.put("login", labelmap);
	    
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
	    
	    
	    return rsmap;
	}
	
	
}
