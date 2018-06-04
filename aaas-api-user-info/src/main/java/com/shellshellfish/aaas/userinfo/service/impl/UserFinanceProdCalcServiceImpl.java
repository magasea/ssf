package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.MonetaryFundEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.dao.service.UserInfoRepoService;
import com.shellshellfish.aaas.userinfo.model.BonusInfo;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.*;
import com.shellshellfish.aaas.userinfo.repositories.funds.MongoCoinFundYieldRateRepository;
import com.shellshellfish.aaas.userinfo.repositories.funds.MongoFundYieldRateRepository;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoBankCardsRepository;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.repositories.redis.RedisFundNetDao;
import com.shellshellfish.aaas.userinfo.repositories.redis.RedisSellRateDao;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UserAssetService;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

@Service
public class UserFinanceProdCalcServiceImpl implements UserFinanceProdCalcService {

    private static final Logger logger = LoggerFactory
            .getLogger(UserFinanceProdCalcServiceImpl.class);

    @Autowired
    private UiProductRepo uiProductRepo;

    @Autowired
    private UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    private FundTradeApiService fundTradeApiService;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    private MongoTemplate zhongZhengMongoTemplate;

    @Value("${daily-finance-calculate-thread:10}")
    private int threadNum;

    @Autowired
    private UserInfoRepository userInfoRepository;


    @Autowired
    UserInfoRepoService userInfoRepoService;

    @Autowired
    UserInfoBankCardsRepository userInfoBankCardsRepository;

    @Autowired
    MongoFundYieldRateRepository mongoFundYieldRateRepository;

    @Autowired
    MongoCoinFundYieldRateRepository mongoCoinFundYieldRateRepository;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    @Autowired
    MongoDailyAmountRepository mongoDailyAmountRepository;

    @Autowired
    RpcOrderService rpcOrderService;

    @Autowired
    RedisFundNetDao redisFundNetDao;

    @Autowired
    RedisSellRateDao redisSellRateDao;

    //date format pattern
    private static final String yyyyMMdd = InstantDateUtil.yyyyMMdd;

    /**
     * 计算用户每一次购买，每一只基金,用户所持有的总资产　该方法幂等
     *
     * @param userUuid
     * @param prodId
     * @param userProdId
     * @param fundCode
     * @param date
     * @param uiProductDetail
     * @return
     * @throws Exception
     */
    private BigDecimal calcDailyAsset(String userUuid, Long prodId, Long userProdId, String fundCode,
                                      String date, UiProductDetail uiProductDetail) throws Exception {
        //确认失败的不计算
        if (TrdOrderStatusEnum.failBuy(uiProductDetail.getStatus()))
            return null;

        BigDecimal share = getFundQuantityAtDate(fundCode, userProdId, date, uiProductDetail);
        BigDecimal netValue = getFundNetValue(fundCode, InstantDateUtil.format(date, yyyyMMdd));
        BigDecimal rateOfSellFund = getSellRate(fundCode);
        logger.info("update asset====>>>> share:{},netValue:{}，rateOfSellFund:{}", share, netValue, rateOfSellFund);
        BigDecimal fundAsset = share.multiply(netValue)
                .multiply(BigDecimal.ONE.subtract(rateOfSellFund));

        String today = date;//getTodayAsString();

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(today))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId))
                .addCriteria(Criteria.where("userProdId").is(userProdId));

        Update update = new Update();
        update.set("asset", fundAsset);
        update.set("lastUpdate", System.currentTimeMillis());
        DailyAmount dailyAmount = zhongZhengMongoTemplate
                .findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
                        DailyAmount.class);
        logger.info(
                "set asset ==> dailyAmount:{}", dailyAmount);

        return fundAsset;
    }


    @Override
    public List<Map<String, Object>> getCalcYieldof7days(String fundCode, String type, String date)
            throws Exception {
        List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
        if (!fundCode.contains("OF") && !fundCode.contains("SH") && !fundCode.contains("SZ")) {
            fundCode = fundCode + ".OF";
        }
        Date enddate = null;
        long sttime = 0L;
        long endtime = 0L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String settingdate = "";
        if (date == null) {
            settingdate = sdf.format(new Date());
        } else {
            settingdate = date;
        }
        enddate = sdf.parse(settingdate);
        endtime = enddate.getTime() / 1000;

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(enddate);
        if (type.equals("1")) {
            gc.add(2, -Integer.parseInt("3"));//3 month
        } else if (type.equals("2")) {
            gc.add(2, -Integer.parseInt("6"));//6 month
        } else if (type.equals("3")) {
            gc.add(2, -Integer.parseInt("12"));//1 year
        } else {
            gc.add(2, -Integer.parseInt("36"));//3 year
        }

        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(gc.getTime()));
        sttime = gc.getTime().getTime() / 1000;

        // 货币基金使用附权单位净值
        List<CoinFundYieldRate> coinFundYieldRateList = mongoCoinFundYieldRateRepository
                .findAllByCodeAndQueryDateIsBetween(fundCode, sttime, endtime,
                        new Sort(new Order(Direction.DESC, "querydate")));
        if (coinFundYieldRateList != null && coinFundYieldRateList.size() > 0) {
            for (int i = 0; i < coinFundYieldRateList.size(); i++) {
                Map<String, Object> resultMap = new HashMap<String, Object>();
                CoinFundYieldRate coinFundYieldRate = coinFundYieldRateList.get(i);
                resultMap.put("profit", coinFundYieldRate.getYieldOf7Days());
                resultMap.put("yieldOf7Days", coinFundYieldRate.getYieldOf7Days());
                resultMap.put("code", coinFundYieldRate.getCode());
                Long queryDate = coinFundYieldRate.getQueryDate();
                LocalDateTime localDateTime = LocalDateTime
                        .ofInstant(Instant.ofEpochSecond(queryDate), ZoneId.systemDefault());
                String dateNow = localDateTime.toLocalDate().toString();
                resultMap.put("querydate", dateNow);
                resultMap.put("date", dateNow);
                resultList.add(resultMap);
            }
        }
        return resultList;
    }

    private String getYesterdayAsString() {
        final Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(TradeUtil.getUTCTime1DayBefore());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(yyyyMMdd);
        return simpleDateFormat.format(cal.getTime());
    }

    @Override
    public void calcIntervalAmount(String userUuid, Long prodId, String fundCode, String startDate)
            throws Exception {
        List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.upsert(true);

        for (BonusInfo info : bonusInfoList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(userUuid))
                    .addCriteria(Criteria.where("date").is(info.getConfirmdate()))
                    .addCriteria(Criteria.where("fundCode").is(fundCode))
                    .addCriteria(Criteria.where("prodId").is(prodId));

            Update update = new Update();
            update.set("userUuid", userUuid);
            update.set("prodId", prodId);
            update.set("fundCode", fundCode);
            update.set("bonus", info.getFactbonussum());
            DailyAmount dailyAmount = zhongZhengMongoTemplate
                    .findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }

        List<ConfirmResult> confirmList = fundTradeApiService
                .getConfirmResults(userUuid, fundCode, null);
        for (ConfirmResult result : confirmList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(userUuid))
                    .addCriteria(Criteria.where("date").is(result.getConfirmdate()))
                    .addCriteria(Criteria.where("fundCode").is(fundCode))
                    .addCriteria(Criteria.where("prodId").is(prodId));

            Update update = new Update();
            update.set("userUuid", userUuid);
            update.set("prodId", prodId);
            update.set("fundCode", fundCode);
            if ("022".equals(result.getCallingcode())) {
                update.set("buyAmount", result.getTradeconfirmsum());
            } else if ("024".equals(result.getCallingcode())) {
                update.set("sellAmount", result.getTradeconfirmsum());
            }

            DailyAmount dailyAmount = zhongZhengMongoTemplate
                    .findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }
    }


    private void calcIntervalAmount2(String userUuid, Long prodId, Long userProdId,
                                     String fundCode, String startDate) {

        //FIXME 此处缺少分红
        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoBuy = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
                        fundCode, TrdOrderOpTypeEnum.BUY.getOperation(),
                        TrdOrderStatusEnum.CONFIRMED.getStatus(), startDate);

        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoSell = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDate(userProdId,
                        fundCode, TrdOrderOpTypeEnum.REDEEM.getOperation(),
                        TrdOrderStatusEnum.SELLCONFIRMED.getStatus(), startDate);

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(startDate))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId))
                .addCriteria(Criteria.where("userProdId").is(userProdId));

        Update update = new Update();

        BigDecimal buyAmount = BigDecimal.ZERO;
        BigDecimal sellAmount = BigDecimal.ZERO;

        for (MongoUiTrdZZInfo buy : mongoUiTrdZZInfoBuy) {
            buyAmount = buyAmount.add(Optional.ofNullable(buy)
                    .map(m -> TradeUtil.getBigDecimalNumWithDiv100(m.getTradeConfirmSum()))
                    .orElse(BigDecimal.ZERO));
        }

        for (MongoUiTrdZZInfo sell : mongoUiTrdZZInfoSell) {
            sellAmount = sellAmount.add(Optional.ofNullable(sell)
                    .map(m -> TradeUtil.getBigDecimalNumWithDiv100(m.getTradeConfirmSum()))
                    .orElse(BigDecimal.ZERO));
        }

        update.set("buyAmount", buyAmount);
        update.set("sellAmount", sellAmount);

        DailyAmount dailyAmount = zhongZhengMongoTemplate
                .findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
                        DailyAmount.class);
        logger.info(
                "set buyAmount and sell Amount ==> dailyAmount:{}", dailyAmount);

    }

    @Override
    public void initDailyAmount(String userUuid, Long prodId, Long userProdId, String date,
                                String fundCode) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(date))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId))
                .addCriteria(Criteria.where("userProdId").is(userProdId));

        Update update = new Update();
        update.set("asset", BigDecimal.ZERO);
        update.set("bonus", BigDecimal.ZERO);
        update.set("buyAmount", BigDecimal.ZERO);
        update.set("sellAmount", BigDecimal.ZERO);
        DailyAmount dailyAmount = zhongZhengMongoTemplate
                .findAndModify(query, update, new FindAndModifyOptions().returnNew(true).upsert(true),
                        DailyAmount.class);

        logger.info("update or save  dailyAmount ：{}", dailyAmount);
    }

    /**
     * to be refactored to remove duplicate code
     */
    @Override
    @Deprecated
    public BigDecimal calcYieldValue(String userUuid, Long prodId, String startDate, String endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
                .addCriteria(Criteria.where("userProdId").is(prodId));

        List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        for (DailyAmount dailyAmount : dailyAmountList) {
            if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
                assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
            } else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
                assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
            }

            if (dailyAmount.getBonus() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getBonus());
            }
            if (dailyAmount.getSellAmount() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
            }
            if (dailyAmount.getBuyAmount() != null) {
                intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
            }
        }

        return assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount);
    }

    /**
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     */
    @Override
    @Deprecated
    public BigDecimal calcYieldRate(String userUuid, Long prodId, String startDate, String endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
                .addCriteria(Criteria.where("userProdId").is(prodId));

        List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        BigDecimal buyAmount = BigDecimal.ZERO;
        for (DailyAmount dailyAmount : dailyAmountList) {
            if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
                assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
            } else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
                assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
            }

            if (dailyAmount.getBonus() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getBonus());
            }
            if (dailyAmount.getSellAmount() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
            }
            if (dailyAmount.getBuyAmount() != null) {
                intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
                buyAmount = buyAmount.add(dailyAmount.getBuyAmount());
            }
        }

        BigDecimal result = BigDecimal.ZERO;
        if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
            //(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
            result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
                    .divide(assetOfStartDay.add(buyAmount),
                            MathContext.DECIMAL128);
        }

        return result;
    }


    @Override
    @Deprecated
    public BigDecimal calcYieldValue(String userUuid, String startDate, String endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

        List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        for (DailyAmount dailyAmount : dailyAmountList) {
            if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
                assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
            } else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
                assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
            }

            if (dailyAmount.getBonus() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getBonus());
            }
            if (dailyAmount.getSellAmount() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
            }
            if (dailyAmount.getBuyAmount() != null) {
                intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
            }
        }

        return assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount);
    }


    @Override
    public BigDecimal calcYieldRate(String userUuid, String startDate, String endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

        List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        BigDecimal buyAmount = BigDecimal.ZERO;
        for (DailyAmount dailyAmount : dailyAmountList) {
            if (dailyAmount.getDate().equals(startDate) && dailyAmount.getAsset() != null) {
                assetOfStartDay = assetOfStartDay.add(dailyAmount.getAsset());
            } else if (dailyAmount.getDate().equals(endDate) && dailyAmount.getAsset() != null) {
                assetOfEndDay = assetOfEndDay.add(dailyAmount.getAsset());
            }

            if (dailyAmount.getBonus() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getBonus());
            }
            if (dailyAmount.getSellAmount() != null) {
                intervalAmount = intervalAmount.add(dailyAmount.getSellAmount());
            }
            if (dailyAmount.getBuyAmount() != null) {
                intervalAmount = intervalAmount.subtract(dailyAmount.getBuyAmount());
                buyAmount = buyAmount.add(dailyAmount.getBuyAmount());
            }
        }

        BigDecimal result = BigDecimal.ZERO;
        if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
            //(区间结束总资产-起始总资产+分红+赎回-区间购买金额)/(起始总资产+区间购买金额)
            result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount)
                    .divide((assetOfStartDay.add(buyAmount)), MathContext.DECIMAL128);
        }

        return result;
    }

    //    @Scheduled(cron = "0 0 3 * * ?", zone= "Asia/Shanghai")
    //   定时任务改为脚本执行
    @Deprecated
    @Override
    public void dailyCalculation() throws Exception {
        logger.info("daily calculation every morning: {}", new Date());

        dailyCalculation(getYesterdayAsString());
    }

    @Override
    public void dailyCalculation(String date, List<UiUser> uiUsers) {
        for (UiUser user : uiUsers) {

            List<UiProducts> userProducts = uiProductRepo.findByUserId(user.getId());
            for (UiProducts prod : userProducts) {

                // 查询时间晚于购买时间
                LocalDate localDate = InstantDateUtil.format(date, yyyyMMdd);
                if (InstantDateUtil.getEpochSecondOfZero(localDate) * 1000 < prod.getCreateDate()) {
                    continue;
                }

                List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
                for (UiProductDetail detail : prodDetails) {
                    calculateProductAsset(detail, user.getUuid(), prod.getProdId(), date);
                }
            }
        }
    }

    @Override
    public void calculateProductAsset(UiProductDetail detail, String uuid, Long prodId, String date) {
        logger.info("calculate Product Asset : {}", detail);
        String fundCode = detail.getFundCode();
//        initDailyAmount(uuid, prodId, detail.getUserProdId(), date, fundCode);
        try {
            //计算当日总资产
            calcDailyAsset(uuid, prodId, detail.getUserProdId(), fundCode,
                    date, detail);
            //获取当日分红，以及确认购买和赎回的金额 分红直接从mongo.trdzzinfo中获取
            //calcIntervalAmount2(uuid, prodId, detail.getUserProdId(), fundCode, date);
        } catch (Exception e) {
            logger.error("计算{用户:{},userProdId:{},基金code:{},基金名称：{}}日收益出错", detail.getCreateBy(), detail.getUserProdId(),
                    detail.getFundCode(), detail.getFundName(), e);
            //FIXME  记录错误数据 并返回
        }
    }

    @Override
    public void calculateFromZzInfo(UiProductDetail detail, String uuid, Long prodId, String date)
            throws Exception {

        logger.info("calculate from zzinfo :{}", detail);
        String fundCode = detail.getFundCode();
//        addDailyAmount(uuid, date, fundCode, prodId, detail.getUserProdId());
        //计算当日总资产
        calcDailyAsset(uuid, prodId, detail.getUserProdId(), fundCode,
                date, detail);

    }

    private void addDailyAmount(String userUuid, String date, String fundCode, Long prodId,
                                Long userProdId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(date))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId))
                .addCriteria(Criteria.where("userProdId").is(userProdId));

        DailyAmount dailyAmount1 = zhongZhengMongoTemplate.findOne(query, DailyAmount.class);
        if (dailyAmount1 == null) {
            initDailyAmount(userUuid, prodId, userProdId, date, fundCode);
        }
    }

    /**
     * FIXME date:2018-01-27 author: pierre  threadNum 表示线程数量而不是每个线程处理的数据量
     */
    @Override
    public void dailyCalculation(String date) throws Exception {
        final List<UiUser> users = userInfoRepository.findAll();
        if (users.isEmpty()) {
            logger.info("user list is empty.");
            return;
        }
        int size = users.size();

        if (size == 1) {
            dailyCalculation(date, users);
        } else {
            int countDown;
            if (size % threadNum == 0) {
                countDown = size / threadNum;
            } else {
                countDown = size / threadNum + 1;
            }
            ExecutorService threadPool = Executors.newCachedThreadPool();
            CountDownLatch countDownLatch = new CountDownLatch(countDown);
            for (int i = 0; i < countDown; i++) {
                int fromIndex = i * threadNum;
                int toIndex = (i + 1) * threadNum <= size ? (i + 1) * threadNum : size;
                threadPool.submit(new FinanceProdCalculate(this, date,
                        users.subList(fromIndex, toIndex), countDownLatch));
            }
            countDownLatch.await();
            logger.info("all thread process over.");
        }
    }

    @Override
    public BigDecimal getAssert(String userUuid, Long prodId) throws Exception {
        BigDecimal fundAsset = new BigDecimal(0);
        logger.info("dailyAmount:{}", fundAsset);
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("prodId").is(prodId));

        List<DailyAmount> dailyAmountList = zhongZhengMongoTemplate.find(query, DailyAmount.class);
        if (dailyAmountList != null && dailyAmountList.size() > 0) {
            fundAsset = dailyAmountList.get(0).getAsset();
        }
        return fundAsset;
    }


    /**
     * 计算　没一个userProdId下用户在某一日所持有的基金份额
     */
    private BigDecimal getFundQuantityAtDate(String fundCode, Long userProdId, String date, UiProductDetail
            uiProductDetail) {
        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoOfBuy = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDateGreaterThan(userProdId,
                        fundCode, TrdOrderOpTypeEnum.BUY.getOperation(),
                        TrdOrderStatusEnum.CONFIRMED.getStatus(), date);
        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoSell = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDateGreaterThan(userProdId,
                        fundCode, TrdOrderOpTypeEnum.REDEEM.getOperation(), TrdOrderStatusEnum.SELLCONFIRMED.getStatus(), date);

        //赎回总份额
        Long sellAmount = 0L;
        //申购总份额
        Long buyAmount = 0L;
        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : mongoUiTrdZZInfoOfBuy) {
            buyAmount += mongoUiTrdZZInfo.getTradeConfirmShare();
        }

        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : mongoUiTrdZZInfoSell) {
            sellAmount += mongoUiTrdZZInfo.getTradeConfirmShare();
        }

        //用户当前持有的份额-期间申购份额+期间赎回份额
        Integer fundQuantity = Optional.ofNullable(uiProductDetail).map(m -> m.getFundQuantity()).orElse(0);

        Long result = fundQuantity - buyAmount + sellAmount;

        return TradeUtil.getBigDecimalNumWithDiv100(result);
    }

    /**
     * 获取最近一日胡基金净值　货币基金使用复权单位净值，非货币基金使用单位净值
     */
    private BigDecimal getFundNetValue(String fundCode, LocalDate localDate) {
        String date = InstantDateUtil.format(localDate, yyyyMMdd);
        BigDecimal netValue = redisFundNetDao.get(fundCode, date);
        if (netValue != null)
            return netValue;

        Long endTime = InstantDateUtil.getEpochSecondOfZero(localDate.plusDays(1));
        if (MonetaryFundEnum.containsCode(fundCode)) {
            //货币基金使用附权单位净值
            CoinFundYieldRate coinFundYieldRate = mongoCoinFundYieldRateRepository
                    .findFirstByCodeAndQueryDateBefore(fundCode, endTime,
                            new Sort(new Order(Direction.DESC, "querydate")));
            if (coinFundYieldRate == null || coinFundYieldRate.getNavadj() == null) {
                return BigDecimal.ZERO;
            }
            netValue = coinFundYieldRate.getNavadj();
        } else {
            FundYieldRate fundYieldRate = mongoFundYieldRateRepository
                    .findFirstByCodeAndQueryDateBefore(fundCode, endTime,
                            new Sort(new Order(Direction.DESC, "querydate")));

            if (fundYieldRate == null || fundYieldRate.getUnitNav() == null) {
                return BigDecimal.ZERO;
            }
            netValue = fundYieldRate.getUnitNav();
        }
        redisFundNetDao.set(fundCode, date, netValue);
        return netValue;
    }

    /**
     * 获取基金的赎回费率
     */
    private BigDecimal getSellRate(String fundCode) throws Exception {
        //货币即基金赎回费率为零
        if (MonetaryFundEnum.containsCode(fundCode))
            return BigDecimal.ZERO;

        BigDecimal sellRate = redisSellRateDao.get(fundCode);
        if (sellRate != null)
            return sellRate;

        sellRate = fundTradeApiService.getRate(fundCode, "024");
        redisSellRateDao.set(fundCode, sellRate);
        return sellRate;
    }
}
