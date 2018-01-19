package com.shellshellfish.aaas.finance.trade.pay.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.PayPreOrderDto;
import com.shellshellfish.aaas.common.message.order.ProdSellDTO;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.common.grpc.trade.pay.ApplyResult;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import java.util.concurrent.ExecutionException;

public interface PayService {

  /**
   * 根据Order模块消息传来的TrdOrderPay生成支付流水完成支付
   * @param payOrderDto
   * @return
   */
  PayOrderDto payOrder(PayOrderDto payOrderDto) throws Exception;

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
  String bindCard(BindBankCard bindBankCard)
      throws ExecutionException, InterruptedException, JsonProcessingException;

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

  ApplyResult queryOrder(Long userId, Long orderDetailId)
      throws ExecutionException, InterruptedException;

  /**
   * 根据order状态为未完成，找到orderDetail表中没有apply_serial 且fund_quantity > 0 且
   * order_detail_status 为WAITPAY(0）
   * 把对应信息找到拼凑成PayDto 让pay 模块再试一次，试的时候要检查，orderDetailId 是否在pay_flow这表里出现
   * 如果已经出现，只有
   * @param payOrderDto
   * @return
   */
  PayOrderDto payOrderByJob(PayOrderDto payOrderDto) throws Exception;


  /**
   * 用预订单去发起中证接口调用
   * @param preOrderPayReq
   * @return
   */
  PreOrderPayResult preOrder2Pay(PreOrderPayReq preOrderPayReq);

  PayPreOrderDto payPreOrder(PayPreOrderDto message) throws Exception;
}
