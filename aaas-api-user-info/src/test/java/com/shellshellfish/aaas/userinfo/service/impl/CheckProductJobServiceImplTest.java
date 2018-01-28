package com.shellshellfish.aaas.userinfo.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.CheckProductJobService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 一月 - 26
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "local")
public class CheckProductJobServiceImplTest {

  @Autowired
  CheckProductJobService checkProductJobService;

  @Test
  public void checkProductsAndSetStatus1() throws Exception {
    checkProductJobService.checkProductsAndSetStatus();
  }

  @Autowired
  UiProductRepo uiProductRepo;

  @Test
  public void checkProductsAndSetStatus() throws Exception {
//    List<UiProducts> uiProductsList = uiProductRepo.findAllByStatusIsNull();
//    uiProductsList.forEach(item -> System.out.println(item.getProdId()));
  }

}