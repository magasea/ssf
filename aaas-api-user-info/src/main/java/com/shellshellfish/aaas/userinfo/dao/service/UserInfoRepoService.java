package com.shellshellfish.aaas.userinfo.dao.service;

import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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

  List<UiPersonMsg> getUiPersonMsg(Long userId);

  List<UiProdMsg> getUiProdMsg(Long prodId);

  Boolean updateUiUserPersonMsg(List<String> msgs, Long user_id, Boolean readedStatus);

  List<UiSysMsg> getUiSysMsg();

  Long getUserIdFromUUID(String userUuid) throws Exception;

  UiPersonMsg addUiPersonMsg(UiPersonMsg uiPersonMsg);

  Page<UiTrdLog> getUiTrdLog(PageRequest pageRequest, Long userId);

  Iterable<UiTrdLog> addUiTrdLog(List<UiTrdLog> trdLogs);

  List<UiFriendRule> getUiFriendRule(Long bankId);

  UiCompanyInfo getCompanyInfo(Long id);

  UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo);
}
