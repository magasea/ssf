package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.FinanceApp;
import com.shellshellfish.aaas.finance.model.dto.UserProdChg;
import com.shellshellfish.aaas.finance.service.UserProdChangeLogService;
import java.util.ArrayList;
import java.util.List;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="dev")
public class UserProdChangeLogServiceImplTest {

  @Autowired
  UserProdChangeLogService userProdChangeLogService;

  @Test
  public void getGeneralChangeLogs() throws Exception {
  }

  @Test
  public void getDetailChangeLogs() throws Exception {
  }

  @Test
  public void insertGeneralChangeLogs() throws Exception {


    List<UserProdChg> userProdChgs = new ArrayList<>();
    for(int idx = 1; idx <= 14; idx ++){
      UserProdChg userProdChg = new UserProdChg();
      userProdChg.setProdId(idx);
      userProdChg.setCreateTime(TradeUtil.getUTCTime());
      userProdChg.setGroupId(12);
      userProdChg.setModifyComment();
    }

    userProdChangeLogService.insertGeneralChangeLogs(userProdChgs);
  }

  @Test
  public void insertDetailChangeLogs() throws Exception {
  }



}
