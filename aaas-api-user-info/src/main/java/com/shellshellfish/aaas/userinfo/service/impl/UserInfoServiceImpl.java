package com.shellshellfish.aaas.userinfo.service.impl;

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
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc;
import com.shellshellfish.aaas.finance.trade.pay.PayRpcServiceGrpc.PayRpcServiceFutureStub;
import com.shellshellfish.aaas.finance.trade.pay.ZhongZhengQueryByOrderDetailId;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.exception.UserInfoException;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.UiBankcard;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.model.dao.UserDailyIncomeAggregation;
import com.shellshellfish.aaas.userinfo.model.dto.*;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoUserDailyIncomeRepository;
import com.shellshellfish.aaas.userinfo.service.*;
import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import io.grpc.ManagedChannel;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
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
import java.util.stream.Collectors;

import static com.shellshellfish.aaas.common.utils.InstantDateUtil.*;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;


@Service
public class UserInfoServiceImpl implements UserInfoService {

    Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);

    @Autowired
    @Qualifier("mongoTemplate")
    MongoTemplate template;

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
    public UserBaseInfoDTO getUserInfoBase(String userUuid) {
        UserBaseInfoDTO userInfoDao = userInfoRepoService.getUserInfo(userUuid);

        logger.info(
                "com.shellshellfish.aaas.userinfo.service.impl.UserInfoServiceImpl.getUserInfoBase(String)===>end");
        return userInfoDao;
    }


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
        return bankcards;
    }


    @Override
    public BankCardDTO getUserInfoBankCard(String cardNumber) throws RuntimeException {
        BankCardDTO bankCard = userInfoRepoService.getUserInfoBankCard(cardNumber);
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
        return uiAssetDailyRepts;
    }


    @Override
    public List<UserSysMsgDTO> getUserSysMsg(String userUuid)
            throws IllegalAccessException, InstantiationException {
        List<UserSysMsgDTO> userSysMsgs = userInfoRepoService.getUiSysMsg();

        return userSysMsgs;
    }

    @Override
    public List<UserPersonMsgDTO> getUserPersonMsg(String userUuid) throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        List<UserPersonMsgDTO> uiPersonMsgs = userInfoRepoService.getUiPersonMsg(userId);

        return uiPersonMsgs;
    }

    @Override
    public Boolean updateUserPersonMsg(String msgId, String userUuid, Boolean readedStatus)
            throws Exception {
        Long userId = getUserIdFromUUID(userUuid);
        Boolean result = userInfoRepoService.updateUiUserPersonMsg(msgId, userId, readedStatus);

        return result;
    }


    @Override
    public List<UserInfoFriendRuleDTO> getUserInfoFriendRules(Long bankId)
            throws InstantiationException, IllegalAccessException, RuntimeException {
        List<UserInfoFriendRuleDTO> userInfoFriendRules = userInfoRepoService.getUiFriendRule(bankId);
        return userInfoFriendRules;
    }


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

    /**
     * 确认前
     * ------- asset = 申购金额（标记为昨日）
     * ------- 昨日收益 = 0
     * ------- 走势图最后一日收益 = 昨日收益
     * 确认后
     * ------- asset = 基金×昨日净值×（1-赎回费率）（标记为昨日）
     * ------- 昨日收益 = asset - 申购金额
     * ------- 走势图最后一日收益（昨日）= 昨日收益
     *
     * @param userUuid
     * @return
     * @throws Exception
     */
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

        Iterator<TrendYield> iterator = result.iterator();
        TrendYield today = null;
        TrendYield yesterday = null;
        while (iterator.hasNext()) {
            TrendYield trendYield = iterator.next();
            String date = trendYield.getDate().replace("-", "");
            trendYield.setDate(date);

            if (now().toString().equalsIgnoreCase(date)) {
                today = trendYield;
                today.setDate(InstantDateUtil.format(yesterday(), yyyyMMdd));
                iterator.remove();
            }
            if (yesterday().toString().equalsIgnoreCase(date)) {
                yesterday = trendYield;
                iterator.remove();
            }
        }
        // 确认日当天,会生成当日的asset 但是因为当日的基金净值还没有生成，所以用的是昨日的基金净值
        // today 不为空，表明今天有确认信息，今天的数据表示昨日（今天的asset 用昨日的净值计算的）
        //
        yesterday = today == null ? yesterday : today;
        if (yesterday != null)
            result.add(yesterday);

        Collections
                .sort(result, Comparator.comparing(o -> InstantDateUtil.format(o.getDate(), yyyyMMdd)));


        return result;
    }

    @Override
    public Map<String, Object> getTotalAssets(String uuid) throws Exception {
        List<Map<String, Object>> productsList = this.getMyCombinations(uuid);
        if (CollectionUtils.isEmpty(productsList)) {
            logger.error("我的智投组合暂时不存在");
            return new HashMap<>();
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("count", productsList.size());
        BigDecimal assets = BigDecimal.ZERO;
        BigDecimal dailyIncome = BigDecimal.ZERO;
        BigDecimal totalIncome = BigDecimal.ZERO;
        if (productsList != null && productsList.size() > 0) {
            for (int i = 0; i < productsList.size(); i++) {
                Map<String, Object> products = productsList.get(i);
                if (products.get("totalAssets") != null) {
                    assets = assets.add((BigDecimal) products.get("totalAssets"));
                }
                if (products.get("dailyIncome") != null) {
                    dailyIncome = dailyIncome.add((BigDecimal) products.get("dailyIncome"));
                }
                if (products.get("totalIncome") != null) {
                    totalIncome = totalIncome.add((BigDecimal) products.get("totalIncome"));
                }
            }
        }
        resultMap.put("asset", assets.setScale(2, BigDecimal.ROUND_HALF_UP));
        resultMap.put("dailyIncome", dailyIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
        resultMap.put("totalIncome", totalIncome.setScale(2, BigDecimal.ROUND_HALF_UP));
        if (!BigDecimal.ZERO.equals(assets)) {
            BigDecimal incomeRate = (totalIncome.divide(assets, MathContext.DECIMAL32)).setScale(4,
                    BigDecimal.ROUND_HALF_UP);
            resultMap.put("totalIncomeRate", incomeRate);
        } else {
            resultMap.put("totalIncomeRate", BigDecimal.ZERO);
        }

        return resultMap;
    }

    /**
     * 获取用户从注册以来的累计收益
     *
     * @param userUuid
     * @return
     */
    @Override
    public BigDecimal getTotalIncome(String userUuid) throws Exception {
        //TODO 生命周期已经结束的持仓，没有必要每天都计算其累计收益值，也就不能用这中方法统计其累计收益
        UserDailyIncomeAggregation userDailyIncomeAggregation = mongoUserDailyIncomeRepository.getTotalIncome
                (getUserIdFromUUID(userUuid));

        if (userDailyIncomeAggregation == null)
            return BigDecimal.ZERO;

        return userDailyIncomeAggregation.getTotalIncome();
    }

    /**
     * 计算组合的累计净值，累计收益，累计收益率 ，日收益，日收益率
     */
    @Override
    public PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products,
                                                 LocalDate date) {

        if (date == null) {
            date = InstantDateUtil.now();
        }

        List<OrderDetail> orderDetailList = rpcOrderService.getOrderDetails(products.getId(),
                TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        //完全确认标志
        boolean flag = false;
        if (CollectionUtils.isEmpty(orderDetailList)) {
            flag = true;
        }

        return getChicombinationAssets(uuid, userId, products, date, flag);

    }

    /**
     * 计算组合的累计净值，累计收益，累计收益率 ，日收益，日收益率
     */
    @Override
    public PortfolioInfo getChicombinationAssets(String uuid, Long userId, ProductsDTO products,
                                                 LocalDate endDate, boolean flag) {
        if (flag) {
            //完全确认
            products.setStatus(TrdOrderStatusEnum.CONFIRMED.getStatus());
            return userAssetService
                    .calculateUserAssetAndIncome(products.getId(), endDate);
        } else {
            //部分确认
            logger.info("\n未完全确认数据 userProdId :{}\n", products.getId());
            products.setStatus(TrdOrderStatusEnum.PARTIALCONFIRMED.getStatus());
            return userAssetService
                    .calculateUserAssetAndIncomePartialConfirmed(userId, products.getId(), endDate);
        }
    }

    /**
     * 计算组合的累计收益，累计收益率
     * 确认前
     * ------- asset = 申购金额（标记为昨日）
     * ------- 昨日收益 = 0
     * ------- 走势图最后一日收益 = 昨日收益
     * <p>
     * 确认后
     * ------- asset = 基金×昨日净值×（1-赎回费率）（标记为昨日）
     * ------- 昨日收益 = asset - 申购金额
     * ------- 走势图最后一日收益（昨日）= 昨日收益
     */
    @Override
    public Map<String, PortfolioInfo> getCalculateTotalAndRate(String uuid, Long userId,
                                                               ProductsDTO products) {
        Map<String, PortfolioInfo> result = new HashMap<>();

        List<OrderDetail> orderDetailList = rpcOrderService.getOrderDetails(products.getId(),
                TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        //完全确认标志
        boolean flag = false;
        if (CollectionUtils.isEmpty(orderDetailList)) {
            flag = true;
        }

        Long startDate = products.getCreateDate();
        LocalDate startLocalDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(startDate),
                ZoneId.systemDefault()).toLocalDate();


        LocalDate endLocalDate = yesterday();
        while (startLocalDate.isBefore(endLocalDate) || startLocalDate.isEqual(endLocalDate)) {
            String endDay = InstantDateUtil.format(endLocalDate, yyyyMMdd);
            result.put(endDay, getChicombinationAssets(uuid, userId, products, endLocalDate, flag));
            endLocalDate = endLocalDate.plusDays(-1);
        }

        //==========================================================================================================
        // 当日确认的持仓，其资产会计算到当日，但是使用的是前一日的基金净值数据（当日基金净值数据尚未生成）
        // 所以此时资产时间定为昨天（需求定的）
        //==========================================================================================================
        DailyAmount dailyAmount = mongoDailyAmountRepository.findFirstByUserProdIdOrderByDateDesc(products.getId());
        LocalDate date = Optional.ofNullable(dailyAmount).map(m -> InstantDateUtil.format(m.getDate(), yyyyMMdd)).orElse
                (yesterday());

        //补丁程序
        if (now().equals(date)) {
            // 该只持仓今日有确认信息
            // 今日净值尚未产生，所以今日资产= 份额×净值×（1-赎回费率） 故标记为昨日
            // 昨日收益 也用 今日的数据
            PortfolioInfo portfolioInfo = getChicombinationAssets(uuid, userId, products,
                    now(), flag);
            portfolioInfo.setDate(yesterday());
            result.replace(InstantDateUtil.format(yesterday(), yyyyMMdd), portfolioInfo);
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

                    if (TrdOrderStatusEnum.CONFIRMED.getStatus() == status
                            || TrdOrderStatusEnum.SELLCONFIRMED.getStatus() == status) {
                        value.put("status", CombinedStatusEnum.CONFIRMED.getComment());
                    } else if (TrdOrderStatusEnum.FAILED.getStatus() == status
                            || TrdOrderStatusEnum.REDEEMFAILED.getStatus() == status) {
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
        List<Map<String, Object>> resultList = new ArrayList<>();
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
                        }
                        if (TrdOrderStatusEnum.notSuccess(uiProductDetailDTO.getStatus())) {
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
                if (count >= 0) {
                    List<OrderDetail> orderDetails = rpcOrderService
                            .getOrderDetails(products.getId(), Integer.MAX_VALUE);
                    String type = "";
                    if (orderDetails != null && !orderDetails.isEmpty()) {
                        OrderDetail orderDetail = orderDetails.get(0);
                        int tradeType = orderDetail.getTradeType();
                        type = TrdOrderOpTypeEnum.getComment(tradeType);
                        count = orderDetails.stream()
                                .filter(o -> TrdOrderStatusEnum.isInWaiting(o.getOrderDetailStatus()))
                                .collect(Collectors.toList()).size();
                        resultMap.put("count", count);
                    }
                    if (count > 0) {
                        resultMap.put("title2", "* 您有" + count + "支基金正在" + type + "确认中");
                    }
                }
            }

            Long userId = getUserIdFromUUID(uuid);
            // 总资产
            PortfolioInfo portfolioInfo = this.getChicombinationAssets(uuid, userId, products, null);
            if (portfolioInfo == null || portfolioInfo.getTotalAssets() == null
                    || portfolioInfo.getTotalAssets().compareTo(BigDecimal.ZERO) <= 0) {
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

            //==========================================================================================================
            // 当日确认的持仓，其资产会计算到当日，但是使用的是前一日的基金净值数据（当日基金净值数据尚未生成）
            // 所以此时资产时间定为昨天（需求定的）
            //==========================================================================================================

            DailyAmount dailyAmount = mongoDailyAmountRepository.findFirstByUserProdIdOrderByDateDesc
                    (products.getId());

            String recentDate;
            if (dailyAmount == null && now().equals(InstantDateUtil.format(products.getCreateDate())))
                //申购日当天显示当日
                recentDate = InstantDateUtil.format(now(), yyyyMMdd);
            else {
                recentDate = Optional.ofNullable(dailyAmount)
                        .map(m -> m.getDate())
                        .orElse(InstantDateUtil.format(yesterday(), yyyyMMdd));

                recentDate = InstantDateUtil.format(recentDate, yyyyMMdd).isBefore(now()) ? recentDate :
                        InstantDateUtil.format(yesterday(), yyyyMMdd);
            }

            resultMap.put("recentDate", InstantDateUtil.format(recentDate, yyyyMMdd).toString());
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
                        TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum
                                .getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
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
                        TrdOrderStatusEnum trdOrderStatus = TrdOrderStatusEnum
                                .getTrdOrderStatusEnum(Integer.parseInt(valueMap.get("tradeStatusValue") + ""));
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
    public Map<String, Object> getTradLogsOfUser2(String userUuid, Integer pageSize,
                                                  Integer pageIndex, Integer type) {
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
                throw new UserInfoException("404",
                        String.format("交易记录为空 userUuid:{}, type:{}", userUuid, type));
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
           /*
            *  List<MongoUiTrdLog> distinctElements = originList.stream().filter(distinctByKey(p -> p
        .getUserProdId()+ p.getFundCode() +p.getOperations()+ p.getTradeStatus() + (p
        .getApplySerial() == null? p.getTradeDate():p.getApplySerial())))
        .collect(Collectors.toList());
            *
            * */

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
                    if (mongoUiTrdLogDTO.getOperations() == TrdOrderOpTypeEnum.BUY.getOperation()) {
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
                    } else {
                        //if the log is of type sell record, we sum up the num values first
                        if (mongoUiTrdLogDTO.getTradeConfirmShare() != null
                                && mongoUiTrdLogDTO.getTradeConfirmShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare();
                        } else if (mongoUiTrdLogDTO.getTradeTargetShare() != null
                                && mongoUiTrdLogDTO.getTradeTargetShare() > 0) {
                            sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare();
                        } else if (mongoUiTrdLogDTO.getTradeConfirmSum() != null
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

    @Override
    public Map<String, Object> getTradLogsOfUser3(String userUuid, Integer pageSize,
                                                  Integer pageIndex, Integer type) {
        Long userId = 0L;
        Map<String, Object> result = new HashMap<String, Object>();
        final List<Map<String, Object>> tradeLogs = new ArrayList<>();
        try {
            userId = getUserIdFromUUID(userUuid);
            Map<String, Long> ordersDate = this.getOrderIds(pageSize, pageIndex, userId, type);
            List<MongoUiTrdLogDTO> mongoUiTrdLogDTOS = userInfoRepoService
                    .findByOrderIdIn(ordersDate.keySet());
            Map<String, Map<String, Object>> funds = this
                    .combineTradeLogsToFunds(mongoUiTrdLogDTOS);
            this.combineFundsToProduct(funds, tradeLogs);
            //合并基金时间组合成product时间
            for (Map<String, Object> log : tradeLogs) {
                String orderId = (String) log.get("orderId");
                Long aLong = ordersDate.get(orderId);
                System.out.println(aLong);
                String dateString = TradeUtil.getReadableDateTime(aLong).split("T")[0] + " " + TradeUtil.getReadableDateTime(aLong).split("T")[1].split("\\.")[0];
                log.put("date", dateString);
                log.put("dateLong", aLong / 1000);
                System.out.println(dateString);
                //log.remove("orderId");
            }

            Collections.sort(tradeLogs, (o1, o2) -> {
                Long map1value = (Long) o1.get("dateLong");
                Long map2value = (Long) o2.get("dateLong");
                return map2value.compareTo(map1value);
            });
            int total = this.getTotal(type, userId);
            int totalPage = 0;
            if (total != 0) {
                totalPage = (total - 1) / pageSize + 1;
            }

            result.put("totalPage", totalPage);
            result.put("totalRecord", total);
            result.put("currentPage", pageSize);

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println(tradeLogs.size());
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
    public Map<String, Object> selectUserFindAll(Pageable pageable)
            throws InstantiationException, IllegalAccessException {
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

    /**
     * @param pageSize    页数
     * @param currentPage 当前页
     * @param userId      用户id
     * @Descible 按时间获取前pagesize的操作的orderId
     */
    public Map<String, Long> getOrderIds(int pageSize, int currentPage, Long userId,
                                         Integer operation) {
        int operationCode = operation == null ? 0 : operation;
        Aggregation aggregation = null;
        if (operation > 0) {
            aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("user_id").is(userId)),
                    Aggregation.match(Criteria.where("operations").is(operationCode)),
                    Aggregation.match(Criteria.where("order_id").exists(true)),
                    Aggregation.sort(Direction.ASC, "trade_date"),
                    Aggregation.group("order_id").first("trade_date").as("trade_date"),
                    Aggregation.sort(Direction.DESC, "trade_date"),
                    Aggregation.skip(currentPage * pageSize * 1L),
                    Aggregation.limit(pageSize)

            );
        } else {
            aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("user_id").is(userId)),
                    Aggregation.match(Criteria.where("order_id").exists(true)),
                    Aggregation.sort(Direction.ASC, "trade_date"),
                    Aggregation.group("order_id").first("trade_date").as("trade_date"),
                    Aggregation.sort(Direction.DESC, "trade_date"),
                    Aggregation.skip(currentPage * pageSize * 1L),
                    Aggregation.limit(pageSize)

            );
        }
        Map<String, Long> map = new HashMap<>();

        System.out.println(template == null);
        AggregationResults<Document> aggregate = template
                .aggregate(aggregation, "ui_trdlog", Document.class);
        System.out.println(template.getDb().getName());
        List<Document> mappedResults = aggregate.getMappedResults();
        //List<String> list = new LinkedList<>();
        int size = mappedResults.size();
        System.out.println("document size:" + size);
        for (Document document : mappedResults) {
            String id = document.get("_id", String.class);
            Long dateLong = document.getLong("trade_date");
            map.put(id, dateLong);
            //list.add(id);
        }

        return map;
    }

    public int getTotal(Integer operation, Long userId) {
        int operationCode = operation == null ? 0 : operation;
        Aggregation aggregation = null;
        if (operation > 0) {
            aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("user_id").is(userId)),
                    Aggregation.match(Criteria.where("operations").is(operationCode)),
                    Aggregation.match(Criteria.where("order_id").exists(true)),
                    Aggregation.group("order_id").last("trade_date").as("trade_date"),
                    Aggregation.sort(Direction.DESC, "trade_date")
            );
        } else {
            aggregation = Aggregation.newAggregation(
                    Aggregation.match(Criteria.where("user_id").is(userId)),
                    Aggregation.match(Criteria.where("order_id").exists(true)),
                    Aggregation.group("order_id").last("trade_date").as("trade_date"),
                    Aggregation.sort(Direction.DESC, "trade_date")

            );
        }
        AggregationResults<Document> aggregate = template
                .aggregate(aggregation, "ui_trdlog", Document.class);

        return aggregate.getMappedResults().size();
    }


    public Map<String, Map<String, Object>> combineTradeLogsToFunds(
            List<MongoUiTrdLogDTO> tradeLogList) {
        String dateStr = null;
        Map<String, String> products2 = null;
        boolean failure = true;
        Map<String, Map<String, Object>> tradLogsMap = new HashMap<String, Map<String, Object>>();

        //Map<String, Map<String, Object>> tradLogsSum = new HashMap<String, Map<String, Object>>();

        for (MongoUiTrdLogDTO mongoUiTrdLogDTO : tradeLogList) {
            Map<String, Object> map = new HashMap<String, Object>();
            try {
                if (products2 == null && failure) {
                    products2 = userInfoRepoService.findAllProducts();
                    System.out.println(products2);
                    if (products2 == null) {
                        failure = false;
                    }
                }
                Long userProdId = mongoUiTrdLogDTO.getUserProdId();
                if (mongoUiTrdLogDTO.getFundCode() == null) {
                    continue;
                }
                String fundCode = mongoUiTrdLogDTO.getFundCode();
                map.put("fundCode", fundCode);
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
                map.put("orderId", mongoUiTrdLogDTO.getOrderId());
                dateLong = dateLong / 1000;
                map.put("dateLong", dateLong);
           /*
            *  List<MongoUiTrdLog> distinctElements = originList.stream().filter(distinctByKey(p -> p
        .getUserProdId()+ p.getFundCode() +p.getOperations()+ p.getTradeStatus() + (p
        .getApplySerial() == null? p.getTradeDate():p.getApplySerial())))
        .collect(Collectors.toList());
            *
            * */

                String ufoKey =
                        userProdId + "-" + fundCode + "-" + operation + "-" + mongoUiTrdLogDTO.getOrderId();
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
                    //ProductsDTO products = this.findByProdId(userProdId + "");
                    // logger.info("理财产品findByProdId查询end");
                    if (products2 == null) {
                        map.put("prodName", "");
                    } else {
                        map.put("prodName", products2.get(userProdId + ""));
                    }
                } else {
                    map.put("prodName", "");
                }
                Long sumFromLog = null;
                //if the log is of type buy record, we sum up the sum values first
                if (mongoUiTrdLogDTO.getOperations() == TrdOrderOpTypeEnum.BUY.getOperation()) {
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
                } else {
                    //if the log is of type sell record, we sum up the num values first
                    if (mongoUiTrdLogDTO.getTradeConfirmShare() != null
                            && mongoUiTrdLogDTO.getTradeConfirmShare() > 0) {
                        sumFromLog = mongoUiTrdLogDTO.getTradeConfirmShare();
                    } else if (mongoUiTrdLogDTO.getTradeTargetShare() != null
                            && mongoUiTrdLogDTO.getTradeTargetShare() > 0) {
                        sumFromLog = mongoUiTrdLogDTO.getTradeTargetShare();
                    } else if (mongoUiTrdLogDTO.getTradeConfirmSum() != null
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
        //System.out.println(System.currentTimeMillis() - t1);
        return tradLogsMap;
    }

    public void combineFundsToProduct(Map<String, Map<String, Object>> tradLogsMap,
                                      final List<Map<String, Object>> tradeLogs) {
        Map<String, Map<String, Object>> tradLogsSum = new HashMap<>();
        List<Map<String, Object>> amount = tradLogsMap.values().stream().filter(k ->
                k.get("amount") != null &&
                        k.get("amount") instanceof BigDecimal &&
                        (((BigDecimal) k.get("amount")).compareTo(BigDecimal.ZERO)) > 0).
                collect(Collectors.toList());
        Map<String, Map<String, Object>> fundCodeMap = amount.stream().collect(Collectors.toMap(
                k -> k.get("prodId") + "-" + k.get("fundCode") + "-" + k.get("operationsStatus") + "-" + k.get("orderId"),
                v -> v));
        if (fundCodeMap != null && fundCodeMap.size() > 0) {
            for (Map.Entry<String, Map<String, Object>> entry : fundCodeMap.entrySet()) {

                String[] params = entry.getKey().split("-");
                String uoKey = String.format("%s-%s-%s", params[0], params[2], params[3]);
                Map<String, Object> valueMap = entry.getValue();
                valueMap.remove("fundCode");

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

    }


    @Override
    public Map<String, Object> getMyAssetByProdId(String uuid, String prodId) {
        Map<String, Object> result = new HashMap<String, Object>();
        try {
            Long userId = this.getUserIdFromUUID(uuid);
            ProductsDTO products = userInfoRepoService.findByProdId(prodId + "");
            // 总资产
            PortfolioInfo portfolioInfo = this.getChicombinationAssets(uuid, userId, products, null);

            portfolioInfo.setTotalAssets(Optional.ofNullable(portfolioInfo).map(m -> m.getTotalAssets())
                    .orElse(BigDecimal.ZERO).setScale(2, RoundingMode.HALF_UP));

            result.put("result", portfolioInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }


}
