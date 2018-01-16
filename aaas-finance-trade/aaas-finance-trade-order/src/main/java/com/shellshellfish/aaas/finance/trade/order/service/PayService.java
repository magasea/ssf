package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import java.util.concurrent.ExecutionException;

/**
 * Created by chenwei on 2017- 十二月 - 22
 */
public interface PayService {

  /**
   * 绑银行卡产生tradAcco
   * @param bindBankCard
   * @return
   */
  String bindCard(BindBankCard bindBankCard) throws ExecutionException, InterruptedException;

  /**
   * 定时任务检查处于等待支付状态或者等待赎回状态的订单去发起中证接口调用
   * @param payOrderDto
   * @return
   */
  int order2PayJob(PayOrderDto payOrderDto);

  /**
   * 用订单去发起中证接口调用
   * @param payOrderDto
   * @return PayOrderDto
   */
  TrdOrderStatusEnum order2Pay(PayOrderDto payOrderDto);


  /**
   * 用预订单去发起中证接口调用
   * @param preOrderPayReq
   * @return
   */
  PreOrderPayResult preOrder2Pay(PreOrderPayReq preOrderPayReq)
      throws ExecutionException, InterruptedException;

}
