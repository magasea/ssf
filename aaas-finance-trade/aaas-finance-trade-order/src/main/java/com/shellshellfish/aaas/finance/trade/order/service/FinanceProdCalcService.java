package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.finance.trade.order.model.PoundageResult;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public interface FinanceProdCalcService {
    BigDecimal getMinBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception;

    BigDecimal getMaxBuyAmount(List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception;

    PoundageResult getPoundageOfBuyFund(BigDecimal amount, List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception;

    PoundageResult getPoundageOfSellFund(BigDecimal totalAmount, List<ProductMakeUpInfo> productMakeUpInfoList) throws Exception;
}
