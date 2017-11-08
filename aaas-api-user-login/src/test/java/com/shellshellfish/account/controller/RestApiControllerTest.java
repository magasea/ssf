package com.shellshellfish.account.controller;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import com.shellshellfish.account.model.Account;
import com.shellshellfish.account.service.AccountService;


@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//@WebMvcTest(RestApiController.class)
@SpringBootApplication 
@WebAppConfiguration
public class RestApiControllerTest {
	
	//@Autowired
	private MockMvc mvc;
	
	@Autowired
    protected WebApplicationContext wac;

	//@MockBean
	//private RestApiController accountController;
	
	
	@MockBean
	private AccountService accountService;

	@Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }

	@Test	
	public void login() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/api/login")
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
		
	}
	
	@Test	
	public void loginverify() throws Exception {
		Account account = new Account();
		account.setId(5);
		account.setPassword("123654aF");
		account.setTelnum("12332321211");
		/*
		mvc.perform(MockMvcRequestBuilders.post("/api/login?id="+ account.getId())
				.contentType("application/json"))		
        .andExpect(status().isOk());
		
		return ;*/
		
		BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
		mvc.perform(MockMvcRequestBuilders.post("/api/loginverify?telnum="+ account.getTelnum()+"&password="+account.getPassword())
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk());
				//.andExpect(MockMvcResultMatchers.jsonPath("$.password", Is.is(account.getPassword())))
				//.andExpect(MockMvcResultMatchers.jsonPath("$.telnum", Is.is(account.getTelnum())));
						
	}

}
