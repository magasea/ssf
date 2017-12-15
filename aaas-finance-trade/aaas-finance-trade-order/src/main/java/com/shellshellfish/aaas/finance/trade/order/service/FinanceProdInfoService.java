package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FinanceProdInfoService {

  List<ProductMakeUpInfo> getFinanceProdMakeUpInfo(ProductBaseInfo productBaseInfo)
      throws ExecutionException, InterruptedException;


    BigDecimal getMinBuyValue(ProductBaseInfo productBaseInfo) throws ExecutionException, InterruptedException;

  BigDecimal getMaxBuyValue(ProductBaseInfo productBaseInfo);
}
