package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.util.BankUtil;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Override
    public UserBaseInfo getUserInfoBase(Long userId) {
        UiUser userInfoDao = userInfoRepoService.getUserInfoBase(userId);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        if( null != userInfoDao) {
            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
        }
        return userBaseInfo;
    }

    @Override
    public UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId) {
        UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UiAsset userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
        if(null != userInfoAssect){
            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
        }
        return userInfoAssectsBrief;
    }

    @Override
    public List<BankCard> getUserInfoBankCards(Long userId) {

        List<UiBankcard> uiBankcards =  userInfoRepoService.getUserInfoBankCards(userId);
        List<BankCard> bankCardsDto = new ArrayList<>();
        for(UiBankcard uiBankcard: uiBankcards ){
            BankCard bankCard = new BankCard();
            BeanUtils.copyProperties(uiBankcard, bankCard);
            bankCardsDto.add(bankCard);
        }
        return bankCardsDto;
    }

    @Override
    public List<UserPortfolio> getUserPortfolios(Long userId) {
        List<UiPortfolio> userPortfolioDaos =  userInfoRepoService.getUserPortfolios(userId);
        List<UserPortfolio> userPortfolios = new ArrayList<>();
        for(UiPortfolio userPortfolioDao: userPortfolioDaos){
            UserPortfolio userPortfolio = new UserPortfolio();
            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
            userPortfolios.add(userPortfolio);
        }
        return userPortfolios;
    }

    @Override
    public BankCard getUserInfoBankCard(String cardNumber) {
        UiBankcard uiBankcard = userInfoRepoService.getUserInfoBankCard(cardNumber);
        BankCard bankCard = new BankCard();
        BeanUtils.copyProperties(uiBankcard, bankCard);
        return bankCard;
    }

    @Override
    public BankCard createBankcard(Map params) {
        UiBankcard uiBankcard = new UiBankcard();
        uiBankcard.setCardNumber(params.get("cardNumber").toString());
        uiBankcard.setUserName(params.get("cardUserName").toString());
        uiBankcard.setCellphone(params.get("cardCellphone").toString());
        uiBankcard.setUserPid(params.get("cardUserPid").toString());
        uiBankcard.setUserId(new BigInteger(params.get("cardUserId").toString()));
        uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
        uiBankcard =  userInfoRepoService.addUserBankcard(uiBankcard);
        BankCard bankCard = new BankCard();
        BeanUtils.copyProperties(uiBankcard, bankCard);
        return bankCard;
    }

    @Override
    public List<AssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate) {

        List<UiAssetDailyRept> uiAssetDailyRepts = userInfoRepoService.getAssetDailyRept(userId,
            beginDate, endDate);
        List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
        for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
            AssetDailyRept assetDailyRept = new AssetDailyRept();
            BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
            assetDailyRepts.add(assetDailyRept);
        }
        return assetDailyRepts;
    }

    @Override
    public AssetDailyRept addAssetDailyRept(AssetDailyRept assetDailyRept) throws ParseException {
        UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
        BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
        uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
        UiAssetDailyRept result =  userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
        AssetDailyRept assetDailyReptResult = new AssetDailyRept();
        BeanUtils.copyProperties(result, assetDailyReptResult);
        Date date = new Date(result.getDate());
        assetDailyRept.setDate(date);
        return assetDailyReptResult;
    }

}
