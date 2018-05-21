package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.dao.UiProductDetail;
import com.shellshellfish.aaas.userinfo.model.dao.UiProducts;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductDetailRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UiProductRepo;
import com.shellshellfish.aaas.userinfo.repositories.mysql.UserInfoRepository;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

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

/**
 * @Author pierre 18-3-1
 */
@Service
public class CalculateConfirmedAsset {

    Logger logger = LoggerFactory.getLogger(CalculateConfirmedAsset.class);

    @Autowired
    UserFinanceProdCalcService userFinanceProdCalcService;

    @Autowired
    UiProductDetailRepo uiProductDetailRepo;

    @Autowired
    UiProductRepo uiProductRepo;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    MongoTemplate zhongZhengMongoTemplate;

    @Autowired
    UserInfoRepository userInfoRepository;

    /**
     * 当有申购或者赎回确认的消息时 ,重新计算资产
     */
    public void calculateConfirmedAsset(MongoUiTrdZZInfo mongoUiTrdZZInfo) {
        final String pattern = InstantDateUtil.yyyyMMdd;
        Optional<UiProducts> uiProducts = uiProductRepo.findById(mongoUiTrdZZInfo.getUserProdId());
        List<UiProductDetail> uiProductDetailList = uiProductDetailRepo
                .findAllByUserProdId(mongoUiTrdZZInfo.getUserProdId());
        String date = mongoUiTrdZZInfo.getConfirmDate();

        //从确认日期开始到当前时间的数据都要修正（此处默认此次确认到当前时间没有其他操作）
        String uuid = Optional.ofNullable(userInfoRepository.findById(mongoUiTrdZZInfo.getUserId()))
                .map(m -> m.get().getUuid()).orElse("-1");

        LocalDate now = LocalDate.now(ZoneId.systemDefault()).plusDays(1);
        LocalDate confirmDate = InstantDateUtil.format(date, pattern);
        for (LocalDate startDate = confirmDate; startDate.isBefore(now); startDate = startDate.plusDays(1)) {
            for (UiProductDetail uiProductDetail : uiProductDetailList) {
                try {
                    userFinanceProdCalcService
                            .calculateFromZzInfo(uiProductDetail, uuid, uiProducts.get().getProdId(),
                                    InstantDateUtil.format(startDate, pattern));
                } catch (Exception e) {
                    logger.error("calculate dailyAmount failed:{}", uiProductDetail, e);
                }
            }
        }
        //确认日期才更新
        updateDailyAmountFromZzInfo(uuid, uiProducts.get().getProdId(), uiProducts.get().getId(),
                mongoUiTrdZZInfo.getFundCode(), InstantDateUtil.format(confirmDate, pattern),
                TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZInfo.getTradeConfirmSum()),
                mongoUiTrdZZInfo.getTradeType());

    }

    private void updateDailyAmountFromZzInfo(String userUuid, Long prodId, Long userProdId,
                                             String fundCode, String startDate, BigDecimal amount, int type) {

        Query query = new Query();
        query.addCriteria(Criteria.where("userUuid").is(userUuid))
                .addCriteria(Criteria.where("date").is(startDate))
                .addCriteria(Criteria.where("fundCode").is(fundCode))
                .addCriteria(Criteria.where("prodId").is(prodId))
                .addCriteria(Criteria.where("userProdId").is(userProdId));

        Update update = new Update();

        if (TrdOrderOpTypeEnum.BUY.getOperation() == type) {
            update.set("buyAmount", amount);
            logger.info("buyAmount:{}", amount);
        }
        if (TrdOrderOpTypeEnum.REDEEM.getOperation() == type) {
            update.set("sellAmount", amount);
            logger.info("sellAmount:{}", amount);
        }

        DailyAmount dailyAmount = zhongZhengMongoTemplate
                .findAndModify(query, update, new FindAndModifyOptions().returnNew(true),
                        DailyAmount.class);
        logger.info(
                "set buyAmount and sell Amount ==> dailyAmount:{}", dailyAmount);

    }
}
