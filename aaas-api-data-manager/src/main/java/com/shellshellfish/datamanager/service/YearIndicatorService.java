package com.shellshellfish.datamanager.service;


import com.shellshellfish.aaas.datamanager.YearIndicatorQuery;
import com.shellshellfish.aaas.datamanager.YearIndicatorRpc;
import io.grpc.stub.StreamObserver;


public interface YearIndicatorService {
	void getHistoryNetByCodeAndQuerydate(YearIndicatorQuery query, StreamObserver<YearIndicatorRpc> responseObserver);

}
