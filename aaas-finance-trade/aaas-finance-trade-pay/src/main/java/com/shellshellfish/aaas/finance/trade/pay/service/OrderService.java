package com.shellshellfish.aaas.finance.trade.pay.service;

import com.shellshellfish.aaas.finance.trade.pay.model.dao.mysql.TrdPayFlow;

public interface OrderService {

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param trdPayFlow
   * @return
   */
  String getPidFromTrdAccoBrokerId(TrdPayFlow trdPayFlow   ) throws Exception;


}
