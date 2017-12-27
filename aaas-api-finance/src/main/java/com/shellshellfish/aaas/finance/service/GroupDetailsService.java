package com.shellshellfish.aaas.finance.service;

import com.shellshellfish.aaas.finance.model.dto.HistoryList;

import java.util.List;
import java.util.Map;

/**
 * @Author pierre
 * 17-12-22
 */
public interface GroupDetailsService {


	List<HistoryList> getHistoryList(String methodUrl,Map params);

	String connectDataManager(String methodUrl, Map params);


}