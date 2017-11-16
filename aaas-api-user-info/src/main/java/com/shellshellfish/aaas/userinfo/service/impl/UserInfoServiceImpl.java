package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.userinfo.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.bankcard.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.invest.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.invest.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.user.UserSysMsg;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.util.BankUtil;
import com.shellshellfish.aaas.userinfo.util.UserInfoUtils;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import org.hibernate.mapping.Collection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

@Service("userInfoService")
@Transactional
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Override
    public UserBaseInfo getUserInfoBase(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        UiUser userInfoDao = userInfoRepoService.getUserInfoBase(userId);
        UserBaseInfo userBaseInfo = new UserBaseInfo();
        if( null != userInfoDao) {
            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
        }
        return userBaseInfo;
    }

    @Override
    public UserInfoAssectsBrief getUserInfoAssectsBrief(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UiAsset userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
        if(null != userInfoAssect){
            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
        }
        return userInfoAssectsBrief;
    }

    @Override
    public List<BankCard> getUserInfoBankCards(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
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
    public List<UserPortfolio> getUserPortfolios(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
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
    public BankCard createBankcard(Map params) throws Exception {
        Long userId = getUserIdFromUUID(params.get("userUuid").toString());
        UiBankcard uiBankcard = new UiBankcard();
        uiBankcard.setCardNumber(params.get("cardNumber").toString());
        uiBankcard.setUserName(params.get("cardUserName").toString());
        uiBankcard.setCellphone(params.get("cardCellphone").toString());
        uiBankcard.setUserPid(params.get("cardUserPid").toString());
        uiBankcard.setUserId(userId);
        uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
        uiBankcard =  userInfoRepoService.addUserBankcard(uiBankcard);
        BankCard bankCard = new BankCard();
        BeanUtils.copyProperties(uiBankcard, bankCard);
        return bankCard;
    }

    @Override
    public List<AssetDailyRept> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
        throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UiAssetDailyRept> uiAssetDailyRepts = userInfoRepoService.getAssetDailyRept(userId,
            beginDate, endDate);
        List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
        for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
            AssetDailyRept assetDailyRept = new AssetDailyRept();
            BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
            assetDailyRept.setDate(new Date(uiAssetDailyRept.getDate()));
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

    @Override
    public List<UserSysMsg> getUserSysMsg(String userUuid) {
        List<UiSysMsg> uiSysMsgs = userInfoRepoService.getUiSysMsg();
        List<UserSysMsg> userSysMsgs = new ArrayList<>();
        for(UiSysMsg uiSysMsg: uiSysMsgs){
            UserSysMsg userSysMsg = new UserSysMsg();
            BeanUtils.copyProperties(uiSysMsg, userSysMsg);
            userSysMsgs.add(userSysMsg);
        }
        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsg> getUserPersonMsg(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UiPersonMsg> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
        List<UserPersonMsg> userPersonMsgs = new ArrayList<>();
        for(UiPersonMsg uiPersonMsg: uiPersonMsgs){
            UserPersonMsg userPersonMsg = new UserPersonMsg();
            BeanUtils.copyProperties(uiPersonMsg, userPersonMsg);
            userPersonMsgs.add(userPersonMsg);
        }
        return userPersonMsgs;
    }

    @Override
    public Boolean updateUserPersonMsg(List<String> msgIds, String userUuid,
        Boolean readedStatus) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgIds, userId,
            readedStatus);

        return result;
    }

    @Override
    public Page<TradeLog> getUserTradeLogs(String userUuid, PageRequest pageRequest) throws
        Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Page<UiTrdLog> tradeLogsPage = userInfoRepoService.getUiTrdLog(pageRequest, userId);
        List<UiTrdLog> tradeLogs = tradeLogsPage.getContent();
        List<TradeLog> tradeLogList = new ArrayList<>();
        if(CollectionUtils.isEmpty(tradeLogs)){
            logger.error("failed to retrieve trade logs for user with uuid:" + userUuid);
            return UserInfoUtils.convertListToPage(tradeLogList, pageRequest, 0);
        }
        tradeLogList = UserInfoUtils.convertList(tradeLogs, TradeLog.class);
        return  UserInfoUtils.convertListToPage(tradeLogList, pageRequest, tradeLogsPage
            .getTotalElements());
    }

    @Override
    public List<UserInfoFriendRule> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException {
        List<UiFriendRule> uiFriendRules = userInfoRepoService.getUiFriendRule(bankId);
        List<UserInfoFriendRule> userInfoFriendRules = new ArrayList<>();
        if(CollectionUtils.isEmpty(uiFriendRules)){
            return userInfoFriendRules;
        }else{
            userInfoFriendRules = UserInfoUtils.convertList(uiFriendRules, UserInfoFriendRule.class);
            return userInfoFriendRules;
        }
    }

    @Override
    public UserInfoCompanyInfo getCompanyInfo(String userUuid, Long bankId) {
        Long id = getCompanyId(userUuid, bankId);
        UiCompanyInfo uiCompanyInfo =  userInfoRepoService.getCompanyInfo(id);
        UserInfoCompanyInfo userInfoCompanyInfo = new UserInfoCompanyInfo();
        if(null == uiCompanyInfo){
            return userInfoCompanyInfo;
        }
        BeanUtils.copyProperties(uiCompanyInfo, userInfoCompanyInfo);
        return userInfoCompanyInfo;

    }
    //TODO: this function will be adjusted by business rule
    private Long getCompanyId(String userUuid, Long bankId) {
        return 1L;
    }


    private Long getUserIdFromUUID(String userUuid) throws Exception {
        Long userId =  userInfoRepoService.getUserIdFromUUID(userUuid);
        return userId;
    }
}
