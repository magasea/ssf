package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
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
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.shellshellfish.aaas.common.utils.InstantDateUtil.yyyyMMdd;

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


    @Override
    public PortfolioInfo calculateUserAssetAndIncome(Long userProdId, LocalDate endDate) {

        PortfolioInfo portfolioInfo = PortfolioInfo.getNullInstance();
        logger.debug("calculate user asset userProdId:{}", userProdId);

        List<DailyAmountAggregation> dailyAmountAggregationList = mongoDailyAmountRepository.getUserAssetAndIncome(
                InstantDateUtil.format(endDate, yyyyMMdd), userProdId);
        if (CollectionUtils.isEmpty(dailyAmountAggregationList)) {
            return portfolioInfo;
        }

        portfolioInfo.setTotalAssets(dailyAmountAggregationList.get(0).getAsset());
        portfolioInfo.setDate(InstantDateUtil.format(dailyAmountAggregationList.get(0).getDate(), yyyyMMdd));
        if (dailyAmountAggregationList.size() > 1)
            portfolioInfo.setAssetOfOneDayBefore(dailyAmountAggregationList.get(1).getAsset());

        getIntervalAmount(userProdId, portfolioInfo);
        return portfolioInfo;
    }

    /**
     * 计算组合部分确认的资产和收益
     */
    @Override
    public PortfolioInfo calculateUserAssetAndIncomePartialConfirmed(Long userId, Long prodId, LocalDate endDate) {
        String endDay = InstantDateUtil.format(endDate, yyyyMMdd);

        PortfolioInfo portfolioInfo = calculateUserAssetAndIncome(prodId, endDate);

        List<MongoUiTrdZZInfo> mongoUiTrdZZinfoList = mongoUiTrdZZInfoRepo
                .findAllByUserIdAndUserProdIdAndTradeTypeAndTradeStatus(userId, prodId,
                        TrdOrderOpTypeEnum.BUY.getOperation(),
                        TrdOrderStatusEnum.CONFIRMED.getStatus());

        //已经确认部分金额
        BigDecimal confirmAsset = BigDecimal.ZERO;
        BigDecimal confirmAssetOfEndDay = BigDecimal.ZERO;
        for (MongoUiTrdZZInfo mongoUiTrdZZinfo : mongoUiTrdZZinfoList) {
            //购买不成功，不需要计算
            if (TrdOrderStatusEnum.failBuy(mongoUiTrdZZinfo.getTradeStatus()))
                continue;
            confirmAsset = confirmAsset.add(TradeUtil.getBigDecimalNumWithDiv100(
                    Optional.ofNullable(mongoUiTrdZZinfo).map(m -> m.getTradeConfirmSum())
                            .orElse(0L)));

            if (endDay.equals(mongoUiTrdZZinfo.getConfirmDate())) {
                confirmAssetOfEndDay = confirmAssetOfEndDay
                        .add(TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZinfo.getTradeConfirmSum()));
            }
        }

        List<OrderDetail> orderDetailList = rpcOrderService.getOrderDetails(prodId,
                TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus());

        BigDecimal notConfirmAsset = BigDecimal.ZERO;
        for (OrderDetail orderDetail : orderDetailList) {
            long amount = Optional.ofNullable(orderDetail).map(m -> m.getFundSum()).orElse(0L);
            notConfirmAsset = notConfirmAsset.add(TradeUtil.getBigDecimalNumWithDiv100(amount));
        }

        //总的申购金额＝　确认部分的确认金额＋未确认部分的申购金额
        BigDecimal applyAsset = confirmAsset.add(notConfirmAsset);

        BigDecimal assetOfEndDay = Optional.ofNullable(portfolioInfo.getTotalAssets())
                .orElse(BigDecimal.ZERO);

        // 总资产 = 确认基金资产+ 未确认的基金的申购金额
        BigDecimal asset = assetOfEndDay.add(notConfirmAsset);


        // 累计收益=确认部分资产- 确认部分申购金额  (默认未完全确认  不能追加和赎回)
        BigDecimal totalIncome = assetOfEndDay.subtract(confirmAsset);

        // 累计收益率= 累计收益/申购金额
        BigDecimal totalIncomeRate = Optional.ofNullable(portfolioInfo.getTotalIncomeRate())
                .orElse(BigDecimal.ZERO);

        if (applyAsset.compareTo(BigDecimal.ZERO) != 0) {
            totalIncomeRate = totalIncome.divide(applyAsset, 4, RoundingMode.HALF_UP);
        }

        portfolioInfo.setTotalAssets(asset.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncome(totalIncome.setScale(4, RoundingMode.HALF_UP));
        portfolioInfo.setTotalIncomeRate(totalIncomeRate.setScale(4, RoundingMode.HALF_UP));

        return portfolioInfo;
    }

    private PortfolioInfo getIntervalAmount(Long userProdId, PortfolioInfo portfolioInfo) {

        String dateStr = InstantDateUtil.format(portfolioInfo.getDate(), yyyyMMdd);

        //FIXME  需要额外处理分红的情况
        BigDecimal bonus = BigDecimal.ZERO;
        BigDecimal sellAmount = BigDecimal.ZERO;
        BigDecimal buyAmount = BigDecimal.ZERO;

        BigDecimal bonusOfEndDay = BigDecimal.ZERO;
        BigDecimal sellAmountOfEndDay = BigDecimal.ZERO;
        BigDecimal buyAmountOfEndDay = BigDecimal.ZERO;
        BigDecimal assetOfEndDay = portfolioInfo.getTotalAssets();
        BigDecimal assetOfOneDayBefore = portfolioInfo.getAssetOfOneDayBefore();


        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList = mongoUiTrdZZInfoRepo
                .findAllByUserProdIdAndConfirmDateLessThanEqual(userProdId, dateStr);
        if (CollectionUtils.isEmpty(mongoUiTrdZZInfoList))
            return portfolioInfo;


        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : mongoUiTrdZZInfoList) {
            if (!TrdOrderStatusEnum.isConfirmed(mongoUiTrdZZInfo.getTradeStatus()))
                continue;

            BigDecimal confirmSum = TradeUtil.getBigDecimalNumWithDiv100(mongoUiTrdZZInfo.getTradeConfirmSum());
            if (TrdOrderOpTypeEnum.BUY.getOperation() == mongoUiTrdZZInfo.getTradeType()) {
                buyAmount = buyAmount.add(confirmSum);
                if (dateStr.equals(mongoUiTrdZZInfo.getConfirmDate())) {
                    buyAmountOfEndDay = buyAmountOfEndDay.add(confirmSum);
                }
            } else if (TrdOrderOpTypeEnum.REDEEM.getOperation() == mongoUiTrdZZInfo.getTradeType()) {
                sellAmount = sellAmount.add(confirmSum);
                if (dateStr.equals(mongoUiTrdZZInfo.getConfirmDate())) {
                    sellAmountOfEndDay = sellAmountOfEndDay.add(confirmSum);
                }
            }
        }

        // 区间净赎回金额= 区间该基金累计分红现金+区间该基金累计赎回金额-区间该基金累计购买金额
        BigDecimal intervalAmount = bonus.add(sellAmount).subtract(buyAmount);
        //区间结束日数据
        BigDecimal intervalAmountOfEndDay = bonusOfEndDay.add(sellAmountOfEndDay).subtract(buyAmountOfEndDay);
        //区间开始总资产 恒为零
        BigDecimal startAsset = BigDecimal.ZERO;
        //累计收益 = 结束日总资产 - 开始日总资产 + 区间净赎回
        BigDecimal totalIncome = assetOfEndDay.add(intervalAmount).subtract(startAsset);

        //日收益=结束日净值 - 前一日净值
        BigDecimal dailyIncome = assetOfEndDay.subtract(assetOfOneDayBefore).add(intervalAmountOfEndDay);

        BigDecimal totalIncomeRate = BigDecimal.ZERO;
        if (startAsset.add(buyAmount).compareTo(BigDecimal.ZERO) != 0) {
            //区间收益率 =(区间结束总资产-起始总资产+区间净赎回金额)/(起始总资产+区间购买金额)
            totalIncomeRate = assetOfEndDay.subtract(startAsset).add(intervalAmount)
                    .divide(startAsset.add(buyAmount), MathContext.DECIMAL128);
        }
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
}
