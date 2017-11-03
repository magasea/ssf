package com.shellshellfish.account.service;

import java.util.HashMap;

import com.shellshellfish.account.model.ButtonItem;
import com.shellshellfish.account.model.EditorItem;
import com.shellshellfish.account.model.LinkItem;
import com.shellshellfish.account.model.PageSchema;


public class SchemaManager {

	public SchemaManager(){;}
	
	
	
	public  PageSchema getSchemafile(String schemaname) {
		if (schemaname.equals("login"))
			return loginSchema();
		else if (schemaname.equals("register"))
			return registerSchema();
		
		return null;
	}
	
	public PageSchema loginSchema() {
        PageSchema ps= new PageSchema();
		
		//PageItem PageItem= new PageItem();
		HashMap<String ,Object> map =new HashMap(); 
		map.put("$schema","http://www.shellfishfish.com/api/schema#");
		map.put("title","登录");
		map.put("description", "用户登录页");
		map.put("_schemaVersion", "0.1.1");
		map.put("_serviceId","960010ee-1c86-431c-97c3-160d5d782731");
		//self description
		
		//for title
		EditorItem titleitem= new EditorItem();
		titleitem.setName("login");
		titleitem.setTitle("登录");
		titleitem.setDescription("login label");
		//titleitem.setSelfLink(null);
		titleitem.setType("String");
		titleitem.setEditable(false);
		//titleitem.setAction(null);
		titleitem.setOrdinal("1");		
		map.put("login", titleitem); 
		
		//for telphone ,pwd editor
		EditorItem[] telitems= new EditorItem[2];
		//telphone
		telitems[0] =new EditorItem();
		telitems[0].setName("telnum");
		telitems[0].setTitle("手机号码");
		telitems[0].setDescription("telphone editor");
		//edititems[0].setSelfLink(null);
		telitems[0].setType("String");
		//edititems[0].setAction(null);
		telitems[0].setOrdinal("2");
		//edititems[0].setValidflag(true); //phone: size check
		telitems[0].setEditable(true);
		telitems[0].setMinlength(11); //length check
		telitems[0].setMaxlength(11);
		
		telitems[1] =new EditorItem();
		telitems[1].setName("password");
		telitems[1].setTitle("登录密码");
		telitems[1].setDescription("password editor");
		//edititems[0].setSelfLink(null);
		telitems[1].setType("String");
		//edititems[0].setAction(null);
		telitems[1].setOrdinal("3");
		//telitems[1].setValidflag(true); //pwd: password check
		//edititems[0].setValidflag(true); //phone: size check
		telitems[1].setEditable(true);
		telitems[1].setMinlength(6); //length check
		telitems[1].setMaxlength(16);
		
		map.put("telnum", telitems[0]);
		map.put("password", telitems[1]);
		
		//link 
		LinkItem[] linkitems= new LinkItem[2];
		linkitems[0]= new LinkItem();
		linkitems[0].setName("registration");
		linkitems[0].setTitle("快速注册");
		linkitems[0].setDescription("registration link");
		linkitems[0].setHref("/api/register");
		linkitems[0].setType("link");
		//linkitems[0].setAction("login?username='11'&pwd='111'");
		linkitems[0].setOrdinal("4");
		
		linkitems[1]= new LinkItem();
		linkitems[1].setName("forgottenpwd");
		linkitems[1].setTitle("忘记密码 ?");
		linkitems[1].setDescription("pwd forgotten link");
		linkitems[1].setHref("/api/pwdforgotten");		
		linkitems[1].setType("link");
		//linkitems[0].setAction("login?username='11'&pwd='111'");
		linkitems[1].setOrdinal("5");		
		
		map.put("registration", linkitems[0]);
		map.put("forgottenpwd", linkitems[1]);
		
		//for login button
		ButtonItem loginbtnitem= new ButtonItem();
		loginbtnitem.setName("loginclick");
		loginbtnitem.setTitle("登录");
		loginbtnitem.setDescription("login link");
		//loginbtnitem.setSelfLink(null);
		loginbtnitem.setType("link");
		loginbtnitem.setAction("/api/loginaction?");
		loginbtnitem.setClickable(true);
		loginbtnitem.setOrdinal("6");		
		map.put("loginclick", loginbtnitem); 
						
		ps.setProperties(map);
		return ps;

	}
	
	//register page
	public PageSchema registerSchema() {
        PageSchema ps= new PageSchema();
		
		//PageItem PageItem= new PageItem();
		HashMap<String ,Object> map =new HashMap(); 
		map.put("$schema","http://www.shellfishfish.com/api/schema#");
		map.put("title","register");
		map.put("description", "用户注册页");
		map.put("_schemaVersion", "0.1.1");
		map.put("_serviceId","960010ee-1c86-431c-97c3-160d5d782731");
		//self description
		
		//for 注册title
		EditorItem titleitem= new EditorItem();
		titleitem.setName("registration");
		titleitem.setTitle("注册");
		titleitem.setDescription("register label");
		//titleitem.setSelfLink(null);
		titleitem.setType("String");
		titleitem.setEditable(false);
		//titleitem.setAction(null);
		titleitem.setOrdinal("1");		
		map.put("registration", titleitem); 
		
		//for telsuffix,telphone  editor
		EditorItem[] telitems= new EditorItem[2];
		//tel suffix
		telitems[0]=new EditorItem();
		telitems[0].setName("telsuffix");
		telitems[0].setTitle("中国大陆 +86");
		telitems[0].setDescription("telphone suffix editor");
		//edititems[0].setSelfLink(null);
		telitems[0].setType("String");
		//edititems[0].setAction(null);
		telitems[0].setOrdinal("2");
		//edititems[0].setValidflag(true); //phone: size check
		map.put("telsuffix", telitems[0]);
		
		//telphone
		telitems[1]=new EditorItem();
		telitems[1].setName("telnum");
		telitems[1].setTitle("手机号码");
		telitems[1].setDescription("telphone editor");
		//edititems[0].setSelfLink(null);
		telitems[1].setType("String");
		//edititems[0].setAction(null);
		telitems[1].setOrdinal("3");
		//edititems[0].setValidflag(true); //phone: size check
		//telitems[1].setEditflag(true);
		telitems[1].setMinlength(11); //length check
		telitems[1].setMaxlength(11);
		
		map.put("telnum", telitems[1]);
		
		//link 
		LinkItem linkitems= new LinkItem();
		linkitems= new LinkItem();
		linkitems.setName("agreement");
		linkitems.setTitle("我已阅读并同意<贝贝鱼用户协议>");
		linkitems.setDescription("agreement link");
		linkitems.setHref("/api/agreement");
		linkitems.setType("link");
		//linkitems[0].setAction("login?username='11'&pwd='111'");
		linkitems.setOrdinal("4");
		
		map.put("agreement", linkitems);
		
		//for login button
		ButtonItem loginbtnitem= new ButtonItem();
		loginbtnitem.setName("register");
		loginbtnitem.setTitle("注册");
		loginbtnitem.setDescription("register link");
		//loginbtnitem.setSelfLink(null);
		loginbtnitem.setType("link");
		loginbtnitem.setAction("/api/loginaction?");
		loginbtnitem.setClickable(true);
		loginbtnitem.setOrdinal("5");		
		map.put("register", loginbtnitem); 
						
		ps.setProperties(map);
		return ps;

	}

}
