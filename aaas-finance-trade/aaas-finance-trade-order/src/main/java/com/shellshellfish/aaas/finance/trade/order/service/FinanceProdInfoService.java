package com.shellshellfish.aaas.finance.trade.order.service;

import static io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall;

import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;

import com.shellshellfish.aaas.trade.finance.prod.FinanceMoneyFundInfo;
import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.ExecutionException;

public interface FinanceProdInfoService {

  List<ProductMakeUpInfo> getFinanceProdMakeUpInfo(ProductBaseInfo productBaseInfo)
      throws ExecutionException, InterruptedException;

  /**用产品组合信息去查询对应的货币基金信息
   */
  public FinanceMoneyFundInfo getMoneyFund(com.shellshellfish.aaas.trade.finance.prod.FinanceProdInfoQuery request);

  /**
   *  用prodId 和 groupId获取基金产品配比
  */

  List<ProductMakeUpInfo> getFinanceProdMakeUpInfo(Long prodId, Long groupId)
      throws ExecutionException, InterruptedException;

}
