package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.math.BigInteger;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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


}
