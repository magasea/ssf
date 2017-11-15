package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.invest.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserSysMsg;
import java.text.ParseException;
import java.util.List;
import java.util.Map;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface UserInfoService {
    UserBaseInfo getUserInfoBase(Long userId);

    UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

    List<BankCard> getUserInfoBankCards(Long userId);

    List<UserPortfolio> getUserPortfolios(Long userId);

    BankCard getUserInfoBankCard(String cardNumber);

    BankCard createBankcard(Map params);

    List<AssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate);

    AssetDailyRept addAssetDailyRept(AssetDailyRept assetDailyRept) throws ParseException;

    List<UserSysMsg> getUserSysMsg(String userUuid);

    List<UserPersonMsg> getUserPersonMsg(String userUuid) throws Exception;

    List<UserPersonMsg> updateUserPersonMsg(List<String> msgId, String userUuid,
        Boolean readedStatus) throws Exception;

    Page<TradeLog> getUserTradeLogs(String userUuid, PageRequest pageRequest) throws Exception;

    List<UserInfoFriendRule> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException;

}
