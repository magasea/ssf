package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

public interface UserInfoService {
    UserBaseInfo getUserInfoBase(Long userId);

    UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId);

    List<BankCard> getUserInfoBankCards(Long userId);

    List<UserPortfolio> getUserPortfolios(Long userId);

    BankCard getUserInfoBankCard(String cardNumber);

    BankCard createBankcard(Map params);

    List<AssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate);

    AssetDailyRept addAssetDailyRept(AssetDailyRept assetDailyRept) throws ParseException;
}
