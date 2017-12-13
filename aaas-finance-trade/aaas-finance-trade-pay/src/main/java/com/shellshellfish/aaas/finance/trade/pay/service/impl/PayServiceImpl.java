package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.message.order.TrdOrderDetail;
import com.shellshellfish.aaas.common.utils.DateUtil;
import com.shellshellfish.aaas.finance.trade.pay.message.BroadcastMessageProducers;
import com.shellshellfish.aaas.finance.trade.pay.model.BuyFundResult;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.repositories.TrdBrokerUserRepository;
import com.shellshellfish.aaas.finance.trade.pay.service.FundTradeApiService;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import com.shellshellfish.aaas.finance.trade.pay.util.TradeUtil;
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

  @Autowired
  TrdBrokerUserRepository trdBrokerUserRepository;

  @Override
  public TrdPayFlow payOrder(TrdOrderDetail trdOrderPay) throws Exception {
    logger.info("payOrder fundCode:"+trdOrderPay.getFundCode());
    String userTrdAcco = trdBrokerUserRepository.findByUserId(trdOrderPay.getUserId())
        .getTradeAcco();
    //ToDo: 调用基金交易平台系统接口完成支付并且生成交易序列号供跟踪
    trdOrderPay.getFundCode();
    double payAmount = TradeUtil.getDoubleWithDiv100(trdOrderPay.getPayAmount());
    BuyFundResult fundResult = fundTradeApiService.buyFund(userTrdAcco, payAmount, String
        .valueOf(trdOrderPay.getId()),trdOrderPay.getFundCode());
    TrdPayFlow trdPayFlow = new TrdPayFlow();
    trdPayFlow.setCreateDate(DateUtil.getCurrentDateInLong());
    trdPayFlow.setCreateBy(0L);
    trdPayFlow.setPayAmount((trdOrderPay.getPayAmount()));
    trdPayFlow.setPayStatus(TrdOrderStatusEnum.WAITCONFIRM.ordinal());

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
