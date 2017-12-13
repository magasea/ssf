package com.shellshellfish.aaas.finance.service;

import java.util.Map;

public interface AdjustmentsService {
	Map<String, Object> getAdjustments(String groupId);
	Map<String, Object> getPerformanceVolatility(String groupId, String custRiskId, String investmentHorizonId,int type);
}