package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.BonusInfo;
import com.shellshellfish.aaas.userinfo.model.FundInfo;
import com.shellshellfish.aaas.userinfo.model.FundShare;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.service.FinanceProdCalcService;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class FinanceProdCalcServiceImpl implements FinanceProdCalcService {

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

    public BigDecimal calcDailyAsset(String userUuid, String fundCode) throws Exception {
        FundShare fundShare = fundTradeApiService.getFundShare(userUuid, fundCode);
        FundInfo fundInfo = fundTradeApiService.getFundInfoAsEntity(fundCode);
        BigDecimal share = new BigDecimal(fundShare.getUsableremainshare());
        BigDecimal netValue = new BigDecimal(fundInfo.getPernetvalue());
        BigDecimal rateOfSellFund = fundTradeApiService.getRate(fundCode, "024");

        BigDecimal fundAsset = share.multiply(netValue).multiply(BigDecimal.ONE.subtract(rateOfSellFund));
        return fundAsset;
    }

    private BigDecimal calcIntervalAmount(String userUuid, String fundCode, String startDate) throws Exception {
        List<BonusInfo> bonusInfoList = fundTradeApiService.getBonusList(userUuid, fundCode, startDate);
        Map<String, BigDecimal> bonusMap = new HashMap<>();
//        mongoTemplate.findAndModify();
        for(BonusInfo info: bonusInfoList) {
//            Query query = new Query();
//            query.addCriteria(Criteria.where("name").is("Markus"));
//            Update update = new Update();
//            update.set("name", "Nick");
//            User user = mongoTemplate.findAndModify(query, update, User.class);

            info.getConfirmdate();
            info.getFactbonussum();
        }
        return null;
    }

    @Override
    public BigDecimal calcTotalAssetOfFinanceProduct() {
        return null;
    }
}
