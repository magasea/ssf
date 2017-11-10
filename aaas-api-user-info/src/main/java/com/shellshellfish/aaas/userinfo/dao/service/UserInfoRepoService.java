package com.shellshellfish.aaas.userinfo.dao.service;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.util.Date;
import java.util.List;

public interface UserInfoRepoService {
  UiUser getUserInfoBase(Long userId);

  UiAsset getUserInfoAssectsBrief(Long userId);

  List<UiBankcard> getUserInfoBankCards(Long userId);

  List<UiPortfolio> getUserPortfolios(Long userId);

  UiBankcard getUserInfoBankCard(String cardNumber);

  UiBankcard addUserBankcard(UiBankcard uiBankcard);

  List<UiAssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate);

  UiAssetDailyRept addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept);

  List<UiAssetDailyRept> getAssetDailyReptByUserId(Long userId);
}
