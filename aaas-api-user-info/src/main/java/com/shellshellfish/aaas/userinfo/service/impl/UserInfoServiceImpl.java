package com.shellshellfish.aaas.userinfo.service.impl;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
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
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Override
    public UserBaseInfo getUserInfoBase(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        UserBaseInfo userInfoDao = userInfoRepoService.getUserInfoBase(userId);
//        UserBaseInfo userBaseInfo = new UserBaseInfo();
//        if( null != userInfoDao) {
//            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
//        }
        return userInfoDao;
    }

    @Override
    public UserInfoAssectsBrief getUserInfoAssectsBrief(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        //UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UserInfoAssectsBrief userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
//        if(null != userInfoAssect){
//            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
//        }
        return userInfoAssect;
    }

    @Override
    public List<BankCard> getUserInfoBankCards(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<BankCard> bankcards =  userInfoRepoService.getUserInfoBankCards(userId);
//        List<BankCard> bankCardsDto = new ArrayList<>();
//        for(UiBankcard uiBankcard: uiBankcards ){
//            BankCard bankCard = new BankCard();
//            BeanUtils.copyProperties(uiBankcard, bankCard);
//            bankCardsDto.add(bankCard);
//        }
        return bankcards;
    }

    @Override
    public List<UserPortfolio> getUserPortfolios(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UserPortfolio> userPortfolioDaos =  userInfoRepoService.getUserPortfolios(userId);
//        List<UserPortfolio> userPortfolios = new ArrayList<>();
//        for(UiPortfolio userPortfolioDao: userPortfolioDaos){
//            UserPortfolio userPortfolio = new UserPortfolio();
//            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
//            userPortfolios.add(userPortfolio);
//        }
        return userPortfolioDaos;
    }

    @Override
    public BankCard getUserInfoBankCard(String cardNumber) {
    	BankCard bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
//        BankCard bankCard = new BankCard();
//        BeanUtils.copyProperties(uiBankcard, bankCard);
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
        BankCard bankCard =  userInfoRepoService.addUserBankcard(uiBankcard);
        return bankCard;
    }

    @Override
    public List<AssetDailyRept> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
        throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<AssetDailyRept> uiAssetDailyRepts = userInfoRepoService.getAssetDailyRept(userId,
            beginDate, endDate);
//        List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
//        for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
//            AssetDailyRept assetDailyRept = new AssetDailyRept();
//            BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
//            assetDailyRept.setDate(new Date(uiAssetDailyRept.getDate()));
//            assetDailyRepts.add(assetDailyRept);
//        }
        return uiAssetDailyRepts;
    }

    @Override
    public AssetDailyRept addAssetDailyRept(AssetDailyRept assetDailyRept) throws ParseException {
        UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
        BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
        uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
        AssetDailyRept result =  userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
//        AssetDailyRept assetDailyReptResult = new AssetDailyRept();
//        BeanUtils.copyProperties(result, assetDailyReptResult);
//        Date date = new Date(result.getDate());
//        assetDailyRept.setDate(date);
        return result;
    }

    @Override
    public List<UserSysMsg> getUserSysMsg(String userUuid) throws IllegalAccessException, InstantiationException {
    	List<UserSysMsg> userSysMsgs = userInfoRepoService.getUiSysMsg();
//        List<UserSysMsg> userSysMsgs = new ArrayList<>();
//        for(UiSysMsg uiSysMsg: uiSysMsgs){
//            UserSysMsg userSysMsg = new UserSysMsg();
//            BeanUtils.copyProperties(uiSysMsg, userSysMsg);
//            userSysMsgs.add(userSysMsg);
//        }
        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsg> getUserPersonMsg(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UserPersonMsg> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
//        List<UserPersonMsg> userPersonMsgs = new ArrayList<>();
//        for(UiPersonMsg uiPersonMsg: uiPersonMsgs){
//            UserPersonMsg userPersonMsg = new UserPersonMsg();
//            BeanUtils.copyProperties(uiPersonMsg, userPersonMsg);
//            userPersonMsgs.add(userPersonMsg);
//        }
        return uiPersonMsgs;
    }

    @Override
    public Boolean updateUserPersonMsg(String msgId, String userUuid,
        Boolean readedStatus) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgId, userId,
            readedStatus);

        return result;
    }

    @Override
    public Page<TradeLog> findByUserId(String userUuid, Pageable pageable) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Page<UiTrdLog> tradeLogsPage = userInfoRepoService.findByUserId(pageable, userId);
        Page<TradeLog> tradeLogResult = MyBeanUtils.convertPageDTO(pageable, tradeLogsPage, TradeLog.class);
        return tradeLogResult;
    }

    @Override
    public List<UserInfoFriendRule> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException {
    	List<UserInfoFriendRule> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
//      List<UserInfoFriendRule> userInfoFriendRules = new ArrayList<>();
    	return userInfoFriendRules;
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
