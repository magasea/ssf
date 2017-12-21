package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdCalcService;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdInfoService;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class FinanceProdCalcServiceImpl implements FinanceProdCalcService {

    private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalcServiceImpl.class);

    @Autowired
    private FinanceProdInfoService financeProdInfoService;

    @Autowired
    private FundInfoZhongZhengApiService fundInfoService;

    @Override
    public BigDecimal getMinBuyAmount(ProductBaseInfo productBaseInfo) throws Exception {
        List<BigDecimal> minAmountList = new ArrayList<>();
        List<ProductMakeUpInfo> productMakeUpInfoList = financeProdInfoService.getFinanceProdMakeUpInfo(productBaseInfo);
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService.getTradeLimits(info.getFundCode(), "022");
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                BigDecimal minValue = new BigDecimal(tradeLimitResult.getMinValue());
                minAmountList.add(minValue.divide(BigDecimal.valueOf(info.getFundShare()/10000d)));
            }
        }
        logger.info("{}", minAmountList);
        return Collections.max(minAmountList);
    }

    @Override
    public BigDecimal getMaxBuyAmount(ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public BigDecimal getPoundageOfBuyFund(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException {
        return null;
    }

    @Override
    public BigDecimal getPoundageDiscountSavingOfBuyFund(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException {
        return null;
    }

    public BigDecimal getPoundageOfSellFund() {
        return null;
    }

}
