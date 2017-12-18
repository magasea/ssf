package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.shellshellfish.aaas.finance.model.ChartResource;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.ReturnType;
import com.shellshellfish.aaas.finance.service.IndexService;
import com.shellshellfish.aaas.finance.util.FishLinks;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	@Autowired
	AssetAllocationServiceImpl assetAllocationService;

	@Override
	public Map<String, Object> homepage(String uid,String productType) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		List<Map> relateList = new ArrayList();
		Map<String, Object> linkMap = new HashMap<String, Object>();
		linkMap.put("href", "/api/ssf-finance/retests");
		linkMap.put("name", "retest");
		linkMap.put("description", "重新测试");
		relateList.add(linkMap);

		Map<String, Object> riskMap = new HashMap<String, Object>();
		riskMap.put("href", "/api/ssf-finance/prerisktypes/{risktype}");
		riskMap.put("name", "prerisktypes");
		linkMap.put("description", "link:\"<\"");
		relateList.add(riskMap);

		riskMap = new HashMap<String, Object>();
		riskMap.put("href", "/api/ssf-finance/nextrisktypes/{risktype}");
		riskMap.put("name", "nextrisktypes");
		linkMap.put("description", "link:\">\"");
		relateList.add(riskMap);

//		Map<String, Object> chartMap = new HashMap<String, Object>();
//		chartMap.put("href", "/api/ssf-finance/product-groups/charts/1");
//		chartMap.put("name", "charts");
//		relateList.add(chartMap);
//		linksMap.put("related", relateList);

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/homepage");
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/homepage.json");
		linksMap.put("self", selfMap);

		double historicalYearPerformance = 0;
		double historicalvolatility = 0;
		PerformanceVolatilityReturn performanceVolatilityReturn = assetAllocationService.getPerformanceVolatility(uid,
				productType, "1");
		if (performanceVolatilityReturn != null) {
			// .getPerformanceVolatility(cust_risk, investment_horizon);
			List<Map<String, Object>> list = performanceVolatilityReturn.get_items();
			if (list != null) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> map = list.get(i);
					Integer id = (Integer) map.get("id");
					if (id == 1) {
						historicalYearPerformance = (double) map.get("value");
					} else if (id == 2) {
						historicalvolatility = (double) map.get("value");
					}
				}
			}
		}
		result.put("historicalYearPerformance", historicalYearPerformance);
		result.put("historicalvolatility", historicalvolatility);
		String groupId = performanceVolatilityReturn.getProductGroupId();
		String subGroupId = performanceVolatilityReturn.getProductSubGroupId();
		result.put("groupId", groupId);
		result.put("subGroupId", subGroupId);

		FundReturn fundReturn = assetAllocationService.selectById(groupId, subGroupId);
		if(fundReturn!=null){
			List<Map<String, Double>> assetsRatiosList = fundReturn.getAssetsRatios();
			result.put("product_list", assetsRatiosList);
		}
		//近6个月收益图
		ReturnType returnType = assetAllocationService.getPortfolioYield(groupId, subGroupId, new Integer(-6), "income");
		result.put("income6month", returnType);
		
		List<String> banner_list = new ArrayList<>();
		banner_list.add("/phoneapi-ssf/finance-home/image/page-1.jpg");
		banner_list.add("/phoneapi-ssf/finance-home/image/page-2.jpg");
		banner_list.add("/phoneapi-ssf/finance-home/image/page-3.jpg");
		banner_list.add("/phoneapi-ssf/finance-home/image/page-4.jpg");
		result.put("banner_list", banner_list);
		
		result.put("name", "理财产品 首页");
		result.put("_links", linksMap);

		return result;
	}

	@Override
	public ChartResource getChart() {
	        ChartResource chartResource = new ChartResource();
	        chartResource.setName("最大回撤走势图");
	        chartResource.setLineValues(Arrays.asList(
	                Arrays.asList(Arrays.asList("2014-10-13", 0.1)
	        )));

		FishLinks links = new FishLinks();
		links.setSelf("/api/ssf-finance/product-groups/homepage/charts/1");
		links.setDescribedBy("/schema/api/ssf-finance/product-groups/homepage/charts/item.json");
		chartResource.setLinks(links);
		return chartResource;
	}

	@Override
	public Map<String, Object> getRiskInfo(String risktype) {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		PerformanceVolatilityReturn performanceVolatilityReturn = assetAllocationService.getPerformanceVolatility("1",
				risktype, null);
		String groupId = performanceVolatilityReturn.getProductGroupId();
		String subGroupId = performanceVolatilityReturn.getProductSubGroupId();
		FundReturn fundReturn = assetAllocationService.selectById(groupId, subGroupId);
		if (fundReturn == null) {
			result.put("error", "404 DATA NOT FOUND.");
			return result;
		}
		List<Map<String, Double>> assetsRatiosList = fundReturn.getAssetsRatios();
		result.put("assetsRatios", assetsRatiosList);

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/risktypes/" + risktype);
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/risktypes.json");
		linksMap.put("self", selfMap);

		result.put("_links", linksMap);
		return result;
	}

}
