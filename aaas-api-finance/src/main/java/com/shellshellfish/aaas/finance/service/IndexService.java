package com.shellshellfish.aaas.finance.service;

import java.util.Map;
import com.shellshellfish.aaas.finance.model.ChartResource;

public interface IndexService {
	ChartResource getChart();
	Map<String, Object> getRiskInfo(String risktype);
	Map<String, Object> homepage(String uuid, String isTestFlag, String testResult) throws Exception;
}