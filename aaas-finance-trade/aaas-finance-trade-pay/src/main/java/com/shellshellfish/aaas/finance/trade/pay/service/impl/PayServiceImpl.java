package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.message.order.TrdOrderPay;
import com.shellshellfish.aaas.finance.trade.pay.model.dao.TrdPayFlow;
import com.shellshellfish.aaas.finance.trade.pay.service.PayService;
import org.springframework.stereotype.Service;

@Service
public class PayServiceImpl implements PayService{

  @Override
  public TrdPayFlow payOrder(TrdOrderPay trdOrderPay) {
    return null;
  }
}
