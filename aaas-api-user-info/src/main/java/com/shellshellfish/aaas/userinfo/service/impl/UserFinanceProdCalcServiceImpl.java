package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.*;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
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
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Override
    public BigDecimal calcTotalDailyAsset(String userUuid) throws Exception {
        BigDecimal totalDailyAsset = BigDecimal.ZERO;
        List<UiProducts> userProducts = uiProductRepo.findAll();
        for(UiProducts prod: userProducts) {
            List<UiProductDetail> prodDetails = uiProductDetailRepo.findAllByUserProdId(prod.getId());
            for(UiProductDetail detail: prodDetails) {
                String fundCode = detail.getFundCode();
                BigDecimal asset = calcDailyAsset(userUuid, fundCode, getTodayAsString());
                totalDailyAsset.add(asset);
            }
        }
        return totalDailyAsset;
    }

    @Override
    public BigDecimal calcDailyAsset(String userUuid, String fundCode, String date) throws Exception {
        FundShare fundShare = fundTradeApiService.getFundShare(userUuid, fundCode);
        FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
        BigDecimal share = new BigDecimal(fundShare.getUsableremainshare());
        BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
        BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

        BigDecimal fundAsset = share.multiply(netValue).multiply(BigDecimal.ONE.subtract(rateOfSellFund));

        String today = date;//getTodayAsString();

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(today))
                .addCriteria(Criteria.where("fundCode").is(fundCode));

        Update update = new Update();
        update.set("userUuid", userUuid);
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

    @Override
    public void calcIntervalAmount(String userUuid, String fundCode, String startDate) throws Exception {
        List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
        Map<String, BigDecimal> bonusMap = new HashMap<>();
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.upsert(true);

        for(BonusInfo info: bonusInfoList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("userUuid").is(userUuid))
                    .addCriteria(Criteria.where("date").is(info.getConfirmdate()))
                    .addCriteria(Criteria.where("fundCode").is(fundCode));

            Update update = new Update();
            update.set("userUuid", userUuid);
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
                    .addCriteria(Criteria.where("fundCode").is(fundCode));

            Update update = new Update();
            update.set("userUuid", userUuid);
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
    public void initDailyAmount(String userUuid, String date, String fundCode) {
        DailyAmount dailyAmount = new DailyAmount();
        dailyAmount.setUserUuid(userUuid);
        dailyAmount.setDate(date);
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
    public BigDecimal calcYieldRate(String userUuid, String startDate, String endDate){
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

        BigDecimal result = assetOfEndDay.subtract(assetOfStartDay).add(intervalAmount).divide(assetOfStartDay, MathContext.DECIMAL128);

        return result;
    }


    @Override
    public BigDecimal calcTotalAssetOfFinanceProduct() {
        return null;
    }
}
