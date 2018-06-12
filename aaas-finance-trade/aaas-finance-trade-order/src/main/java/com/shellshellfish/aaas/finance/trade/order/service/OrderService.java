package com.shellshellfish.aaas.finance.trade.order.service;

import java.util.List;
import java.util.Map;

import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrderDetail;

public interface OrderService {

	List<TrdOrderDetail> getOrderByUserId(Long userId);

	List<TrdOrderDetail> findOrderDetailByOrderId(String orderId);

	TrdOrder getOrderByOrderId(String orderId);

	List<TrdOrderDetail> getOrderDetailByGenOrderIdAndFundCode(String orderId, String fundCode)
			throws IllegalAccessException;

	TrdOrderDetail getOrderDetailByApplySerial(String applySerial);

	TrdOrder findOrderByUserProdIdAndUserIdAndorderType(Long prodId, Long userId, int orderType);
//	TrdOrder findOrderByUserProdIdAndUserId(Long prodId, Long userId);

	Map<String, Object> getBankInfos(String bankShortName);

	void syncBankInfos();

    Map<String, Object> getBanklists();


}
