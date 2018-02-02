package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.finance.trade.order.OrderDetail;
import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import java.util.List;

public interface RpcOrderService {

	/**
	 * rpc 调用TrdOrder
	 */
	OrderResult getOrderInfoByProdIdAndOrderStatus(Long userProdId, Integer orderStatus);

	List<OrderDetail> getOrderDetails(Long userProdId, Integer orderDetailStatus);
}
