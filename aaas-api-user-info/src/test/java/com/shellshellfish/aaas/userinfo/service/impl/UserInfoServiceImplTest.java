package com.shellshellfish.aaas.userinfo.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import io.grpc.ManagedChannel;
import javax.annotation.PostConstruct;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(value= SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="local")

public class UserInfoServiceImplTest {
  PayRpcServiceFutureStub payRpcServiceFutureStub;

  @Autowired
  UserInfoService userInfoService;



  @Test
  public void queryTrdResultByOrderDetailId() throws Exception {
    Long userId = 5605L;
    Long orderDetailId = 971L;
    userInfoService.queryTrdResultByOrderDetailId(userId,orderDetailId);
  }

  @Before
  public void setUp() throws Exception {
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void getUserInfoBase() throws Exception {
  }

  @Test
  public void getUserInfoAssectsBrief() throws Exception {
  }

  @Test
  public void getUserInfoBankCards() throws Exception {
  }

  @Test
  public void getUserPortfolios() throws Exception {
  }

  @Test
  public void getUserInfoBankCard() throws Exception {
  }

  @Test
  public void createBankcard() throws Exception {
  }

  @Test
  public void getAssetDailyRept() throws Exception {
  }

  @Test
  public void addAssetDailyRept() throws Exception {
  }

  @Test
  public void getUserSysMsg() throws Exception {
  }

  @Test
  public void getUserPersonMsg() throws Exception {
  }

  @Test
  public void updateUserPersonMsg() throws Exception {
  }

  @Test
  public void getUserTradeLogs() throws Exception {
  }

  @Test
  public void getUserInfoFriendRules() throws Exception {
  }

  @Test
  public void getCompanyInfo() throws Exception {
  }

}