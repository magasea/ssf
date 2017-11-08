package com.shellshellfish.aaas.userinfo.dao.service;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;

import java.util.List;

public interface UserInfoRepoService {
  UiUser getUserInfoBase(Long userId);

  UiAsset getUserInfoAssectsBrief(Long userId);

  List<UiBankcard> getUserInfoBankCards(Long userId);

  List<UiPortfolio> getUserPortfolios(Long userId);
}
