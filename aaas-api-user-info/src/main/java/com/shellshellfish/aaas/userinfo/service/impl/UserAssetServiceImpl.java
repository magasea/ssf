package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import com.shellshellfish.aaas.userinfo.service.UserAssetService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 * 用来处理用户持仓的数据
 */
@Service
public class UserAssetServiceImpl implements UserAssetService {

    @Autowired
    MongoDailyAmountRepository mongoDailyAmountRepository;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    @Autowired
    RpcOrderService rpcOrderService;

    Logger logger = LoggerFactory.getLogger(UserAssetServiceImpl.class);


    final String DATE_FORMAT_PATTERN = InstantDateUtil.yyyyMMdd;

    @Override
    public PortfolioInfo calculateUserAssetAndIncome(String userUuid, Long prodId, String startDate, String endDate) {


        // 区间数据
        DailyAmountAggregation dailyAmountAggregation = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid, startDate,
                endDate,
                prodId);

        if (dailyAmountAggregation == null) {
            return PortfolioInfo.getNullInstance();
        }
        //区间结束日前一天数据
        LocalDate startLocalDate = InstantDateUtil.format(startDate, DATE_FORMAT_PATTERN);
        LocalDate endLocalDate = InstantDateUtil.format(endDate, DATE_FORMAT_PATTERN);
        LocalDate oneDayBefore = endLocalDate.plusDays(-1);
        String oneDayBeforeStr = InstantDateUtil.format(oneDayBefore, DATE_FORMAT_PATTERN);

        //区间结束日数据
        DailyAmountAggregation dailyAmountAggregationOfEndDay = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid, endDate, endDate,
                prodId);
        //结束日前一天数据
        DailyAmountAggregation dailyAmountAggregationOfOneDayBefore = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid,
                oneDayBeforeStr, oneDayBeforeStr, prodId);

        if (dailyAmountAggregationOfEndDay == null) {

            LocalDate endLocalDateCopy;
            LocalDate oneDayBeforeCopy = oneDayBefore;

            while (dailyAmountAggregationOfEndDay == null && oneDayBeforeCopy.isAfter(startLocalDate)) {
                if (dailyAmountAggregationOfOneDayBefore != null) {
                    //前推一天
                    oneDayBeforeCopy = oneDayBeforeCopy.plusDays(-1);
                    dailyAmountAggregationOfEndDay = dailyAmountAggregationOfOneDayBefore;
                    dailyAmountAggregationOfOneDayBefore = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid,
                            InstantDateUtil.format(oneDayBeforeCopy, DATE_FORMAT_PATTERN),
                            InstantDateUtil.format(oneDayBeforeCopy, DATE_FORMAT_PATTERN), prodId);
                } else {
                    //前推两天
                    endLocalDateCopy = oneDayBeforeCopy.plusDays(-1);
                    oneDayBeforeCopy = endLocalDateCopy.plusDays(-1);

                    dailyAmountAggregationOfEndDay = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid,
                            InstantDateUtil.format(endLocalDateCopy, DATE_FORMAT_PATTERN),
                            InstantDateUtil.format(endLocalDateCopy, DATE_FORMAT_PATTERN), prodId);

                    dailyAmountAggregationOfOneDayBefore = mongoDailyAmountRepository.getUserAssetAndIncome(userUuid,
                            InstantDateUtil.format(oneDayBeforeCopy, DATE_FORMAT_PATTERN),
                            InstantDateUtil.format(oneDayBeforeCopy, DATE_FORMAT_PATTERN), prodId);
                }
            }
        }

        if (dailyAmountAggregationOfEndDay == null) {
            return PortfolioInfo.getNullInstance();
        }

        //区间数据
        BigDecimal buyAmount = dailyAmountAggregation.getBuyAmount();
        BigDecimal sellAmount = dailyAmountAggregation.getSellAmount();
        BigDecimal bonus = dailyAmountAggregation.getBonus();
        // 区间净赎回金额= 区间该基金累计分红现金+区间该基金累计赎回金额-区间该基金累计购买金额
        BigDecimal intervalAmount = bonus.add(sellAmount).subtract(buyAmount);

        //区间结束日数据
        BigDecimal assetOfEndDay = dailyAmountAggregationOfEndDay.getAsset();
        BigDecimal buyAmountOfEndDay = dailyAmountAggregationOfEndDay.getBuyAmount();
        BigDecimal sellAmountOfEndDay = dailyAmountAggregationOfEndDay.getSellAmount();
        BigDecimal bonusOfEndDay = dailyAmountAggregationOfEndDay.getBonus();
        BigDecimal intervalAmountOfEndDay = bonusOfEndDay.add(sellAmountOfEndDay)
                .subtract(buyAmountOfEndDay);

        //确认当天才会有 asset 值
        if (dailyAmountAggregationOfOneDayBefore == null) {
            dailyAmountAggregationOfOneDayBefore = DailyAmountAggregation.getEmptyInstance();
        }

        //区间结束日前一天数据
        Optional<DailyAmountAggregation> dailyAmountAggregationOfOneDayBeforeOptional = Optional
                .ofNullable(dailyAmountAggregationOfOneDayBefore);

        BigDecimal assetOfOneDayBefore = dailyAmountAggregationOfOneDayBeforeOptional
                .map(DailyAmountAggregation::getAsset).orElse(BigDecimal.ZERO);

        //区间开始总资产 恒为零
        BigDecimal startAsset = BigDecimal.ZERO;

        //累计收益 = 结束日总资产 - 开始日总资产 + 区间净赎回
        BigDecimal totalIncome = assetOfEndDay.add(intervalAmount).subtract(startAsset);

        //日收益=结束日净值 - 前一日净值
        BigDecimal dailyIncome = assetOfEndDay.subtract(assetOfOneDayBefore)
                .add(intervalAmountOfEndDay);

        BigDecimal totalIncomeRate = BigDecimal.ZERO;
        if (startAsset.add(buyAmount).compareTo(BigDecimal.ZERO) != 0) {
            //区间收益率 =(区间结束总资产-起始总资产+区间净赎回金额)/(起始总资产+区间购买金额)
            totalIncomeRate = assetOfEndDay.subtract(startAsset).add(intervalAmount)
                    .divide(startAsset.add(buyAmount), MathContext.DECIMAL128);

        }
        PortfolioInfo portfolioInfo = new PortfolioInfo();

        portfolioInfo.setTotalAssets(assetOfEndDay.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncome(totalIncome.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncomeRate(totalIncomeRate.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setDailyIncome(dailyIncome.setScale(4, RoundingMode.HALF_UP));

        //设置区间分红 ，申购和赎回
        portfolioInfo.setBonus(bonus);
        portfolioInfo.setBuyAmount(buyAmount);
        portfolioInfo.setSellAmount(sellAmount);

        //设置最后一日 分红，申购以及赎回
        portfolioInfo.setBonusOfEndDay(bonusOfEndDay);
        portfolioInfo.setBuyAmountOfEndDay(buyAmountOfEndDay);
        portfolioInfo.setSellAmountOfEndDay(sellAmountOfEndDay);

        portfolioInfo.setAssetOfOneDayBefore(assetOfOneDayBefore);
        return portfolioInfo;
    }

    /**
     * 计算组合部分确认的资产和收益
     */
    @Override
    public PortfolioInfo calculateUserAssetAndIncomePartialConfirmed(String uuid, Long userId, Long prodId,
                                                                     String startDay, String endDay) {
        PortfolioInfo portfolioInfo = calculateUserAssetAndIncome(uuid, prodId, startDay, endDay);

        List<MongoUiTrdZZInfo> mongoUiTrdZZinfoList = mongoUiTrdZZInfoRepo
                .findAllByUserIdAndUserProdIdAndTradeTypeAndTradeStatus(userId, prodId,
                        TrdOrderOpTypeEnum.BUY.getOperation(),
                        TrdOrderStatusEnum.CONFIRMED.getStatus());

        //已经确认部分金额
        BigDecimal conifrmAsset = BigDecimal.ZERO;
        BigDecimal confirmAssetOfEndDay = BigDecimal.ZERO;
        for (MongoUiTrdZZInfo mongoUiTrdZZinfo : mongoUiTrdZZinfoList) {
            logger.info("fundCode:{},confirmSum:{}", mongoUiTrdZZinfo.getFundCode(),
                    mongoUiTrdZZinfo.getTradeConfirmSum());
            conifrmAsset = conifrmAsset.add(TradeUtil.getBigDecimalNumWithDiv100(
                    Optional.ofNullable(mongoUiTrdZZinfo).map(m -> m.getTradeConfirmSum())
                            .orElse(0L)));

            if (endDay.equals(mongoUiTrdZZinfo.getConfirmDate())) {
                confirmAssetOfEndDay = confirmAssetOfEndDay
                        .add(TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZinfo.getTradeConfirmSum()));
            }
        }

        OrderResult orderResult = rpcOrderService
                .getOrderInfoByProdIdAndOrderStatus(prodId,
                        TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        BigDecimal applyAsset = BigDecimal.valueOf(orderResult.getPayAmount())
                .divide(BigDecimal.valueOf(100));

        logger.info("\nuserProdId:{}  ===  applyAsset {}\n", prodId, applyAsset);
        BigDecimal assetOfEndDay = Optional.ofNullable(portfolioInfo.getTotalAssets())
                .orElse(BigDecimal.ZERO);

        logger.info("\nuserProdId:{}  === assetOfEndDay {}\n", prodId, assetOfEndDay);
        // 总资产 = 确认基金资产+ 未确认的基金的申购金额  = 结束日资产（即申购成功部分结束日资产） +（总申购资产-确认部分申购资产）
        BigDecimal asset = assetOfEndDay.add(applyAsset.subtract(conifrmAsset));

        logger.info("\nuserProdId:{}  === asset {}\n", prodId, asset);

        logger.info("\nuserProdId:{}  === confirmAsset {}\n", prodId, conifrmAsset);
        // 累计收益=确认部分资产- 确认部分申购金额  (默认未完全确认  不能追加和赎回)
        BigDecimal toltalIncome = assetOfEndDay.subtract(conifrmAsset);

        // 累计收益率= 累计收益/申购金额
        BigDecimal toltalIncomeRate = Optional.ofNullable(portfolioInfo.getTotalIncomeRate())
                .orElse(BigDecimal.ZERO);

        if (applyAsset.compareTo(BigDecimal.ZERO) != 0) {
            toltalIncomeRate = toltalIncome.divide(applyAsset, 4, RoundingMode.HALF_UP);
        }

        portfolioInfo.setTotalAssets(asset.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncome(toltalIncome.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncomeRate(toltalIncomeRate.setScale(4, RoundingMode.HALF_UP));

        return portfolioInfo;
    }
}
