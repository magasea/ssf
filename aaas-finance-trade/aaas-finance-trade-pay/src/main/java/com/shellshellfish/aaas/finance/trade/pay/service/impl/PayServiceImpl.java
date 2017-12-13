package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;

import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService{

  Logger logger = LoggerFactory.getLogger(PayServiceImpl.class);

  @Autowired
  BroadcastMessageProducers broadcastMessageProducers;

  @Autowired
  FundTradeApiService fundTradeApiService;

  @Override
  public TrdPayFlow payOrder(TrdOrderPay trdOrderPay) {
    logger.info("payOrder fundCode:"+trdOrderPay.getFundCode());
    //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
    trdOrderPay.getFundCode();

    BuyFundResult fundResult = fundTradeApiService.buyFund()
    TrdPayFlow trdPayFlow = new TrdPayFlow();
    return trdPayFlow;
  }

  @Override
  public TrdPayFlow notifyPay(TrdPayFlow trdPayFlow) {
    logger.info("notify trdPayFlow fundCode:" + trdPayFlow.getFundCode());
    com.shellshellfish.aaas.common.message.order.TrdPayFlow trdPayFlowMsg = new com
        .shellshellfish.aaas.common.message.order.TrdPayFlow();
    BeanUtils.copyProperties(trdPayFlow, trdPayFlowMsg);
    broadcastMessageProducers.sendMessage(trdPayFlowMsg);
    return trdPayFlow;
  }
}
