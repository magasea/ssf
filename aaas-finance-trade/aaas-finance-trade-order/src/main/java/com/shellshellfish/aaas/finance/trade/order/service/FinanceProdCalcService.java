package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

@Service
public interface FinanceProdCalcService {
    BigDecimal getMinBuyAmount(ProductBaseInfo productBaseInfo) throws Exception;

    BigDecimal getMaxBuyAmount(ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

    BigDecimal getPoundageOfBuyFund(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

    BigDecimal getPoundageDiscountSavingOfBuyFund(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;
}
