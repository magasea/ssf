package com.shellshellfish.aaas.finance.service.impl;

import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.finance.model.HistoryPerformance;
import com.shellshellfish.aaas.finance.service.HistoryPerformanceService;

@Service
public class HistoryPerformanceServiceImpl implements HistoryPerformanceService {
	@Override
	public HistoryPerformance getHistoryPerformance() {
		HistoryPerformance historyPerformance = new HistoryPerformance();
		return historyPerformance;
	}
}
