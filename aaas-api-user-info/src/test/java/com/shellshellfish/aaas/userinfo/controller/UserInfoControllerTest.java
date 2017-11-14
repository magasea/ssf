package com.shellshellfish.aaas.userinfo.controller;

import org.junit.After;
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

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="prod")
public class UserInfoControllerTest {
  //@Autowired
  private MockMvc mvc;

  @Autowired
  protected WebApplicationContext wac;


  @Before
  public void setUp() throws Exception {
    mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void getUserBaseInfo() throws Exception {
    mvc.perform(MockMvcRequestBuilders.get("/api/userinfo/id")
        .contentType("application/json"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }


  @Test
  public void getUserBankCards() throws Exception {
  }

  @Test
  public void getUserPersonalInfo() throws Exception {
  }

  @Test
  public void addBankCardWithCardNumber() throws Exception {
  }

  @Test
  public void addBankCardWithDetailInfo() throws Exception {
  }

  @Test
  public void getUserAssetsOverview() throws Exception {
  }



}
