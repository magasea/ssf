package com.shellshellfish.aaas.finance.trade.pay.service;

import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import java.util.concurrent.ExecutionException;

public interface PayService {

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param payDto
   * @return
   */
  PayDto payOrder(PayDto payDto) throws Exception;

  /**
   * 根据支付情况发出消息，将支付信息广播
   */
  TrdPayFlow notifyPay(TrdPayFlow trdPayFlow);

  /**
   * 根据赎回情况发出消息，将赎回信息广播
   */
  TrdPayFlow notifySell(TrdPayFlow trdPayFlow);

  /**
   * 绑银行卡产生tradAcco
   * @param bindBankCard
   * @return
   */
  String bindCard(BindBankCard bindBankCard) throws ExecutionException, InterruptedException;

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param prodSellDTO
   * @return
   */
  boolean sellProd(ProdSellDTO prodSellDTO) throws Exception;

  /**
   * 根据外部流水号查询交易状态
   *
   */

  ApplyResult queryOrder(Long userId, String applySerial);

  /**
   * 根据外部订单号查询交易状态
   *
   */

  ApplyResult queryOrder(Long userId, Long orderDetailId);
}
