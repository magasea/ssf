package com.shellshellfish.aaas.finance.service;

import com.shellshellfish.aaas.finance.model.dto.FundCompany;
import com.shellshellfish.aaas.finance.model.dto.FundManager;
import com.shellshellfish.aaas.finance.model.dto.HistoryList;

import java.util.List;
import java.util.Map;

/**
 * @Author pierre
 * 17-12-22
 */
public interface GroupDetailsService {


	List<HistoryList> getHistoryList(String methodUrl, Map params);

	FundManager getFundManager(String methodUrl, Map params);

	FundCompany getFundCompany(String methodUrl, Map params);

	String connectDataManager(String methodUrl, Map params);


}
