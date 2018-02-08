package com.shellshellfish.aaas.datacollection.server.service;

import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFundCollection;
import com.shellshellfish.aaas.datacollect.MonetaryFundsQueryItem;
import com.shellshellfish.aaas.datacollection.server.model.FundYeildRate;
import io.grpc.stub.StreamObserver;

import java.util.List;

public interface DataCollectionService {

	List<String> CollectItemsSyn(List<String> collectDatas) throws Exception;

	List<String> CollectItemsAsyn(List<String> collectDatas) throws Exception;

	List<FundYeildRate> getLastFundYeildRates(List<String> fundCodes);

	List<FundYeildRate> getLastFundYeildRates4Test(List<String> fundCodes);

	/**
	 * @param monetaryFundsQueryItem
	 * @param responseObserver
	 */

	void getGrowthRateOfMonetaryFundsList(MonetaryFundsQueryItem monetaryFundsQueryItem,
										  StreamObserver<GrowthRateOfMonetaryFundCollection> responseObserver);

	List<FundYeildRate> getLatestFundYeildRates(List<String> fundCodes, Long queryDate);

}
