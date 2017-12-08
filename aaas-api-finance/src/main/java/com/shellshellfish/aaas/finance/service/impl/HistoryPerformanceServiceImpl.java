package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.finance.model.HistoryPerformance;
import com.shellshellfish.aaas.finance.service.HistoryPerformanceService;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
public class HistoryPerformanceServiceImpl implements HistoryPerformanceService {
	public HistoryPerformance getHistoryPerformance() {
		HistoryPerformance historyPerformance = new HistoryPerformance();
		return historyPerformance;
	}
}
