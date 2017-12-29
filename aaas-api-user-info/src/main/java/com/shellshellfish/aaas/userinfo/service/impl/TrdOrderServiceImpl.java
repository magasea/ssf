package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfo;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfoQuery;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderServiceGrpc;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderServiceGrpc.TrdOrderServiceFutureStub;
import com.shellshellfish.aaas.userinfo.service.TrdOrderService;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * @Author pierre
 * 17-12-28
 */
@Service
public class TrdOrderServiceImpl  implements TrdOrderService {

	private TrdOrderServiceFutureStub trdOrderServiceFutureStub;


	@Autowired
	ManagedChannel managedTrdChannel;

	@PostConstruct
	void init(){
		trdOrderServiceFutureStub= TrdOrderServiceGrpc.newFutureStub(managedTrdChannel);
	}


	@Override
	public TrdOrderInfo getOrderInfo(String orderId) throws ExecutionException, InterruptedException {
		TrdOrderInfoQuery.Builder queryBuilder = TrdOrderInfoQuery.newBuilder().setOrderId(orderId);
		TrdOrderInfo orderInfo = trdOrderServiceFutureStub.getOrderInfo(queryBuilder.build()).get();

		return orderInfo;
	}
}
