package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;

import java.util.List;


public interface DataCollectionService {

	/**
	 *
	 * @param code
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<GrowthRateOfMonetaryFund> getGrowthRateOfMonetaryFundsList(String code, Long startDate, Long endDate);

	/**
	 *
	 * @param codes
	 * @param startOfDay
	 * @param endOfDay
	 * @return
	 */
	public List<DCDailyFunds> getFundDataOfDay(List<String> codes, String startOfDay, String
			endOfDay);
}
