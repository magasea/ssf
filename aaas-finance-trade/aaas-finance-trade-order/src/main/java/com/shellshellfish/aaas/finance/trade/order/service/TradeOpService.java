package com.shellshellfish.aaas.finance.trade.order.service;


import com.shellshellfish.aaas.finance.trade.order.model.vo.FinanceProdBuyInfo;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import java.util.concurrent.ExecutionException;

public interface TradeOpService {

  TrdOrder buyFinanceProduct(FinanceProdBuyInfo financeProdInfo)
      throws Exception;

  Long getUserId(String uuid) throws ExecutionException, InterruptedException;

  void updateByParam(String tradeApplySerial, Long updateDate, Long updateBy, Long id,
      int orderDetailStatus);


}
