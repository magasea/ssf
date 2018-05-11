package com.shellshellfish.aaas.userinfo.dao.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.mongodb.client.result.UpdateResult;
import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.grpc.ItemStatus;
import com.shellshellfish.aaas.common.exceptions.ErrorConstants;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.SellPersentProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductDetail;
import com.shellshellfish.aaas.userinfo.grpc.SellProductDetailResult;
import com.shellshellfish.aaas.userinfo.grpc.SellProducts;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult;
import com.shellshellfish.aaas.userinfo.grpc.SellProductsResult.Builder;
import com.shellshellfish.aaas.userinfo.grpc.UpdateUserProdReq;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserId;
import com.shellshellfish.aaas.userinfo.grpc.UserIdQuery;
import com.shellshellfish.aaas.userinfo.grpc.UserInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserInfoServiceGrpc;
import com.shellshellfish.aaas.userinfo.grpc.UserUUID;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiAsset;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiPortfolio;
import com.shellshellfish.aaas.userinfo.model.dao.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserProdMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.model.redis.UserBaseInfoRedis;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserAssectsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserPersonMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserProdMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserSysMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUserTrdLogMsgRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoAssetsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoCompanyInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoFriendRuleRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserPortfolioRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserTradeLogRepository;
import com.shellshellfish.aaas.userinfo.repositories.redis.UserInfoBaseDao;
import com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl;
import com.shellshellfish.aaas.userinfo.utils.MongoUiTrdLogUtil;
import io.grpc.stub.StreamObserver;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

@Service
public class UserInfoRepoServiceImpl extends UserInfoServiceGrpc.UserInfoServiceImplBase
		implements UserInfoRepoService {

	Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

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
	UiProductRepo uiProductRepo;

	@Autowired
	UiProductDetailRepo uiProductDetailRepo;

	@Autowired
	MongoUserAssectsRepository mongoUserAssectsRepository;

	@Autowired
	MongoUserPersonMsgRepo mongoUserPersonMsgRepo;

	@Autowired
	MongoUserSysMsgRepo mongoUserSysMsgRepo;

	@Autowired
	MongoUserProdMsgRepo mongoUserProdMsgRepo;
	
	@Autowired
	MongoUserTrdLogMsgRepo mongoUserTrdLogMsgRepo;

	@Autowired
	MongoTemplate mongoTemplate;

	@Resource
	UserInfoBaseDao userInfoBaseDao;

	@Override
	public UserBaseInfoDTO getUserInfoBase(Long id) {
		// BigInteger userIdLocal = BigInteger.valueOf(id);
		logger.info(
				"com.shellshellfish.aaas.userinfo.dao.service.impl.UserInfoRepoServiceImpl.getUserInfoBase(Long)  start"
						+ id);
		Optional<UiUser> uiUser = userInfoRepository.findById(id);
		logger.info("==>" + uiUser);
		UserBaseInfoDTO user = new UserBaseInfoDTO();
		BeanUtils.copyProperties(uiUser.get(), user);
		logger.info(
				"com.shellshellfish.aaas.userinfo.dao.service.impl.UserInfoRepoServiceImpl.getUserInfoBase(Long)  end"
						+ user);
		return user;
	}
	
	@Override
	public UserBaseInfoDTO getUserInfo(String uuid) {
		UiUser uiUser = userInfoRepository.findByUuid(uuid);
		logger.info("==>" + uiUser);
		UserBaseInfoDTO user = new UserBaseInfoDTO();
		if (uiUser != null) {
			BeanUtils.copyProperties(uiUser, user);
		}
		return user;
	}

	@Override
	public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(Long userId) {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		UiAsset uiAsset = userInfoAssetsRepository.findByUserId(userId);
		UserInfoAssectsBriefDTO asset = new UserInfoAssectsBriefDTO();
		if (uiAsset != null) {
			BeanUtils.copyProperties(uiAsset, asset);
		}
		return asset;
	}

	@Override
	public List<BankCardDTO> getUserInfoBankCards(Long userId) throws IllegalAccessException, InstantiationException {
		// BigInteger userIdLocal = BigInteger.valueOf(userId);
		List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserIdAndStatusIs
				(userId, BankCardStatusEnum.VALID.getStatus());
		List<BankCardDTO> bankcardDtoList = MyBeanUtils.convertList(bankcardList, BankCardDTO.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPortfolioDTO> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPortfolio> uiPortfolioList = userPortfolioRepository.findAllByUserId(userId);
		List<UserPortfolioDTO> bankcardDtoList = MyBeanUtils.convertList(uiPortfolioList, UserPortfolioDTO.class);
		return bankcardDtoList;
	}

	@Override
	public BankCardDTO getUserInfoBankCard(String cardNumber) {
		List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findUiBankcardByCardNumberIsAndStatusIsNot
				(cardNumber, -1);
		BankCardDTO bankcard = new BankCardDTO();
		if (!CollectionUtils.isEmpty(uiBankcards)) {
			BeanUtils.copyProperties(uiBankcards.get(0), bankcard);
		}
		return bankcard;
	}

	@Override
	public BankCardDTO addUserBankcard(UiBankcard uiBankcard) throws Exception {

		List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findAllByUserIdAndCardNumber
				(uiBankcard.getUserId(), uiBankcard.getCardNumber());
		if(!CollectionUtils.isEmpty(uiBankcards)){
			logger.info("update bankcard status to 1 for userId:" + uiBankcard.getUserId() + " and "
					+ "bankCardNumber:" + uiBankcard.getCardNumber());
			if(uiBankcards.size() > 1){
				logger.error("there is more than 1 same cardNumber for for userId:" + uiBankcard.getUserId
						() + " and bankCardNumber:" + uiBankcard.getCardNumber());
				throw new Exception("duplicated card number for :" + uiBankcard.getCardNumber());
			}else{
				uiBankcards.get(0).setStatus(1);
				uiBankcards.get(0).setUserPid(uiBankcard.getUserPid());
				uiBankcards.get(0).setCellphone(uiBankcard.getCellphone());
				userInfoBankCardsRepository.save(uiBankcards.get(0));
			}
		}else{
			userInfoBankCardsRepository.save(uiBankcard);
		}
		BankCardDTO bankcard = new BankCardDTO();
		BeanUtils.copyProperties(uiBankcard, bankcard);
		return bankcard;
	}

	@Override
	public List<AssetDailyReptDTO> getAssetDailyRept(Long userId, Long beginDate, Long endDate)
			throws IllegalAccessException, InstantiationException {
		Query query = new Query();
		query.addCriteria(Criteria.where("userId").is(userId));
		Date beginDated = new Date(beginDate);
		query.addCriteria(Criteria.where("date").gte(beginDate).lt(endDate));
		System.out.println("userId:" + userId + " beginDate:" + beginDate + " endDate:" + endDate);
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository
				.findByUserIdAndDateIsBetween(BigInteger.valueOf(userId), beginDate, endDate);
		List<AssetDailyReptDTO> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,
				AssetDailyReptDTO.class);
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
	public List<AssetDailyReptDTO> getAssetDailyReptByUserId(Long userId)
			throws IllegalAccessException, InstantiationException {
		List<UiAssetDailyRept> uiAssetDailyReptList = mongoUserAssectsRepository.findByUserId(userId);
		List<AssetDailyReptDTO> bankcardDtoList = MyBeanUtils.convertList(uiAssetDailyReptList,
				AssetDailyReptDTO.class);
		return bankcardDtoList;
	}

	@Override
	public List<UserPersonMsgDTO> getUiPersonMsg(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiPersonMsg> uiPersonMsgList = mongoUserPersonMsgRepo.findByUserIdAndReadedOrderByCreatedDateDesc(userId.toString(),
				Boolean.FALSE);
		List<UserPersonMsgDTO> personMsgDtoList = MyBeanUtils.convertList(uiPersonMsgList, UserPersonMsgDTO.class);
		return personMsgDtoList;
	}

	@Override
	public List<UserProdMsgDTO> getUiProdMsg(Long prodId) throws IllegalAccessException, InstantiationException {
		List<UiProdMsg> uiProdMsgList = mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
		List<UserProdMsgDTO> uiProdMsgDtoList = MyBeanUtils.convertList(uiProdMsgList, UserProdMsgDTO.class);
		return uiProdMsgDtoList;
	}

	@Override
	public Boolean updateUiUserPersonMsg(String msg, Long userId, Boolean readedStatus) {
		Query query = new Query();
		query.addCriteria(Criteria.where("id").is(msg).and("userId").is(userId.toString()));
		Update update = new Update();
		update.set("readed", readedStatus);
		UpdateResult result = mongoTemplate.updateMulti(query, update, UiPersonMsg.class);
		System.out.println(result);
		return result.isModifiedCountAvailable();
	}

	@Override
	public List<UserSysMsgDTO> getUiSysMsg() throws IllegalAccessException, InstantiationException {
		List<UiSysMsg> uiSysMsgList = mongoUserSysMsgRepo.findAllByOrderByCreatedDateDesc();
		List<UserSysMsgDTO> uiSysMsgDtoList = MyBeanUtils.convertList(uiSysMsgList, UserSysMsgDTO.class);
		return uiSysMsgDtoList;
	}
	
	@Override
	public List<MongoUiTrdLogDTO> findByUserIdAndProdId(Long userId,Long userProdId) throws IllegalAccessException, InstantiationException {
		List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserIdAndUserProdId(userId,userProdId);
		List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils.convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
		return mongoUiTrdLogDtoList;
	}

	@Override
	public List<MongoUiTrdLogDTO> findByUserProdIdAndOperType(Long userProdId, Integer operType)
			throws
			IllegalAccessException, InstantiationException {
		List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserProdIdAndOperations
				(userProdId, operType);
		List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils.convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
		return mongoUiTrdLogDtoList;
	}

	@Override
	public List<MongoUiTrdLogDTO> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId,Long userProdId,int operations,int tradeStatus) throws IllegalAccessException, InstantiationException {
		List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(userId,userProdId,operations,tradeStatus);
		List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils.convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
		return mongoUiTrdLogDtoList;
	}
	@Override
	public List<MongoUiTrdLogDTO> findByUserId(Long userId) throws IllegalAccessException, InstantiationException {
		Criteria criteria = Criteria.where("user_id").is(userId);
		Query query = new Query(criteria);
		List<MongoUiTrdLog> mongoUiTrdLogList = mongoTemplate.find(query, MongoUiTrdLog.class);
//		List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserId(userId);
		List<MongoUiTrdLog> uiTrdLogsUnique =  MongoUiTrdLogUtil.getDistinct(mongoUiTrdLogList);
		List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils.convertList(uiTrdLogsUnique, MongoUiTrdLogDTO.class);
		return mongoUiTrdLogDtoList;
	}




	@Override
	public UserSysMsgDTO addUiSysMsg(UiSysMsg uiSysMsg) throws IllegalAccessException,
			InstantiationException {
		UiSysMsg uiSysMsgResult = mongoUserSysMsgRepo.save(uiSysMsg);
		UserSysMsgDTO uiSysMsgDto = new UserSysMsgDTO();
		BeanUtils.copyProperties(uiSysMsgResult, uiSysMsgDto);
		return uiSysMsgDto;
	}

	@Override
	public Long getUserIdFromUUID(String userUuid) throws Exception {
		UserBaseInfoRedis userBaseInfoRedis = userInfoBaseDao.get(userUuid);
		UiUser uiUser = null;
		Long userId = new Long(0);
		if(userBaseInfoRedis == null){
			uiUser = userInfoRepository.findByUuid(userUuid);
			if (null == uiUser) {
				logger.error("not vaild userUuid:{}", userUuid);
				throw new Exception("not vaild userUuid:" + userUuid);
			} else {
				userId = uiUser.getId();
				userBaseInfoRedis = new UserBaseInfoRedis();
				MyBeanUtils.mapEntityIntoDTO(uiUser, userBaseInfoRedis);
				userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
			}
		}else{
			userId = userBaseInfoRedis.getId();
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
	public Page<UiTrdLog> findTradeLogDtoByUserId(Pageable pageable, Long userId) {
		Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findByUserId(pageable, userId);
		return uiTrdLogPage;
	}

	@Override
	public List<TradeLogDTO> findTradeLogDtoByUserId(Long userId) throws IllegalAccessException, InstantiationException {
		List<UiTrdLog> uiTrdLogList = userTradeLogRepository.findByUserId(userId);
		List<TradeLogDTO> trdLogsDtoList = MyBeanUtils.convertList(uiTrdLogList, TradeLogDTO.class);
		return trdLogsDtoList;
	}

	@Override
	public Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs)
			throws IllegalAccessException, InstantiationException {
		userTradeLogRepository.saveAll(trdLogs);
		// FIXME
		List<TradeLogDTO> trdLogsDtoList = MyBeanUtils.convertList(trdLogs, TradeLogDTO.class);
		return trdLogsDtoList;
	}

	@Override
	public List<UserInfoFriendRuleDTO> getUiFriendRule(Long bankId)
			throws IllegalAccessException, InstantiationException {
		List<UiFriendRule> uiFriendRuleList = new ArrayList<>();

		if (null == bankId) {
			uiFriendRuleList = userInfoFriendRuleRepository.findAll();
		} else {
			uiFriendRuleList = userInfoFriendRuleRepository.findAllByBankId(bankId);
		}
		List<UserInfoFriendRuleDTO> trdLogsDtoList = MyBeanUtils.convertList(uiFriendRuleList,
				UserInfoFriendRuleDTO.class);
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
	public void getUserId(UserIdQuery userIdQuery, StreamObserver<UserId> responseObserver) {
		Long userId = null;
		try {
			userId = getUserIdFromUUID(userIdQuery.getUuid());
		} catch (Exception e) {
			logger.error("exception:",e);
		}
		responseObserver.onNext(UserId.newBuilder().setUserId(userId).build());
		responseObserver.onCompleted();
	}

	@Override
	@Transactional
	public Boolean deleteBankCard(String userUuid, String cardNumber) {
		Long userId = null;
		try {
			userId = getUserIdFromUUID(userUuid);
		} catch (Exception e) {
			logger.error("exception:",e);
		}
		List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserIdAndCardNumber(userId, cardNumber);
		if (CollectionUtils.isEmpty(bankcardList)) {
			logger.error("用户:{}, 要解绑的银行卡:{} 不存在", userId, cardNumber);
			throw new UserInfoException("404", String.format("用户:%s, 要解绑的银行卡:%s 不存在", userId, cardNumber));
		}
		//用状态来控制银行卡
		userInfoBankCardsRepository.setBankCardInvalid(userId, cardNumber);

		return true;
	}

	@Override
	public Boolean saveUser(String userUuid, String cellphone, String isTestFlag) {
		UiUser uiUser = new UiUser();
		List<UiUser> userList = userInfoRepository.findByCellPhone(cellphone);
		if (userList != null && userList.size() > 0) {
			uiUser = userList.get(0);
		}
		uiUser.setUuid(userUuid);
		int activity = 1;
		uiUser.setActivated(activity);
		uiUser.setCellPhone(cellphone);
		uiUser.setOccupation("金融");
		uiUser.setCreatedBy("" + SystemUserEnum.SYSTEM_USER_ENUM.ordinal());
		// uiUser.setIsTestFlag(UserRiskTestFlagEnum.valueOf(isTestFlag).getRiskTestFlag());
		uiUser.setIsTestFlag(Integer.valueOf(isTestFlag));
		uiUser.setCreatedDate(TradeUtil.getUTCTime());
		uiUser = userInfoRepository.save(uiUser);
		UserBaseInfoRedis userBaseInfoRedis = new UserBaseInfoRedis();
		MyBeanUtils.mapEntityIntoDTO(uiUser, userBaseInfoRedis);
		if(userInfoBaseDao.get(userUuid) == null){
			userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
		}else{
			userInfoBaseDao.updateUserBaseInfo(userBaseInfoRedis);
		}
		return true;
	}

	@Override
	public Boolean updateCellphone(String cellphone, String isTestFlag, String riskLevel) {
		List<UiUser> userList = userInfoRepository.findByCellPhone(cellphone);
		UiUser uiUser = new UiUser();
		Boolean flag = false;
		if (userList != null && userList.size() > 0) {
			uiUser = userList.get(0);
			// uiUser.setIsTestFlag(UserRiskTestFlagEnum.valueOf(isTestFlag).getRiskTestFlag());
			uiUser.setIsTestFlag(Integer.valueOf(isTestFlag));
			//uiUser.setRiskLevel(UserRiskLevelEnum.valueOf(riskLevel).getRiskLevel());
			uiUser.setRiskLevel(UserRiskLevelEnum.get(riskLevel).getRiskLevel());
			userInfoRepository.save(uiUser);
			UserBaseInfoRedis userBaseInfoRedis = new UserBaseInfoRedis();
			MyBeanUtils.mapEntityIntoDTO(uiUser, userBaseInfoRedis);
			if(userInfoBaseDao.get(uiUser.getUuid()) == null){
				userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
			}else{
				userInfoBaseDao.updateUserBaseInfo(userBaseInfoRedis);
			}
			return true;
		}
		return flag;
	}

	@Override
	public UserBaseInfoDTO findByCellphone(String cellphone) {
		List<UiUser> userList = userInfoRepository.findByCellPhone(cellphone);
		UiUser uiUser = new UiUser();
		UserBaseInfoDTO user = new UserBaseInfoDTO();
		Boolean flag = false;
		if (userList != null && userList.size() > 0) {
			uiUser = userList.get(0);
			BeanUtils.copyProperties(uiUser, user);
			return user;
		}
		return user;
	}


	/**
	 */
	public void getUserBankInfo(com.shellshellfish.aaas.userinfo.grpc.UserIdOrUUIDQuery request,
								io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserBankInfo> responseObserver) {
		Long userId = request.getUserId();
		String userUUID = request.getUuid();
		UiUser uiUser = null;
		if(userId <= 0){
			logger.error("userId is not valid:" + userId);
			if(StringUtils.isEmpty(userUUID)){
				logger.error("userId and userUUID both is not valid:" + userId + " "+ userUUID);
				//Todo: 是否直接返回？
				return;
			}else{
				try {
					uiUser = getUserInfoByUserUUID(userUUID);
					userId = uiUser.getId();
				} catch (Exception e) {
					logger.error("exception:",e);
					logger.error("failed to retrieve userId by userUUID:" + userUUID);
					return;
				}
			}
		}else{
			uiUser = getUserInfoByUserId(userId);
		}
		List<BankCardDTO> bankCardDTOS = null;
		try {
			bankCardDTOS = getUserInfoBankCards(userId);
		} catch (IllegalAccessException e) {
			logger.error("exception:",e);
		} catch (InstantiationException e) {
			logger.error("exception:",e);
		}
		UserBankInfo.Builder builder = UserBankInfo.newBuilder();
		builder.setUserId(userId);

		if( bankCardDTOS.size() <= 0 ){
			logger.error("failed to find bankCards by userId:" + userId);
			responseObserver.onNext(builder.build());
			responseObserver.onCompleted();
			return;
		}
		builder.setUserName(bankCardDTOS.get(0).getUserName());

		builder.setUuid(request.getUuid());
		builder.setCellphone(bankCardDTOS.get(0).getCellphone());
		CardInfo.Builder ciBuilder = CardInfo.newBuilder();
		if(null != uiUser.getRiskLevel()){
			builder.setRiskLevel(uiUser.getRiskLevel());
		}else{
			builder.setRiskLevel(-1);
		}
		for(int idx = 0; idx < bankCardDTOS.size(); idx++){
			ciBuilder.setCardNumbers(bankCardDTOS.get(idx).getCardNumber());
			ciBuilder.setUserPid(bankCardDTOS.get(idx).getUserPid());
			builder.addCardNumbers(ciBuilder);
			ciBuilder.clear();
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}


	/**
	 */
	public void genUserProdsFromOrder(com.shellshellfish.aaas.userinfo.grpc.FinanceProdInfosQuery request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.grpc.common.UserProdId>
			responseObserver) {
		UserProdId.Builder respBuilder = UserProdId.newBuilder();
		List<com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo> financeProdInfoList =
				request.getProdListList();
		if(CollectionUtils.isEmpty(financeProdInfoList)){
			logger.error("FinanceProdInfoCollection is empty! will return -1");
			respBuilder.setUserProdId(-1L);
			responseObserver.onNext(respBuilder.build());
			responseObserver.onCompleted();
			return;
		}
		try{
			List<com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo> financeProdInfosValue =
					financeProdInfoList;
			FinanceProdInfo financeProdInfoFirst = financeProdInfosValue.get(0);
			logger.info("financeProdInfoFirst is:" + financeProdInfoFirst + " prodName:"
					+ " "+financeProdInfoFirst.getProdName() + "groupId:"+financeProdInfoFirst.getGroupId() +
					"prodId:" +financeProdInfoFirst.getProdId()  );
			UiProducts uiProducts = new UiProducts();
			uiProducts.setCreateBy(request.getUserId());
			BeanUtils.copyProperties(financeProdInfoFirst, uiProducts);
			uiProducts.setProdId(financeProdInfoFirst.getProdId());
			uiProducts.setGroupId(financeProdInfoFirst.getGroupId());
			uiProducts.setCreateDate(TradeUtil.getUTCTime());
			uiProducts.setUpdateBy(request.getUserId());
			uiProducts.setUpdateDate(TradeUtil.getUTCTime());
			uiProducts.setStatus(TrdOrderStatusEnum.WAITPAY.getStatus());
			uiProducts.setUserId(request.getUserId());
			uiProducts.setBankCardNum(request.getBankCardNum());
			UiProducts saveResult = uiProductRepo.save(uiProducts);
			Long userProdId = saveResult.getId();
			logger.info("saved UiProducts with result id:" + userProdId);
			for(FinanceProdInfo financeProdInfo: financeProdInfosValue){
				UiProductDetail uiProductDetail = new UiProductDetail();
				BeanUtils.copyProperties(financeProdInfo, uiProductDetail);
				uiProductDetail.setCreateBy(request.getUserId());
				uiProductDetail.setCreateDate(TradeUtil.getUTCTime());
				uiProductDetail.setUpdateBy(request.getUserId());
				uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
				uiProductDetail.setUserProdId(userProdId);
				uiProductDetailRepo.save(uiProductDetail);
			}
			respBuilder.setUserProdId(userProdId);
			responseObserver.onNext(respBuilder.build());
			responseObserver.onCompleted();
			return;
		}catch (Exception ex){

			respBuilder.setUserProdId(-1L);
			responseObserver.onNext(respBuilder.build());
			responseObserver.onCompleted();
			return;
		}

	}

	@Override
	public ProductsDTO findByProdId(String prodId) {
		if(StringUtils.isEmpty(prodId)){
			logger.error("智投组合产品id不能为空");
			throw new UserInfoException("404", "智投组合产品id不能为空");
		}
		Optional<UiProducts> productsData = uiProductRepo.findById(Long.valueOf(prodId));
		if (!productsData.isPresent()) {
			logger.error("智投组合产品：{}为空", prodId);
			throw new UserInfoException("404", "智投组合产品：" + prodId + "为空");
		}
		ProductsDTO product = new ProductsDTO();
		BeanUtils.copyProperties(productsData.get(), product);
		return product;
	}

	@Override
	public List<ProductsDTO> findTradeLogDtoByUserId(String uuid) throws IllegalAccessException, InstantiationException {
		if(StringUtils.isEmpty(uuid)){
			logger.error("用户uuid不能为空");
			throw new UserInfoException("404", "用户uuid不能为空");
		}
		Long userId = null;
		try {
			userId = getUserIdFromUUID(uuid);
		} catch (Exception e) {
			logger.error("exception:",e);
		}
		List<ProductsDTO> productsList = new ArrayList<>();
		List<UiProducts> productsData = uiProductRepo.findByUserId(userId);
		if (productsData == null||productsData.size()==0) {
			//throw new UserInfoException("404", "用户："+userId+"为空");
		} else {
			productsList = MyBeanUtils.convertList(productsData,ProductsDTO.class);
		}
		
		
		return productsList;
	}

	@Override
	public void getUerUUIDByUserId(com.shellshellfish.aaas.userinfo.grpc.UserId request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserUUID> responseObserver) {
			String userUUID = findUserUUIDByUserId(request.getUserId());
			if(StringUtils.isEmpty(userUUID)){
				logger.error("cause the userId:" + request.getUserId() + " cannot find corresponding "
						+ "uiuser, use -1 to return");
				userUUID = "-1";
			}
			UserUUID.Builder builder = UserUUID.newBuilder();
			builder.setUserUUID(userUUID);
			responseObserver.onNext(builder.build());
			responseObserver.onCompleted();
	}

	@Override
	public String findUserUUIDByUserId(Long userId) {
		Optional<UiUser> uiUser =  userInfoRepository.findById(userId);
		if(null != uiUser){
			return uiUser.get().getUuid();
		}else{
			logger.error("failed to find user by userId:" + userId);
			return null;
		}
	}


	/**
	 */
	@Override
	public void updateUserProd(com.shellshellfish.aaas.userinfo.grpc.UpdateUserProdReqs request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.grpc.common.UserProdId>
					responseObserver) {
//		request
//		uiProductRepo.findByProdId(request.getProdId())
		UiProducts uiProducts = uiProductRepo.findByProdId(request.getUserProdId());
		if(null == uiProducts){
			logger.error("the userProdId:"+request.getUserProdId()+" intended to be updated by preOrder process "
					+ "is not fund, now need to save new uiProduct");
			uiProducts = new UiProducts();
			uiProducts.setStatus(TrdOrderStatusEnum.CONVERTWAITCONFIRM.getStatus());
			uiProducts.setProdId(request.getProdId());
			uiProducts.setGroupId(request.getGroupId());
//			uiProducts.setCreateDate(TradeUtil.getUTCTime());
//			uiProducts.setUpdateDate(TradeUtil.getUTCTime());
			uiProducts.setCreateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
			uiProducts.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
			uiProductRepo.save(uiProducts);
			long userProdId = uiProducts.getId();
			logger.info("the preorder recreate the product with userProdId:"+ userProdId);
			for(UpdateUserProdReq updateUserProdReq: request.getUpdateUserProdReqList()){
				UiProductDetail uiProductDetail = new UiProductDetail();
				uiProductDetail.setUserProdId(userProdId);
				uiProductDetail.setFundCode(updateUserProdReq.getFundCode());
				uiProductDetail.setFundShare(updateUserProdReq.getFundShare());
//				uiProductDetail.setCreateDate(TradeUtil.getUTCTime());
//				uiProductDetail.setCreateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
				uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
				uiProductDetail.setUpdateBy(SystemUserEnum.SYSTEM_USER_ENUM.getUserId());
				uiProductDetailRepo.save(uiProductDetail);
			}
		}else{
			uiProductRepo.updateUiProductsById(SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), TradeUtil
					.getUTCTime(), request.getUserProdId());
			for(UpdateUserProdReq updateUserProdReq: request.getUpdateUserProdReqList()){
				uiProductDetailRepo.updateFundShareByParam(updateUserProdReq.getFundShare(),TradeUtil
								.getUTCTime(), SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), updateUserProdReq
								.getUserProdId(), updateUserProdReq.getFundCode());
			}
		}
	}

	/**
	 */
	@Override
	public void getUserInfo(com.shellshellfish.aaas.userinfo.grpc.UserId request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserInfo> responseObserver) {
		UiUser uiUser = userInfoRepository.findById(request.getUserId());
		UserInfo.Builder uiBuilder = UserInfo.newBuilder();
		MyBeanUtils.mapEntityIntoDTO(uiUser, uiBuilder);
		responseObserver.onNext(uiBuilder.build());
		responseObserver.onCompleted();
	}

	/**
	 */
	/**
	 */
	public void sellUserProducts(com.shellshellfish.aaas.userinfo.grpc.SellProducts request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.SellProductsResult> responseObserver) {

		SellProductsResult.Builder result = null;
		try {
			result = updateProductQuantity(request);
		} catch (Exception e) {
			logger.error("exception:",e);
			ErrInfo.Builder eiBuilder = ErrInfo.newBuilder();
			eiBuilder.setErrCode(ErrorConstants.GRPC_ERROR_UI_CHECKSELL_FAIL_GENERAL);
			eiBuilder.setErrMsg(e.getMessage());
			result.setErrInfo(eiBuilder);
		}
		responseObserver.onNext(result.build());
		responseObserver.onCompleted();

	}


	@Override
	@Transactional
	public Builder updateProductQuantity(SellProducts request) throws Exception {
		//检查出售的每个基金在当前用户拥有的产品有足够的份额
		UiProducts uiProducts = uiProductRepo.findById(request.getUserProductId());
		List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(request
				.getUserProductId());
		Map<String, UiProductDetail> currentAvailableFunds = new HashMap<>();
//		Map<String, Integer> currentFundsStatus = new HashMap<>();
		for(UiProductDetail uiProductDetail: uiProductDetails){
			currentAvailableFunds.put(uiProductDetail.getFundCode(), uiProductDetail);
//			currentFundsStatus.put(uiProductDetail.getFundCode(), uiProductDetail.getStatus());
		}
		SellProductsResult.Builder sprBuilder = SellProductsResult.newBuilder();
		SellProductDetailResult.Builder spdrBuilder = SellProductDetailResult.newBuilder();
		boolean canDuduct = true;
		for(SellProductDetail sellProductDetail: request.getSellProductDetailsList()){
			logger.info("check fundCode:" + sellProductDetail.getFundCode() + " of userProdId:" +
					request.getUserProductId());
			spdrBuilder.setFundCode(sellProductDetail.getFundCode());
			spdrBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
			UiProductDetail currentProductDetail = currentAvailableFunds.get(sellProductDetail
					.getFundCode());
			if(currentProductDetail == null){
				logger.error("no currentProductDetail info available for fundCode:{}", sellProductDetail
						.getFundCode());
				throw new Exception(String.format("no currentProductDetail info available for "
						+ "fundCode:%s", sellProductDetail
						.getFundCode()));
//				spdrBuilder.setResult(-1);
//				long smallestRemain = -1;
//				spdrBuilder.setFundQuantityTrade(smallestRemain);
//				canDuduct = false;
//				sprBuilder.addSellProductDetailResults(spdrBuilder);
//				continue;
			}
			long currentAvailFundQuantityTrade = currentProductDetail.getFundQuantityTrade()
					== null ? 0L : currentProductDetail.getFundQuantityTrade();

			if(sellProductDetail.getFundQuantityTrade() > currentAvailFundQuantityTrade)
			{
				spdrBuilder.setResult(-1);
				long smallestRemain = currentAvailFundQuantityTrade;
				spdrBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
				spdrBuilder.setFundQuantityTradeRemain(smallestRemain);
				logger.error("the userProdId:{}'s fundCode:{} will be dump to 0 because "
						+ "getFundQuantityTrade:{} exceed the smallestRemain:{}",
						request.getUserProductId(), sellProductDetail.getFundCode(), sellProductDetail
						.getFundQuantityTrade(), smallestRemain);
			}
			if(currentProductDetail.getStatus() != null && currentProductDetail.getStatus() ==
					TrdOrderStatusEnum.WAITSELL.getStatus()){
				logger.error("fundCode:{} is in WAITSELL status:{}", currentProductDetail.getFundCode(),
						currentProductDetail.getStatus());
				throw new Exception(String.format("fundCode:%s is in WAITSELL status:%s",
						currentProductDetail.getFundCode(), currentProductDetail.getStatus()));
			}
			sprBuilder.addSellProductDetailResults(spdrBuilder);
			spdrBuilder.clear();
		}

		sprBuilder.setUserId(request.getUserId());
		sprBuilder.setUserProductId(request.getUserProductId());
		if(!StringUtils.isEmpty(uiProducts.getBankCardNum())){
			sprBuilder.setUserBankNum(uiProducts.getBankCardNum());
		}


		if(canDuduct){
			Long fundQuantityRemain = null;
//			for(SellProductDetail sellProductDetail: request.getSellProductDetailsList()){
//				Integer fundQuantityTrade = currentAvailableFunds.get(sellProductDetail.getFundCode())
//						.getFundQuantityTrade() ;
//				if(fundQuantityTrade == null || fundQuantityTrade <=0){
//					fundQuantityRemain = 0L;
//				}else if(fundQuantityTrade < sellProductDetail.getFundQuantityTrade()){
//					fundQuantityRemain = 0L;
//				}else{
//					fundQuantityRemain = currentAvailableFunds.get(sellProductDetail.getFundCode()).getFundQuantityTrade() -
//							sellProductDetail.getFundQuantityTrade();
//				}
//				uiProductDetailRepo.updateByParamDeductTrade(fundQuantityRemain, TradeUtil.getUTCTime(),
//						request.getUserId(), request.getUserProductId(),sellProductDetail.getFundCode(),
//						TrdOrderStatusEnum.WAITSELL.getStatus() );
//			}

			for(SellProductDetailResult.Builder sellProductDetail: sprBuilder
					.getSellProductDetailResultsBuilderList()){

				if(sellProductDetail.getResult() == -1){
					logger.error("Because the target sell amount exceed remain ammount, now use remain "
							+ "amount to update uiProductDetail:{} and default make the fundQuantityTrade to 0",
							sellProductDetail.getFundQuantityTradeRemain());
					uiProductDetailRepo.updateByParamDeductTrade(0L, TradeUtil.getUTCTime(),
							request.getUserId(), request.getUserProductId(),sellProductDetail.getFundCode(),
							TrdOrderStatusEnum.WAITSELL.getStatus() );
				}else{
					fundQuantityRemain = currentAvailableFunds.get(sellProductDetail.getFundCode()).getFundQuantityTrade() -
							sellProductDetail.getFundQuantityTrade();
					if(fundQuantityRemain < 0){
						logger.error("cannot deduct the fundQuantity, because getFundQuantityTrade:{} is less "
								+ "than target sellProductDetail.getFundQuantityTrade:{}",currentAvailableFunds.get
								(sellProductDetail.getFundCode()).getFundQuantityTrade(), sellProductDetail.getFundQuantityTrade());
						sellProductDetail.setResult(-1);
						sellProductDetail.setFundQuantityTradeRemain(currentAvailableFunds.get
								(sellProductDetail.getFundCode()).getFundQuantityTrade());
						uiProductDetailRepo.updateByParamDeductTrade(0L, TradeUtil.getUTCTime(),
								request.getUserId(), request.getUserProductId(),sellProductDetail.getFundCode(),
								TrdOrderStatusEnum.WAITSELL.getStatus() );
					}else{
						uiProductDetailRepo.updateByParamDeductTrade(fundQuantityRemain, TradeUtil.getUTCTime(),
								request.getUserId(), request.getUserProductId(),sellProductDetail.getFundCode(),
								TrdOrderStatusEnum.WAITSELL.getStatus() );
					}
				}
			}


		}
		return sprBuilder;
	}

	@Override
	public UiUser getUserInfoByUserId(Long userId) {
		return userInfoRepository.findById(userId).get();
	}

	@Override
	public UiUser getUserInfoByUserUUID(String userUUID) throws Exception {
		UserBaseInfoRedis userBaseInfoRedis = userInfoBaseDao.get(userUUID);
		UiUser uiUser = null;
		Long userId = new Long(0);
		if(userBaseInfoRedis == null){
			uiUser = userInfoRepository.findByUuid(userUUID);
			if (null == uiUser) {
				logger.error("not vaild userUuid:{}", userUUID);
				throw new Exception("not vaild userUuid:" + userUUID);
			} else {
				userBaseInfoRedis = new UserBaseInfoRedis();
				MyBeanUtils.mapEntityIntoDTO(uiUser, userBaseInfoRedis);
				userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
			}
		}else{
			uiUser = new UiUser();
			MyBeanUtils.mapEntityIntoDTO(userBaseInfoRedis, uiUser);
		}
		return uiUser;
	}

	/**
	 */
	@Override
	public void getUserInfoByUserUUID(com.shellshellfish.aaas.userinfo.grpc.UserIdQuery request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserInfo> responseObserver) {
		UiUser uiUser = null;
		try {
			uiUser = getUserInfoByUserUUID(request.getUuid());
		} catch (Exception e) {
			logger.error("exception:",e);
			logger.error("failed to find uiUser by uuid:" + request.getUuid());
		}
		UserInfo.Builder uiBuilder = UserInfo.newBuilder();
		if(uiUser.getRiskLevel() == null || uiUser.getRiskLevel() < 0){
			logger.error("this user haven't done risk evaluate");
			uiUser.setRiskLevel(-1);
		}
		MyBeanUtils.mapEntityIntoDTO(uiUser, uiBuilder);
		responseObserver.onNext(uiBuilder.build());
		responseObserver.onCompleted();
	}

	/**
	 */
	public void rollbackUserProducts(com.shellshellfish.aaas.userinfo.grpc.SellProducts request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.SellProducts> responseObserver) {
		SellProducts result = rollbackUserProducts(request);

		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	private SellProducts rollbackUserProducts(SellProducts request) {
		//检查出售的每个基金在当前用户拥有的产品有足够的份额
		UiProducts uiProducts = uiProductRepo.findById(request.getUserProductId());
		List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(request
				.getUserProductId());
		Map<String, Long[]> currentAvailableFunds = new HashMap<>();
		for(UiProductDetail uiProductDetail: uiProductDetails){
			Long[] currentAndOrigin = {Long.valueOf(uiProductDetail.getFundQuantityTrade()),
					Long.valueOf(uiProductDetail.getFundQuantity())};
			currentAvailableFunds.put(uiProductDetail.getFundCode(), currentAndOrigin);
		}
		SellProducts.Builder spBuilder = SellProducts.newBuilder();
		SellProductDetail.Builder spdBuilder = SellProductDetail.newBuilder();
		boolean canRollback = true;
		for(SellProductDetail sellProductDetail: request.getSellProductDetailsList()){
			logger.info("check fundCode:" + sellProductDetail.getFundCode() + " of userProdId:" +
					request.getUserProductId());
			spdBuilder.setFundCode(sellProductDetail.getFundCode());
			spdBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
			if(sellProductDetail.getFundQuantityTrade() + currentAvailableFunds.get(sellProductDetail
					.getFundCode())[0]	> currentAvailableFunds.get(sellProductDetail.getFundCode())[1]){
				spdBuilder.setResult(-1);
				spdBuilder.setFundQuantityTrade(currentAvailableFunds.get(sellProductDetail.getFundCode()
						)[0]);
				canRollback = false;
			}
			spBuilder.addSellProductDetails(spdBuilder);
			spdBuilder.clear();
		}
		spBuilder.setUserId(request.getUserId());
		spBuilder.setUserProductId(request.getUserProductId());
		if(!StringUtils.isEmpty(uiProducts.getBankCardNum())){
			spBuilder.setUserBankNum(uiProducts.getBankCardNum());
		}


		if(canRollback){
			Long fundQuantityRemain = null;
			for(SellProductDetail sellProductDetail: request.getSellProductDetailsList()){
				fundQuantityRemain = currentAvailableFunds.get(sellProductDetail.getFundCode())[0] +
						sellProductDetail.getFundQuantityTrade();
				uiProductDetailRepo.updateByParamDeductTrade(fundQuantityRemain, TradeUtil.getUTCTime(),
						request.getUserId(), request.getUserProductId(),sellProductDetail.getFundCode(),
						TrdOrderStatusEnum.WAITSELL.getStatus() );
			}
		}
		return spBuilder.build();
	}

  @Override
  public List<MongoUiTrdLogDTO> findByUserProdIdIn(List dataList) {
    List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = new ArrayList<>();
    try {
      List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findByUserProdIdIn(dataList);
      List<MongoUiTrdLog> uiTrdLogsUnique = MongoUiTrdLogUtil.getDistinct(mongoUiTrdLogList);
      mongoUiTrdLogDtoList =
          MyBeanUtils.convertList(uiTrdLogsUnique, MongoUiTrdLogDTO.class);
    } catch (IllegalAccessException e) {
    	logger.error("error:", e);
      e.printStackTrace();
    } catch (InstantiationException e) {
    	logger.error("error:", e);
      e.printStackTrace();
    }
    return mongoUiTrdLogDtoList;
  }

	@Override
	@Transactional
	public Builder updateProductQuantity(SellPersentProducts request) throws Exception {
		Long percent = request.getPercent();
		if(percent < 0 || percent > 10000){
			logger.error("percent:{} is out of range", percent);
			throw new Exception(String.format("percent:{} is out of range", percent));
		}
		//检查出售的每个基金在当前用户拥有的产品有足够的份额
		UiProducts uiProducts = uiProductRepo.findById(request.getUserProductId());
		List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(request
				.getUserProductId());
		Map<String, UiProductDetail> currentAvailableFunds = new HashMap<>();
//		Map<String, Integer> currentFundsStatus = new HashMap<>();
		for(UiProductDetail uiProductDetail: uiProductDetails){
			if(uiProductDetail.getFundQuantityTrade() == null || uiProductDetail.getFundQuantityTrade() <=
					0){
				continue;
			}
			if(uiProductDetail.getStatus() != null && uiProductDetail.getStatus() ==
					TrdOrderStatusEnum.WAITSELL.getStatus()){
				logger.error("fundCode:{} is in WAITSELL status:{}", uiProductDetail.getFundCode(),
						uiProductDetail.getStatus());
				throw new Exception(String.format("fundCode:%s is in WAITSELL status:%s",
						uiProductDetail.getFundCode(), uiProductDetail.getStatus()));
			}
			currentAvailableFunds.put(uiProductDetail.getFundCode(), uiProductDetail);
//			currentFundsStatus.put(uiProductDetail.getFundCode(), uiProductDetail.getStatus());
		}
		SellProductsResult.Builder sprBuilder = SellProductsResult.newBuilder();
		SellProductDetailResult.Builder spdrBuilder = SellProductDetailResult.newBuilder();
		for(Map.Entry<String, UiProductDetail> entryItem : currentAvailableFunds.entrySet()){
			spdrBuilder.setFundCode(entryItem.getKey());
			Long quantity = TradeUtil.getLongFromNumWithDiv10000 (entryItem.getValue()
					.getFundQuantityTrade()*percent);

			Long quantityRemain = entryItem.getValue().getFundQuantityTrade() - quantity;
			if(quantityRemain < 100){
				logger.error("quantityRemain:{} is too small", quantityRemain);
				//按百分比份额赎回如果剩余的份额不足一份， 则把剩余份额全部赎回
				quantity = new Long( entryItem.getValue().getFundQuantityTrade());
				quantityRemain = 0L;
				spdrBuilder.setFundQuantityTrade(quantity);
				spdrBuilder.setFundQuantityTradeRemain(quantityRemain);
			}else{
				spdrBuilder.setFundQuantityTrade(quantity);
				spdrBuilder.setFundQuantityTradeRemain(quantityRemain);
			}
			spdrBuilder.setResult(ItemStatus.SUCCESS.ordinal());
			sprBuilder.addSellProductDetailResults(spdrBuilder);
			uiProductDetailRepo.updateByParamDeductTrade(quantityRemain, TradeUtil.getUTCTime(),
					request.getUserId(), request.getUserProductId(), entryItem.getValue().getFundCode(),
					TrdOrderStatusEnum.WAITSELL.getStatus() );
			spdrBuilder.clear();
		}
		if(!StringUtils.isEmpty(uiProducts.getBankCardNum())){
			sprBuilder.setUserBankNum(uiProducts.getBankCardNum());
		}
		return sprBuilder;
	}


	/**
	 */
	@Override
	public void sellPersentUserProducts(com.shellshellfish.aaas.userinfo.grpc.SellPersentProducts request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.SellProductsResult> responseObserver) {
		SellProductsResult.Builder result = null;
		try {
			result = updateProductQuantity(request);
		} catch (Exception e) {
			logger.error("exception:",e);
			ErrInfo.Builder eiBuilder = ErrInfo.newBuilder();
			eiBuilder.setErrCode(ErrorConstants.GRPC_ERROR_UI_CHECKSELL_FAIL_GENERAL);
			eiBuilder.setErrMsg(e.getMessage());
			result.setErrInfo(eiBuilder);
		}
		responseObserver.onNext(result.build());
		responseObserver.onCompleted();

	}
}

