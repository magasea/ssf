package com.shellshellfish.aaas.userinfo.dao.service.impl;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoCollectionOrBuilder;
import com.shellshellfish.aaas.userinfo.grpc.UserBankInfo;
import com.shellshellfish.aaas.userinfo.grpc.UserProdId;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.mongodb.WriteResult;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
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
import com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl;
import com.shellshellfish.aaas.userinfo.utils.MyBeanUtils;

import io.grpc.stub.StreamObserver;
import org.springframework.util.CollectionUtils;

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
		UiBankcard uiBankcard = userInfoBankCardsRepository.findUiBankcardByCardNumberIs(cardNumber);
		BankCardDTO bankcard = new BankCardDTO();
		BeanUtils.copyProperties(uiBankcard, bankcard);
		return bankcard;
	}

	@Override
	public BankCardDTO addUserBankcard(UiBankcard uiBankcard) throws Exception {
		logger.info("reservice149");
		userInfoBankCardsRepository.save(uiBankcard);
		logger.info("reservice151");
		BankCardDTO bankcard = new BankCardDTO();
		logger.info("reservice153");
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
		List<UiPersonMsg> uiPersonMsgList = mongoUserPersonMsgRepo.getUiPersonMsgsByUserIdAndReaded(userId,
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
		WriteResult result = mongoTemplate.updateMulti(query, update, UiPersonMsg.class);
		System.out.println(result);
		return result.isUpdateOfExisting();
	}

	@Override
	public List<UserSysMsgDTO> getUiSysMsg() throws IllegalAccessException, InstantiationException {
		List<UiSysMsg> uiSysMsgList = mongoUserSysMsgRepo.findAllByOrderByDateDesc();
		List<UserSysMsgDTO> uiSysMsgDtoList = MyBeanUtils.convertList(uiSysMsgList, UserSysMsgDTO.class);
		return uiSysMsgDtoList;
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
		UiUser uiUser = userInfoRepository.findByUuid(userUuid);
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
		Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findByUserId(pageable, userId);
		return uiTrdLogPage;
	}

	@Override
	public Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs)
			throws IllegalAccessException, InstantiationException {
		userTradeLogRepository.save(trdLogs);
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
			e.printStackTrace();
		}
		responseObserver.onNext(UserId.newBuilder().setUserId(userId).build());
		responseObserver.onCompleted();
	}

	@Override
	public Boolean deleteBankCard(String userUuid, String cardNumber) {
		UiBankcard bank = new UiBankcard();
		bank.setCardNumber(cardNumber);
		bank.setUserId(Long.parseLong(userUuid));
		List<UiBankcard> bankcardList = userInfoBankCardsRepository
				.findAllByUserIdAndCardNumber(Long.parseLong(userUuid), cardNumber);
		if (bankcardList == null || bankcardList.size() == 0) {
			return false;
		}
		UiBankcard bankcard = bankcardList.get(0);
		userInfoBankCardsRepository.delete(bankcard.getId());
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
		userInfoRepository.save(uiUser);
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
		List<BankCardDTO> bankCardDTOS = null;
		try {
			bankCardDTOS = getUserInfoBankCards(userId);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		}
		if( bankCardDTOS.size() >= 0 ){
			logger.error("failed to find bankCards by userId:" + userId);
		}
		UserBankInfo.Builder builder = UserBankInfo.newBuilder();
		builder.setUserId(userId);
		builder.setUserName(bankCardDTOS.get(0).getUserName());
		builder.setUserPid(bankCardDTOS.get(0).getUserPid());
		builder.setUuid(request.getUuid());
		builder.setCellphone(bankCardDTOS.get(0).getCellphone());
		for(int idx = 0; idx < bankCardDTOS.size(); idx++){
			builder.setCardNumbers(idx, bankCardDTOS.get(idx).getCardNumber());
		}
		responseObserver.onNext(builder.build());
		responseObserver.onCompleted();
	}


	/**
	 */
	public void genUserProdsFromOrder(com.shellshellfish.aaas.userinfo.grpc.FinanceProdInfosQuery request,
			io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserProdId> responseObserver) {
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
			uiProducts.setCreateDate(TradeUtil.getUTCTime());
			uiProducts.setUpdateBy(request.getUserId());
			uiProducts.setUpdateDate(TradeUtil.getUTCTime());
			uiProducts.setStatus(TrdOrderStatusEnum.WAITPAY.getStatus());
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
			logger.error(ex.getMessage());
			respBuilder.setUserProdId(-1L);
			responseObserver.onNext(respBuilder.build());
			responseObserver.onCompleted();
			return;
		}

	}
}
