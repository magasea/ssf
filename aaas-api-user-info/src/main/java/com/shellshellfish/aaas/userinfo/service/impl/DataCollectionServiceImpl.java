package com.shellshellfish.aaas.userinfo.service.impl;


import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFundCollection;
import com.shellshellfish.aaas.datacollect.MonetaryFundsQueryItem;
import com.shellshellfish.aaas.userinfo.service.MonetaryFundsService;
import io.grpc.ManagedChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class MonetaryFundsServiceImpl implements MonetaryFundsService {


	@Autowired
	private ManagedChannel managedDCSChannel;

	private DataCollectionServiceGrpc.DataCollectionServiceBlockingStub dataCollectionServiceFutureStub;


	@PostConstruct
	void init() {
		dataCollectionServiceFutureStub = DataCollectionServiceGrpc.newBlockingStub(managedDCSChannel);
	}

	@Override
	public List<GrowthRateOfMonetaryFund> getGrowthRateOfMonetaryFundsList(String code, Long startDate, Long endDate) {
		MonetaryFundsQueryItem.Builder monetaryFundsQueryItemBuilder = MonetaryFundsQueryItem.newBuilder();

		monetaryFundsQueryItemBuilder.setCode(code);
		if (startDate == null || startDate <= 0)
			startDate = System.currentTimeMillis();

		if (endDate == null || endDate <= 0)
			endDate = System.currentTimeMillis();


		monetaryFundsQueryItemBuilder.setStartDate(startDate);
		monetaryFundsQueryItemBuilder.setEndDate(endDate);

		GrowthRateOfMonetaryFundCollection collection = dataCollectionServiceFutureStub.getGrowthRateOfMonetaryFundsList(monetaryFundsQueryItemBuilder.build());

		return collection.getGrowthRateOfMonetaryFundsList();
	}
}
