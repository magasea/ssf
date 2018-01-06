package com.shellshellfish.aaas.account.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = com.shellshellfish.aaas.account.AccountServiceApplication.class)
@ActiveProfiles(profiles = "dev")
public class RestApiControllerTest {

	//@Autowired
	private MockMvc mvc;

	@Autowired
	protected WebApplicationContext wac;

	public static String URL_HEAD = "/api/useraccount";

	@Before()  //这个方法在每个方法执行之前都会执行一遍
	public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
	}


	//for login button 
	@Test
	public void loginverify() throws Exception {

		//BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
		String jsonstr = "{\"password\":\"abccd4djsN-999\",\"telnum\": \"13611442221\"}";
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/login")
				//.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().is(201)); //created

	}

	//for login : telnum format error 
	@Test
	public void loginverifyforformaterror() throws Exception {

		//BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
		String jsonstr = "{\"password\":\"abccd4djsN-999\",\"telnum\": \"11611442221\"}";

		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/login")
				.contentType("application/json")
				.content(jsonstr))
				.andExpect(jsonPath("code").value(102))
				.andExpect(jsonPath("message").value("手机号格式不对"))
				.andExpect(MockMvcResultMatchers.status().is(400));

	}
		
	
	
	/*controller 里面需要不加这两个方法
	//for topwdsetting 
	@Test	
	public void topwdsetting() throws Exception {
			
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD+"/pwdforgettingpage?pwdsetting?telnum=12134567890&verfiedcode=1234")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().isOk());		
	}
		
	//for topwdsetting unauthorized
	@Test	
	public void topwdsettingforunauthorized() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post(URL_HEAD+"/pwdforgettingpage?pwdsetting?telnum=12134567890&verfiedcode=123456")
					.contentType("application/json"))
					.andExpect(MockMvcResultMatchers.status().is(401));		
	}*/

	//for verifycodeget 获取验证码 
	@Test
	public void verifycodeget() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/pwdforgettingpage/request?telnum=15026646271")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	//for tosmsverification 
	@Test
	public void smsverification() throws Exception {
		String jsonstr = "{\"telnum\": \"13611442221\",\"identifyingcode\": \"123456\"}";
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/smsverificationpage/1")
				.contentType("application/json")
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	@Test
	public void smsverificationforregisterred() throws Exception {

		String jsonstr = "{\"telnum\": \"13611442221\"}";
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/registrationpage/1")
				.contentType("application/json")
				.content(jsonstr))
				.andExpect(jsonPath("code").value(103))
				.andExpect(jsonPath("message").value("抱歉，此电话号码已注册"))
				.andExpect(MockMvcResultMatchers.status().is(400));

	}

	//for smsverconfirm
	@Test
	public void smsverifyconfirmation() throws Exception {

		String jsonstr = "{\"identifyingcode\":\"123456\",\"telnum\": \"13611442221\"}";
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/smsverificationpage/1")
				.contentType("application/json")
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}
	
	
	/*
	//for bkverrify
	@Test	
	public void bankcardverification() throws Exception {
			mvc.perform(MockMvcRequestBuilders.post(URL_HEAD+"/bankcards.bankcardverification?telnum=13611683357"
					                              + "&bankcardno=6212151111156743"
					                              + "&bankname=cmbbank"
					                              + "&name=test")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());		
	}*/


	//for addbankcard resource
	@Test
	public void bankcards() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/registerations/1/bankcards?telnum=13684567890")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	//for viewbklist reosurce
	public void selectbanks() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/bankcards.selectbanks")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	//for bklist resource
	public void banks() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD + "/banks")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


	//for smsverification reosurce
	/*@Test	
	public void pwdsettings() throws Exception {
		mvc.perform(MockMvcRequestBuilders.post(URL_HEAD+"/pwdsettings?telnum=13684567890")
						.contentType("application/json"))
						.andExpect(MockMvcResultMatchers.status().isOk());		
	}*/


	//for login resource
	@Test
	public void loginres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/loginpage")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}

	//for register resource
	@Test
	public void registerres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/registrationpage?telnum=12134567890")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


	//for pwdsetting resource
	@Test
	public void pwdsettingres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/pwdsettingpage?telnum=12134567890")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


	//for pwdsetting confirm 确认按钮
	//if ok, password is new
	//else password is repeated
	@Test
	public void pwdsetting() throws Exception {//有可能多于1条的冗余数据,或者密码重复使用,导致测试没通过
		String jsonstr = "{\"pwdconfirm\":\"650sol-YP\",\"password\":\"650sol-YP\",\"telnum\": \"13611442221\"}";
		mvc.perform(MockMvcRequestBuilders.patch(URL_HEAD + "/pwdsettingpage/1")
				.contentType("application/json")
				.content(jsonstr))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


	//for pwdforgettingpage resource
	@Test
	public void pwdresettingsres() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get(URL_HEAD + "/pwdforgettingpage?telnum=21212122112")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
	}


}
