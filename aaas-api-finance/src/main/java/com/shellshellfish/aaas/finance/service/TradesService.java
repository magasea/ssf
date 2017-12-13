package com.shellshellfish.aaas.finance.service;

import java.util.Map;

import com.shellshellfish.aaas.finance.model.dto.TradesBodyDTO;

public interface TradesService {
	Map<String, Object> getTrades(String groupId, String subGroupId, TradesBodyDTO tradesBodyDTO);
}