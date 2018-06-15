package com.shellshellfish.aaas.userinfo.service.impl;


import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.common.utils.MyBeanUtils;
import com.shellshellfish.aaas.datacollect.DailyFundsQuery;
import com.shellshellfish.aaas.datacollect.DataCollectionServiceGrpc;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFundCollection;
import com.shellshellfish.aaas.datacollect.MonetaryFundsQueryItem;
import com.shellshellfish.aaas.userinfo.service.DataCollectionService;
import io.grpc.ManagedChannel;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;


@Service
public class DataCollectionServiceImpl implements DataCollectionService {


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

	@Override
	public List<DCDailyFunds> getFundDataOfDay(List<String> codes, String startOfDay,
			String endOfDay) {
		DailyFundsQuery.Builder dfqBuilder = DailyFundsQuery.newBuilder();
		codes.forEach(
				item->dfqBuilder.addCodes(item)
		);
		dfqBuilder.setNavLatestDateStart(startOfDay);
		dfqBuilder.setNavLatestDateEnd(endOfDay);
		List<com.shellshellfish.aaas.datacollect.DailyFunds> dailyFunds =
				dataCollectionServiceFutureStub.getFundDataOfDay(dfqBuilder.build()).getDailyFundsList();
		List<DCDailyFunds> dcDailyFunds = new ArrayList<>();
		dailyFunds.forEach(
				originItem->{
					DCDailyFunds dcDailyFunds1 = new DCDailyFunds();
					MyBeanUtils.mapEntityIntoDTO(originItem, dcDailyFunds1);
					dcDailyFunds.add(dcDailyFunds1);
				}
		);
		return dcDailyFunds;
	}
}
