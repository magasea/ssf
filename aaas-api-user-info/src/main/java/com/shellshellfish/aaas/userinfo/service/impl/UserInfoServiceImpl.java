package com.shellshellfish.aaas.userinfo.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.shellshellfish.aaas.common.enums.BankCardStatusEnum;
import com.shellshellfish.aaas.common.enums.CombinedStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.common.utils.TrdStatusToCombStatusUtils;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryByOrderDetailId;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiAssetDailyRept;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
//import com.shellshellfish.aaas.userinfo.model.dao.UiCompanyInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dto.AssetDailyReptDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.MongoUiTrdLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.ProductsDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TradeLogDTO;
import com.shellshellfish.aaas.userinfo.model.dto.TrendYield;
import com.shellshellfish.aaas.userinfo.model.dto.UiProductDetailDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserBaseInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoAssectsBriefDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoCompanyInfoDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserInfoFriendRuleDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPersonMsgDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserPortfolioDTO;
import com.shellshellfish.aaas.userinfo.model.dto.UserSysMsgDTO;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoUserDailyIncomeRepository;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UiProductService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.UserInfoService;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import io.grpc.ManagedChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.*;
import java.util.*;
import java.util.concurrent.ExecutionException;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Autowired
    UserFinanceProdCalcService userFinanceProdCalcService;

    @Autowired
    UiProductService uiProductService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;


    @Autowired
    RpcOrderService rpcOrderService;

    @Autowired
    MongoDailyAmountRepository mongoDailyAmountRepository;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    MongoClient mongoClient;

    @Autowired
    MongoDatabase mongoDatabase;

    @Autowired
    MongoUserDailyIncomeRepository mongoUserDailyIncomeRepository;

    @Autowired
    UserAssetService userAssetService;

    PayRpcServiceFutureStub payRpcServiceFutureStub;

    @Autowired
    ManagedChannel managedPayChannel;

    @PostConstruct
    void init() {
        payRpcServiceFutureStub = PayRpcServiceGrpc.newFutureStub(managedPayChannel);
    }

    @Override
    public UserBaseInfoDTO getUserInfoBase(String userUuid) throws Exception {
//		logger.info(
//				"com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl.getUserInfoBase(String)===>start");
//		Long userId = getUserIdFromUUID(userUuid);
//		logger.info(
//				"com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl.getUserInfoBase(String)===>"
//						+ userId);
        UserBaseInfoDTO userInfoDao = userInfoRepoService.getUserInfo(userUuid);
        // UserBaseInfo userBaseInfo = new UserBaseInfo();
        // if( null != userInfoDao) {
        // BeanUtils.copyProperties(userInfoDao, userBaseInfo);
        // }
        logger.info(
                "com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl.getUserInfoBase(String)===>end");
        return userInfoDao;
    }

//    @Override
//    public UserInfoAssectsBriefDTO getUserInfoAssectsBrief(String userUuid) throws Exception {
//        Long userId = getUserIdFromUUID(userUuid);
//        // UserInfoAssectsBrief userInfoAssectsBrief = new
//        // UserInfoAssectsBrief();
//        UserInfoAssectsBriefDTO userInfoAssect = userInfoRepoService.getUserInfoAssectsBrief(userId);
//        // if(null != userInfoAssect){
//        // BeanUtils.copyProperties(userInfoAssect, userInfoAssectsBrief);
//        // }
//        return userInfoAssect;
//    }

    @Override
    public List<BankCardDTO> getUserInfoBankCards(String userUuid, String cardNumber) {
        Long userId = null;
        try {
            userId = getUserIdFromUUID(userUuid);
        } catch (Exception e) {
            logger.error("该用户不存在", e);
            throw new UserInfoException("404", "该用户不存在");
        }
        List<BankCardDTO> bankcards = null;
        try {
            bankcards = userInfoRepoService.getUserInfoBankCards(userId, cardNumber);
        } catch (Exception e) {
            logger.error("该用户暂时没有绑定银行卡", e);
            throw new UserInfoException("404", "该用户暂时没有绑定银行卡");
        }
        // List<BankCard> bankCardsDto = new ArrayList<>();
        // for(UiBankcard uiBankcard: uiBankcards ){
        // BankCard bankCard = new BankCard();
        // BeanUtils.copyProperties(uiBankcard, bankCard);
        // bankCardsDto.add(bankCard);
        // }
        return bankcards;
    }

    @Override
    public List<BankCardDTO> getUserInfoBankCards(String userUuid) {
        Long userId = null;
        try {
            userId = getUserIdFromUUID(userUuid);
        } catch (Exception e) {
            logger.error("该用户不存在", e);
            throw new UserInfoException("404", "该用户不存在");
        }
        List<BankCardDTO> bankcards = null;
        try {
            bankcards = userInfoRepoService.getUserInfoBankCards(userId);
        } catch (Exception e) {
            logger.error("该用户暂时没有绑定银行卡", e);
            throw new UserInfoException("404", "该用户暂时没有绑定银行卡");
        }
        // List<BankCard> bankCardsDto = new ArrayList<>();
        // for(UiBankcard uiBankcard: uiBankcards ){
        // BankCard bankCard = new BankCard();
        // BeanUtils.copyProperties(uiBankcard, bankCard);
        // bankCardsDto.add(bankCard);
        // }
        return bankcards;
    }

//    @Override
//    public List<UserPortfolioDTO> getUserPortfolios(String userUuid) throws Exception {
//        Long userId = getUserIdFromUUID(userUuid);
//        List<UserPortfolioDTO> userPortfolioDaos = userInfoRepoService.getUserPortfolios(userId);
//        // List<UserPortfolio> userPortfolios = new ArrayList<>();
//        // for(UiPortfolio userPortfolioDao: userPortfolioDaos){
//        // UserPortfolio userPortfolio = new UserPortfolio();
//        // BeanUtils.copyProperties(userPortfolioDao, userPortfolio);
//        // userPortfolios.add(userPortfolio);
//        // }
//        return userPortfolioDaos;
//    }

    @Override
    public BankCardDTO getUserInfoBankCard(String cardNumber) throws RuntimeException {
        BankCardDTO bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
        // BankCard bankCard = new BankCard();
        // BeanUtils.copyProperties(uiBankcard, bankCard);
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
        uiBankcard.setStatus(BankCardStatusEnum.VALID.getStatus());
        if (!StringUtils.isEmpty(params.get("cardNumber"))) {
            String bankName = BankUtil.getNameOfBank(params.get("cardNumber").toString());
            if (!StringUtils.isEmpty(bankName)) {
                uiBankcard.setBankName(BankUtil.getNameOfBank(params.get("cardNumber").toString()));
            }
        }
        BankCardDTO bankCard = userInfoRepoService.addUserBankcard(uiBankcard);
        return bankCard;
    }

    @Override
    public List<AssetDailyReptDTO> getAssetDailyRept(String userUuid, Long beginDate, Long endDate)
            throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<AssetDailyReptDTO> uiAssetDailyRepts = userInfoRepoService
                .getAssetDailyRept(userId, beginDate, endDate);
        // List<AssetDailyRept> assetDailyRepts = new ArrayList<>();
        // for(UiAssetDailyRept uiAssetDailyRept: uiAssetDailyRepts){
        // AssetDailyRept assetDailyRept = new AssetDailyRept();
        // BeanUtils.copyProperties(uiAssetDailyRept, assetDailyRept);
        // assetDailyRept.setDate(new Date(uiAssetDailyRept.getDate()));
        // assetDailyRepts.add(assetDailyRept);
        // }
        return uiAssetDailyRepts;
    }

    @Override
    public AssetDailyReptDTO addAssetDailyRept(AssetDailyReptDTO assetDailyRept) {
        UiAssetDailyRept uiAssetDailyRept = new UiAssetDailyRept();
        BeanUtils.copyProperties(assetDailyRept, uiAssetDailyRept);
        uiAssetDailyRept.setDate(assetDailyRept.getDate().getTime());
        AssetDailyReptDTO result = userInfoRepoService.addAssetDailyRept(uiAssetDailyRept);
        // AssetDailyRept assetDailyReptResult = new AssetDailyRept();
        // BeanUtils.copyProperties(result, assetDailyReptResult);
        // Date date = new Date(result.getDate());
        // assetDailyRept.setDate(date);
        return result;
    }

    @Override
    public List<UserSysMsgDTO> getUserSysMsg(String userUuid)
            throws IllegalAccessException, InstantiationException {
        List<UserSysMsgDTO> userSysMsgs = userInfoRepoService.getUiSysMsg();
        // List<UserSysMsg> userSysMsgs = new ArrayList<>();
        // for(UiSysMsg uiSysMsg: uiSysMsgs){
        // UserSysMsg userSysMsg = new UserSysMsg();
        // BeanUtils.copyProperties(uiSysMsg, userSysMsg);
        // userSysMsgs.add(userSysMsg);
        // }
        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UserPersonMsgDTO> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);
        // List<UserPersonMsg> userPersonMsgs = new ArrayList<>();
        // for(UiPersonMsg uiPersonMsg: uiPersonMsgs){
        // UserPersonMsg userPersonMsg = new UserPersonMsg();
        // BeanUtils.copyProperties(uiPersonMsg, userPersonMsg);
        // userPersonMsgs.add(userPersonMsg);
        // }
        return uiPersonMsgs;
    }

    @Override
    public Boolean updateUserPersonMsg(String msgId, String userUuid, Boolean readedStatus)
            throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgId, userId, readedStatus);

        return result;
    }

//    @Override
//    public Page<TradeLogDTO> findByUserId(String userUuid, Pageable pageable) throws Exception {
//        Long userId = getUserIdFromUUID(userUuid);
//        Page<UiTrdLog> tradeLogsPage = userInfoRepoService.findTradeLogDtoByUserId(pageable, userId);
//        Page<TradeLogDTO> tradeLogResult = MyBeanUtils
//                .convertPageDTO(pageable, tradeLogsPage, TradeLogDTO.class);
//        return tradeLogResult;
//    }

    @Override
    public List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
            throws InstantiationException, IllegalAccessException, RuntimeException {
        List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
        return userInfoFriendRules;
    }

//    @Override
//    public UserInfoCompanyInfoDTO getCompanyInfo(String userUuid, Long bankId) {
//        Long id = getCompanyId(userUuid, bankId);
//        UiCompanyInfo uiCompanyInfo = userInfoRepoService.getCompanyInfo(id);
//        UserInfoCompanyInfoDTO userInfoCompanyInfo = new UserInfoCompanyInfoDTO();
//        if (null == uiCompanyInfo) {
//            return userInfoCompanyInfo;
//        }
//        BeanUtils.copyProperties(uiCompanyInfo, userInfoCompanyInfo);
//        return userInfoCompanyInfo;
//
//    }

    // TODO: this function will be adjusted by business rule
    private Long getCompanyId(String userUuid, Long bankId) {
        return 1L;
    }

    private Long getUserIdFromUUID(String userUuid) throws Exception {
        Long userId = userInfoRepoService.getUserIdFromUUID(userUuid);
        return userId;
    }

    @Override
    public Boolean deleteBankCard(String userUuid, String bankCardNumber) {
        Boolean result = userInfoRepoService.deleteBankCard(userUuid, bankCardNumber);
        return result;
    }

    @Override
    public Boolean addUiUser(String userUuid, String cellphone, String isTestFlag) {
        Boolean result = userInfoRepoService.saveUser(userUuid, cellphone, isTestFlag);
        return result;
    }

    @Override
    public Boolean updateUiUser(String cellphone, String isTestFlag, String riskLevel) {
        Boolean result = userInfoRepoService.updateCellphone(cellphone, isTestFlag, riskLevel);
        return result;
    }

    @Override
    public UserBaseInfoDTO selectUiUser(String cellphone) {
        UserBaseInfoDTO result = userInfoRepoService.findByCellphone(cellphone);
        return result;
    }

//    @Override
//    public List<TradeLogDTO> findByUserId(String uuid) throws Exception {
//        Long userId = getUserIdFromUUID(uuid);
//        List<TradeLogDTO> uiTrdLogList = userInfoRepoService.findTradeLogDtoByUserId(userId);
//        return uiTrdLogList;
//    }

    @Override
    public List<ProductsDTO> findProductInfos(String uuid) throws Exception {
        List<ProductsDTO> products = userInfoRepoService.findTradeLogDtoByUserId(uuid);
        return products;
    }

    @Override
    public ProductsDTO findByProdId(String prodId)
            throws IllegalAccessException, InstantiationException {
        ProductsDTO products = userInfoRepoService.findByProdId(prodId);
        return products;
    }

    @Override
    public ApplyResult queryTrdResultByOrderDetailId(Long userId, Long orderDetailId) {
        ZhongZhengQueryByOrderDetailId.Builder requestBuilder = ZhongZhengQueryByOrderDetailId
                .newBuilder();

        requestBuilder.setOrderDetailId(orderDetailId);
        requestBuilder.setUserId(userId);

        try {
            requestBuilder.setUserId(userId);
            requestBuilder.setOrderDetailId(orderDetailId);
            com.shellshellfish.aaas.finance.trade.pay.ApplyResult result = payRpcServiceFutureStub
                    .queryZhongzhengTradeInfoByOrderDetailId(requestBuilder.build()).get();
            ApplyResult applyResult = new ApplyResult();
            BeanUtils.copyProperties(result, applyResult);
            return applyResult;
        } catch (InterruptedException e) {
            logger.error("exception:", e);
        } catch (ExecutionException e) {
            logger.error("exception:", e);
            logger.error(e.getMessage());
            return null;
        }
        return null;
    }

    @Override
    public List<TrendYield> getTrendYield(String userUuid) throws Exception {
        Aggregation agg = newAggregation(
                match(Criteria.where("userId").is(getUserIdFromUUID(userUuid))),
                group("createDateStr")
                        .first("createDateStr").as("date")
                        .sum("accumulativeIncome").as("value"));

        List<TrendYield> list = mongoTemplate.aggregate(agg, "user_daily_income", TrendYield.class)
                .getMappedResults();
        if (CollectionUtils.isEmpty(list)) {
            return new ArrayList<>(0);
        }
        List<TrendYield> result = new ArrayList<>(list);
        for (TrendYield trendYield : result) {
            String date = trendYield.getDate().replace("-", "");
            trendYield.setDate(date);
        }
        Collections
                .sort(result, Comparator.comparing(o -> InstantDateUtil.format(o.getDate(), InstantDateUtil.yyyyMMdd)));
        return result;
    }

    @Override
    public Map<String, Object> getTotalAssets(String uuid) throws Exception {
        List<Map<String, Object>> productsList = this.getMyCombinations(uuid);
        if (productsList == null || productsList.size() == 0) {
            logger.error("我的智投组合暂时不存在");
            return new HashMap<>(0);
        }
        Map<String, Object> resultMap = new HashMap<String, Object>();
        BigDecimal asserts = new BigDecimal(0);
        BigDecimal dailyIncome = new BigDecimal(0);
        BigDecimal totalIncome = new BigDecimal(0);
        if (productsList != null && productsList.size() > 0) {
            for (int i = 0; i < productsList.size(); i++) {
                Map<String, Object> products = productsList.get(i);
                if (products.get("totalAssets") != null) {
                    asserts = asserts.add(new BigDecimal(products.get("totalAssets") + "")).setScale(2,
                            RoundingMode.HALF_UP);
                }
                if (products.get("dailyIncome") != null) {
                    dailyIncome = dailyIncome.add(new BigDecimal(products.get("dailyIncome") + ""))
                            .setScale(2,
                                    RoundingMode.HALF_UP);
                }
                if (products.get("totalIncome") != null) {
                    totalIncome = totalIncome.add(new BigDecimal(products.get("totalIncome") + ""))
                            .setScale(2,
                                    RoundingMode.HALF_UP);
                }
            }
        }
        resultMap.put("assert", asserts.setScale(2, RoundingMode.HALF_UP));
        resultMap.put("dailyIncome", dailyIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
        resultMap.put("totalIncome", totalIncome.setScale(2, RoundingMode.HALF_UP));
        if (asserts != BigDecimal.ZERO && !"0.00".equals(asserts + "")) {
            BigDecimal incomeRate = (totalIncome.divide(asserts, MathContext.DECIMAL128)).setScale(4,
                    BigDecimal.ROUND_HALF_UP);
//			BigDecimal incomeRate = totalIncome.divide(asserts, 2, BigDecimal.ROUND_HALF_UP);
            resultMap.put("totalIncomeRate", incomeRate);
        } else {
            resultMap.put("totalIncomeRate", "0");
        }
        return resultMap;
    }

    /**
     * 计算组合的累计净值，累计收益，累计收益率 ，日收益，日收益率
     */
    @Override
    public PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products) {

        List<UiProductDetail> uiProductDetailList = uiProductDetailRepo
                .findAllByUserProdIdAndStatusIn(products.getId(),
                        TrdOrderStatusEnum.WAITPAY.getStatus(),
                        TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        //完全确认标志
        boolean flag = false;
        if (CollectionUtils.isEmpty(uiProductDetailList)) {
            flag = true;
        }

        return getChicombinationAssets(uuid, userId, products, LocalDate.now(), flag);

    }

    /**
     * 计算组合的累计净值，累计收益，累计收益率 ，日收益，日收益率
     */
    @Override
    public PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products,
                                                 LocalDate endDate, boolean flag) {
        Long startDate = products.getCreateDate();
        LocalDate startLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate),
                ZoneId.systemDefault()).toLocalDate();
        String startDay = InstantDateUtil.format(startLocalDate, InstantDateUtil.yyyyMMdd);
        String endDay = InstantDateUtil.format(endDate, InstantDateUtil.yyyyMMdd);

        if (flag) {
            //完全确认
            products.setStatus(TrdOrderStatusEnum.CONFIRMED.getStatus());
            return userAssetService
                    .calculateUserAssetAndIncome(uuid, products.getId(), startDay, endDay);
        } else {
            //部分确认
            logger.info("\n未完全确认数据 userProdId :{}\n", products.getId());
            products.setStatus(TrdOrderStatusEnum.PARTIALCONFIRMED.getStatus());
            return userAssetService.calculateUserAssetAndIncomePartialConfirmed(uuid, userId, products.getId(), startDay, endDay);
        }
    }

    /**
     * 计算组合的累计收益，累计收益率
     */
    @Override
    public Map<String, PortfolioInfo> getCalculateTotalAndRate(String uuid, Long userId,
                                                               ProductsDTO products) {
        Map<String, PortfolioInfo> result = new HashMap<>();
        List<UiProductDetail> uiProductDetailList = uiProductDetailRepo
                .findAllByUserProdIdAndStatusIn(products.getId(),
                        TrdOrderStatusEnum.WAITPAY.getStatus(),
                        TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        //完全确认标志
        boolean flag = false;
        if (CollectionUtils.isEmpty(uiProductDetailList)) {
            flag = true;
        }

        Long startDate = products.getCreateDate();
        LocalDate startLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate),
                ZoneId.systemDefault()).toLocalDate();

        LocalDate endLocalDate = LocalDate.now();
        while (startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate)) {
            String endDay = InstantDateUtil.format(endLocalDate, InstantDateUtil.yyyyMMdd);
            result.put(endDay, getChicombinationAssets(uuid, userId, products, endLocalDate, flag));
            endLocalDate = endLocalDate.plusDays(-1);
        }
        return result;

    }


    @Deprecated
    @Override
    public Map<String, Object> getTradeLogStatus(String uuid, Long userProdId)
            throws Exception {
        Map<String, Object> res = new HashMap<String, Object>();
        Long userId = getUserIdFromUUID(uuid);
        List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService
                .findByUserIdAndProdId(userId, userProdId);
        Map<Integer, Map<String, Object>> resultMap = new HashMap<>();
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (trdLogList != null && trdLogList.size() > 0) {
            String serial = "";
            for (int i = 0; i < trdLogList.size(); i++) {
                Map<String, Object> resultMap2 = new HashMap<String, Object>();
                MongoUiTrdLogDTO trdLog = trdLogList.get(i);
                int status = trdLog.getTradeStatus();
                int operation = trdLog.getOperations();
                serial = trdLog.getApplySerial();
                long lastModifiedDate = 0;
                if (trdLog.getTradeDate() != null && trdLog.getTradeDate() > 0) {
                    lastModifiedDate = trdLog.getTradeDate();
                } else if (trdLog.getLastModifiedDate() != 0) {
                    lastModifiedDate = trdLog.getLastModifiedDate();
                }
                String dateTime = TradeUtil.getReadableDateTime(lastModifiedDate);
                LocalDateTime localDateTime = LocalDateTime
                        .ofInstant(Instant.ofEpochMilli(lastModifiedDate), ZoneOffset.systemDefault());
                if (resultMap.containsKey(status)) {
                    resultMap2 = resultMap.get(status);
                    if (Long.parseLong(resultMap2.get("lastModified") + "") < lastModifiedDate) {
                        resultMap2.put("lastModified", lastModifiedDate);
//						resultMap2.put("date",
//								localDateTime.getYear() + "." + localDateTime.getMonthValue() + "." + localDateTime
//										.getDayOfMonth());
                        resultMap2.put("date", dateTime.split("T")[0]);
//						resultMap2.put("time", localDateTime.getHour() + ":" + localDateTime.getMinute());
                        resultMap2.put("time", dateTime.split("T")[1].substring(0, 8));
                        resultMap2.put("operation", operation);
                        resultMap2.put("serial", serial);
//						resultMap2.put("status", status + "");
                        resultMap.put(status, resultMap2);
                    }
                } else {
                    resultMap2.put("lastModified", lastModifiedDate);
                    resultMap2.put("date", dateTime.split("T")[0]);
                    resultMap2.put("time", dateTime.split("T")[1].substring(0, 8));
                    resultMap2.put("operation", operation);
                    resultMap2.put("serial", serial);
                    resultMap.put(status, resultMap2);
                }
            }

            for (Map.Entry<Integer, Map<String, Object>> entry : resultMap.entrySet()) {
                Map value = entry.getValue();
                try {
                    int status = entry.getKey();
                    Map resultStatusMap = entry.getValue();
                    String operation = "";
                    if (resultStatusMap.get("operation") != null) {
                        operation = resultStatusMap.get("operation") + "";
                    }

                    if (TrdOrderStatusEnum.CONFIRMED.getStatus() == status || TrdOrderStatusEnum.SELLCONFIRMED.getStatus() == status) {
                        value.put("status", CombinedStatusEnum.CONFIRMED.getComment());
                    } else if (TrdOrderStatusEnum.FAILED.getStatus() == status || TrdOrderStatusEnum.REDEEMFAILED.getStatus() == status) {
                        value.put("status", CombinedStatusEnum.CONFIRMEDFAILED.getComment());
                    } else {
                        value.put("status", CombinedStatusEnum.WAITCONFIRM.getComment());
                    }
//					value.put("status", TrdOrderStatusEnum.getComment(entry.getKey()));
                } catch (Exception ex) {
                    logger.error("exception:", ex);

                    value.put("status", "");
                }
                result.add(value);
            }
            res.put("result", result);
            res.put("serial", serial);
        }
        return res;
    }

    @Override
    public List<MongoUiTrdLogDTO> getTradeLogs(String uuid) throws Exception {
        Long userId = getUserIdFromUUID(uuid);
        List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService.findByUserId(userId);
        return trdLogList;
    }

    public List<MongoUiTrdLogDTO> getTradeLogsByUserProdId(List dataList) throws Exception {
        List<MongoUiTrdLogDTO> trdLogList = userInfoRepoService.findByUserProdIdIn(dataList);
        return trdLogList;
    }

    @Override
    public List<Map<String, Object>> getMyCombinations(String uuid) throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        List<ProductsDTO> productsList = this.findProductInfos(uuid);
        if (productsList == null || productsList.size() == 0) {
            logger.error("我的智投组合暂时不存在");
            return resultList;
        }

        Map<String, Object> resultMap;
        ProductsDTO products;
        for (int i = 0; i < productsList.size(); i++) {
            products = productsList.get(i);
            resultMap = new HashMap<>();
            resultMap.put("groupId", products.getProdId());
            resultMap.put("subGroupId", products.getGroupId());

            resultMap.put("title", products.getProdName());
            resultMap.put("createDate", products.getCreateDate());
            // 状态(0-待确认 1-已确认 -1-交易失败)
            List<UiProductDetailDTO> productDetailsList = uiProductService
                    .getProductDetailsByProdId(products.getId());
            Integer count = 0;
            Integer fails = 0;
            Integer status = 0;
            if (productDetailsList != null && productDetailsList.size() > 0) {
                fails = 0;
                count = 0;
                for (int j = 0; j < productDetailsList.size(); j++) {
                    UiProductDetailDTO uiProductDetailDTO = productDetailsList.get(j);
                    if (uiProductDetailDTO.getStatus() != null) {
                        if (TrdOrderStatusEnum.isInWaiting(uiProductDetailDTO.getStatus())) {
                            count++;
                            status = uiProductDetailDTO.getStatus();
                        } else if (TrdOrderStatusEnum.isFailed(uiProductDetailDTO.getStatus())) {
                            fails++;
                        }
                    } else {
                        fails++;
                    }
                }
                if (fails == productDetailsList.size()) {
                    //若组合中全部失败，则不显示
                    continue;
                }
                resultMap.put("count", count);
                if (count > 0) {
                    List<OrderDetail> orderDetails = rpcOrderService
                            .getOrderDetails(products.getId(), status);
                    String type = "";
                    if (orderDetails != null && !orderDetails.isEmpty()) {
                        OrderDetail orderDetail = orderDetails.get(0);
                        int tradeType = orderDetail.getTradeType();
                        type = TrdOrderOpTypeEnum.getComment(tradeType);
                    }
                    resultMap.put("title2", "* 您有" + count + "支基金正在" + type + "确认中");
                }
            }

            Long userId = getUserIdFromUUID(uuid);
            // 总资产
            PortfolioInfo portfolioInfo = this.getChicombinationAssets(uuid, userId, products);
            if (portfolioInfo == null || portfolioInfo.getTotalAssets() == null || portfolioInfo.getTotalAssets().compareTo(BigDecimal.ZERO) <= 0) {
                continue;
            }

            resultMap
                    .put("totalAssets",
                            Optional.ofNullable(portfolioInfo).map(m -> m.getTotalAssets())
                                    .orElse(BigDecimal.ZERO)
                                    .setScale(2, RoundingMode.HALF_UP));
            // 日收益
            resultMap
                    .put("dailyIncome",
                            Optional.ofNullable(portfolioInfo).map(m -> m.getDailyIncome())
                                    .orElse(BigDecimal.ZERO)
                                    .setScale(2, RoundingMode.HALF_UP));
            // 累计收益率
            resultMap.put("totalIncomeRate",
                    Optional.ofNullable(portfolioInfo).map(m -> m.getTotalIncomeRate())
                            .orElse(BigDecimal.ZERO)
                            .setScale(4, RoundingMode.HALF_UP));
            // 累计收益
            resultMap
                    .put("totalIncome",
                            Optional.ofNullable(portfolioInfo).map(m -> m.getTotalIncome())
                                    .orElse(BigDecimal.ZERO)
                                    .setScale(2, RoundingMode.HALF_UP));

            // 智投组合产品ID
            resultMap.put("prodId", products.getId());
            // 买入日期
//			resultMap.put("updateDate", DateUtil.getDateType(products.getUpdateDate()));
            String date = InstantDateUtil.getDayConvertString(products.getCreateDate());
            resultMap.put("updateDate", date);
            resultMap.put("recentDate", Optional.ofNullable(mongoDailyAmountRepository.findFirstByUserProdIdOrderByDateDesc
                    (products.getId())).map(m -> InstantDateUtil.format(m.getDate(), InstantDateUtil.yyyyMMdd).toString())
                    .orElse(InstantDateUtil.now().toString()));
            resultList.add(resultMap);
        }
        return resultList;
    }


    @Override
    public Integer getUserRishLevel(String userId) {
        UiUser uiUser = null;
        try {
            uiUser = userInfoRepoService.getUserInfoByUserUUID(userId);
        } catch (Exception e) {
            logger.error("exception:", e);
        }
        Optional<UiUser> userOptional = Optional.ofNullable(uiUser);
        return userOptional.map(m -> m.getRiskLevel()).orElse(-1);
    }

    /**
     * 从DailyAmount获取，组合资产、组合日收益、组合起始日期
     */
    public Map<String, Object> getCombinations(String uuid, Long prodId) {
        Map<String, Object> resultMap = new HashMap<String, Object>();
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(uuid))
                .addCriteria(Criteria.where("userProdId").is(prodId));
        query.with(new Sort(Sort.DEFAULT_DIRECTION.DESC, "date"));
        List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
        String date = "";
        BigDecimal assets = BigDecimal.ZERO;
        BigDecimal assetsOneDayBeofre = BigDecimal.ZERO;
        int count = 0;
        if (dailyAmountList != null && dailyAmountList.size() > 0) {
            for (int i = 0; i < dailyAmountList.size(); i++) {
                DailyAmount dailyAmount = dailyAmountList.get(i);
                if (date.equals(dailyAmount.getDate()) || i == 0) {
                    if (count == 0) {
                        assets = assets.add(dailyAmount.getAsset());
                    } else if (count == 1) {
                        assetsOneDayBeofre = assetsOneDayBeofre.add(dailyAmount.getAsset());
                    }
                } else {
                    if (count == 1) {
                        if (assetsOneDayBeofre != null && !assetsOneDayBeofre.equals(BigDecimal.ZERO)) {
                            // 前一天的日期
                            resultMap.put("date2", date);
                            break;
                        }
                    } else if (count == 0) {
                        resultMap.put("date", date);
                        assetsOneDayBeofre = dailyAmount.getAsset();
                    }
                    count++;
                }
                date = dailyAmount.getDate();
            }
        }
        if (assets != null) {
            assets = (assets.divide(new BigDecimal(100))).setScale(2, BigDecimal.ROUND_HALF_UP);
            resultMap.put("assert", assets);
        } else {
            resultMap.put("assert", 0);
        }

        if (assetsOneDayBeofre != null) {
            assetsOneDayBeofre = (assetsOneDayBeofre.divide(new BigDecimal(100)))
                    .setScale(2, BigDecimal.ROUND_HALF_UP);
            resultMap.put("dailyIncome", assets.subtract(assetsOneDayBeofre));
        } else {
            resultMap.put("dailyIncome", 0);
        }

        return resultMap;
    }

    @Override
    public Map<String, Object> getProducts(Long prodId)
            throws IllegalAccessException, InstantiationException {
        Map<String, Object> result = new HashMap<String, Object>();
        ProductsDTO product = new ProductsDTO();
        if (prodId != null) {
            product = userInfoRepoService.findByProdId(prodId + "");
            result.put("id", product.getId());
            result.put("userId", product.getUserId());
            result.put("groupId", product.getProdId());
            result.put("subGroupId", product.getGroupId());
            result.put("prodName", product.getProdName());
            result.put("status", product.getStatus());
        }
        return result;
    }

    @Override
    public List<Map<String, Object>> getTradLogsOfUser(String userUuid) throws Exception {
        List<MongoUiTrdLogDTO> tradeLogList = this.getTradeLogs(userUuid);
        List<Map<String, Object>> tradeLogs = new ArrayList<Map<String, Object>>();
        if (tradeLogList == null || tradeLogList.size() == 0) {
            logger.error("交易记录为空");
            throw new UserInfoException("404", "交易记录为空");
        }
        Map<String, Map<String, Object>> tradLogsMap = new HashMap<String, Map<String, Object>>();
        Map<String, Map<String, Object>> tradLogsSum = new HashMap<String, Map<String, Object>>();
        // 获取最新一天的单个基金的信息
        String dateStr = null;
        for (MongoUiTrdLogDTO mongoUiTrdLogDTO : tradeLogList) {
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                Long userProdId = mongoUiTrdLogDTO.getUserProdId();
                if (mongoUiTrdLogDTO.getFundCode() == null) {
                    continue;
                }
                String fundCode = mongoUiTrdLogDTO.getFundCode();
                int operation = mongoUiTrdLogDTO.getOperations();
                Long dateLong = null;
//				SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//				Date date = new Date(dateLong);
//				String dateTime = simpleDateFormat.format(date);
                if (mongoUiTrdLogDTO.getTradeDate() != null && mongoUiTrdLogDTO.getTradeDate() > 0) {
                    dateStr = TradeUtil.getReadableDateTime(mongoUiTrdLogDTO.getTradeDate());
                    dateLong = mongoUiTrdLogDTO.getTradeDate();
                } else if (mongoUiTrdLogDTO.getLastModifiedDate() != null
                        && mongoUiTrdLogDTO.getLastModifiedDate() > 0) {
                    dateStr = TradeUtil.getReadableDateTime(mongoUiTrdLogDTO.getLastModifiedDate());
                    dateLong = mongoUiTrdLogDTO.getLastModifiedDate();
                } else {
                    logger.error("This tradeLog is with no time:" + mongoUiTrdLogDTO.getCreatedDate());
                    continue;
                }

                map.put("date", dateStr.split("T")[0]);
                dateLong = dateLong / 1000;
                map.put("dateLong", dateLong);
                String ufoKey = userProdId + "-" + fundCode + "-" + operation;
                if (tradLogsMap.containsKey(ufoKey)) {
                    if (tradLogsMap.get(ufoKey) != null) {
                        Map<String, Object> map2 = tradLogsMap.get(ufoKey);
                        if (map2.get("dateLong") != null) {
                            long dateLongold = (long) map2.get("dateLong");
                            if (dateLong < dateLongold) {
                                continue;
                            }
                        }
                    }
                }
                map.put("operations", TrdOrderOpTypeEnum.getComment(mongoUiTrdLogDTO.getOperations()));
                if (mongoUiTrdLogDTO.getOperations() == 1) {
                    map.put("operationsStatus", 1);
                } else if (mongoUiTrdLogDTO.getOperations() == 2) {
                    map.put("operationsStatus", 2);
                } else if (mongoUiTrdLogDTO.getOperations() == 3
                        || mongoUiTrdLogDTO.getOperations() == 4) {
                    map.put("operationsStatus", 3);
                } else {
                    map.put("operationsStatus", 4);
                }
                map.put("tradeStatusValue", mongoUiTrdLogDTO.getTradeStatus());
                map.put("prodId", userProdId);
                if (mongoUiTrdLogDTO.getTradeStatus() == -1) {
                    logger.error("mongoUiTrdLogDTO.getTradeStatus()为-1，prodId：" + userProdId + "--UserId:"
                            + mongoUiTrdLogDTO.getUserId());
                }
                if (userProdId != null && userProdId != 0) {
                    ProductsDTO products = this.findByProdId(userProdId + "");
//					logger.info("理财产品findByProdId查询end");
                    if (products == null) {
                        map.put("prodName", "");
                    } else {
                        map.put("prodName", products.getProdName());
                    }
                } else {
                    map.put("prodName", "");
                }
                Long sumFromLog = null;
                if (mongoUiTrdLogDTO.getTradeConfirmSum() != null && mongoUiTrdLogDTO.getTradeConfirmSum()
                        > 0) {
                    sumFromLog = mongoUiTrdLogDTO.getTradeConfirmSum();
//					logger.info("sumFromLog = mongoUiTrdLogDTO.getTradeConfirmSum():{}",sumFromLog);
                } else if (mongoUiTrdLogDTO.getTradeTargetSum() != null && mongoUiTrdLogDTO
                        .getTradeTargetSum() > 0) {
                    sumFromLog = mongoUiTrdLogDTO.getTradeTargetSum();
//					logger.info("sumFromLog = mongoUiTrdLogDTO.getTradeTargetSum():{}",sumFromLog);
                } else if (mongoUiTrdLogDTO.getTradeConfirmShare() != null && mongoUiTrdLogDTO
                        .getTradeConfirmShare() > 0) {
                    sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare();
//					logger.info("sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare():{}",sumFromLog);
                } else if (mongoUiTrdLogDTO.getTradeTargetShare() != null && mongoUiTrdLogDTO
                        .getTradeTargetShare() > 0) {
                    sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare();
//					logger.info("sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare():{}",sumFromLog);
                } else if (mongoUiTrdLogDTO.getAmount() != null) {
                    sumFromLog = TradeUtil.getLongNumWithMul100(mongoUiTrdLogDTO.getAmount());
//					logger.info("sumFromLog = TradeUtil.getLongNumWithMul100(mongoUiTrdLogDTO.getAmount()):{}",sumFromLog);
                } else {
                    logger.error("havent find trade money or quantity info for userProdId:{} and "
                            + "fundCode:{}", mongoUiTrdLogDTO.getUserProdId(), mongoUiTrdLogDTO.getFundCode());
                    sumFromLog = 0L;
                }
                map.put("amount", TradeUtil.getBigDecimalNumWithDiv100(sumFromLog));

                tradLogsMap.put(ufoKey, map);
            } catch (Exception ex) {
                logger.error(ex.getMessage());
                logger.error("exception:", ex);
                continue;
            }
            // tradeLogs.add(map);
        }

        if (tradLogsMap != null && tradLogsMap.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : tradLogsMap.entrySet()) {

                String[] params = entry.getKey().split("-");
                String uoKey = String.format("%s-%s", params[0], params[2]);
                Map<String, Object> valueMap = entry.getValue();

                Map<String, String> tradeStatusMap = new HashMap<>();
                if (!tradLogsSum.containsKey(uoKey)) {
                    if (valueMap.get("tradeStatusValue") != null) {
                        TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum.getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
                        String comment = TrdStatusToCombStatusUtils.getCSEFromTSE(trdOrderStatus).getComment();
                        tradeStatusMap.put(comment, comment);
                        valueMap.put("tradeStatusMap", tradeStatusMap);
                        valueMap.put("tradeStatus", comment);
                        logger.info("tradeStatusValue:{} trdOrderStatus:{} ", valueMap.get("tradeStatusValue"));
                    }
                    tradLogsSum.put(uoKey, valueMap);
                } else {
                    Map<String, Object> trad = tradLogsSum.get(uoKey);
                    if (trad.get("amount") != null) {
                        BigDecimal amountTotal = new BigDecimal(trad.get("amount") + "");
                        if (valueMap.get("amount") != null) {
                            amountTotal = amountTotal.add(new BigDecimal(valueMap.get("amount") + ""));
                        }
                        valueMap.put("amount", amountTotal);
                        logger.info("now uoKey:{} amountTotal:{}", uoKey, amountTotal);
                    }
                    if (trad.get("tradeStatusValue") != null && trad.get("tradeStatusMap") != null) {
                        tradeStatusMap = (Map<String, String>) trad.get("tradeStatusMap");
                        TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum.getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
                        String comment = TrdStatusToCombStatusUtils.getCSEFromTSE(trdOrderStatus).getComment();
                        tradeStatusMap.put(comment, comment);
                        valueMap.put("tradeStatusMap", tradeStatusMap);
                        valueMap.put("tradeStatus", comment);
                    } else {
                        String comment = CombinedStatusEnum.CONFIRMEDFAILED.getComment();
                        tradeStatusMap.put(comment, comment);
                        valueMap.put("tradeStatusMap", tradeStatusMap);
                        valueMap.put("tradeStatus", comment);
                    }
                    tradLogsSum.put(uoKey, valueMap);
                }
            }

            if (!CollectionUtils.isEmpty(tradLogsSum)) {
                tradLogsSum.forEach((k, v) -> tradeLogs.add(v));
            }
        }
        Collections.sort(tradeLogs, (o1, o2) -> {
            Long map1value = (Long) o1.get("dateLong");
            Long map2value = (Long) o2.get("dateLong");
            return map2value.compareTo(map1value);
        });

        return tradeLogs;
    }

    @Override
    public Map<String, Object> getTradLogsOfUser2(String userUuid, Integer pageSize, Integer pageIndex, Integer type) {
        Map<String, Object> result = new HashMap<String, Object>();
        Long userId = 0L;
        List<Map<String, Object>> tradeLogs = new ArrayList<Map<String, Object>>();
        try {
            userId = getUserIdFromUUID(userUuid);

            List dataList = this.getUsersOfUserProdIds(userId, type);
            Integer total = dataList.size();
            Integer totalPage = 0;
            if (total % pageSize == 0) {
                totalPage = total / pageSize;
            } else {
                totalPage = total / pageSize + 1;
            }
            List data = new ArrayList<>();
            int begin = pageSize * pageIndex;
            for (int i = 0; i < pageSize; i++) {
                if (dataList.size() <= begin) {
                    break;
                }
                data.add(dataList.get(begin++));
            }
            if (data == null || data.size() == 0) {
                result.put("tradeLogs", tradeLogs);
                return result;
            }
            List<MongoUiTrdLogDTO> tradeLogList = this.getTradeLogsByUserProdId(data);
            if (tradeLogList == null || tradeLogList.size() == 0) {
                logger.error("交易记录为空 userUuid:{}, type:{}", userUuid, type);
                throw new UserInfoException("404", String.format("交易记录为空 userUuid:{}, type:{}", userUuid, type));
            }
            Map<String, Map<String, Object>> tradLogsMap = new HashMap<String, Map<String, Object>>();
            Map<String, Map<String, Object>> tradLogsSum = new HashMap<String, Map<String, Object>>();
            // 获取最新一天的单个基金的信息
            String dateStr = null;
            for (MongoUiTrdLogDTO mongoUiTrdLogDTO : tradeLogList) {
                Map<String, Object> map = new HashMap<String, Object>();
                try {
                    Long userProdId = mongoUiTrdLogDTO.getUserProdId();
                    if (mongoUiTrdLogDTO.getFundCode() == null) {
                        continue;
                    }
                    String fundCode = mongoUiTrdLogDTO.getFundCode();
                    int operation = mongoUiTrdLogDTO.getOperations();
                    Long dateLong = null;
                    if (mongoUiTrdLogDTO.getTradeDate() != null && mongoUiTrdLogDTO.getTradeDate() > 0) {
                        dateStr = TradeUtil.getReadableDateTime(mongoUiTrdLogDTO.getTradeDate());
                        dateLong = mongoUiTrdLogDTO.getTradeDate();
                    } else if (mongoUiTrdLogDTO.getLastModifiedDate() != null
                            && mongoUiTrdLogDTO.getLastModifiedDate() > 0) {
                        dateStr = TradeUtil.getReadableDateTime(mongoUiTrdLogDTO.getLastModifiedDate());
                        dateLong = mongoUiTrdLogDTO.getLastModifiedDate();
                    } else {
                        logger.error("This tradeLog is with no time:" + mongoUiTrdLogDTO.getCreatedDate());
                        continue;
                    }

                    map.put("date", dateStr.split("T")[0]);
                    dateLong = dateLong / 1000;
                    map.put("dateLong", dateLong);
                    String ufoKey = userProdId + "-" + fundCode + "-" + operation;
                    if (tradLogsMap.containsKey(ufoKey)) {
                        if (tradLogsMap.get(ufoKey) != null) {
                            Map<String, Object> map2 = tradLogsMap.get(ufoKey);
                            if (map2.get("dateLong") != null) {
                                long dateLongold = (long) map2.get("dateLong");
                                if (dateLong < dateLongold) {
                                    continue;
                                }
                            }
                        }
                    }
                    map.put("operations", TrdOrderOpTypeEnum.getComment(mongoUiTrdLogDTO.getOperations()));
                    if (mongoUiTrdLogDTO.getOperations() == 1) {
                        map.put("operationsStatus", 1);
                    } else if (mongoUiTrdLogDTO.getOperations() == 2) {
                        map.put("operationsStatus", 2);
                    } else if (mongoUiTrdLogDTO.getOperations() == 3
                            || mongoUiTrdLogDTO.getOperations() == 4) {
                        map.put("operationsStatus", 3);
                    } else {
                        map.put("operationsStatus", 4);
                    }
                    map.put("tradeStatusValue", mongoUiTrdLogDTO.getTradeStatus());
                    map.put("prodId", userProdId);
                    if (mongoUiTrdLogDTO.getTradeStatus() == -1) {
                        logger.error("mongoUiTrdLogDTO.getTradeStatus()为-1，prodId：" + userProdId + "--UserId:"
                                + mongoUiTrdLogDTO.getUserId());
                    }
                    if (userProdId != null && userProdId != 0) {
                        ProductsDTO products = this.findByProdId(userProdId + "");
                        // logger.info("理财产品findByProdId查询end");
                        if (products == null) {
                            map.put("prodName", "");
                        } else {
                            map.put("prodName", products.getProdName());
                        }
                    } else {
                        map.put("prodName", "");
                    }
                    Long sumFromLog = null;
                    //if the log is of type buy record, we sum up the sum values first
                    if(mongoUiTrdLogDTO.getOperations() == TrdOrderOpTypeEnum.BUY.getOperation()){
                        if (mongoUiTrdLogDTO.getTradeConfirmSum() != null
                            && mongoUiTrdLogDTO.getTradeConfirmSum() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeConfirmSum();
                        } else if (mongoUiTrdLogDTO.getTradeTargetSum() != null
                            && mongoUiTrdLogDTO.getTradeTargetSum() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeTargetSum();
                        } else if (mongoUiTrdLogDTO.getTradeConfirmShare() != null
                            && mongoUiTrdLogDTO.getTradeConfirmShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare();
                        } else if (mongoUiTrdLogDTO.getTradeTargetShare() != null
                            && mongoUiTrdLogDTO.getTradeTargetShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare();
                        } else if (mongoUiTrdLogDTO.getAmount() != null) {
                            sumFromLog = TradeUtil.getLongNumWithMul100(mongoUiTrdLogDTO.getAmount());
                        } else {
                            logger.error(
                                "havent find trade money info for userProdId:{} and " + "fundCode:{}",
                                mongoUiTrdLogDTO.getUserProdId(), mongoUiTrdLogDTO.getFundCode());
                            sumFromLog = 0L;
                        }
                    }else{
                        //if the log is of type sell record, we sum up the num values first
                        if (mongoUiTrdLogDTO.getTradeConfirmShare() != null
                            && mongoUiTrdLogDTO.getTradeConfirmShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare();
                        } else if (mongoUiTrdLogDTO.getTradeTargetShare() != null
                            && mongoUiTrdLogDTO.getTradeTargetShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare();
                        }else if (mongoUiTrdLogDTO.getTradeConfirmSum() != null
                            && mongoUiTrdLogDTO.getTradeConfirmSum() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeConfirmSum();
                        } else if (mongoUiTrdLogDTO.getTradeTargetSum() != null
                            && mongoUiTrdLogDTO.getTradeTargetSum() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeTargetSum();
                        } else if (mongoUiTrdLogDTO.getAmount() != null) {
                            sumFromLog = TradeUtil.getLongNumWithMul100(mongoUiTrdLogDTO.getAmount());
                        } else {
                            logger.error(
                                "havent find trade quantity info for userProdId:{} and " + "fundCode:{}",
                                mongoUiTrdLogDTO.getUserProdId(), mongoUiTrdLogDTO.getFundCode());
                            sumFromLog = 0L;
                        }
                    }

                    map.put("amount", TradeUtil.getBigDecimalNumWithDiv100(sumFromLog));

                    tradLogsMap.put(ufoKey, map);
                } catch (Exception ex) {
                    logger.error(ex.getMessage());
                    logger.error("exception:", ex);
                    continue;
                }
            }

            if (tradLogsMap != null && tradLogsMap.size() > 0) {
                for (Map.Entry<String, Map<String, Object>> entry : tradLogsMap.entrySet()) {

                    String[] params = entry.getKey().split("-");
                    String uoKey = String.format("%s-%s", params[0], params[2]);
                    Map<String, Object> valueMap = entry.getValue();

                    Map<String, String> tradeStatusMap = new HashMap<>();
                    if (!tradLogsSum.containsKey(uoKey)) {
                        if (valueMap.get("tradeStatusValue") != null) {
                            TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum
                                    .getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
                            String comment =
                                    TrdStatusToCombStatusUtils.getCSEFromTSE(trdOrderStatus).getComment();
                            tradeStatusMap.put(comment, comment);
                            valueMap.put("tradeStatusMap", tradeStatusMap);
                            valueMap.put("tradeStatus", comment);
                            logger.info("tradeStatusValue:{} trdOrderStatus:{} ",
                                    valueMap.get("tradeStatusValue"));
                        }
                        tradLogsSum.put(uoKey, valueMap);
                    } else {
                        Map<String, Object> trad = tradLogsSum.get(uoKey);
                        if (trad.get("amount") != null) {
                            BigDecimal amountTotal = new BigDecimal(trad.get("amount") + "");
                            if (valueMap.get("amount") != null) {
                                amountTotal = amountTotal.add(new BigDecimal(valueMap.get("amount") + ""));
                            }
                            valueMap.put("amount", amountTotal);
                            logger.info("now uoKey:{} amountTotal:{}", uoKey, amountTotal);
                        }
                        if (trad.get("tradeStatusValue") != null && trad.get("tradeStatusMap") != null) {
                            tradeStatusMap = (Map<String, String>) trad.get("tradeStatusMap");
                            TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum
                                    .getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
                            String comment =
                                    TrdStatusToCombStatusUtils.getCSEFromTSE(trdOrderStatus).getComment();
                            tradeStatusMap.put(comment, comment);
                            valueMap.put("tradeStatusMap", tradeStatusMap);
                            valueMap.put("tradeStatus", comment);
                        } else {
                            String comment = CombinedStatusEnum.CONFIRMEDFAILED.getComment();
                            tradeStatusMap.put(comment, comment);
                            valueMap.put("tradeStatusMap", tradeStatusMap);
                            valueMap.put("tradeStatus", comment);
                        }
                        tradLogsSum.put(uoKey, valueMap);
                    }
                }

                if (!CollectionUtils.isEmpty(tradLogsSum)) {
                    tradLogsSum.forEach((k, v) -> tradeLogs.add(v));
                }
            }
            Collections.sort(tradeLogs, (o1, o2) -> {
                Long map1value = (Long) o1.get("dateLong");
                Long map2value = (Long) o2.get("dateLong");
                return map2value.compareTo(map1value);
            });
            result.put("totalPage", totalPage);
            result.put("totalRecord", total);
            result.put("currentPage", pageSize);
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("tradeLogs", tradeLogs);
        return result;
    }

    public List getUsersOfUserProdIds(Long userId, Integer type) {
        List<Long> dataList = new ArrayList<>();
        try {
            DBObject dbObject = new BasicDBObject();
            dbObject.put("user_id", userId);
            if (type != 0) {
                dbObject.put("operations", type);
            }
            DB db = mongoClient.getDB(mongoDatabase.getName());
            dataList = db.getCollection("ui_trdlog").distinct("user_prod_id", dbObject);
            Collections.sort(dataList, new Comparator<Long>() {
                public int compare(Long o1, Long o2) {
                    return o2.compareTo(o1);
                }
            });
//            dataList = db.getCollection("ui_trdlog").distinct("last_modified_date", dbObject);
//            Collections.sort(dataList, new Comparator<Long>() {
//                public int compare(Long o1, Long o2) {
//                    return o2.compareTo(o1);
//                }
//            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataList;
    }

    @Override
    public Map<String, Object> selectUserFindAll(Pageable pageable) throws InstantiationException, IllegalAccessException {
      Map<String, Object> resudltMap = new HashMap<String, Object>();
      Page<UiUser> users = userInfoRepoService.secectUsers(pageable);
      
      List<UiUser> userList = users.getContent();
      List<UserBaseInfoDTO> userBaseList = MyBeanUtils.convertList(userList, UserBaseInfoDTO.class);
      resudltMap.put("users", userBaseList);
      resudltMap.put("totalPages", users.getTotalPages());
      resudltMap.put("currentPages", users.getPageable().getPageNumber());
      resudltMap.put("size", users.getSize());
      
      return resudltMap;
    }
}
