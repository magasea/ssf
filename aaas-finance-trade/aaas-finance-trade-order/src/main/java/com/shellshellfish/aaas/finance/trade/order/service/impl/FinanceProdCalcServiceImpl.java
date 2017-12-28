package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.finance.trade.order.model.DistributionResult;
import com.shellshellfish.aaas.finance.trade.order.model.FundAmount;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdCalcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.shellshellfish.aaas.finance.trade.order.util.BusinFlag.BUY_FUND;
import static com.shellshellfish.aaas.finance.trade.order.util.BusinFlag.SELL_FUND;

@Service
public class FinanceProdCalcServiceImpl implements FinanceProdCalcService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalcServiceImpl.class);

    @Autowired
    private FundInfoZhongZhengApiService fundInfoService;

    @Override
    public BigDecimal getMinBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception {
        List<BigDecimal> minAmountList = new ArrayList<>();
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService.getTradeLimits(info.getFundCode(), BUY_FUND.getCode());
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double minValue = Double.parseDouble(tradeLimitResult.getMinValue());
                minAmountList.add(BigDecimal.valueOf(minValue/(info.getFundShare()/10000d)));
            }
        }
        logger.info("{}", minAmountList);
        return Collections.max(minAmountList);
    }

    @Override
    public BigDecimal getMaxBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception {
        List<BigDecimal> maxAmountList = new ArrayList<>();
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService.getTradeLimits(info.getFundCode(), BUY_FUND.getCode());
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double maxValue = Double.parseDouble(tradeLimitResult.getMaxValue());
                maxAmountList.add(BigDecimal.valueOf(maxValue/(info.getFundShare()/10000d)));
            }
        }
        logger.info("{}", maxAmountList);
        return Collections.min(maxAmountList);
    }

    @Override
    public DistributionResult getPoundageOfBuyFund(BigDecimal netAmount, List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception {
        BigDecimal totalPoundage = BigDecimal.ZERO;
        BigDecimal totalDiscountSaving = BigDecimal.ZERO;
        List<FundAmount> fundAmountList = new ArrayList<>();
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            BigDecimal amount = netAmount.multiply(BigDecimal.valueOf(info.getFundShare()).divide(BigDecimal.valueOf(10000d)));
            BigDecimal rate = fundInfoService.getRateOfBuyFund(info.getFundCode(), BUY_FUND.getCode());
            BigDecimal discount = fundInfoService.getDiscount(info.getFundCode(), BUY_FUND.getCode());
            BigDecimal poundage = fundInfoService.calcPoundageSaving(amount, rate, discount);
            BigDecimal discountSaving =  fundInfoService.calcDiscountSaving(amount, rate, discount);

            totalPoundage = totalPoundage.add(poundage);
            totalDiscountSaving = totalDiscountSaving.add(discountSaving);

            FundAmount fundAmount = new FundAmount(info.getFundCode(), info.getFundName(), amount.add(poundage));
            fundAmountList.add(fundAmount);
        }
        return new DistributionResult(totalPoundage, totalDiscountSaving, fundAmountList);
    }

    @Override
    public DistributionResult getPoundageOfSellFund(BigDecimal netAmount, List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception {
        BigDecimal totalPoundage = BigDecimal.ZERO;
        BigDecimal totalDiscountSaving = BigDecimal.ZERO;
        List<FundAmount> fundAmountList = new ArrayList<>();
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            BigDecimal amount = netAmount.multiply(BigDecimal.valueOf(info.getFundShare()).divide(BigDecimal.valueOf(10000d)));
            BigDecimal rate = fundInfoService.getRateOfSellFund(info.getFundCode(), SELL_FUND.getCode());
            BigDecimal discount = fundInfoService.getDiscount(info.getFundCode(), SELL_FUND.getCode());
            BigDecimal poundage = fundInfoService.calcPoundageSaving(amount, rate, discount);
            BigDecimal discountSaving =  fundInfoService.calcPoundageSaving(amount, rate, discount);

            totalPoundage = totalPoundage.add(poundage);
            totalDiscountSaving = totalDiscountSaving.add(discountSaving);

            FundAmount fundAmount = new FundAmount(info.getFundCode(), info.getFundName(), amount.subtract(poundage));
            fundAmountList.add(fundAmount);
        }
        return new DistributionResult(totalPoundage, totalDiscountSaving, fundAmountList);
    }

}
