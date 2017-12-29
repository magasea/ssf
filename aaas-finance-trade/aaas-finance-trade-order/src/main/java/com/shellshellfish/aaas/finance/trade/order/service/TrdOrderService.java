package com.shellshellfish.aaas.finance.trade.order.service;

import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfo;
import com.shellshellfish.aaas.finance.trade.grpc.TrdOrderInfoQuery;
import io.grpc.stub.StreamObserver;

public interface TrdOrderService {
	void getOrderInfo(TrdOrderInfoQuery query, StreamObserver<TrdOrderInfo> responseObserver);

}
