package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.*;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.model.dao.UiUser;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class UserFinanceProdCalcServiceImpl implements UserFinanceProdCalcService {

    private static final Logger logger = LoggerFactory.getLogger(UserFinanceProdCalcServiceImpl.class);

    @Autowired
    private UiProductRepo uiProductRepo;

    @Autowired
    private UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    private FundTradeApiService fundTradeApiService;

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate mongoTemplate;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public BigDecimal calcTotalDailyAsset(String userUuid) throws Exception {
        BigDecimal totalDailyAsset = BigDecimal.ZERO;
        List<UiProducts> userProducts = uiProductRepo.findAll();
        for(UiProducts prod: userProducts) {
            List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
            for(UiProductDetail detail: prodDetails) {
                String fundCode = detail.getFundCode();
                initDailyAmount(userUuid, prod.getProdId(), getTodayAsString(), fundCode);
                BigDecimal asset = calcDailyAsset(userUuid, prod.getProdId(), fundCode, getTodayAsString());
                totalDailyAsset.add(asset);
            }
        }
        return totalDailyAsset;
    }

    @Override
    public BigDecimal calcDailyAsset(String userUuid, Long prodId, String fundCode, String date) throws Exception {
        FundShare fundShare = fundTradeApiService.getFundShare(userUuid, fundCode);
        if (fundShare == null) {
            return BigDecimal.ZERO;
        }
        FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
        BigDecimal share = new BigDecimal(fundShare.getUsableremainshare());
        BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
        BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

        BigDecimal fundAsset = share.multiply(netValue).multiply(BigDecimal.ONE.subtract(rateOfSellFund));

        String today = date;//getTodayAsString();

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(today))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId));

        Update update = new Update();
        update.set("userUuid", userUuid);
        update.set("prodId", prodId);
        update.set("fundCode", fundCode);
        update.set("asset", fundAsset);

        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.upsert(true);
        DailyAmount dailyAmount = mongoTemplate.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
        logger.info("dailyAmount:{}", dailyAmount);

        return fundAsset;
    }

    private String getTodayAsString() {
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(cal.getTime());
    }

    private String getYesterdayAsString() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        return simpleDateFormat.format(cal.getTime());
    }

    @Override
    public void calcIntervalAmount(String userUuid, Long prodId, String fundCode, String startDate) throws Exception {
        List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
        Map<String, BigDecimal> bonusMap = new HashMap<>();
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.upsert(true);

        for(BonusInfo info: bonusInfoList) {
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
            DailyAmount dailyAmount = mongoTemplate.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }

        List<ConfirmResult> confirmList = fundTradeApiService.getConfirmResults(userUuid, fundCode);
        for(ConfirmResult result:confirmList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(userUuid))
                    .addCriteria(Criteria.where("date").is(result.getConfirmdate()))
                    .addCriteria(Criteria.where("fundCode").is(fundCode))
                    .addCriteria(Criteria.where("prodId").is(prodId));

            Update update = new Update();
            update.set("userUuid", userUuid);
            update.set("prodId", prodId);
            update.set("fundCode", fundCode);
            if (result.getBusinflagStr().equals("申购确认")) {
                update.set("buyAmount", result.getTradeconfirmsum());
            } else if (result.getBusinflagStr().equals("赎回确认")) {
                update.set("sellAmount", result.getTradeconfirmsum());
            }

            DailyAmount dailyAmount = mongoTemplate.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }
    }

    @Override
    public void initDailyAmount(String userUuid, Long prodId, String date, String fundCode) {
        DailyAmount dailyAmount = new DailyAmount();
        dailyAmount.setUserUuid(userUuid);
        dailyAmount.setDate(date);
        dailyAmount.setProdId(prodId);
        dailyAmount.setFundCode(fundCode);
        dailyAmount.setAsset(BigDecimal.ZERO);
        dailyAmount.setBonus(BigDecimal.ZERO);
        dailyAmount.setBuyAmount(BigDecimal.ZERO);
        dailyAmount.setSellAmount(BigDecimal.ZERO);
        mongoTemplate.save(dailyAmount);
    }

    /**
     *
     * @param startDate yyyyMMdd
     * @param endDate   yyyyMMdd
     * @return
     */
    @Override
    public BigDecimal calcYieldRate(String userUuid, Long prodId, String startDate, String endDate){
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate))
                .addCriteria(Criteria.where("prodId").is(prodId));

        List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        for(DailyAmount dailyAmount: dailyAmountList) {
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

        BigDecimal result = BigDecimal.ZERO;
        if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
             result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount).divide(assetOfStartDay, MathContext.DECIMAL128);
        }

        return result;
    }

    @Override
    public BigDecimal calcYieldRate(String userUuid, String startDate, String endDate) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").gte(startDate).lte(endDate));

        List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
        BigDecimal assetOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfStartDay = BigDecimal.ZERO;
        BigDecimal intervalAmount = BigDecimal.ZERO;
        for(DailyAmount dailyAmount: dailyAmountList) {
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

        BigDecimal result = BigDecimal.ZERO;
        if (assetOfStartDay.compareTo(BigDecimal.ZERO) != 0) {
            result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount).divide(assetOfStartDay, MathContext.DECIMAL128);
        }

        return result;
    }

    @Scheduled(cron = "0 0 3 * * ?", zone= "Asia/Shanghai")
    @Override
    public void dailyCalculation() throws Exception {
        logger.info("daily calculation every morning: {}", new Date());

        dailyCalculation(getYesterdayAsString());
    }

    @Override
    public void dailyCalculation(String date) throws Exception {
        List<UiUser> users = userInfoRepository.findAll();
        UiUser uiUser = new UiUser();
        uiUser.setUuid("shellshellfish");
        users.clear();
        users.add(uiUser);
        for(UiUser user : users) {
            List<UiProducts> userProducts = uiProductRepo.findByUserId(user.getId());
            for(UiProducts prod: userProducts) {
                List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
                for(UiProductDetail detail: prodDetails) {
                    String fundCode = detail.getFundCode();
                    initDailyAmount(user.getUuid(), prod.getProdId(), date, fundCode);
                    BigDecimal asset = calcDailyAsset(user.getUuid(), prod.getProdId(), fundCode, date);
                    calcIntervalAmount(user.getUuid(), prod.getProdId(), fundCode, date);
                }
            }
        }
    }
    
    @Override
    public BigDecimal getAssert(String userUuid, Long prodId) throws Exception {
        BigDecimal fundAsset = new BigDecimal(0);
        logger.info("dailyAmount:{}", fundAsset);
        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("prodId").is(prodId));

        List<DailyAmount> dailyAmountList = mongoTemplate.find(query, DailyAmount.class);
        if(dailyAmountList!=null&&dailyAmountList.size()>0){
        	fundAsset = dailyAmountList.get(0).getAsset(); 
        }
        return fundAsset;
    }
}
