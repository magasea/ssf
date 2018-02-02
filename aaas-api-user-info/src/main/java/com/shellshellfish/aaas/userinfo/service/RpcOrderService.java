package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.finance.trade.order.OrderResult;

public interface RpcOrderService {

	/**
	 * rpc 调用TrdOrder
	 */
	OrderResult getOrderInfoByProdIdAndOrderStatus(Long userProdId, Integer orderStatus);
}
