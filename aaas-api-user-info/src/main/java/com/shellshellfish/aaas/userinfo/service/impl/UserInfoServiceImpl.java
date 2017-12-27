package com.shellshellfish.aaas.userinfo.service.impl;

import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Override
    public UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception{
    	Long userId = getUserIdFromUUID(userUuid);
        UserBaseInfoDTO userInfoDao = userInfoRepoService.getUserInfoBase(userId);
//        UserBaseInfo userBaseInfo = new UserBaseInfo();
//        if( null != userInfoDao) {
//            BeanUtils.copyProperties(userInfoDao, userBaseInfo);
//        }
        return userInfoDao;
    }

    @Override
    public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        //UserInfoAssectsBrief userInfoAssectsBrief = new UserInfoAssectsBrief();
        UserInfoAssectsBriefDTO userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
//        if(null != userInfoAssect){
//            BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
//        }
        return userInfoAssect;
    }

    @Override
    public List<BankCardDTO> getUserInfoBankCards(String userUuid) throws Exception {
    	Long userId=null;
    	try{
    	 userId = getUserIdFromUUID(userUuid);
    	}catch(Exception e){
    		throw new UserInfoException("404", "该用户不存在");
    	}
    	List<BankCardDTO> bankcards=null;
    	try{
         bankcards =  userInfoRepoService.getUserInfoBankCards(userId);
    	}catch(Exception e){
    		throw new UserInfoException("404", "该用户暂时没有绑定银行卡");
    	}
//        List<BankCard> bankCardsDto = new ArrayList<>();
//        for(UiBankcard uiBankcard: uiBankcards ){
//            BankCard bankCard = new BankCard();
//            BeanUtils.copyProperties(uiBankcard, bankCard);
//            bankCardsDto.add(bankCard);
//        }
        return bankcards;
    }

    @Override
    public List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<UserPortfolioDTO> userPortfolioDaos =  userInfoRepoService.getUserPortfolios(userId);
//        List<UserPortfolio> userPortfolios = new ArrayList<>();
//        for(UiPortfolio userPortfolioDao: userPortfolioDaos){
//            UserPortfolio userPortfolio = new UserPortfolio();
//            BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
//            userPortfolios.add(userPortfolio);
//        }
        return userPortfolioDaos;
    }

    @Override
    public BankCardDTO getUserInfoBankCard(String cardNumber) throws RuntimeException {
    	BankCardDTO bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
//        BankCard bankCard = new BankCard();
//        BeanUtils.copyProperties(uiBankcard, bankCard);
        return bankCard;
    }

    @Override
    public BankCardDTO createBankcard(Map params) throws Exception {
    	Long userId = getUserIdFromUUID(params.get("userUuid").toString());
        UiBankcard uiBankcard = new UiBankcard();
        uiBankcard.setCardNumber(params.get("cardNumber").toString());
        uiBankcard.setUserName(params.get("cardUserName").toString());
        uiBankcard.setCellphone(params.get("cardCellphone").toString());
        uiBankcard.setUserPid(params.get("cardUserPid").toString());
        uiBankcard.setUserId(userId);
        if(!StringUtils.isEmpty(params.get("cardNumber"))){
        	String bankName = BankUtil.getNameOfBank(params.get("cardNumber").toString());
        	if(!StringUtils.isEmpty(bankName)){
        		uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
        	}
        }
        BankCardDTO bankCard =  userInfoRepoService.addUserBankcard(uiBankcard);
        return bankCard;
    }

    @Override
    public List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
        throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<AssetDailyReptDTO> uiAssetDailyRepts = userInfoRepoService.getAssetDailyRept(userId,
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
    public AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept){
        UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
        BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
        uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
        AssetDailyReptDTO result =  userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
//        AssetDailyRept assetDailyReptResult = new AssetDailyRept();
//        BeanUtils.copyProperties(result, assetDailyReptResult);
//        Date date = new Date(result.getDate());
//        assetDailyRept.setDate(date);
        return result;
    }

    @Override
    public List<UserSysMsgDTO> getUserSysMsg(String userUuid) throws IllegalAccessException, InstantiationException {
    	List<UserSysMsgDTO> userSysMsgs = userInfoRepoService.getUiSysMsg();
//        List<UserSysMsg> userSysMsgs = new ArrayList<>();
//        for(UiSysMsg uiSysMsg: uiSysMsgs){
//            UserSysMsg userSysMsg = new UserSysMsg();
//            BeanUtils.copyProperties(uiSysMsg, userSysMsg);
//            userSysMsgs.add(userSysMsg);
//        }
        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        List<UserPersonMsgDTO> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
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
    public Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception {
    	Long userId = getUserIdFromUUID(userUuid);
        Page<UiTrdLog> tradeLogsPage = userInfoRepoService.findByUserId(pageable, userId);
        Page<TradeLogDTO> tradeLogResult = MyBeanUtils.convertPageDTO(pageable, tradeLogsPage, TradeLogDTO.class);
        return tradeLogResult;
    }

    @Override
    public List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
        throws InstantiationException, IllegalAccessException,RuntimeException {
    	List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
//      List<UserInfoFriendRule> userInfoFriendRules = new ArrayList<>();
    	return userInfoFriendRules;
    }

    @Override
    public UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId){
        Long id = getCompanyId(userUuid, bankId);
        UiCompanyInfo uiCompanyInfo =  userInfoRepoService.getCompanyInfo(id);
        UserInfoCompanyInfoDTO userInfoCompanyInfo = new UserInfoCompanyInfoDTO();
        if(null == uiCompanyInfo){
            return userInfoCompanyInfo;
        }
        BeanUtils.copyProperties(uiCompanyInfo, userInfoCompanyInfo);
        return userInfoCompanyInfo;

    }
    //TODO: this function will be adjusted by business rule
    private Long getCompanyId(String userUuid, Long bankId){
        return 1L;
    }


    private Long getUserIdFromUUID(String userUuid) throws Exception {
        Long userId =  userInfoRepoService.getUserIdFromUUID(userUuid);
        return userId;
    }

	@Override
	public Boolean deleteBankCard(String userUuid, String bankcardId) {
		Boolean result = userInfoRepoService.deleteBankCard(userUuid, bankcardId);
		return result;
	}

	@Override
	public Boolean addUiUser(String userUuid, String cellphone, String isTestFlag) {
		Boolean result = userInfoRepoService.saveUser(userUuid, cellphone,isTestFlag);
		return result;
	}

	@Override
	public Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel) {
		Boolean result = userInfoRepoService.updateCellphone(cellphone,isTestFlag,riskLevel);
		return result;
	}

	@Override
	public UserBaseInfoDTO selectUiUser(String cellphone) {
		UserBaseInfoDTO result = userInfoRepoService.findByCellphone(cellphone);
		return result;
	}
	
	@Override
	public List<TradeLogDTO> findByUserId(String uuid) throws Exception {
		Long userId = getUserIdFromUUID(uuid);
		List<TradeLogDTO> uiTrdLogList = userInfoRepoService.findByUserId(userId);
		return uiTrdLogList;
	}
	
	@Override
	public List<ProductsDTO> findProductInfos(String uuid) throws Exception {
		List<ProductsDTO> products = userInfoRepoService.findByUserId(uuid);
		return products;
	}

	@Override
	public ProductsDTO findByProdId(String prodId) throws IllegalAccessException, InstantiationException {
		ProductsDTO products = userInfoRepoService.findByProdId(prodId);
		return products;
	}
}
