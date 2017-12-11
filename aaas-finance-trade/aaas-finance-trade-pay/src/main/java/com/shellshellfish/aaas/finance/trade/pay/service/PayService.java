package com.shellshellfish.aaas.finance.trade.pay.service;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;

public interface PayService {

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param trdOrderPay
   * @return
   */
  TrdPayFlow payOrder(TrdOrderPay trdOrderPay);

  /**
   * 根据支付情况发出消息，将支付信息广播
   */
  TrdPayFlow notifyPay(TrdPayFlow trdPayFlow);

}
