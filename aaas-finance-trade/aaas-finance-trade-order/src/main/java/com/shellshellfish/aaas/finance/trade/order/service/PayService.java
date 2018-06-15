package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.grpc.trade.pay.BindBankCard;
import com.shellshellfish.aaas.common.message.order.PayOrderDto;
import com.shellshellfish.aaas.common.message.order.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.pay.FundNetInfo;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayReq;
import com.shellshellfish.aaas.finance.trade.pay.PreOrderPayResult;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import java.util.List;
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
  String bindCard(BindBankCard bindBankCard) throws Exception;

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

  /**
   * 获取中证的基金净值
   * @param userPid
   * @param fundCodes
   * @param days
   * @return
   * @throws ExecutionException
   * @throws InterruptedException
   */
  public List<FundNetInfo> getFundNetInfo(String userPid , List<String> fundCodes, int days)
      throws ExecutionException, InterruptedException;

  /**
   *
   * @param trdOrderDetail
   * @return
   */
  public TrdPayFlow patchOrderToPay(TrdOrderDetail trdOrderDetail);


}
