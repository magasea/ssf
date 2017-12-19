package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;

public interface FinanceProdCalcService {
    BigDecimal getMinBuyValue(ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

    BigDecimal getMaxBuyValue(ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

    BigDecimal getPoundage(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

    BigDecimal getPoundageDiscountSaving(BigDecimal amount, ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;
}
