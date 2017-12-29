package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.datamanager.YearIndicatorQuery;
import com.shellshellfish.aaas.datamanager.YearIndicatorRpc;
import com.shellshellfish.aaas.datamanager.YearIndicatorServiceGrpc;
import com.shellshellfish.aaas.datamanager.YearIndicatorServiceGrpc.YearIndicatorServiceFutureStub;
import com.shellshellfish.aaas.userinfo.service.YearIndicatorService;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutionException;

/**
 * @Author pierre
 * 17-12-27
 */
@Service
public class YearIndicatorServiceImpl implements YearIndicatorService {


	private YearIndicatorServiceFutureStub yearIndicatorServiceFutureStub;


	@Autowired
	ManagedChannel managedDMChannel;


	@PostConstruct
	public  void init(){
		yearIndicatorServiceFutureStub = YearIndicatorServiceGrpc.newFutureStub(managedDMChannel);
	}




	@Override
	public YearIndicatorRpc getHistoryNetByCodeAndQuerydate(YearIndicatorQuery query) throws ExecutionException, InterruptedException {
		return yearIndicatorServiceFutureStub.getHistoryNetByCodeAndQuerydate(query).get();
	}
}
