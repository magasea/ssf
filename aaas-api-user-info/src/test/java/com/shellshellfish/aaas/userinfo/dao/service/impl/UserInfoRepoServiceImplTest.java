package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserAssetsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.utils.MongoUiTrdLogUtil;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

@RunWith(value= SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class UserInfoRepoServiceImplTest {

  @Test
  public void addUserBankcard() throws Exception {
    List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findUiBankcardByCardNumberIsAndStatusIsNot
        ("6222081001023607157", -1);
    if(!CollectionUtils.isEmpty(uiBankcards)){
      System.out.println(uiBankcards.size());
      uiBankcards.get(0).setStatus(-1);
      userInfoBankCardsRepository.save(uiBankcards.get(0));
    }

  }

  @Test
  public void addUiSysMsg() throws Exception {
    UiSysMsg uiSysMsg = new UiSysMsg();
    for(int idx = 0; idx < 100; idx ++){
      uiSysMsg = new UiSysMsg();
      uiSysMsg.setContent(""+idx);
      uiSysMsg.setCreatedDate(TradeUtil.getUTCTime());
      uiSysMsg.setCreatedBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
      uiSysMsg.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
      uiSysMsg.setUpdateDate(TradeUtil.getUTCTime());
      userInfoRepoService.addUiSysMsg(uiSysMsg);
      uiSysMsg = null;
    }

  }

//  @Test
//  public void addCompanyInfo() throws Exception {
//
//    UiCompanyInfo uiCompanyInfo = new UiCompanyInfo();
//    uiCompanyInfo.setCompanyInfo("zn;lan;flkjanewjn;lakjn中午只能你好噢虢");
//    uiCompanyInfo.setServiceNum("400-838-217314gj");
//    userInfoRepoService.addCompanyInfo(uiCompanyInfo);
//  }

  @Autowired
  UserInfoRepoService userInfoRepoService;
//
//  @Test
//  public void addUiTrdLog() throws Exception {
//    List<UiTrdLog> uiTradesLogs = new ArrayList<>();
//    for(int idx = 0; idx < 100; idx ++){
//      UiTrdLog uiTradesLog = new UiTrdLog();
//      uiTradesLog.setUserId(3L);
//      uiTradesLog.setAmount(UserInfoUtils.getRandomDecimalInRange(1, 1000000));
//      uiTradesLog.setOperations(UserInfoUtils.getRandomNumberInRange(1,4));
//      uiTradesLog.setUserProdId(Long.valueOf(UserInfoUtils.getRandomNumberInRange(1,100) ));
//      uiTradesLog.setTradeDate(UserInfoUtils.getCurrentUTCTime());
//      uiTradesLog.setTradeStatus(UserInfoUtils.getRandomNumberInRange(1,3));
//      uiTradesLogs.add(uiTradesLog);
//    }
//    userInfoRepoService.addUiTrdLog(uiTradesLogs);
//  }

  @Autowired
  MongoTemplate mongoTemplate;

  @Test
  public void updateUiUserPersonMsg() throws Exception {
    List<String> msgs = new ArrayList<>();
    msgs.add("5a098b8a3f6b9e23048bd335");
    msgs.add("5a098b8a3f6b9e23048bd336");
    msgs.add("5a098b8a3f6b9e23048bd337");
    Query query = new Query();
    query.addCriteria(Criteria.where("id").in(msgs).and("userId").is("1"));
    Update update = new Update();
    update.set("readed", Boolean.FALSE);
    UpdateResult result = mongoTemplate.updateMulti(query, update, UiPersonMsg.class);
    System.out.println(result);
  }

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
  public void selectUiTrdLog(){

    Criteria criteria = Criteria.where("user_id").is(5625);
    Query query = new Query(criteria);
    List<MongoUiTrdLog> uiTrdLogs = mongoTemplate.find(query, MongoUiTrdLog.class);
    List<MongoUiTrdLog> uiTrdLogsUnique =  MongoUiTrdLogUtil.getDistinct(uiTrdLogs);
    Comparator<MongoUiTrdLog> byUserProdIdAndTradeDate =
        ((MongoUiTrdLog o1, MongoUiTrdLog o2)->{
        if(o1.getUserProdId() == null && o2.getUserProdId() != null){
          return -1;
        }
        if(o1.getUserProdId() != null && o2.getUserProdId() == null){
          return 1;
        }
        if(o1.getUserProdId() == o2.getUserProdId()){
          if(o1.getTradeDate() == null && o2.getTradeDate() != null){
            return -1;
          }
          if(o1.getTradeDate() != null && o2.getTradeDate() == null){
            return 1;
          }
          return Long.valueOf(o1.getTradeDate() - o2.getTradeDate()).intValue();
        }else{
          return Long.valueOf(o1.getUserProdId() - o2.getUserProdId()).intValue();
        }
    });


    Collections.sort(uiTrdLogs,byUserProdIdAndTradeDate);

    uiTrdLogs.forEach(o-> System.out.println(String.format("getUserProdId():%s getFundCode():%s"
        + "getOperations():%s getTradeStatus():%s getApplySerial():%s",o.getUserProdId(),o
        .getFundCode(),o.getOperations(),o.getTradeStatus(), o.getApplySerial())));

    System.out.println("==================================================");

    Collections.sort(uiTrdLogsUnique, byUserProdIdAndTradeDate);

    uiTrdLogsUnique.forEach(o-> System.out.println(String.format("getUserProdId():%s getFundCode():%s"
        + "getOperations():%s getTradeStatus():%s getApplySerial():%s",o.getUserProdId(),o
        .getFundCode(),o.getOperations(),o.getTradeStatus(), o.getApplySerial())));
  }

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
      UiUser uiUser =userInfoRepository.findById(userId);
      System.out.println(uiUser);
  }

  @org.junit.Test
  public void getUserInfoAssectsBrief() throws Exception {
  }

  @org.junit.Test
  public void getUserInfoBankCards() throws Exception {
//    BigInteger userId = BigInteger.valueOf(1);
    Optional<UiBankcard> uiBankcard= userInfoBankCardsRepository.findById(1L);
    System.out.println(uiBankcard.get());
    String bankName = "工商银行";
    List<UiBankcard> uiBankcards= userInfoBankCardsRepository.findByBankName(bankName);
    System.out.println(uiBankcard.get());
  }

  @org.junit.Test
  public void getUserPortfolios() throws Exception {
    Long userId = Long.parseLong("11111");
    List<UiAssetDailyRept> uiAssetDailyRepts = mongoUserAssetsRepository.findByUserId(userId);
    System.out.println(uiAssetDailyRepts);
  }


}
