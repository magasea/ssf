package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.AssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.math.BigInteger;
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
  AssetsRepository assetsRepository;

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
    query.addCriteria(Criteria.where("date").gte(beginDate).lt(endDate));
    return assetsRepository.findByDateBetweenAndUserId(userId, beginDate, endDate);
  }

  @Override
  public UiAssetDailyRept addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept) {
    return assetsRepository.save(uiAssetDailyRept);
  }

  @Override
  public List<UiAssetDailyRept> getAssetDailyReptByUserId(Long userId) {
    return assetsRepository.findByUserId(userId);
  }


}
