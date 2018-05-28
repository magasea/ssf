package com.shellshellfish.aaas.finance.service.impl;

import com.shellshellfish.aaas.asset.allocation.FundGroupIndexResult;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.finance.model.ChartResource;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.ReturnType;
import com.shellshellfish.aaas.finance.service.IndexService;
import com.shellshellfish.aaas.finance.util.FishLinks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.*;

@Service("indexService")
@Transactional
public class IndexServiceImpl implements IndexService {

    private static final Logger logger = LoggerFactory.getLogger(IndexServiceImpl.class);

    private static final String GROUP_ID = "30";
    private static final String SUB_GROUP_ID = "30000";
    private static final String CUST_RISK = "C5";
    private static final String INVESTMENT_HORIZON = "3";

    @Autowired
    AssetAllocationServiceImpl assetAllocationService;

    @Autowired
    DataManagerService dataManagerService;

    @Autowired
    AllocationRpcService allocationRpcService;

    private final String CONSERV = "保守型";
    private final String STABLE = "稳健型";
    private final String BALANCE = "平衡型";
    private final String INPROVING = "成长型";
    private final String AGGRESSIVE = "进取型";

    @Override
    public Map<String, Object> homepage(String uuid, String isTestFlag, String testResult, Integer oemid) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> resultC = new HashMap<String, Object>();
        Map<String, Object> linksMap = new HashMap<String, Object>();
        List<Map<String, Object>> relateList = new ArrayList<Map<String, Object>>();
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

        Map<String, Object> investmentHorizonMap = new HashMap<String, Object>();
        List<Map<String, Object>> riskList = new ArrayList<Map<String, Object>>();
        // 未进行风险评测时
        if (StringUtils.isEmpty(isTestFlag) || "0".equals(isTestFlag)) {
            logger.info("未进行风险评测时，显示组合为：15_150048.");
            investmentHorizonMap = new HashMap<String, Object>();
            investmentHorizonMap.put("id", 5);
            investmentHorizonMap.put("investmentHorizon", AGGRESSIVE);
            investmentHorizonMap.put("investmentHorizonCode", CUST_RISK);
            String groupId = GROUP_ID;
            String subGroupId = SUB_GROUP_ID;
            //--------------------------------------
            FundReturn fundReturn = assetAllocationService.selectById(groupId, subGroupId, oemid);
            if (fundReturn == null) {
                logger.error("产品不存在.");
                throw new Exception("产品不存在.");
            }
            resultC.put("name", fundReturn.getName());
            //--------------------------------------

            //历史年化收益率和历史年化波动率
            FundGroupIndexResult fundGroupIndex = allocationRpcService.getAnnualVolatilityAndAnnualYield(groupId,
                    subGroupId, oemid);
            resultC.put("historicalYearPerformance", fundGroupIndex.getHistoricalAnnualYeild());
            resultC.put("historicalvolatility", fundGroupIndex.getHistoricalAnnualVolatility());
            resultC.put("groupId", groupId);
            resultC.put("subGroupId", subGroupId);

            ReturnType proportionOne = assetAllocationService.getProportionOne(groupId, subGroupId, oemid);
            if (proportionOne != null) {
                List<Map<String, Object>> proportionOneList = proportionOne.get_items();
                resultC.put("product_list", proportionOneList);
            }
            //近6个月收益图
            ReturnType returnType = assetAllocationService.getPortfolioYield(groupId, subGroupId, 0, "income", oemid);
            resultC.put("income6month", returnType);

            Map first = returnType.get_items().get(0);
            LocalDate date = InstantDateUtil.format((String) first.get("time"));


            Map baseLine = dataManagerService.getBaseLine(Long.parseLong(groupId), 5, date);
            resultC.put("baseLine", baseLine);
            align(returnType, baseLine, resultC);
            result.put("C5", resultC);

            riskList.add(investmentHorizonMap);
            result.put("productTypeList", riskList);
        } else {
            logger.info("进行风险评测时，显示其组合.");
            ReturnType resultType = assetAllocationService.getPerformanceVolatilityHomePage(oemid);
            List<Map<String, Object>> items = resultType.get_items();
            Map<String, Object> itemMap = new HashMap<String, Object>();
            List<Map<String, Object>> investmentHorizonMap2 = new ArrayList<Map<String, Object>>();
            Map<String, Object> obj = null;
            if (items != null && items.size() > 0) {
                for (int i = 0; i < items.size(); i++) {
                    itemMap = items.get(i);
                    for (Object key : itemMap.keySet()) {
                        investmentHorizonMap = new HashMap<String, Object>();
                        if ("C1".equals(key)) {
                            investmentHorizonMap.put("id", 1);
                            investmentHorizonMap.put("investmentHorizon", CONSERV);
                            investmentHorizonMap.put("investmentHorizonCode", "C1");
                            if ("1".equals(isTestFlag) && !CONSERV.equals(testResult)) {
                                continue;
                            }
                        } else if ("C2".equals(key)) {
                            investmentHorizonMap.put("id", 2);
                            investmentHorizonMap.put("investmentHorizon", STABLE);
                            investmentHorizonMap.put("investmentHorizonCode", "C2");
                            if ("1".equals(isTestFlag) && !STABLE.equals(testResult)) {
                                continue;
                            }
                        } else if ("C3".equals(key)) {
                            investmentHorizonMap.put("id", 3);
                            investmentHorizonMap.put("investmentHorizon", BALANCE);
                            investmentHorizonMap.put("investmentHorizonCode", "C3");
                            if ("1".equals(isTestFlag) && !BALANCE.equals(testResult)) {
                                continue;
                            }
                        } else if ("C4".equals(key)) {
                            investmentHorizonMap.put("id", 4);
                            investmentHorizonMap.put("investmentHorizon", INPROVING);
                            investmentHorizonMap.put("investmentHorizonCode", "C4");
                            if ("1".equals(isTestFlag) && !INPROVING.equals(testResult)) {
                                continue;
                            }
                        } else if ("C5".equals(key)) {
                            investmentHorizonMap.put("id", 5);
                            investmentHorizonMap.put("investmentHorizon", AGGRESSIVE);
                            investmentHorizonMap.put("investmentHorizonCode", "C5");
                            if ("1".equals(isTestFlag) && !AGGRESSIVE.equals(testResult)) {
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
                        FundReturn fundReturn = assetAllocationService.selectById(groupId, subGroupId, oemid);
                        if (fundReturn == null) {
                            logger.error("产品不存在.");
                            throw new Exception("产品不存在.");
                        }
                        resultC.put("name", fundReturn.getName());
                        //--------------------------------------
                        Map<String, Object> itemMap2 = new HashMap<String, Object>();
                        double historicalYearPerformance = 0;
                        double historicalvolatility = 0;
                        if (investmentHorizonMap2 != null && investmentHorizonMap2.size() > 0) {
                            for (int j = 0; j < investmentHorizonMap2.size(); j++) {
                                itemMap2 = investmentHorizonMap2.get(j);
                                int id = (int) itemMap2.get("id");
                                if (id == 1) {
                                    historicalYearPerformance = (double) itemMap2.get("value");
                                } else if (id == 2) {
                                    historicalvolatility = (double) itemMap2.get("value");
                                }
                            }
                        }
                        resultC.put("historicalYearPerformance", historicalYearPerformance);
                        resultC.put("historicalvolatility", historicalvolatility);
                        resultC.put("groupId", groupId);
                        resultC.put("subGroupId", subGroupId);

                        ReturnType proportionOne = assetAllocationService.getProportionOne(groupId, subGroupId, oemid);
                        if (proportionOne != null) {
                            List<Map<String, Object>> proportionOneList = proportionOne.get_items();
                            resultC.put("product_list", proportionOneList);
                        }
                        //近6个月收益图
                        ReturnType returnType = assetAllocationService.getPortfolioYield(groupId, subGroupId, 0, "income", oemid);
                        resultC.put("income6month", returnType);

                        Map first = returnType.get_items().get(0);
                        LocalDate date = InstantDateUtil.format((String) first.get("time"));

                        Map baseLine = dataManagerService.getBaseLine(Long.parseLong(groupId), 5, date);
                        resultC.put("baseLine", baseLine);
                        result.put(key + "", resultC);
                        align(returnType, baseLine, resultC);
                    }

                    Collections.sort(riskList, (o1, o2) -> {
                        int map1value = (Integer) o1.get("id");
                        int map2value = (Integer) o2.get("id");
                        return map1value - map2value;
                    });


                    result.put("productTypeList", riskList);
                }
            }
        }

        List<String> banner_list = new ArrayList<>();
        banner_list.add("http://47.96.164.161/1.png");
        banner_list.add("http://47.96.164.161/2.png");
        banner_list.add("http://47.96.164.161/3.png");
        banner_list.add("http://47.96.164.161/4.png");
        result.put("banner_list", banner_list);

        result.put("name", "理财产品 首页");
        result.put("title1", "组合");
        result.put("title2", "比较基准");
        result.put("_links", linksMap);
        result.put("uuid", uuid);

        return result;
    }


    private void align(ReturnType returnType, Map target, Map result) {

        List<Map<String, Object>> srcList = returnType.get_items();
        List<Map<String, Object>> targetList = (List<Map<String, Object>>) target.get("value");
        Set<String> set = new TreeSet<>();

        for (Map<String, Object> map : srcList) {
            set.add((String) map.get("time"));
        }


        Iterator<Map<String, Object>> it = targetList.iterator();
        while (it.hasNext()) {
            String date = Optional.of(it.next()).map(m -> m.get("date")).orElse("").toString();
            if (!set.contains(date))
                it.remove();
        }

        target.put("value", targetList);
        result.put("baseLine", target);

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
    public Map<String, Object> getRiskInfo(String risktype, Integer oemid) {
        Map<String, Object> result = new HashMap<String, Object>();
        Map<String, Object> linksMap = new HashMap<String, Object>();
        PerformanceVolatilityReturn performanceVolatilityReturn = assetAllocationService.getPerformanceVolatility("1",
                risktype, null);
        String groupId = performanceVolatilityReturn.getProductGroupId();
        String subGroupId = performanceVolatilityReturn.getProductSubGroupId();
        FundReturn fundReturn = assetAllocationService.selectById(groupId, subGroupId, oemid);
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
