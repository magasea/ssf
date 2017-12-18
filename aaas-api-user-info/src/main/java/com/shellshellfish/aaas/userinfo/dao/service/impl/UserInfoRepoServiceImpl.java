package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.mongodb.WriteResult;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.grpc.UserId;
import com.shellshellfish.aaas.userinfo.grpc.UserIdQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
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
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserAssectsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoCompanyInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoFriendRuleRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserTradeLogRepository;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;
import io.grpc.stub.StreamObserver;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

@Service
public class UserInfoRepoServiceImpl extends UserInfoServiceGrpc.UserInfoServiceImplBase implements UserInfoRepoService {

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
	public UserBaseInfoDTO getUserInfoBase(Long id) {
		BigInteger userIdLocal = BigInteger.valueOf(id);
		UiUser uiUser = userInfoRepository.findById(id);
		UserBaseInfoDTO user = new UserBaseInfoDTO();
		BeanUtils.copyProperties(uiUser, user);
		return user;
	}

	@Override
	public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(Long userId) {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		UiAsset uiAsset = userInfoAssetsRepository.findByUserId(userId);
		UserInfoAssectsBriefDTO asset = new UserInfoAssectsBriefDTO();
		BeanUtils.copyProperties(uiAsset, asset);
		return asset;
	}

	@Override
	public List<BankCardDTO> getUserInfoBankCards(Long userId) throws IllegalAccessException, InstantiationException {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserId(userId);
		List<BankCardDTO> bankcardDtoList = MyBeanUtils.convertList(bankcardList,BankCardDTO.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPortfolioDTO> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPortfolio> uiPortfolioList = userPortfolioRepository.findAllByUserId(userId);
		List<UserPortfolioDTO> bankcardDtoList = MyBeanUtils.convertList(uiPortfolioList,UserPortfolioDTO.class);
		return bankcardDtoList;
	}

	@Override
	public BankCardDTO getUserInfoBankCard(String cardNumber) {
		UiBankcard uiBankcard = userInfoBankCardsRepository.findUiBankcardByCardNumberIs(cardNumber);
		BankCardDTO bankcard = new BankCardDTO();
		BeanUtils.copyProperties(uiBankcard, bankcard);
		return bankcard;
	}

	@Override
	public BankCardDTO addUserBankcard(UiBankcard uiBankcard) {
		 userInfoBankCardsRepository.save(uiBankcard);
		 BankCardDTO bankcard = new BankCardDTO();
		 BeanUtils.copyProperties(uiBankcard, bankcard);
		 return bankcard;
	}

	@Override
	public List<AssetDailyReptDTO> getAssetDailyRept(Long userId, Long beginDate, Long endDate) throws IllegalAccessException, InstantiationException {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Date beginDated = new Date(beginDate);
		query.addCriteria(Criteria.where("date").gte(beginDate).lt(endDate));
		System.out.println("userId:" + userId + " beginDate:" + beginDate + " endDate:" + endDate);
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository.findByUserIdAndDateIsBetween(BigInteger.valueOf(userId), beginDate, endDate);
		List<AssetDailyReptDTO> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,AssetDailyReptDTO.class);
		return bankcardDtoList;
	}

	@Override
	public AssetDailyReptDTO addAssetDailyRept(UiAssetDailyRept uiAssetDailyRept) {
		mongoUserAssectsRepository.save(uiAssetDailyRept);
		AssetDailyReptDTO assetDailyRept = new AssetDailyReptDTO();
		BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
		return assetDailyRept;
	}

	@Override
	public List<AssetDailyReptDTO> getAssetDailyReptByUserId(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository.findByUserId(userId);
		List<AssetDailyReptDTO> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,AssetDailyReptDTO.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPersonMsgDTO> getUiPersonMsg(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPersonMsg> uiPersonMsgList = mongoUserPersonMsgRepo.getUiPersonMsgsByUserIdAndReaded(userId, Boolean.FALSE);
		List<UserPersonMsgDTO> personMsgDtoList = MyBeanUtils.convertList(uiPersonMsgList,UserPersonMsgDTO.class);
		return personMsgDtoList;
	}

	@Override
	public List<UserProdMsgDTO> getUiProdMsg(Long prodId) throws IllegalAccessException, InstantiationException {
		List<UiProdMsg> uiProdMsgList = mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
		List<UserProdMsgDTO> uiProdMsgDtoList = MyBeanUtils.convertList(uiProdMsgList,UserProdMsgDTO.class);
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
	public List<UserSysMsgDTO> getUiSysMsg() throws IllegalAccessException, InstantiationException {
		List<UiSysMsg> uiSysMsgList = mongoUserSysMsgRepo.findAllByOrderByDateDesc();
		List<UserSysMsgDTO> uiSysMsgDtoList = MyBeanUtils.convertList(uiSysMsgList,UserSysMsgDTO.class);
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
	public UserPersonMsgDTO addUiPersonMsg(UiPersonMsg uiPersonMsg) {
		UiPersonMsg uiPersonMsgResult = mongoUserPersonMsgRepo.save(uiPersonMsg);
		UserPersonMsgDTO personMsg = new UserPersonMsgDTO();
		BeanUtils.copyProperties(uiPersonMsgResult, personMsg);
		return personMsg;
	}

	@Override
	public Page<UiTrdLog> findByUserId(Pageable pageable, Long userId) {
		Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findByUserId(pageable,userId);
		return uiTrdLogPage;
	}

	@Override
	public Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs) throws IllegalAccessException, InstantiationException {
		userTradeLogRepository.save(trdLogs);
		List<TradeLogDTO> trdLogsDtoList = MyBeanUtils.convertList(trdLogs,TradeLogDTO.class);
		return trdLogsDtoList;
	}

	@Override
	public List<UserInfoFriendRuleDTO> getUiFriendRule(Long bankId) throws IllegalAccessException, InstantiationException {
		List<UiFriendRule> uiFriendRuleList = new ArrayList<>();
		
		if (null == bankId) {
			uiFriendRuleList = userInfoFriendRuleRepository.findAll();
		} else {
			uiFriendRuleList =  userInfoFriendRuleRepository.findAllByBankId(bankId);
		}
		List<UserInfoFriendRuleDTO> trdLogsDtoList = MyBeanUtils.convertList(uiFriendRuleList,UserInfoFriendRuleDTO.class);
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

	@Override
	public void getUserId(UserIdQuery userIdQuery, StreamObserver<UserId> responseObserver){
		Long userId = null;
		try {
			userId = getUserIdFromUUID(userIdQuery.getUuid());
		} catch (Exception e) {
			e.printStackTrace();
		}
		responseObserver.onNext(UserId.newBuilder().setUserId(userId).build());
		responseObserver.onCompleted();
	}

	@Override
	public Boolean deleteBankCard(String userUuid,String cardNumber) {
		UiBankcard bank = new UiBankcard();
		bank.setCardNumber(cardNumber);
		bank.setUserId(Long.parseLong(userUuid));
		List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserIdAndCardNumber(Long.parseLong(userUuid),cardNumber);
		if(bankcardList==null||bankcardList.size()==0){
			return false;
		}
		UiBankcard bankcard = bankcardList.get(0);
		userInfoBankCardsRepository.delete(bankcard.getId());
		return true;
	}

	@Override
	public Boolean saveUser(String userUuid, String cellphone, String isTestFlag) {
		UiUser uiUser = new UiUser();
		uiUser.setId(Long.parseLong(userUuid));
		uiUser.setUuid(userUuid);
		byte activity = 1;
		uiUser.setActivated(activity);
		uiUser.setCellPhone(cellphone);
		uiUser.setOccupation("金融");
		uiUser.setCreatedBy("sys");
		uiUser.setIsTestFlag(isTestFlag);
		Date currentTime = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd HHmmss"); 
		String dateString = sdf.format(currentTime);
		dateString = dateString.replace(" ", "");
		BigInteger createDate = new BigInteger(dateString);
		uiUser.setCreatedDate(createDate);
		userInfoRepository.save(uiUser);
		return true;
	}

	@Override
	public Boolean updateCellphone(String cellphone, String isTestFlag) {
		List<UiUser> userList= userInfoRepository.findByCellPhone(cellphone);
		UiUser uiUser = new UiUser();
		Boolean flag = false;
		if(userList!=null&&userList.size()>0){
			uiUser = userList.get(0);
			uiUser.setIsTestFlag(isTestFlag);
			userInfoRepository.save(uiUser);
			return true;
		}
		return flag;
	}

	@Override
	public UserBaseInfoDTO findByCellphone(String cellphone) {
		List<UiUser> userList= userInfoRepository.findByCellPhone(cellphone);
		UiUser uiUser = new UiUser();
		UserBaseInfoDTO user = new UserBaseInfoDTO();
		Boolean flag = false;
		if(userList!=null&&userList.size()>0){
			uiUser = userList.get(0);
			BeanUtils.copyProperties(uiUser, user);
			return user;
		}
		return user;
	}
}
