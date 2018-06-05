package com.shellshellfish.aaas.userinfo.service;


import com.shellshellfish.aaas.datacollect.GrowthRateOfMonetaryFund;

import java.util.List;


public interface MonetaryFundsService {

	/**
	 *
	 * @param code
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	List<GrowthRateOfMonetaryFund> getGrowthRateOfMonetaryFundsList(String code, Long startDate, Long endDate);
}
