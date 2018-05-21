package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountRepository;
import com.shellshellfish.aaas.userinfo.service.UserAssetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
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
}
