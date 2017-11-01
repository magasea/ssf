package com.shellshellfish.account.controller;

import static org.junit.Assert.*;

import org.hamcrest.collection.IsCollectionWithSize;
import org.hamcrest.core.Is;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.shellshellfish.account.model.Account;
import com.shellshellfish.account.service.AccountService;

@RunWith(SpringRunner.class)
@WebMvcTest(RestApiController.class)
public class RestApiControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	//@MockBean
	//private RestApiController accountController;
	
	@MockBean
	private AccountService accountService;

	@Test
	public void getAccountById() throws Exception {
		Account account = new Account();
		account.setId(1);
		account.setName("test name");
		account.setType("test type");
		
		BDDMockito.given(accountService.findById(account.getId())).willReturn(account);
		mvc.perform(MockMvcRequestBuilders.get("/api/accounts/" + account.getId())
				.contentType("application/json"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.name", Is.is(account.getName())))
				.andExpect(MockMvcResultMatchers.jsonPath("$.type", Is.is(account.getType())));
						
	}

}
