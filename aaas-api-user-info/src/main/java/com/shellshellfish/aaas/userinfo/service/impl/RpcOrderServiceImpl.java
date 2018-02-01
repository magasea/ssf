package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.finance.trade.order.OrderQueryInfo;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.finance.trade.order.OrderRpcServiceGrpc.OrderRpcServiceBlockingStub;
import com.shellshellfish.aaas.userinfo.service.RpcOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author pierre 18-1-31
 */
@Service
public class RpcOrderServiceImpl implements RpcOrderService {


	@Autowired
	OrderRpcServiceBlockingStub orderRpcServiceBlockingStub;


	@Override
	public OrderResult getOrderInfoByProdIdAndOrderStatus(Long userProdId, Integer orderStatus) {
		OrderQueryInfo.Builder builder = OrderQueryInfo.newBuilder();
		builder.setOrderStatus(orderStatus);
		builder.setUserProdId(userProdId);

		OrderResult orderResult = orderRpcServiceBlockingStub.getOrderInfo(builder.build());
		return orderResult;
	}
}
