package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfo;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfoQuery;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderServiceGrpc.TrdOrderServiceImplBase;
import com.shellshellfish.aaas.finance.trade.order.model.dao.TrdOrder;
import com.shellshellfish.aaas.finance.trade.order.repositories.TrdOrderRepository;
import com.shellshellfish.aaas.finance.trade.order.service.TrdOrderService;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class TrdOrderServiceImpl extends TrdOrderServiceImplBase implements TrdOrderService {

	@Autowired
	TrdOrderRepository trdOrderRepository;

	@Override
	public void getOrderInfo(TrdOrderInfoQuery query, StreamObserver<TrdOrderInfo> responseObserver) {
		TrdOrder order = trdOrderRepository.findByOrderId(query.getOrderId());
		TrdOrderInfo.Builder orderBuilder = TrdOrderInfo.newBuilder();
		copyProperties(orderBuilder, order);
		responseObserver.onNext(orderBuilder.build());
		responseObserver.onCompleted();
	}


	private void copyProperties(TrdOrderInfo.Builder builder, TrdOrder order) {

		if (!StringUtils.isEmpty(order.getBoughtDate()))
			builder.setBoughtDate(order.getBoughtDate());

		if (!StringUtils.isEmpty(order.getBuyDiscount()))
			builder.setBuyDiscount(builder.getBuyDiscount());

		if (!StringUtils.isEmpty(order.getBuyFee()))
			builder.setBuyFee(order.getBuyFee());

		if (!StringUtils.isEmpty(order.getCreateBy()))
			builder.setCreateBy(order.getCreateBy());

		if (!StringUtils.isEmpty(order.getCreateDate()))
			builder.setCreateDate(order.getCreateDate());

		if (!StringUtils.isEmpty(order.getFundNum()))
			builder.setFundNum(order.getFundNum());

		if (!StringUtils.isEmpty(order.getFundQuantity()))
			builder.setFundQuantity(order.getFundQuantity());

		if (!StringUtils.isEmpty(order.getFundNumConfirmed()))
			builder.setFundNumConfirmed(order.getFundNumConfirmed());

		if (!StringUtils.isEmpty(order.getId()))
			builder.setId(order.getId());

		if (!StringUtils.isEmpty(order.getOrderDate()))
			builder.setOrderDate(order.getOrderDate());

		if (!StringUtils.isEmpty(order.getOrderDetailStatus()))
			builder.setOrderDetailStatus(order.getOrderDetailStatus());

		if (!StringUtils.isEmpty(order.getOrderStatus()))
			builder.setOrderStatus(order.getOrderStatus());

		if (!StringUtils.isEmpty(order.getOrderId()))
			builder.setOrderId(order.getOrderId());

		if (!StringUtils.isEmpty(order.getOrderType()))
			builder.setOrderType(order.getOrderType());

		if (!StringUtils.isEmpty(order.getPayFee()))
			builder.setPayFee(order.getPayFee());

		if (!StringUtils.isEmpty(order.getPayAmount()))
			builder.setPayAmount(order.getPayAmount());

		if (!StringUtils.isEmpty(order.getProdId()))
			builder.setProdId(order.getProdId());

		if (!StringUtils.isEmpty(order.getTradeType()))
			builder.setTradeType(order.getTradeType());

		if (!StringUtils.isEmpty(order.getTradeApplySerial()))
			builder.setTradeApplySerial(order.getTradeApplySerial());

		if (!StringUtils.isEmpty(order.getProdCode()))
			builder.setProdCode(order.getProdCode());

		if (!StringUtils.isEmpty(order.getUserId()))
			builder.setUserId(order.getUserId());

		if (!StringUtils.isEmpty(order.getUserProdId()))
			builder.setUserProdId(order.getUserProdId());

		if (!StringUtils.isEmpty(order.getProdCode()))
			builder.setProdCode(order.getProdCode());


	}
}
