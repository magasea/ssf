package com.shellshellfish.datamanager.service;


import com.shellshellfish.aaas.datamanager.DayIndicatorCollection;
import com.shellshellfish.aaas.datamanager.DayIndicatorQuery;
import io.grpc.stub.StreamObserver;


public interface DayIndicatorService {
	void findByCode(DayIndicatorQuery query,StreamObserver<DayIndicatorCollection> responseObserver);

}
