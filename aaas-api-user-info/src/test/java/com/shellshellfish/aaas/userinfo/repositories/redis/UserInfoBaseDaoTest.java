package com.shellshellfish.aaas.userinfo.repositories.redis;

import com.shellshellfish.aaas.userinfo.UserInfoApp;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

/**
 * Created by developer4 on 2018- 七月 - 03
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("local")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class UserInfoBaseDaoTest {

  @Autowired
  UserInfoBaseDao userInfoBaseDao;

  @Test
  public void setCaculateStatus() {
    userInfoBaseDao.setCaculateStatus(1000L, "003333.OF");
  }

  @Test
  public void getCaculateStatus() {
    userInfoBaseDao.setCaculateStatus(1000L, "003333.OF");
    while(true){
      Boolean result = userInfoBaseDao.getCaculateStatus(1000L, "003333.OF");
      System.out.println(result);
      if(result == null){
        break;
      }
      try {
        Thread.sleep(1000);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
