package com.shellshellfish.aaas.finance.trade.order.service.impl;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderDetailRepository;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.OrderService;
import com.shellshellfish.aaas.trade.finance.prod.FinanceProductServiceGrpc.FinanceProductServiceImplBase;

public class OrderServiceImpl  extends FinanceProductServiceImplBase implements OrderService{

  @Autowired
  TrdOrderRepository trdOrderRepository;

  @Autowired
  TrdOrderDetailRepository trdOrderDetailRepository;

  @Override
  public List<TrdOrderDetail> getOrderByUserId(Long userId) {
    List<TrdOrderDetail> trdOrderDetails = new ArrayList<>();
    List<TrdOrder> trdOrders = trdOrderRepository.findTrdOrdersByUserId(userId);
    for(TrdOrder trdOrder: trdOrders){

    }
    return null;
  }
  
  
	@Override
	public TrdOrder getOrderByOrderId(String orderId) {
		TrdOrder trdOrders = trdOrderRepository.findByOrderId(orderId);
		return trdOrders;
	}


	@Override
	public List<TrdOrderDetail> findOrderDetailByOrderId(String orderId) {
		//List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findTrdOrderDetailsByOrderId(orderId);
		List<TrdOrderDetail> trdOrderDetails = trdOrderDetailRepository.findAllByOrderId(orderId);
		return trdOrderDetails;
	}
}
