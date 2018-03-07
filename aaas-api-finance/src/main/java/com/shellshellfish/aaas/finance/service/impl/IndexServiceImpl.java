package com.shellshellfish.aaas.finance.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.shellshellfish.aaas.finance.model.ChartResource;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.ReturnType;
import com.shellshellfish.aaas.finance.service.IndexService;
import com.shellshellfish.aaas.finance.util.FishLinks;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

	private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);
	
	@Autowired
	AssetAllocationServiceImpl assetAllocationService;
	
	private final String CONSERV = "保守型";
	private final String STABLE = "稳健型";
	private final String BALANCE = "平衡型";
	private final String INPROVING = "成长型";
	private final String AGGRESSIVE = "进取型";

	@Override
	public Map<String, Object> homepage(String uuid, String isTestFlag, String testResult) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		Map<String, Object> resultC = new HashMap<String, Object>();
		Map<String, Object> linksMap = new HashMap<String, Object>();
		List<Map<String,Object>> relateList = new ArrayList<Map<String,Object>>();
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
		selfMap.put("describedBy", "/api/ssf-finance/r-groups/homepage.json");
		linksMap.put("self", selfMap);
		
		ReturnType resultType = assetAllocationService.getPerformanceVolatilityHomePage();
		List<Map<String, Object>> items = resultType.get_items();
		Map<String, Object> itemMap = new HashMap<String, Object>();
		Map<String, Object> investmentHorizonMap = new HashMap<String, Object>();
		List<Map<String, Object>> riskList = new ArrayList<Map<String, Object>>();
		List<Map<String,Object>> investmentHorizonMap2 = new ArrayList<Map<String,Object>>();
		Map<String,Object> obj = null;
		if (items != null && items.size() > 0) {
			for (int i = 0; i < items.size(); i++) {
				itemMap = items.get(i);
				for (Object key : itemMap.keySet()) {
					investmentHorizonMap = new HashMap<String, Object>();
					if ("C1".equals(key)) {
						investmentHorizonMap.put("id", 1);
						investmentHorizonMap.put("investmentHorizon", CONSERV);
						investmentHorizonMap.put("investmentHorizonCode", "C1");
						if("1".equals(isTestFlag)&&!CONSERV.equals(testResult)){
							continue;
						}
					} else if ("C2".equals(key)) {
						investmentHorizonMap.put("id", 2);
						investmentHorizonMap.put("investmentHorizon", STABLE);
						investmentHorizonMap.put("investmentHorizonCode", "C2");
						if("1".equals(isTestFlag)&&!STABLE.equals(testResult)){
							continue;
						}
					} else if ("C3".equals(key)) {
						investmentHorizonMap.put("id", 3);
						investmentHorizonMap.put("investmentHorizon", BALANCE);
						investmentHorizonMap.put("investmentHorizonCode", "C3");
						if("1".equals(isTestFlag)&&!BALANCE.equals(testResult)){
							continue;
						}
					} else if ("C4".equals(key)) {
						investmentHorizonMap.put("id", 4);
						investmentHorizonMap.put("investmentHorizon", INPROVING);
						investmentHorizonMap.put("investmentHorizonCode", "C4");
						if("1".equals(isTestFlag)&&!INPROVING.equals(testResult)){
							continue;
						}
					} else if ("C5".equals(key)) {
						investmentHorizonMap.put("id", 5);
						investmentHorizonMap.put("investmentHorizon", AGGRESSIVE);
						investmentHorizonMap.put("investmentHorizonCode", "C5");
						if("1".equals(isTestFlag)&&!AGGRESSIVE.equals(testResult)){
							continue;
						}
					}
					resultC = new HashMap<String, Object>();
					obj = (Map<String, Object>) itemMap.get(key);
					riskList.add(investmentHorizonMap);
					investmentHorizonMap2 = (List<Map<String, Object>>) obj.get("_items");
					String groupId = (String) obj.get("productGroupId");
					String subGroupId = (String) obj.get("productSubGroupId");
					
					//--------------------------------------
					FundReturn fundReturn = assetAllocationService.selectById(groupId,subGroupId);
					if(fundReturn==null){
						logger.error("产品不存在.");
						throw new Exception("产品不存在.");
					}
					resultC.put("name", fundReturn.getName());
					//--------------------------------------
					Map<String, Object> itemMap2 = new HashMap<String, Object>();
					double historicalYearPerformance = 0;
					double historicalvolatility = 0;
					if(investmentHorizonMap2!=null&&investmentHorizonMap2.size()>0){
						for(int j=0;j<investmentHorizonMap2.size();j++){
							itemMap2 = investmentHorizonMap2.get(j);
							int id = (int) itemMap2.get("id");
							if(id == 1){
								historicalYearPerformance = (double) itemMap2.get("value");
							} else if(id == 2){
								historicalvolatility = (double) itemMap2.get("value");
							}
						}
					}
					resultC.put("historicalYearPerformance", historicalYearPerformance);
					resultC.put("historicalvolatility", historicalvolatility);
					resultC.put("groupId", groupId);
					resultC.put("subGroupId", subGroupId);

					ReturnType proportionOne = assetAllocationService.getProportionOne(groupId, subGroupId);
					if(proportionOne!=null){
						List<Map<String, Object>> proportionOneList = proportionOne.get_items();
						resultC.put("product_list", proportionOneList);
					}
					//近6个月收益图
					ReturnType returnType = assetAllocationService.getPortfolioYield(groupId, subGroupId, new Integer(-6), "income");
					resultC.put("income6month", returnType);
					
					result.put(key+"", resultC);
				}
				
				Collections.sort(riskList, new Comparator<Map<String, Object>>() {
		            public int compare(Map<String, Object> o1, Map<String, Object> o2) {
		                int map1value = (Integer) o1.get("id");
		                int map2value = (Integer) o2.get("id");
		                return map1value - map2value;
		            }
		        });
				
				
				result.put("productTypeList", riskList);
			}
		} else {
			logger.error("产品类型不存在.");
			throw new Exception("产品类型不存在.");
		}
		
		List<String> banner_list = new ArrayList<>();
		banner_list.add("http://47.96.164.161/1.png");
		banner_list.add("http://47.96.164.161/2.png");
		banner_list.add("http://47.96.164.161/3.png");
		banner_list.add("http://47.96.164.161/4.png");
		result.put("banner_list", banner_list);
		
		result.put("name", "理财产品 首页");
		result.put("_links", linksMap);
		result.put("uuid", uuid);

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
		List<Map<String, Object>> assetsRatiosList = fundReturn.getAssetsRatios();
		result.put("assetsRatios", assetsRatiosList);

		Map<String, Object> selfMap = new HashMap<String, Object>();
		selfMap.put("href", "/api/ssf-finance/product-groups/risktypes/" + risktype);
		selfMap.put("describedBy", "/api/ssf-finance/product-groups/risktypes.json");
		linksMap.put("self", selfMap);

		result.put("_links", linksMap);
		return result;
	}
}
