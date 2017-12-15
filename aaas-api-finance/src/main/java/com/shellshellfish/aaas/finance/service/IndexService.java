package com.shellshellfish.aaas.finance.service;

import java.util.Map;
import com.shellshellfish.aaas.finance.model.ChartResource;

public interface IndexService {
	Map<String, Object> homepage(String uid,String productType);
	ChartResource getChart();
	Map<String, Object> getRiskInfo(String risktype);
}