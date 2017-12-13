package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.service.AdjustmentsService;

@Service("adjustmentsService")
@Transactional
public class AdjustmentsServiceImpl implements AdjustmentsService {

	@Autowired
	AssetAllocationServiceImpl assetAllocationService;

	@Override
	public Map<String, Object> getAdjustments(String groupId) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		List<Map> relateList = new ArrayList();
		Map<String, Object> riskMap = new HashMap<String, Object>();
		riskMap.put("href", "/api/ssf-finance/risks/1");
		riskMap.put("name", "risks");
		relateList.add(riskMap);

		Map<String, Object> investmentHorizonMap = new HashMap<String, Object>();
		investmentHorizonMap.put("href", "/api/ssf-finance/investment-horizons/1");
		investmentHorizonMap.put("name", "investment horizon");
		relateList.add(investmentHorizonMap);
		linksMap.put("related", relateList);

		Map<String, Object> excuteMap = new HashMap<String, Object>();
		excuteMap.put("href", "/api/ssf-finance/product-groups/" + groupId);
		excuteMap.put("describedBy", "/api/ssf-finance/product-groups/" + groupId + ".json");
		excuteMap.put("method", "GET");
		excuteMap.put("name", "adjustment");
		linksMap.put("execute", excuteMap);

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/homepage");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/homepage.json");
		linksMap.put("self", selfMap);

		double historicalYearPerformance = 0;
		double historicalvolatility = 0;
		double confidenceInterval = 0;
		double maximumLosses = 0;
		PerformanceVolatilityReturn performanceVolatilityReturn = assetAllocationService
				.getPerformanceVolatility(groupId, "C1", "1");
		if (performanceVolatilityReturn != null) {
			// .getPerformanceVolatility(cust_risk, investment_horizon);
			List<Map<String, Object>> list = performanceVolatilityReturn.get_items();
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					Integer id = (Integer) map.get("id");
					if (id == 1) {
						historicalYearPerformance = (double) map.get("value");
					} else if (id == 2) {
						historicalvolatility = (double) map.get("value");
					} else if (id == 3) {
						confidenceInterval = (double) map.get("value");
					} else if (id == 4) {
						maximumLosses = (double) map.get("value");
					}
				}
			}
			double investment = 10000;
			result.put("historicalYearPerformance", historicalYearPerformance);
			result.put("historicalVolatility", historicalvolatility);
			result.put("confidenceInterval", confidenceInterval);
			result.put("maximumLosses", maximumLosses);
			result.put("investment", investment);
			result.put("historicalincome", investment * historicalYearPerformance);
		}

		List<List<String>> investmentHorizon = Arrays.asList(
				Arrays.asList("短期", "0-1年"), 
				Arrays.asList("中期", "1-3年"),
				Arrays.asList("长期", "3年以上"));

		List<List<String>> custRiskList = Arrays.asList(
				Arrays.asList("保守型", "C1"), 
				Arrays.asList("稳健型", "C2"),
				Arrays.asList("平衡型", "C3"), 
				Arrays.asList("积极型", "C4"), 
				Arrays.asList("进取型", "C5"));
		result.put("investmentHorizon", investmentHorizon);
		result.put("custRiskList", custRiskList);

		result.put("groupId", groupId);
		result.put("subGroupId", performanceVolatilityReturn.getProductSubGroupId());
		result.put("name", "优化调整");
		result.put("_links", linksMap);

		return result;
	}

	@Override
	public Map<String, Object> getPerformanceVolatility(String groupId, String custRiskId, String investmentHorizonId,int type) {
		Map<String, Object> result = new HashMap<String, Object>();
		PerformanceVolatilityReturn performanceVolatilityReturn = assetAllocationService
				.getPerformanceVolatility(groupId, custRiskId, investmentHorizonId);
		double historicalYearPerformance = 0;
		double historicalvolatility = 0;
		double confidenceInterval = 0;
		double maximumLosses = 0;
		if (performanceVolatilityReturn != null) {
			// .getPerformanceVolatility(cust_risk, investment_horizon);
			List<Map<String, Object>> list = performanceVolatilityReturn.get_items();
			if(list!=null){
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					Integer id = (Integer) map.get("id");
					if (id == 1) {
						historicalYearPerformance = (double) map.get("value");
					} else if (id == 2) {
						historicalvolatility = (double) map.get("value");
					} else if (id == 3) {
						confidenceInterval = (double) map.get("value");
					} else if (id == 4) {
						maximumLosses = (double) map.get("value");
					}
				}
			}
			double investment = 10000;
			result.put("historicalYearPerformance", historicalYearPerformance);
			result.put("historicalVolatility", historicalvolatility);
			result.put("confidenceInterval", confidenceInterval);
			result.put("maximumLosses", maximumLosses);
			result.put("investment", investment);
			result.put("historicalincome", investment * historicalYearPerformance);
		}
		Map<String, Object> linksMap = new HashMap<String, Object>();
		Map<String, Object> selfMap = new HashMap<String, Object>();
		if (type == 1) {
			selfMap.put("href", "/product-groups/" + groupId + "/cust-risks/" + custRiskId + "?investmentHorizonId="
					+ investmentHorizonId);
			selfMap.put("describedBy", "/product-groups/" + groupId + "/cust-risks/" + custRiskId
					+ "?investmentHorizonId=" + investmentHorizonId + ".json");
			result.put("name", "风险承受级别");
		} else if (type == 2) {
			selfMap.put("href", "/product-groups/" + groupId + "/investment-horizons/" + investmentHorizonId
					+ "?custRiskId=" + custRiskId);
			selfMap.put("describedBy", "/product-groups/" + groupId + "/investment-horizons/" + investmentHorizonId
					+ "?custRiskId=" + custRiskId + ".json");
			result.put("name", "大致投资年限");
		}
		linksMap.put("self", selfMap);
		
		result.put("_links", linksMap);
		result.put("groupId", "");
		result.put("description", "XXXXXXXXXXXXXXXXXXXXX");
		result.put("custRiskId", custRiskId);
		result.put("investmentHorizonId", investmentHorizonId);
		
		return result;
	}

}
