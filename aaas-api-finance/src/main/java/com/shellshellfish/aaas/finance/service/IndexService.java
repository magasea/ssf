package com.shellshellfish.aaas.finance.service;

import java.util.Map;
import com.shellshellfish.aaas.finance.model.ChartResource;

public interface IndexService {
	Map<String, Object> homepage(String uuid) throws Exception ;
	ChartResource getChart();
	Map<String, Object> getRiskInfo(String risktype);
}