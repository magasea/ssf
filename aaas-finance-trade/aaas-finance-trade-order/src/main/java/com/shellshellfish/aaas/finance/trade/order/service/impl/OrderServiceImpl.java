package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceImplBase;
import java.util.List;
import org.springframework.stereotype.Service;


public class OrderServiceImpl  extends FinanceProductServiceImplBase implements OrderService{

  @Override
  public List<TrdOrderDetail> getOrderByUserId(Long userId) {
    return null;
  }
}
