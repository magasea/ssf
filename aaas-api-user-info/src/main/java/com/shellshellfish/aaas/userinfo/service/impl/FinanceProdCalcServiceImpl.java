package com.shellshellfish.aaas.userinfo.service.impl;

import com.rabbitmq.client.AMQP;
import com.shellshellfish.aaas.userinfo.model.*;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.FinanceProdCalcService;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceProdCalcServiceImpl implements FinanceProdCalcService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalcServiceImpl.class);

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
                BigDecimal asset = calcDailyAsset(userUuid, fundCode);
                totalDailyAsset.add(asset);
            }
        }
        return totalDailyAsset;
    }

    @Override
    public BigDecimal calcDailyAsset(String userUuid, String fundCode) throws Exception {
        FundShare fundShare = fundTradeApiService.getFundShare(userUuid, fundCode);
        FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
        BigDecimal share = new BigDecimal(fundShare.getUsableremainshare());
        BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
        BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

        BigDecimal fundAsset = share.multiply(netValue).multiply(BigDecimal.ONE.subtract(rateOfSellFund));
        return fundAsset;
    }

    @Override
    public BigDecimal calcIntervalAmount(String userUuid, String fundCode, String startDate) throws Exception {
        List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
        Map<String, BigDecimal> bonusMap = new HashMap<>();
        FindAndModifyOptions findAndModifyOptions = new FindAndModifyOptions();
        findAndModifyOptions.upsert(true);

        for(BonusInfo info: bonusInfoList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("date").is(info.getConfirmdate()));
            Update update = new Update();
            update.set("bonus", info.getFactbonussum());
            DailyAmount dailyAmount = mongoTemplate.findAndModify(query, update, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }

        List<ConfirmResult> confirmList = fundTradeApiService.getConfirmResults(userUuid, fundCode);
        for(ConfirmResult result:confirmList) {
            Query query = new Query();
            query.addCriteria(Criteria.where("date").is(result.getConfirmdate()));
            Update update = new Update();
            if (result.getBusinflagStr().equals("申购确认")) {
                update.set("buyFund", result.getTradeconfirmsum());
            } else if (result.getBusinflagStr().equals("赎回确认")) {
                update.set("sellFund", result.getTradeconfirmsum());
            }

            DailyAmount dailyAmount = mongoTemplate.findAndModify(query, update, findAndModifyOptions, DailyAmount.class);
            logger.info("dailyAmount:{}", dailyAmount);
        }

        return null;
    }

    @Override
    public BigDecimal calcTotalAssetOfFinanceProduct() {
        return null;
    }
}
