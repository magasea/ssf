package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.mongodb.client.result.UpdateResult;
import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.PendingRecordStatusEnum;
import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.UserRiskLevelEnum;
import com.shellshellfish.aaas.common.enums.grpc.ItemStatus;
import com.shellshellfish.aaas.common.exceptions.ErrorConstants;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.grpc.common.ErrInfo;
import com.shellshellfish.aaas.grpc.common.UserProdDetail;
import com.shellshellfish.aaas.grpc.common.UserProdId;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.grpc.CardInfo;
import com.shellshellfish.aaas.userinfo.grpc.GetUserProdDetailResults;
import com.shellshellfish.aaas.userinfo.grpc.NeedPatchOutsideOrderIds;
import com.shellshellfish.aaas.userinfo.grpc.OrderId;
import com.shellshellfish.aaas.userinfo.grpc.OrderLogDetail;
import com.shellshellfish.aaas.userinfo.grpc.OrderLogResults;
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
import com.shellshellfish.aaas.userinfo.model.dao.MongoCaculateBase;
import com.shellshellfish.aaas.userinfo.model.dao.MongoPendingRecords;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdLog;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiFriendRule;
import com.shellshellfish.aaas.userinfo.model.dao.UiPersonMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiProdMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiSysMsg;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
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
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoFriendRuleRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.redis.UserInfoBaseDao;
import com.shellshellfish.aaas.userinfo.utils.MongoUiTrdLogUtil;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
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
public class
UserInfoRepoServiceImpl extends UserInfoServiceGrpc.UserInfoServiceImplBase
    implements UserInfoRepoService {

  Logger logger = LoggerFactory.getLogger(getClass());

  @Autowired
  UserInfoBankCardsRepository userInfoBankCardsRepository;

//	@Autowired
//	UserPortfolioRepository userPortfolioRepository;
//
//	@Autowired
//	UserInfoAssetsRepository userInfoAssetsRepository;

  @Autowired
  UserInfoRepository userInfoRepository;

  @Autowired
  UserInfoFriendRuleRepository userInfoFriendRuleRepository;

//	@Autowired
//	UserTradeLogRepository userTradeLogRepository;

//	@Autowired
//	UserInfoCompanyInfoRepository userInfoCompanyInfoRepository;

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

//	@Override
//	public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(Long userId) {
//		// BigInteger userIdLocal = BigInteger.valueOf(userId);
//		UiAsset uiAsset = userInfoAssetsRepository.findByUserId(userId);
//		UserInfoAssectsBriefDTO asset = new UserInfoAssectsBriefDTO();
//		if (uiAsset != null) {
//			BeanUtils.copyProperties(uiAsset, asset);
//		}
//		return asset;
//	}

  @Override
  public List<BankCardDTO> getUserInfoBankCards(Long userId)
      throws IllegalAccessException, InstantiationException {
    // BigInteger userIdLocal = BigInteger.valueOf(userId);
    List<UiBankcard> bankcardList = userInfoBankCardsRepository.findAllByUserIdAndStatusIs
        (userId, BankCardStatusEnum.VALID.getStatus());
    List<BankCardDTO> bankcardDtoList = MyBeanUtils.convertList(bankcardList, BankCardDTO.class);
    return bankcardDtoList;
  }

  @Override
  public List<BankCardDTO> getUserInfoBankCards(Long userId, String cardNumber)
      throws IllegalAccessException, InstantiationException {
    List<UiBankcard> bankcardList = userInfoBankCardsRepository
        .findAllByUserIdAndStatusIsAndCardNumberIs
            (userId, BankCardStatusEnum.VALID.getStatus(), cardNumber);
    List<BankCardDTO> bankcardDtoList = MyBeanUtils.convertList(bankcardList, BankCardDTO.class);
    return bankcardDtoList;
  }

//  @Override
//	public List<UserPortfolioDTO> getUserPortfolios(Long userId) throws IllegalAccessException, InstantiationException {
//		List<UiPortfolio> uiPortfolioList = userPortfolioRepository.findAllByUserId(userId);
//		List<UserPortfolioDTO> bankcardDtoList = MyBeanUtils.convertList(uiPortfolioList, UserPortfolioDTO.class);
//		return bankcardDtoList;
//	}

  @Override
  public BankCardDTO getUserInfoBankCard(String cardNumber) {
    List<UiBankcard> uiBankcards = userInfoBankCardsRepository
        .findUiBankcardByCardNumberIsAndStatusIsNot
            (cardNumber, -1);
    BankCardDTO bankcard = new BankCardDTO();
    if (!CollectionUtils.isEmpty(uiBankcards)) {
      BeanUtils.copyProperties(uiBankcards.get(0), bankcard);
    }
    return bankcard;
  }

  @Override
  public BankCardDTO addUserBankcard(UiBankcard uiBankcard) throws Exception {
    List<UiBankcard> uiBankcardsWithCardNum = userInfoBankCardsRepository.findAllByCardNumber
        (uiBankcard.getCardNumber());
    if (!CollectionUtils.isEmpty(uiBankcardsWithCardNum) && uiBankcardsWithCardNum.get(0)
        .getUserId() != uiBankcard.getUserId()) {
      logger.warn("kcard:{} origin is with userId{} and status is:{} now intend to bind it"
          + " with userId{}", uiBankcard.getCardNumber(), uiBankcardsWithCardNum.get(0).getUserId
          (), uiBankcardsWithCardNum.get(0).getStatus(), uiBankcard.getUserId()
      );
      if (uiBankcardsWithCardNum.get(0).getStatus() != BankCardStatusEnum.INVALID.getStatus()) {
        throw new Exception(String.format("此银行卡已被其他账户绑定"));
      } else {
        uiBankcardsWithCardNum.get(0).setStatus(BankCardStatusEnum.VALID.getStatus());
        uiBankcardsWithCardNum.get(0).setUserId(uiBankcard.getUserId());
        uiBankcardsWithCardNum.get(0).setCellphone(uiBankcard.getCellphone());
        userInfoBankCardsRepository.save(uiBankcardsWithCardNum.get(0));
      }
    } else {
      List<UiBankcard> uiBankcards = userInfoBankCardsRepository.findAllByUserIdAndCardNumber
          (uiBankcard.getUserId(), uiBankcard.getCardNumber());
      if (!CollectionUtils.isEmpty(uiBankcards)) {
        logger.info("update bankcard status to 1 for userId:" + uiBankcard.getUserId() + " and "
            + "bankCardNumber:" + uiBankcard.getCardNumber());
        if (uiBankcards.size() > 1) {
          logger.error("there is more than 1 same cardNumber for for userId:" + uiBankcard.getUserId
              () + " and bankCardNumber:" + uiBankcard.getCardNumber());
          throw new Exception("duplicated card number for :" + uiBankcard.getCardNumber());
        } else {
          uiBankcards.get(0).setStatus(1);
          uiBankcards.get(0).setUserPid(uiBankcard.getUserPid());
          uiBankcards.get(0).setCellphone(uiBankcard.getCellphone());
          userInfoBankCardsRepository.save(uiBankcards.get(0));
        }
      } else {
        userInfoBankCardsRepository.save(uiBankcard);
      }
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
  public List<UserPersonMsgDTO> getUiPersonMsg(Long userId)
      throws IllegalAccessException, InstantiationException {
    List<UiPersonMsg> uiPersonMsgList = mongoUserPersonMsgRepo
        .findByUserIdAndReadedOrderByCreatedDateDesc(userId.toString(),
            Boolean.FALSE);
    List<UserPersonMsgDTO> personMsgDtoList = MyBeanUtils
        .convertList(uiPersonMsgList, UserPersonMsgDTO.class);
    return personMsgDtoList;
  }

  @Override
  public List<UserProdMsgDTO> getUiProdMsg(Long prodId)
      throws IllegalAccessException, InstantiationException {
    List<UiProdMsg> uiProdMsgList = mongoUserProdMsgRepo.findAllByProdIdOrderByDateDesc(prodId);
    List<UserProdMsgDTO> uiProdMsgDtoList = MyBeanUtils
        .convertList(uiProdMsgList, UserProdMsgDTO.class);
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
    List<UserSysMsgDTO> uiSysMsgDtoList = MyBeanUtils
        .convertList(uiSysMsgList, UserSysMsgDTO.class);
    return uiSysMsgDtoList;
  }

  @Override
  public List<MongoUiTrdLogDTO> findByUserIdAndProdId(Long userId, Long userProdId)
      throws IllegalAccessException, InstantiationException {
    List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo
        .findAllByUserIdAndUserProdId(userId, userProdId);
    List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils
        .convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
    return mongoUiTrdLogDtoList;
  }

  @Override
  public List<MongoUiTrdLogDTO> findByUserProdIdAndOperType(Long userProdId, Integer operType)
      throws
      IllegalAccessException, InstantiationException {
    List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserProdIdAndOperations
        (userProdId, operType);
    List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils
        .convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
    return mongoUiTrdLogDtoList;
  }

  @Override
  public List<MongoUiTrdLogDTO> findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(Long userId,
      Long userProdId, int operations, int tradeStatus)
      throws IllegalAccessException, InstantiationException {
    List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo
        .findAllByUserIdAndUserProdIdAndOperationsAndTradeStatus(userId, userProdId, operations,
            tradeStatus);
    List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils
        .convertList(mongoUiTrdLogList, MongoUiTrdLogDTO.class);
    return mongoUiTrdLogDtoList;
  }

  @Override
  public List<MongoUiTrdLogDTO> findByUserId(Long userId)
      throws IllegalAccessException, InstantiationException {
    Criteria criteria = Criteria.where("user_id").is(userId);
    Query query = new Query(criteria);
    List<MongoUiTrdLog> mongoUiTrdLogList = mongoTemplate.find(query, MongoUiTrdLog.class);
//		List<MongoUiTrdLog> mongoUiTrdLogList = mongoUserTrdLogMsgRepo.findAllByUserId(userId);
    List<MongoUiTrdLog> uiTrdLogsUnique = MongoUiTrdLogUtil.getDistinct(mongoUiTrdLogList);
    List<MongoUiTrdLogDTO> mongoUiTrdLogDtoList = MyBeanUtils
        .convertList(uiTrdLogsUnique, MongoUiTrdLogDTO.class);
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
    if (userBaseInfoRedis == null) {
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
    } else {
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

//	@Override
//	public Page<UiTrdLog> findTradeLogDtoByUserId(Pageable pageable, Long userId) {
//		Page<UiTrdLog> uiTrdLogPage = userTradeLogRepository.findByUserId(pageable, userId);
//		return uiTrdLogPage;
//	}

//	@Override
//	public List<TradeLogDTO> findTradeLogDtoByUserId(Long userId) throws IllegalAccessException, InstantiationException {
//		List<UiTrdLog> uiTrdLogList = userTradeLogRepository.findByUserId(userId);
//		List<TradeLogDTO> trdLogsDtoList = MyBeanUtils.convertList(uiTrdLogList, TradeLogDTO.class);
//		return trdLogsDtoList;
//	}

//	@Override
//	public Iterable<TradeLogDTO> addUiTrdLog(List<UiTrdLog> trdLogs)
//			throws IllegalAccessException, InstantiationException {
//		userTradeLogRepository.saveAll(trdLogs);
//		// FIXME
//		List<TradeLogDTO> trdLogsDtoList = MyBeanUtils.convertList(trdLogs, TradeLogDTO.class);
//		return trdLogsDtoList;
//	}

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

//	@Override
//	public UiCompanyInfo getCompanyInfo(Long id) {
//
//		return userInfoCompanyInfoRepository.findAll().get(0);
//	}

//	@Override
//	public UiCompanyInfo addCompanyInfo(UiCompanyInfo uiCompanyInfo) {
//		return userInfoCompanyInfoRepository.save(uiCompanyInfo);
//	}

  @Override
  public void getUserId(UserIdQuery userIdQuery, StreamObserver<UserId> responseObserver) {
    Long userId = null;
    try {
      userId = getUserIdFromUUID(userIdQuery.getUuid());
    } catch (Exception e) {
      logger.error("exception:", e);
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
      logger.error("exception:", e);
    }
    List<UiBankcard> bankcardList = userInfoBankCardsRepository
        .findAllByUserIdAndCardNumber(userId, cardNumber);
    if (CollectionUtils.isEmpty(bankcardList)) {
      logger.error("用户:{}, 要解绑的银行卡:{} 不存在", userId, cardNumber);
      throw new UserInfoException("404",
          String.format("用户:%s, 要解绑的银行卡:%s 不存在", userId, cardNumber));
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
    if (userInfoBaseDao.get(userUuid) == null) {
      userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
    } else {
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
      if (userInfoBaseDao.get(uiUser.getUuid()) == null) {
        userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
      } else {
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
    UserBankInfo.Builder builder = UserBankInfo.newBuilder();
    //有个需要是直接去拿pid就行了
    if(!StringUtils.isEmpty(request.getBankCardNo())){
      BankCardDTO bankCardDTO =  getUserInfoBankCard(request.getBankCardNo());
      if(bankCardDTO == null || StringUtils.isEmpty(bankCardDTO.getUserPid())){
        onError(responseObserver, new Exception("Failed to get bankCardInfo by bankCardNo"+
            request.getBankCardNo()));
      }
      MyBeanUtils.mapEntityIntoDTO(bankCardDTO, builder);
      builder.setUserPid(bankCardDTO.getUserPid());
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
      return;
    }

    if (userId <= 0) {
      logger.error("userId is not valid:" + userId);
      if (StringUtils.isEmpty(userUUID)) {
        logger.error("userId and userUUID both is not valid:" + userId + " " + userUUID);
        //Todo: 是否直接返回？
        onError(responseObserver, new Exception("userId and userUUID both is not valid:" + userId + " " + userUUID));
        return;
      } else {
        try {
          uiUser = getUserInfoByUserUUID(userUUID);
          userId = uiUser.getId();
        } catch (Exception e) {
          logger.error("exception:", e);
          logger.error("failed to retrieve userId by userUUID:" + userUUID);
          onError(responseObserver, new Exception("failed to retrieve userId by userUUID:" + userUUID));
          return;
        }
      }
    } else {
      uiUser = getUserInfoByUserId(userId);
    }
    List<BankCardDTO> bankCardDTOS = null;
    try {
      bankCardDTOS = getUserInfoBankCards(userId);
    } catch (IllegalAccessException e) {
      logger.error("exception:", e);
    } catch (InstantiationException e) {
      logger.error("exception:", e);
    }

    builder.setUserId(userId);

    if (bankCardDTOS.size() <= 0) {
      logger.error("failed to find bankCards by userId:" + userId);
      responseObserver.onNext(builder.build());
      responseObserver.onCompleted();
      return;
    }
    builder.setUserName(bankCardDTOS.get(0).getUserName());

    builder.setUuid(request.getUuid());
    builder.setCellphone(bankCardDTOS.get(0).getCellphone());
    CardInfo.Builder ciBuilder = CardInfo.newBuilder();
    if (null != uiUser.getRiskLevel()) {
      builder.setRiskLevel(uiUser.getRiskLevel());
    } else {
      builder.setRiskLevel(-1);
    }
    for (int idx = 0; idx < bankCardDTOS.size(); idx++) {
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
  @Transactional
  public void genUserProdsFromOrder(
      com.shellshellfish.aaas.userinfo.grpc.FinanceProdInfosQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.grpc.common.UserProdId>
          responseObserver) {
    UserProdId.Builder respBuilder = UserProdId.newBuilder();
    List<com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo> financeProdInfoList =
        request.getProdListList();
    if (CollectionUtils.isEmpty(financeProdInfoList)) {
      logger.error("FinanceProdInfoCollection is empty! will return -1");
      respBuilder.setUserProdId(-1L);
      responseObserver.onNext(respBuilder.build());
      responseObserver.onCompleted();
      return;
    }
    try {
      List<com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfo> financeProdInfosValue =
          financeProdInfoList;
      FinanceProdInfo financeProdInfoFirst = financeProdInfosValue.get(0);
      logger.info("financeProdInfoFirst is:" + financeProdInfoFirst + " prodName:"
          + " " + financeProdInfoFirst.getProdName() + "groupId:" + financeProdInfoFirst
          .getGroupId() +
          "prodId:" + financeProdInfoFirst.getProdId());
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
      for (FinanceProdInfo financeProdInfo : financeProdInfosValue) {
        List<MongoPendingRecords> mongoPendingRecords = getMongoPendingRecordsNotInited
            (financeProdInfo.getFundCode(), uiProducts.getProdId(), uiProducts.getGroupId(),
                TrdOrderOpTypeEnum.BUY.getOperation(), request.getUserId(), userProdId);
        if(!CollectionUtils.isEmpty(mongoPendingRecords)){
          throw new Exception(String.format("There still pending request need to be handled "
              + "prodId:%s groupId:%s fundCode:%s created in:%s", mongoPendingRecords.get(0)
              .getProdId(), mongoPendingRecords.get(0).getGroupId(),  mongoPendingRecords.get(0)
              .getFundCode(), mongoPendingRecords.get(0).getCreatedDate()));
        }


        UiProductDetail uiProductDetail = new UiProductDetail();
        BeanUtils.copyProperties(financeProdInfo, uiProductDetail);
        uiProductDetail.setCreateBy(request.getUserId());
        uiProductDetail.setCreateDate(TradeUtil.getUTCTime());
        uiProductDetail.setUpdateBy(request.getUserId());
        uiProductDetail.setUpdateDate(TradeUtil.getUTCTime());
        uiProductDetail.setUserProdId(userProdId);
        uiProductDetailRepo.save(uiProductDetail);

        MongoPendingRecords mongoPendingRecordNew = new MongoPendingRecords();
        mongoPendingRecordNew.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordNew.setUserId(request.getUserId());
        mongoPendingRecordNew.setProdId(uiProducts.getProdId());
        mongoPendingRecordNew.setGroupId(uiProducts.getGroupId());
        mongoPendingRecordNew.setCreatedDate(TradeUtil.getUTCTime());
        mongoPendingRecordNew.setCreatedBy(request.getUserId());
        mongoPendingRecordNew.setFundCode(financeProdInfo.getFundCode());
        mongoPendingRecordNew.setTradeType(TrdOrderOpTypeEnum.BUY.getOperation());
        mongoPendingRecordNew.setUserProdId(userProdId);
        mongoTemplate.save(mongoPendingRecordNew, "ui_pending_records");
      }
      respBuilder.setUserProdId(userProdId);
      responseObserver.onNext(respBuilder.build());
      responseObserver.onCompleted();
      return;
    } catch (Exception ex) {
      onError(responseObserver, ex);
    }

  }

  @Override
  public ProductsDTO findByProdId(String prodId) {
    if (StringUtils.isEmpty(prodId)) {
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
  public Map<String, String> findAllProducts() {
    List<UiProducts> products = uiProductRepo.findAll();
    Map<String, String> map = new HashMap();
    for (UiProducts product : products) {
      map.put(product.getId() + "", product.getProdName());
    }

    return map;
  }

  @Override
  public List<ProductsDTO> findTradeLogDtoByUserId(String uuid)
      throws IllegalAccessException, InstantiationException {
    if (StringUtils.isEmpty(uuid)) {
      logger.error("用户uuid不能为空");
      throw new UserInfoException("404", "用户uuid不能为空");
    }
    Long userId = null;
    try {
      userId = getUserIdFromUUID(uuid);
    } catch (Exception e) {
      logger.error("exception:", e);
    }
    List<ProductsDTO> productsList = new ArrayList<>();
    List<UiProducts> productsData = uiProductRepo.findByUserId(userId);
    if (productsData == null || productsData.size() == 0) {
      //throw new UserInfoException("404", "用户："+userId+"为空");
    } else {
      productsList = MyBeanUtils.convertList(productsData, ProductsDTO.class);
    }

    return productsList;
  }

  @Override
  public void getUerUUIDByUserId(com.shellshellfish.aaas.userinfo.grpc.UserId request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.UserUUID> responseObserver) {
    String userUUID = findUserUUIDByUserId(request.getUserId());
    if (StringUtils.isEmpty(userUUID)) {
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
    Optional<UiUser> uiUser = userInfoRepository.findById(userId);
    if (null != uiUser) {
      return uiUser.get().getUuid();
    } else {
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
    if (null == uiProducts) {
      logger.error("the userProdId:" + request.getUserProdId()
          + " intended to be updated by preOrder process "
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
      logger.info("the preorder recreate the product with userProdId:" + userProdId);
      for (UpdateUserProdReq updateUserProdReq : request.getUpdateUserProdReqList()) {
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
    } else {
      uiProductRepo.updateUiProductsById(SystemUserEnum.SYSTEM_USER_ENUM.getUserId(), TradeUtil
          .getUTCTime(), request.getUserProdId());
      for (UpdateUserProdReq updateUserProdReq : request.getUpdateUserProdReqList()) {
        uiProductDetailRepo.updateFundShareByParam(updateUserProdReq.getFundShare(), TradeUtil
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
      logger.error("exception:", e);
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
    for (UiProductDetail uiProductDetail : uiProductDetails) {
      currentAvailableFunds.put(uiProductDetail.getFundCode(), uiProductDetail);
//			currentFundsStatus.put(uiProductDetail.getFundCode(), uiProductDetail.getStatus());
    }
    SellProductsResult.Builder sprBuilder = SellProductsResult.newBuilder();
    SellProductDetailResult.Builder spdrBuilder = SellProductDetailResult.newBuilder();
    boolean canDuduct = true;
    for (SellProductDetail sellProductDetail : request.getSellProductDetailsList()) {
      logger.info("check fundCode:" + sellProductDetail.getFundCode() + " of userProdId:" +
          request.getUserProductId());
      spdrBuilder.setFundCode(sellProductDetail.getFundCode());
      spdrBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
      UiProductDetail currentProductDetail = currentAvailableFunds.get(sellProductDetail
          .getFundCode());
      if (currentProductDetail == null) {
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

      if (sellProductDetail.getFundQuantityTrade() > currentAvailFundQuantityTrade) {
        spdrBuilder.setResult(-1);
        long smallestRemain = currentAvailFundQuantityTrade;
        spdrBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
        spdrBuilder.setFundQuantityTradeRemain(smallestRemain);
        logger.error("the userProdId:{}'s fundCode:{} will be dump to 0 because "
                + "getFundQuantityTrade:{} exceed the smallestRemain:{}",
            request.getUserProductId(), sellProductDetail.getFundCode(), sellProductDetail
                .getFundQuantityTrade(), smallestRemain);
      }
      if (currentProductDetail.getStatus() != null && currentProductDetail.getStatus() ==
          TrdOrderStatusEnum.WAITSELL.getStatus()) {
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
    if (!StringUtils.isEmpty(uiProducts.getBankCardNum())) {
      sprBuilder.setUserBankNum(uiProducts.getBankCardNum());
    }

    if (canDuduct) {
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

      for (SellProductDetailResult.Builder sellProductDetail : sprBuilder
          .getSellProductDetailResultsBuilderList()) {

        if (sellProductDetail.getResult() == -1) {
          logger.error("Because the target sell amount exceed remain ammount, now use remain "
                  + "amount to update uiProductDetail:{} and default make the fundQuantityTrade to 0",
              sellProductDetail.getFundQuantityTradeRemain());
          uiProductDetailRepo.updateByParamDeductTrade(0L, TradeUtil.getUTCTime(),
              request.getUserId(), request.getUserProductId(), sellProductDetail.getFundCode(),
              TrdOrderStatusEnum.WAITSELL.getStatus());
        } else {
          fundQuantityRemain =
              currentAvailableFunds.get(sellProductDetail.getFundCode()).getFundQuantityTrade() -
                  sellProductDetail.getFundQuantityTrade();
          if (fundQuantityRemain < 0) {
            logger.error("cannot deduct the fundQuantity, because getFundQuantityTrade:{} is less "
                    + "than target sellProductDetail.getFundQuantityTrade:{}", currentAvailableFunds.get
                    (sellProductDetail.getFundCode()).getFundQuantityTrade(),
                sellProductDetail.getFundQuantityTrade());
            sellProductDetail.setResult(-1);
            sellProductDetail.setFundQuantityTradeRemain(currentAvailableFunds.get
                (sellProductDetail.getFundCode()).getFundQuantityTrade());
            uiProductDetailRepo.updateByParamDeductTrade(0L, TradeUtil.getUTCTime(),
                request.getUserId(), request.getUserProductId(), sellProductDetail.getFundCode(),
                TrdOrderStatusEnum.WAITSELL.getStatus());
          } else {
            uiProductDetailRepo.updateByParamDeductTrade(fundQuantityRemain, TradeUtil.getUTCTime(),
                request.getUserId(), request.getUserProductId(), sellProductDetail.getFundCode(),
                TrdOrderStatusEnum.WAITSELL.getStatus());
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
    if (userBaseInfoRedis == null) {
      uiUser = userInfoRepository.findByUuid(userUUID);
      if (null == uiUser) {
        logger.error("not vaild userUuid:{}", userUUID);
        throw new Exception("not vaild userUuid:" + userUUID);
      } else {
        userBaseInfoRedis = new UserBaseInfoRedis();
        MyBeanUtils.mapEntityIntoDTO(uiUser, userBaseInfoRedis);
        userInfoBaseDao.addUserBaseInfo(userBaseInfoRedis);
      }
    } else {
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
      logger.error("exception:", e);
      logger.error("failed to find uiUser by uuid:" + request.getUuid());
    }
    UserInfo.Builder uiBuilder = UserInfo.newBuilder();
    if (uiUser.getRiskLevel() == null || uiUser.getRiskLevel() < 0) {
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
    for (UiProductDetail uiProductDetail : uiProductDetails) {
      Long[] currentAndOrigin = {Long.valueOf(uiProductDetail.getFundQuantityTrade()),
          Long.valueOf(uiProductDetail.getFundQuantity())};
      currentAvailableFunds.put(uiProductDetail.getFundCode(), currentAndOrigin);
    }
    SellProducts.Builder spBuilder = SellProducts.newBuilder();
    SellProductDetail.Builder spdBuilder = SellProductDetail.newBuilder();
    boolean canRollback = true;
    for (SellProductDetail sellProductDetail : request.getSellProductDetailsList()) {
      logger.info("check fundCode:" + sellProductDetail.getFundCode() + " of userProdId:" +
          request.getUserProductId());
      spdBuilder.setFundCode(sellProductDetail.getFundCode());
      spdBuilder.setFundQuantityTrade(sellProductDetail.getFundQuantityTrade());
      if (sellProductDetail.getFundQuantityTrade() + currentAvailableFunds.get(sellProductDetail
          .getFundCode())[0] > currentAvailableFunds.get(sellProductDetail.getFundCode())[1]) {
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
    if (!StringUtils.isEmpty(uiProducts.getBankCardNum())) {
      spBuilder.setUserBankNum(uiProducts.getBankCardNum());
    }

    if (canRollback) {
      Long fundQuantityRemain = null;
      for (SellProductDetail sellProductDetail : request.getSellProductDetailsList()) {
        fundQuantityRemain = currentAvailableFunds.get(sellProductDetail.getFundCode())[0] +
            sellProductDetail.getFundQuantityTrade();
        uiProductDetailRepo.updateByParamDeductTrade(fundQuantityRemain, TradeUtil.getUTCTime(),
            request.getUserId(), request.getUserProductId(), sellProductDetail.getFundCode(),
            TrdOrderStatusEnum.WAITSELL.getStatus());
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
  public List<MongoUiTrdLogDTO> findByOrderIdIn(Set orders) {
    List<MongoUiTrdLogDTO> mongoUiTrdLogDTOList = new ArrayList<>();

    try {
      List<MongoUiTrdLog> byOrderIdIn = mongoUserTrdLogMsgRepo.findByOrderIdIn(orders);
     // List<MongoUiTrdLog> distinct = MongoUiTrdLogUtil.getDistinct(byOrderIdIn);
      mongoUiTrdLogDTOList = MyBeanUtils.convertList(byOrderIdIn, MongoUiTrdLogDTO.class);
    } catch (IllegalAccessException e) {
      logger.error("error:", e);
      e.printStackTrace();
    } catch (InstantiationException e) {
      logger.error("error:", e);
      e.printStackTrace();
    }
    return mongoUiTrdLogDTOList;
  }

  @Override
  @Transactional
  //ToDo:
  // 1. set redis the user_prod_id and fund_code is in caculate status
  // 2. if latest_serial is empty then calculate origin quantity first
  // 3. set the scheduler job to check redis user_prod_id and fund_code status, if is in caculate
  // status, then ignore this element continue others
  public Builder updateProductQuantity(SellPersentProducts request) throws Exception {
    Long percent = request.getPercent();
    if (percent < 0 || percent > 10000) {
      logger.error("percent:{} is out of range", percent);
      throw new Exception(String.format("percent:{} is out of range", percent));
    }
    //检查出售的每个基金在当前用户拥有的产品有足够的份额
    UiProducts uiProducts = uiProductRepo.findById(request.getUserProductId());

    List<UiProductDetail> uiProductDetails = uiProductDetailRepo.findAllByUserProdId(request
        .getUserProductId());



    Map<String, UiProductDetail> currentAvailableFunds = new HashMap<>();
    Map<String, MongoPendingRecords> mongoPendingRecordsHashMap = new HashMap<>();

//		Map<String, Integer> currentFundsStatus = new HashMap<>();
    SellProductsResult.Builder sprBuilder = SellProductsResult.newBuilder();
    if (!StringUtils.isEmpty(uiProducts.getBankCardNum())) {
      sprBuilder.setUserBankNum(uiProducts.getBankCardNum());
    }
    SellProductDetailResult.Builder spdrBuilder = SellProductDetailResult.newBuilder();
    for (UiProductDetail uiProductDetail : uiProductDetails) {
      //注意 这里开始重构： 查出现有未处理的pendRecords,
      // 1. 如果还没有orderId生成的话， 直接返回错误，提示尚有未处理订单
      // 2. 已经有orderId生成，如果是赎回的话，算出剩余份额
      // 3. 如果这次要赎回的份额大于剩余份额的话，将剩余份额全部返回
      //    如果这次要赎回的份额导致剩余份额小于1份的话，将剩余份额全部返回
      // 重构的原则： 不改动uiProductDetail里的fundQuantity, 只有在收到确认消息后才扣减， 废除fundQuantityTrade
      spdrBuilder.clear();
      Query query = new Query();
      query.addCriteria(Criteria.where("user_prod_id").is(request.getUserProductId()).and
          ("fund_code").is(uiProductDetail.getFundCode()));
      List<MongoPendingRecords> mongoPendingRecords = mongoTemplate
          .find(query, MongoPendingRecords.class);

      //用 ui_calc_base记录做过滤，如果outside_order_id出现了， 那么排除
      Query queryCalc = new Query();
      queryCalc.addCriteria(Criteria.where("user_prod_id").is(request.getUserProductId()).and
          ("fund_code").is(uiProductDetail.getFundCode()));
      Set<String> orderCalcedIds = new HashSet<>();
      List<MongoCaculateBase> mongoCaculateBases = mongoTemplate.find(queryCalc,
          MongoCaculateBase.class);
      for(MongoCaculateBase mongoCaculateBase: mongoCaculateBases){
        orderCalcedIds.add(mongoCaculateBase.getOutsideOrderId());
      }
      Predicate<MongoPendingRecords> mongoPendingRecordsPredicate = p-> orderCalcedIds.contains(p
          .getOutsideOrderId()) ;
      mongoPendingRecords.removeIf(mongoPendingRecordsPredicate);
      Integer originQuantity = uiProductDetail.getFundQuantity();


      if (originQuantity == null || originQuantity <= 0 || StringUtils.isEmpty(uiProductDetail
          .getLastestSerial())) {
        logger.error("uiProductDetail.getLastestSerial:{} originQuantity:{}", uiProductDetail
            .getLastestSerial(), originQuantity);
        recordStopSellInvaidFunds(request, uiProductDetail);
        spdrBuilder.setFundCode(uiProductDetail.getFundCode());
        spdrBuilder.setFundQuantityTrade(0L);
        spdrBuilder.setFundQuantityTradeRemain(0L);
        spdrBuilder.setResult(ItemStatus.FAIL.ordinal());
        sprBuilder.addSellProductDetailResults(spdrBuilder);
        continue;
      }

      Long trdTgtShares = TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundDown
          (originQuantity * percent, 10000L).longValue();
      if (CollectionUtils.isEmpty(mongoPendingRecords)) {
        logger.info("there is no pendingRecord for this redeem with userProdId:{} fundCode:{}",
            request.getUserProductId(), uiProductDetail.getFundCode());
        //直接计算剩余份额

        Long finalTrdTargetShares = 0L;
        if (originQuantity < 100 || originQuantity - trdTgtShares < 100) {
          logger.info("because originQuantity:{} trdTgtShares:{} we need to sell all remaining "
              + "part", originQuantity, trdTgtShares);
          finalTrdTargetShares = new Long(originQuantity);
        } else {
          finalTrdTargetShares = trdTgtShares;
        }
        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsPatch.setUserProdId(request.getUserProductId());
        mongoPendingRecordsPatch.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        mongoPendingRecordsPatch.setFundCode(uiProductDetail.getFundCode());
        mongoPendingRecordsPatch.setCreatedBy(request.getUserId());
        mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());
        if(MonetaryFundEnum.containsCode(uiProductDetail.getFundCode())){
          mongoPendingRecordsPatch.setAbstractTargetShare(finalTrdTargetShares);
        }else {
          mongoPendingRecordsPatch.setTradeTargetShare(finalTrdTargetShares);
        }
        mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
        spdrBuilder.setFundCode(uiProductDetail.getFundCode());
        spdrBuilder.setFundQuantityTrade(finalTrdTargetShares);
        spdrBuilder.setFundQuantityTradeRemain(originQuantity - finalTrdTargetShares);
        spdrBuilder.setResult(ItemStatus.SUCCESS.ordinal());
      } else {
        //有历史尚未处理记录，需要先减去待处理记录
        Long historyTrdTargetShares = 0L;
        Set<String> orderIds = new HashSet<>();
        for (MongoPendingRecords mongoPendingRecordsNotHandled : mongoPendingRecords) {
          if (mongoPendingRecordsNotHandled.getTradeStatus() != TrdOrderStatusEnum.FAILED
              .getStatus()
              && mongoPendingRecordsNotHandled.getTradeStatus() != TrdOrderStatusEnum
              .REDEEMFAILED.getStatus() && !StringUtils.isEmpty(mongoPendingRecordsNotHandled
              .getOutsideOrderId()) && !orderIds.contains(mongoPendingRecordsNotHandled
              .getOutsideOrderId()) ) {
            if (mongoPendingRecordsNotHandled.getTradeType() == TrdOrderOpTypeEnum.REDEEM
                .getOperation()) {
              //如果之前货基的记录里面没有记录abstractTargetShares，那么用之前保存的targetShare来算
              //但是一旦有update， 那么要用确认的购买或者赎回的信息去算abstractTargetShares
              Long abstractTargetShares = mongoPendingRecordsNotHandled.getTradeTargetShare();
              if(mongoPendingRecordsNotHandled.getAbstractTargetShare() > 0L && MonetaryFundEnum
                  .containsCode(mongoPendingRecordsNotHandled.getFundCode())){
                abstractTargetShares = mongoPendingRecordsNotHandled.getAbstractTargetShare();
              }

              historyTrdTargetShares = historyTrdTargetShares - abstractTargetShares;
              orderIds.add(mongoPendingRecordsNotHandled.getOutsideOrderId());
            }
          }
        }
        Long finalTrdTargetShares = 0L;
        if(originQuantity + historyTrdTargetShares - trdTgtShares > 100){
          finalTrdTargetShares = trdTgtShares;
        }else if(originQuantity + historyTrdTargetShares < trdTgtShares ){
          logger.info("because originQuantity:{} histroyTrdTargetShares:{} trdTgtShares:{} we "
              + "can only deny trade ", originQuantity, historyTrdTargetShares, trdTgtShares);
          finalTrdTargetShares = 0L;
        }else if(originQuantity + historyTrdTargetShares - trdTgtShares < 100){
          logger.info("because originQuantity:{} histroyTrdTargetShares:{} trdTgtShares:{} we "
              + "can should sell all remain shares ", originQuantity, historyTrdTargetShares,
              trdTgtShares);
          finalTrdTargetShares = originQuantity + historyTrdTargetShares;
        }

        MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
        mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.NOTHANDLED.getStatus());
        mongoPendingRecordsPatch.setUserProdId(request.getUserProductId());
        mongoPendingRecordsPatch.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
        mongoPendingRecordsPatch.setFundCode(uiProductDetail.getFundCode());
        mongoPendingRecordsPatch.setCreatedBy(request.getUserId());
        mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());
        mongoPendingRecordsPatch.setTradeTargetShare(finalTrdTargetShares);
        if(finalTrdTargetShares > 0){
          mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
        }
        spdrBuilder.setFundCode(uiProductDetail.getFundCode());
        spdrBuilder.setFundQuantityTrade(finalTrdTargetShares);
        spdrBuilder.setFundQuantityTradeRemain(originQuantity + historyTrdTargetShares);
        spdrBuilder.setResult(ItemStatus.SUCCESS.ordinal());
      }
      sprBuilder.addSellProductDetailResults(spdrBuilder);
      //到此循环结束
    }

    return sprBuilder;
  }

  private void recordStopSellInvaidFunds(SellPersentProducts request, UiProductDetail uiProductDetail ){
    logger.error("userProdId:{} fundCode:{}'s quantity is:{}", request.getUserProductId(),
        uiProductDetail.getFundCode(), uiProductDetail.getFundQuantity());
    MongoPendingRecords mongoPendingRecordsPatch = new MongoPendingRecords();
    mongoPendingRecordsPatch.setProcessStatus(PendingRecordStatusEnum.HANDLED.getStatus());
    mongoPendingRecordsPatch.setUserProdId(request.getUserProductId());
    mongoPendingRecordsPatch.setTradeType(TrdOrderOpTypeEnum.REDEEM.getOperation());
    mongoPendingRecordsPatch.setFundCode(uiProductDetail.getFundCode());
    mongoPendingRecordsPatch.setCreatedBy(request.getUserId());
    mongoPendingRecordsPatch.setCreatedDate(TradeUtil.getUTCTime());
    mongoPendingRecordsPatch.setTradeTargetShare(0L);
    mongoTemplate.save(mongoPendingRecordsPatch, "ui_pending_records");
  }

  /**
   */
  @Override
  public void sellPersentUserProducts(
      com.shellshellfish.aaas.userinfo.grpc.SellPersentProducts request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.SellProductsResult> responseObserver) {
    SellProductsResult.Builder result = null;
    try {
      result = updateProductQuantity(request);
    } catch (Exception e) {
      logger.error("exception:", e);
      onError(responseObserver, e);
    }
    responseObserver.onNext(result.build());
    responseObserver.onCompleted();

  }

  @Override
  public Page<UiUser> secectUsers(Pageable pageable)
      throws InstantiationException, IllegalAccessException {

    Page<UiUser> users = userInfoRepository.findAll(pageable);

    return users;
  }

  @Override
  public void getUserTraLog(OrderId request, StreamObserver<OrderLogResults> responseObserver) {
    try {
      List<MongoUiTrdLog> trdLogListResult=new ArrayList<>();
      String orderId = request.getOrderId();
      Query query = new Query();
      query.addCriteria(Criteria.where("order_id").is(orderId));
      List<MongoUiTrdLog> trdLogList = mongoTemplate.find(query, MongoUiTrdLog.class);
      //过滤筛选confirmdate不为空
      Map<String, List<MongoUiTrdLog>> collect = trdLogList.stream().filter(k->k.getFundCode()!=null).collect(Collectors.groupingBy(k -> k.getFundCode()));
      collect.forEach((k,v)->{
        List<MongoUiTrdLog> trdLost = v.stream().filter(item -> item.getConfirmDateExp() != null).collect(Collectors.toList());
        for(MongoUiTrdLog trdLog:trdLost){
          trdLogListResult.add(trdLog);
        }
      });
      OrderLogDetail.Builder builder = OrderLogDetail.newBuilder();
      OrderLogResults.Builder orderResultBuilder = OrderLogResults.newBuilder();
      for(MongoUiTrdLog mongoUiTrdLog: trdLogListResult){
        MyBeanUtils.mapEntityIntoDTO(mongoUiTrdLog, builder);
        orderResultBuilder.addOrderLogDetail(builder);
      }
      responseObserver.onNext(orderResultBuilder.build());
      responseObserver.onCompleted();
    } catch (Exception e) {
      logger.error("exception:", e);
      onError(responseObserver, e);
    }
  }



  private List<MongoPendingRecords> getMongoPendingRecords(Long userProdId, String fundCode){
    Query query = new Query();
    query.addCriteria(Criteria.where("user_prod_id").is(userProdId).and("fund_code").is(fundCode));

    List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
    return mongoPendingRecords;
  }

  private List<MongoPendingRecords> getMongoPendingRecordsNotInited(String fundCode, Long
      prodId, Long groupId, int trdType, Long userId, Long userProdId){
    Query query = new Query();
    query.addCriteria(Criteria.where("prod_id").is(prodId).and("fund_code").is(fundCode).and
        ("group_id").is(groupId).orOperator(Criteria.where("order_id").is(""), Criteria.where
        ("order_id").is(null)).and("trade_type").is(trdType).and("user_id").is(userId).and
        ("user_prod_id").is(userProdId));

    List<MongoPendingRecords> mongoPendingRecords = mongoTemplate.find(query, MongoPendingRecords.class);
    return mongoPendingRecords;
  }


  /**
   */
  @Override
  public void getUserProdDetail(com.shellshellfish.aaas.userinfo.grpc.GetUserProdDetailQuery request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.GetUserProdDetailResults> responseObserver) {


   Long userProdId = request.getUserProdId();
   if(userProdId <= 0){
     Exception ex =  new Exception(String.format("Illegal input param:%s", request.getUserProdId
         ()));
     onError(responseObserver, ex);
   }
   List<UiProductDetail> uiProductDetails =  uiProductDetailRepo.findAllByUserProdId(userProdId);
   if(CollectionUtils.isEmpty(uiProductDetails)){
     Exception ex =  new Exception(String.format("Havent found uiProductDetails by userProdId:%s",
         request.getUserProdId()));
     onError(responseObserver, ex);
   }
    GetUserProdDetailResults.Builder gupdrBuilder = GetUserProdDetailResults.newBuilder();
   UserProdDetail.Builder updBuilder = UserProdDetail.newBuilder();
   for(UiProductDetail uiProductDetail: uiProductDetails){
     MyBeanUtils.mapEntityIntoDTO(uiProductDetail, updBuilder);
     gupdrBuilder.addUserProdDetail(updBuilder);
     updBuilder.clear();
   }
   responseObserver.onNext(gupdrBuilder.build());
   responseObserver.onCompleted();
  }

  /**
   */
  @Override
  public void checkAbsentPendingRecordsOrders(com.shellshellfish.aaas.userinfo.grpc.ConfirmedOutsideOrderIds request,
      io.grpc.stub.StreamObserver<com.shellshellfish.aaas.userinfo.grpc.NeedPatchOutsideOrderIds> responseObserver) {
    List<String> outsiddOrderIds = request.getOutsideOrderIdList();
    if(CollectionUtils.isEmpty(outsiddOrderIds)){
      onError(responseObserver, new Exception("Input list of outsideOrderIds is empty"));
      return;
    }
    NeedPatchOutsideOrderIds.Builder npooiBuilder = NeedPatchOutsideOrderIds.newBuilder();
    for(String outsideOrderId: outsiddOrderIds){
      if(!isOutsideOrderIdHandled(outsideOrderId)){
        npooiBuilder.addOutsideOrderId(outsideOrderId);
      }
    }
    responseObserver.onNext(npooiBuilder.build());
    responseObserver.onCompleted();
  }

  public boolean isOutsideOrderIdHandled(String outsideOrderId){
    Query query = new Query();
    query.addCriteria(Criteria.where("outside_order_id").is(outsideOrderId).and
        ("trade_confirm_share").is(null).orOperator(Criteria
        .where("trade_status").is(TrdOrderStatusEnum.CONFIRMED.getStatus()), Criteria.where
        ("trade_status").is(TrdOrderStatusEnum.SELLCONFIRMED.getStatus())));
    List<MongoPendingRecords> mongoPendingRecords =  mongoTemplate.find(query, MongoPendingRecords
        .class);
    if(CollectionUtils.isEmpty(mongoPendingRecords)){
      return false;
    }else{
      return true;
    }
  }

  private void onError(StreamObserver responseObserver, Exception ex){
    responseObserver.onError(Status.INTERNAL
        .withDescription(ex.getMessage())
        .augmentDescription("customException()")
        .withCause(ex) // This can be attached to the Status locally, but NOT transmitted to
        // the client!
        .asRuntimeException());
  }


}

