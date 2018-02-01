package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.order.OrderQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;
import io.grpc.stub.StreamObserver;
import java.util.List;

public interface OrderService {

	List<TrdOrderDetail> getOrderByUserId(Long userId);

	List<TrdOrderDetail> findOrderDetailByOrderId(String orderId);

	TrdOrder getOrderByOrderId(String orderId);

	TrdOrder findOrderByUserProdIdAndUserId(Long prodId, Long userId);

	void getOrderInfo(OrderQueryInfo request, StreamObserver<OrderResult> responseObserver);

}
