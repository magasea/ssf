package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="prod")
public class UserInfoRepoServiceImplTest {

  @Test
  public void getUiPersonMsg() throws Exception {
    List<UiPersonMsg> uiPersonMsgs = mongoUserPersonMsgRepo.getUiPersonMsgsByUserIdAndReaded(Long
            .valueOf("1"),Boolean.FALSE);
    System.out.println(uiPersonMsgs);
  }


  @Test
  public void getAssetDailyReptByUserId() throws Exception {
  }

  @Autowired
  UserInfoRepository userInfoRepository;

  @Autowired
  UserInfoBankCardsRepository userInfoBankCardsRepository;

  @Autowired
  MongoUserAssetsRepository mongoUserAssetsRepository;

  @Autowired
  MongoUserPersonMsgRepo mongoUserPersonMsgRepo;

  @Autowired
  MongoUserSysMsgRepo mongoUserSysMsgRepo;

  @Autowired
  MongoUserProdMsgRepo mongoUserProdMsgRepo;

  @Test
  public void addUiPersonMsg() throws Exception {
    List<UiPersonMsg> uiPersonMsgs = new ArrayList<>();
    for(int idx = 0; idx < 10; idx ++)

  {
    UiPersonMsg uiPersonMsg = new UiPersonMsg();
    uiPersonMsg.setReaded(Boolean.FALSE);
    uiPersonMsg.setUserId(BigInteger.valueOf(idx));
    uiPersonMsg.setContent("aeswrqaewvfaervfa");
    uiPersonMsg.setCreatedDate((new Date()).getTime());
    uiPersonMsg.setCreatedBy("1");
    uiPersonMsg.setMsgSource("bank");
    uiPersonMsg.setMsgTitle("bank message");
    uiPersonMsgs.add(uiPersonMsg);
    mongoUserPersonMsgRepo.save(uiPersonMsg);
  }


  }

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
    Long userId = Long.parseLong("11111");
    List<UiAssetDailyRept> uiAssetDailyRepts = mongoUserAssetsRepository.findByUserId(userId);
    System.out.println(uiAssetDailyRepts);
  }


}
