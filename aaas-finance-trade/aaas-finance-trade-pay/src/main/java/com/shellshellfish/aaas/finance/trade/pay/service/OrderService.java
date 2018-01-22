package com.shellshellfish.aaas.finance.trade.pay.service;

public interface OrderService {

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param payOrderDto
   * @return
   */
  String getPidFromTrdAccoBrokerId(com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow
   trdPayFlow   ) throws Exception;


}
