package com.shellshellfish.aaas.userinfo.dao.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.mongodb.WriteResult;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserAssectsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoCompanyInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoFriendRuleRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.dao.repositories.mysql.UserTradeLogRepository;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dto.BankCard;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsg;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLog;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfo;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBrief;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRule;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolio;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsg;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;

@Service
public class UserInfoRepoServiceImpl implements UserInfoRepoService {

	@Autowired
	UserInfoBankCardsRepository userInfoBankCardsRepository;

	@Autowired
	UserPortfolioRepository userPortfolioRepository;

	@Autowired
	UserInfoAssetsRepository userInfoAssetsRepository;

	@Autowired
	UserInfoRepository userInfoRepository;

	@Autowired
	UserInfoFriendRuleRepository userInfoFriendRuleRepository;

	@Autowired
	UserTradeLogRepository userTradeLogRepository;

	@Autowired
	UserInfoCompanyInfoRepository userInfoCompanyInfoRepository;

	@Autowired
	MongoUserAssectsRepository mongoUserAssectsRepository;

	@Autowired
	MongoUserPersonMsgRepo mongoUserPersonMsgRepo;

	@Autowired
	MongoUserSysMsgRepo mongoUserSysMsgRepo;

	@Autowired
	MongoUserProdMsgRepo mongoUserProdMsgRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public UserBaseInfo getUserInfoBase(Long id) {
		BigInteger userIdLocal = BigInteger.valueOf(id);
		UiUser uiUser = userInfoRepository.findById(id);
		UserBaseInfo user = new UserBaseInfo();
		BeanUtils.copyProperties(uiUser, user);
		return user;
	}

	@Override
	public UserInfoAssectsBrief getUserInfoAssectsBrief(Long userId) {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		UiAsset uiAsset = userInfoAssetsRepository.findByUserId(userId);
		UserInfoAssectsBrief asset = new UserInfoAssectsBrief();
		BeanUtils.copyProperties(uiAsset, asset);
		return asset;
	}

	@Override
	public List<BankCard> getUserInfoBankCards(Long userId) throws IllegalAccessException, InstantiationException {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserId(userId);
		List<BankCard> bankcardDtoList = MyBeanUtils.convertList(bankcardList,BankCard.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPortfolio> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPortfolio> uiPortfolioList = userPortfolioRepository.findAllByUserId(userId);
		List<UserPortfolio> bankcardDtoList = MyBeanUtils.convertList(uiPortfolioList,UserPortfolio.class);
		return bankcardDtoList;
	}

	@Override
	public BankCard getUserInfoBankCard(String cardNumber) {
		UiBankcard uiBankcard = userInfoBankCardsRepository.findUiBankcardByCardNumberIs(cardNumber);
		BankCard bankcard = new BankCard();
		BeanUtils.copyProperties(uiBankcard, bankcard);
		return bankcard;
	}

	@Override
	public BankCard addUserBankcard(UiBankcard uiBankcard) {
		 userInfoBankCardsRepository.save(uiBankcard);
		 BankCard bankcard = new BankCard();
		 BeanUtils.copyProperties(uiBankcard, bankcard);
		 return bankcard;
	}

	@Override
	public List<AssetDailyRept> getAssetDailyRept(Long userId, Long beginDate, Long endDate) throws IllegalAccessException, InstantiationException {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Date beginDated = new Date(beginDate);
		query.addCriteria(Criteria.where("date").gte(beginDate).lt(endDate));
		System.out.println("userId:" + userId + " beginDate:" + beginDate + " endDate:" + endDate);
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository.findByUserIdAndDateIsBetween(BigInteger.valueOf(userId), beginDate, endDate);
		List<AssetDailyRept> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,AssetDailyRept.class);
		return bankcardDtoList;
	}

	@Override
	public AssetDailyRept addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept) {
		mongoUserAssectsRepository.save(uiAssetDailyRept);
		AssetDailyRept assetDailyRept = new AssetDailyRept();
		BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
		return assetDailyRept;
	}

	@Override
	public List<AssetDailyRept> getAssetDailyReptByUserId(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository.findByUserId(userId);
		List<AssetDailyRept> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,AssetDailyRept.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPersonMsg> getUiPersonMsg(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPersonMsg> uiPersonMsgList = mongoUserPersonMsgRepo.getUiPersonMsgsByUserIdAndReaded(userId, Boolean.FALSE);
		List<UserPersonMsg> personMsgDtoList = MyBeanUtils.convertList(uiPersonMsgList,UserPersonMsg.class);
		return personMsgDtoList;
	}

	@Override
	public List<UserProdMsg> getUiProdMsg(Long prodId) throws IllegalAccessException, InstantiationException {
		List<UiProdMsg> uiProdMsgList = mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
		List<UserProdMsg> uiProdMsgDtoList = MyBeanUtils.convertList(uiProdMsgList,UserProdMsg.class);
		return uiProdMsgDtoList;
	}

	@Override
	public Boolean updateUiUserPersonMsg(String msg, Long userId, Boolean readedStatus) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(msg).and("userId").is(userId.toString()));
		Update update = new Update();
		update.set("readed", readedStatus);
		WriteResult result = mongoTemplate.updateMulti(query, update, UiPersonMsg.class);
		System.out.println(result);
		return result.isUpdateOfExisting();
	}

	@Override
	public List<UserSysMsg> getUiSysMsg() throws IllegalAccessException, InstantiationException {
		List<UiSysMsg> uiSysMsgList = mongoUserSysMsgRepo.findAllByOrderByDateDesc();
		List<UserSysMsg> uiSysMsgDtoList = MyBeanUtils.convertList(uiSysMsgList,UserSysMsg.class);
		return uiSysMsgDtoList;
	}

	@Override
	public Long getUserIdFromUUID(String userUuid) throws Exception {
		UiUser uiUser = userInfoRepository.findUiUserByUuid(userUuid);
		Long userId = new Long(0);
		if (null == uiUser) {
			throw new Exception("not vaild userUuid:" + userUuid);
		} else {
			userId = uiUser.getId();
		}
		return userId;
	}

	@Override
	public UserPersonMsg addUiPersonMsg(UiPersonMsg uiPersonMsg) {
		UiPersonMsg uiPersonMsgResult = mongoUserPersonMsgRepo.save(uiPersonMsg);
		UserPersonMsg personMsg = new UserPersonMsg();
		BeanUtils.copyProperties(uiPersonMsgResult, personMsg);
		return personMsg;
	}

	@Override
	public Page<UiTrdLog> findByUserId(Pageable pageable, Long userId) {
		Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findByUserId(pageable,userId);
		return uiTrdLogPage;
	}

	@Override
	public Iterable<TradeLog> addUiTrdLog(List<UiTrdLog> trdLogs) throws IllegalAccessException, InstantiationException {
		userTradeLogRepository.save(trdLogs);
		List<TradeLog> trdLogsDtoList = MyBeanUtils.convertList(trdLogs,TradeLog.class);
		return trdLogsDtoList;
	}

	@Override
	public List<UserInfoFriendRule> getUiFriendRule(Long bankId) throws IllegalAccessException, InstantiationException {
		List<UiFriendRule> uiFriendRuleList = new ArrayList<>();
		
		if (null == bankId) {
			uiFriendRuleList = userInfoFriendRuleRepository.findAll();
		} else {
			uiFriendRuleList =  userInfoFriendRuleRepository.findAllByBankId(bankId);
		}
		List<UserInfoFriendRule> trdLogsDtoList = MyBeanUtils.convertList(uiFriendRuleList,UserInfoFriendRule.class);
		return trdLogsDtoList;
	}

	@Override
	public UiCompanyInfo getCompanyInfo(Long id) {

		return userInfoCompanyInfoRepository.findAll().get(0);
	}

	@Override
	public UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo) {
		return userInfoCompanyInfoRepository.save(uiCompanyInfo);
	}

}
