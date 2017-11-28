package com.shellshellfish.aaas.userinfo.service;


import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsg;

public interface UserInfoService {
	UserBaseInfo getUserInfoBase(String userUuid) throws Exception;

    UserInfoAssectsBrief getUserInfoAssectsBrief(String userUuid) throws Exception;

    List<BankCard> getUserInfoBankCards(String userUuid) throws Exception;

    List<UserPortfolio> getUserPortfolios(String userUuid) throws Exception;

    BankCard getUserInfoBankCard(String cardNumber);

    BankCard createBankcard(Map params) throws Exception;

    List<AssetDailyRept> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
        throws Exception;

    AssetDailyRept addAssetDailyRept(AssetDailyRept assetDailyRept) throws ParseException;

    List<UserSysMsg> getUserSysMsg(String userUuid) throws IllegalAccessException, InstantiationException;

    List<UserPersonMsg> getUserPersonMsg(String userUuid) throws Exception;

    Boolean updateUserPersonMsg(String msgId, String userUuid,
        Boolean readedStatus) throws Exception;

    Page<TradeLog> findByUserId(String userUuid, Pageable pageable) throws Exception;

    List<UserInfoFriendRule> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException;

    UserInfoCompanyInfo getCompanyInfo(String userUuid, Long bankId);

}
