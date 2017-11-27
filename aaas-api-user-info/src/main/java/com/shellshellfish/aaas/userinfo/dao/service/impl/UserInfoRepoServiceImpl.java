package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.mongodb.WriteResult;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserAssectsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoCompanyInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoFriendRuleRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserTradeLogRepository;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfo;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UserInfoRepoServiceImpl implements UserInfoRepoService {

  @Autowired
  UserInfoBankCardsRepository userInfoBankCardsRepository;

  @Autowired
  UserPortfolioRepository userPortfolioRepository;

  @Autowired
  UserInfoAssetsRepository userInfoAssetsRepository;

  @Autowired
  UserInfoRepository userInfoRepository;

  @Autowired
  UserInfoFriendRuleRepository userInfoFriendRuleRepository;

  @Autowired
  UserTradeLogRepository userTradeLogRepository;

  @Autowired
  UserInfoCompanyInfoRepository userInfoCompanyInfoRepository;

  @Autowired
  MongoUserAssectsRepository mongoUserAssectsRepository;

  @Autowired
  MongoUserPersonMsgRepo mongoUserPersonMsgRepo;

  @Autowired
  MongoUserSysMsgRepo mongoUserSysMsgRepo;

  @Autowired
  MongoUserProdMsgRepo mongoUserProdMsgRepo;

  @Autowired
  MongoTemplate mongoTemplate;


  @Override
  public UiUser getUserInfoBase(Long id) {
    BigInteger userIdLocal = BigInteger.valueOf(id);
    return userInfoRepository.findById(id);
  }

  @Override
  public UiAsset getUserInfoAssectsBrief(Long userId) {
//    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userInfoAssetsRepository.findByUserId(userId);
  }

  @Override
  public List<UiBankcard> getUserInfoBankCards(Long userId) {
//    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userInfoBankCardsRepository.findAllByUserId(userId);
  }

  @Override
  public List<UiPortfolio> getUserPortfolios(Long userId) {
//    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userPortfolioRepository.findAllByUserId(userId);
  }

  @Override
  public UiBankcard getUserInfoBankCard(String cardNumber) {
    return userInfoBankCardsRepository.findUiBankcardByCardNumberIs(cardNumber);
  }

  @Override
  public UiBankcard addUserBankcard(UiBankcard uiBankcard) {
    return userInfoBankCardsRepository.save(uiBankcard);
  }

  @Override
  public List<UiAssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate) {
    Query query = new Query();
    query.addCriteria(Criteria.where("userId").is(userId));
    Date beginDated = new Date(beginDate);
    query.addCriteria(Criteria.where("date").gte(beginDate).lt(endDate));
    System.out.println("userId:" + userId + " beginDate:"+beginDate + " endDate:" + endDate);
    return mongoUserAssectsRepository.findByUserIdAndDateIsBetween(BigInteger.valueOf(userId), beginDate, endDate);
  }

  @Override
  public UiAssetDailyRept addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept) {
    return mongoUserAssectsRepository.save(uiAssetDailyRept);
  }

  @Override
  public List<UiAssetDailyRept> getAssetDailyReptByUserId(Long userId) {
    return mongoUserAssectsRepository.findByUserId(userId);
  }

  @Override
  public List<UiPersonMsg> getUiPersonMsg(Long userId) {
    return mongoUserPersonMsgRepo.getUiPersonMsgsByUserIdAndReaded(userId, Boolean.FALSE) ;
  }

  @Override
  public List<UiProdMsg> getUiProdMsg(Long prodId) {
    return mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
  }

  @Override
  public Boolean updateUiUserPersonMsg(String msg, Long userId, Boolean
      readedStatus) {
    Query query = new Query();
    query.addCriteria(Criteria.where("id").is(msg).and("userId").is(userId.toString()));
    Update update = new Update();
    update.set("readed", readedStatus);
    WriteResult result = mongoTemplate.updateMulti(query, update, UiPersonMsg.class);
    System.out.println(result);
    return result.isUpdateOfExisting();
  }

  @Override
  public List<UiSysMsg> getUiSysMsg() {
    return mongoUserSysMsgRepo.findAllByOrderByDateDesc();
  }

  @Override
  public Long getUserIdFromUUID(String userUuid) throws Exception {
    UiUser uiUser = userInfoRepository.findUiUserByUuid(userUuid);
    if(null == uiUser){
      throw new Exception("not vaild userUuid:" + userUuid);
    }
    return Long.valueOf(userInfoRepository.findUiUserByUuid(userUuid).getId());
  }

  @Override
  public UiPersonMsg addUiPersonMsg(UiPersonMsg uiPersonMsg) {
    UiPersonMsg uiPersonMsgResult = mongoUserPersonMsgRepo.save(uiPersonMsg);
    return uiPersonMsgResult;
  }

  @Override
  public Page<UiTrdLog> getUiTrdLog(PageRequest pageRequest, Long userId ) {
    Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findAllByUserId(userId, pageRequest );
    return uiTrdLogPage;
  }

  @Override
  public Iterable<UiTrdLog> addUiTrdLog(List<UiTrdLog> trdLogs) {
    return userTradeLogRepository.save(trdLogs);
  }

  @Override
  public List<UiFriendRule> getUiFriendRule(Long bankId) {
    if(null == bankId) {
      return userInfoFriendRuleRepository.findAll();
    }else{
      return userInfoFriendRuleRepository.findAllByBankId(bankId);
    }
  }

  @Override
  public UiCompanyInfo getCompanyInfo(Long id) {

    return userInfoCompanyInfoRepository.findAll().get(0);
  }

  @Override
  public UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo) {
    return userInfoCompanyInfoRepository.save(uiCompanyInfo);
  }

}
