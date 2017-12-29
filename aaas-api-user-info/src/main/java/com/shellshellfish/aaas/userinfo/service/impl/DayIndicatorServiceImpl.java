package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.datamanager.DayIndicatorQuery;
import com.shellshellfish.aaas.datamanager.DayIndicatorRpc;
import com.shellshellfish.aaas.datamanager.DayIndicatorServiceGrpc;
import com.shellshellfish.aaas.datamanager.DayIndicatorServiceGrpc.DayIndicatorServiceFutureStub;
import com.shellshellfish.aaas.userinfo.service.DayIndicatorService;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * @Author pierre
 * 17-12-27
 */
@Service
public class DayIndicatorServiceImpl implements DayIndicatorService {


	private DayIndicatorServiceFutureStub dayIndicatorServiceFutureStub;


	@Autowired
	ManagedChannel managedDMChannel;


	@PostConstruct
	public  void init(){
	 	dayIndicatorServiceFutureStub = DayIndicatorServiceGrpc.newFutureStub(managedDMChannel);
	}


	@Override
	public List<DayIndicatorRpc> getDayIndicatorsByCode(DayIndicatorQuery query) throws ExecutionException, InterruptedException {
		return dayIndicatorServiceFutureStub.findByCode(query).get().getDayIndicatorList();

	}
}
