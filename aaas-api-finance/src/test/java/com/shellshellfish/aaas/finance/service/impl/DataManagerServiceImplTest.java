package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.finance.FinanceApp;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenwei on 2018- 三月 - 15
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="dev")
public class DataManagerServiceImplTest {
  


  @Autowired
  DataManagerService dataManagerService;

  @Test
  public void testGetBaseline() throws Exception {
  Long groupId = 1L;
  Integer  period = 5;

  Map map =dataManagerService.getBaseLine(groupId,period);
    Assert.assertNotNull("不为空",map);
    Assert.assertNotEquals(new HashMap(),map);
    System.out.println(map.size());
  }





}
