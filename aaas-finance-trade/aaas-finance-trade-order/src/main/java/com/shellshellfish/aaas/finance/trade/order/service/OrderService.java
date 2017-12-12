package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import java.util.List;

public interface OrderService {

  List<TrdOrderDetail> getOrderByUserId(Long userId);

}
