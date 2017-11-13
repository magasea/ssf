package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserAssectsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
  MongoUserAssectsRepository mongoUserAssectsRepository;

  @Autowired
  MongoUserPersonMsgRepo mongoUserPersonMsgRepo;

  @Autowired
  MongoUserSysMsgRepo mongoUserSysMsgRepo;

  @Autowired
  MongoUserProdMsgRepo mongoUserProdMsgRepo;

//  @Autowired
//  MongoOperations mongoOperations;


  @Override
  public UiUser getUserInfoBase(Long userId) {
    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userInfoRepository.findById(userId);
  }

  @Override
  public UiAsset getUserInfoAssectsBrief(Long userId) {
    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userInfoAssetsRepository.findByUserId(userIdLocal);
  }

  @Override
  public List<UiBankcard> getUserInfoBankCards(Long userId) {
    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userInfoBankCardsRepository.findAllByUserId(userIdLocal);
  }

  @Override
  public List<UiPortfolio> getUserPortfolios(Long userId) {
    BigInteger userIdLocal = BigInteger.valueOf(userId);
    return userPortfolioRepository.findAllByUserId(userIdLocal);
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
    return mongoUserAssectsRepository
        .findByUserIdAndDateIsBetween(BigInteger.valueOf(userId), beginDate, endDate);
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
    return mongoUserPersonMsgRepo.getUiUserPersonMsgByUserIdAndReaded(userId, Boolean.FALSE) ;
  }

  @Override
  public List<UiProdMsg> getUiProdMsg(Long prodId) {
    return mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
  }

  @Override
  public List<UiPersonMsg> updateUiUserPersonMsg(List<String> msgs, Long userId, Boolean
      readedStatus) {
    List<UiPersonMsg> uiPersonMsgs = new ArrayList<>();
//    for(String id: msgs){
//
//      Query query = new Query();
//      query.addCriteria(Criteria.where("_id").is(id).and("userId").is(userId));
//      Update update = new Update().set("readed", readedStatus);
//      UiPersonMsg uiPersonMsg = mongoOperations.findAndModify(query, update, UiPersonMsg.class);
//      uiPersonMsgs.add(uiPersonMsg);
//    }

    for(String id: msgs){
      UiPersonMsg uiPersonMsg = new UiPersonMsg();
      uiPersonMsg.setId(id);
      uiPersonMsg.setUserId(BigInteger.valueOf(userId));
      uiPersonMsg.setReaded(readedStatus);
      uiPersonMsgs.add(uiPersonMsg);
    }
    mongoUserPersonMsgRepo.save(uiPersonMsgs);
    return uiPersonMsgs;
  }

  @Override
  public List<UiSysMsg> getUiSysMsg() {
    return mongoUserSysMsgRepo.findAllByOrderByDateDesc();
  }

  @Override
  public Long getUserIdFromUUID(String userUuid) {
    return userInfoRepository.findUserIdByUuid(userUuid);
  }


}
