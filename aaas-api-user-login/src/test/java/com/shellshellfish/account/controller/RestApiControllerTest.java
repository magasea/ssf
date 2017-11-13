package com.shellshellfish.account.controller;


import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;




import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.shellshellfish.account.model.Account;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=com.shellshellfish.account.AccountServiceApplication.class)
@ActiveProfiles(profiles="prod")
public class RestApiControllerTest {
	
	//@Autowired
	private MockMvc mvc;
    
	
	@Autowired
    protected WebApplicationContext wac;

	
	
	@Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }
	
	
	
	//for login button 
	@Test	
	public void loginverify() throws Exception {
		Account account = new Account();
		account.setId(5);
		account.setPassword("abccd4djsN-999");
		account.setTelnum("13611442221");
		
		//BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
		
		mvc.perform(MockMvcRequestBuilders.post("/api/loginverify?telnum="+ account.getTelnum()+"&password="+account.getPassword())
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
						
	}
	
	//for login : telnum format error 
	@Test	
	public void loginverifyforformaterror() throws Exception {
			Account account = new Account();
			account.setId(5);
			account.setPassword("abccd4djsN-999");
			account.setTelnum("19611442221");
			
			//BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
			
			mvc.perform(MockMvcRequestBuilders.post("/api/loginverify?telnum="+ account.getTelnum()+"&password="+account.getPassword())
					.contentType("application/json"))
			        .andExpect(jsonPath("code").value(101))
		            .andExpect(jsonPath("message").value("手机号格式不对"))
			        .andExpect(MockMvcResultMatchers.status().is(400));
							
	}
		
	//for pwdconfirm
	@Test	
	public void pwdconfirm() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/pwdconfirm?telnum=13611683358&pwdsetting=abccd4djsN-919&pwdconfirm=abccd4djsN-919")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	//for pwdconfirm
	@Test	
	public void pwdconfirmnorepeating() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/pwdconfirm?telnum=13611683358&pwdsetting=abccd4djsN-919&pwdconfirm=abccd4djsN-919")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().is(400));		
	}
	
	//for topwdsetting 
	@Test	
	public void topwdsetting() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/topwdsetting?telnum=12134567890&verfiedcode=1234")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
	//for topwdsetting unauthorized
	@Test	
	public void topwdsettingforunauthorized() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/topwdsetting?telnum=12134567890&verfiedcode=123456")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().is(401));		
	}
			
	//for verifycodeget 
	@Test	
	public void verifycodeget() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("/api/verifycodeget?telnum=12134567890")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
    }
		
	//for tosmsverification 
	@Test	
	public void tosmsverification() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/tosmsverification?telnum=13684567890")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
	@Test	
	public void tosmsverificationforregisterred() throws Exception {
			
		mvc.perform(MockMvcRequestBuilders.post("/api/tosmsverification?telnum=13611683358"))
				    .andExpect(jsonPath("code").value(100))
				    .andExpect(jsonPath("message").value("抱歉，此电话号码已注册"))
					.andExpect(MockMvcResultMatchers.status().is(400));
					  
    }
	
	//for smsverconfirm
	@Test	
	public void smsverconfirm() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/smsverconfirm?telnum=13684567890&verificationcode=123456")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	
	//for bkverrify
	@Test	
	public void bkverrify() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/bkverrify?telnum=13611683357"
					                              + "&bankcardno=6212151111156743"
					                              + "&bankname=cmbbank"
					                              + "&name=test")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	
	//for addbankcard resource
	@Test	
	public void addbankcard() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/addbankcard?telnum=13684567890")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	//for viewbklist reosurce
	public void viewbklist() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/viewbklist")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
    }
	
	//for bklist resource
	public void bklist() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/bklist")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
    }
	

	//for smsverification reosurce
	@Test	
	public void smsverification() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/tosmsverification?telnum=13684567890")
						.contentType("application/json"))
						.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
		
	//for login resource
	@Test	
	public void loginres() throws Exception {
			mvc.perform(MockMvcRequestBuilders.get("/api/login?telnum=12134567890")
						.contentType("application/json"))
						.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	//for register resource
	@Test	
	public void registerres() throws Exception {
				mvc.perform(MockMvcRequestBuilders.get("/api/register?telnum=12134567890")
						.contentType("application/json"))
						.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
		
	//for pwdsetting resource
	@Test	
	public void pwdsettingres() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post("/api/pwdsetting?telnum=12134567890")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	
		
		
			
	//for topwdsetting resource
	@Test	
	public void topwdsettingres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post("/api/topwdsetting?telnum=12134567890&verfiedcode=1234")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
	//for forgottenpwd resource
	@Test	
	public void forgottenpwdres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/forgottenpwd?telnum=21212122112")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}
	
	
		
}
