package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.math.BigInteger;
import java.util.List;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="prod")
public class UserInfoRepoServiceImplTest {

  @Autowired
  UserInfoRepository userInfoRepository;

  @Autowired
  UserInfoBankCardsRepository userInfoBankCardsRepository;

  @org.junit.Test
  public void getUserInfoBase() throws Exception {
      long userId = Long.valueOf(1);
      UiUser uiUser =userInfoRepository.findOne(userId);
      System.out.println(uiUser);
  }

  @org.junit.Test
  public void getUserInfoAssectsBrief() throws Exception {
  }

  @org.junit.Test
  public void getUserInfoBankCards() throws Exception {
    BigInteger userId = BigInteger.valueOf(1);
    UiBankcard uiBankcard= userInfoBankCardsRepository.findOne(userId);
    System.out.println(uiBankcard);
    String bankName = "工商银行";
    List<UiBankcard> uiBankcards= userInfoBankCardsRepository.findByBankName(bankName);
    System.out.println(uiBankcard);
  }

  @org.junit.Test
  public void getUserPortfolios() throws Exception {
  }


}
