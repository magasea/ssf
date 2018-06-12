package com.shellshellfish.aaas.userinfo.service;



import com.shellshellfish.aaas.finance.trade.order.OrderResult;
import com.shellshellfish.aaas.grpc.common.OrderDetail;
import com.shellshellfish.aaas.userinfo.model.dto.BankCardDTO;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import java.util.List;

public interface RpcOrderService {

	/**
	 * rpc 调用TrdOrder
	 */
	OrderResult getOrderInfoByProdIdAndOrderStatus(Long userProdId, Integer orderStatus);

	List<OrderDetail> getOrderDetails(Long userProdId, Integer orderDetailStatus);

	String openAccount(BankcardDetailBodyDTO bankcardDetailBodyDTO) throws Exception;

	BankCardDTO createBankCard(BankcardDetailBodyDTO bankcardDetailBodyDTO) throws Exception;
}
