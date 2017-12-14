package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.finance.model.dto.TradesBodyDTO;
import com.shellshellfish.aaas.finance.service.TradesService;

@Service
public class TradesServiceImpl implements TradesService {

	@Override
	public Map<String, Object> getTrades(String groupId, String subGroupId, TradesBodyDTO tradesBodyDTO) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		result.put("bankCard", tradesBodyDTO.getBankCardNum());
		result.put("bankName", tradesBodyDTO.getBankName());
		result.put("purchaseFee", "");
		result.put("estimatedFee", tradesBodyDTO.getOriginalFee().subtract(tradesBodyDTO.getCostsaving()));
		result.put("originalFee", tradesBodyDTO.getOriginalFee());
		result.put("costsaving", tradesBodyDTO.getCostsaving());
		result.put("fund", tradesBodyDTO.getFundMap());
		
		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/trades");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/trades.json");
		linksMap.put("self", selfMap);
		
		List<Map<String, Object>> relateList = new ArrayList<Map<String, Object>>();
		Map<String, Object> bankMap = new HashMap<String, Object>();
		bankMap.put("href", "/api/ssf-finance/banks");
		bankMap.put("name", "banks");
		relateList.add(bankMap);
		
		Map<String, Object> fundMap = new HashMap<String, Object>();
		fundMap.put("href", "/api/ssf-finance/funds");
		fundMap.put("name", "funds");
		relateList.add(fundMap);
		linksMap.put("related", relateList);
		
		result.put("_links", linksMap);
		
		result.put("groupId", groupId);
		result.put("subGroupId", subGroupId);
		return result;
	}
	
}
