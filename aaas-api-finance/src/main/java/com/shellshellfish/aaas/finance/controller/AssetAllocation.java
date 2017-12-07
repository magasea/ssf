package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.returnType.*;
import com.shellshellfish.aaas.finance.service.AssetAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@RestController
public class AssetAllocation {

    @Value("${shellshellfish.asset-allocation-url}")
    private String url;


    @Autowired
    RestTemplate template;

    @Autowired
    AssetAllocationService assetAllocationService;

    /**
     * 查询所有基金组合
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundAllReturn getss(){
        return assetAllocationService.getProduct();
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn selectById(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        return assetAllocationService.selectById(id,subGroupId);
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param returntype
     * @param subGroupId 风险率
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String,Object> selectReturnAndPullback(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId, String returntype){
        return assetAllocationService.selectReturnAndPullback(id,subGroupId,returntype);
    }

    /**
     * 配置收益贡献
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRevenueContribution(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        return assetAllocationService.getRevenueContribution(id,subGroupId);
    }

    /**
     * 有效前沿线
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn efficientFrontier(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subId){
        return assetAllocationService.efficientFrontier(id,subId);
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     * @param id
     * @param riskValue
     * @param returnValue
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/optimizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn getinterval(@PathVariable("groupId") String id, String riskValue,String returnValue){
        return assetAllocationService.getinterval(id,riskValue,returnValue);
    }

    /**
     * 风险控制
     * @param id
     * @param subGroupId
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/risk-controls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRiskController(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        return assetAllocationService.getRiskController(id,subGroupId);
    }

    /**
     * 风险控制手段与通知
     * @param
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getmeansAndNoticesRetrun(@PathVariable("uuid") String id){
        return assetAllocationService.getmeansAndNoticesRetrun(id);
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param uuid
     * @param cust_risk
     * @param investment_horizon
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getPerformanceVolatility(@PathVariable("groupId") String uuid, String cust_risk, String investment_horizon) {
        return assetAllocationService.getPerformanceVolatility(uuid,cust_risk,investment_horizon);
    }

    /**
     * 滑动条分段数据
     * @param id
     * @param slidebarType
     * @return
     */
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getScaleMark(@PathVariable("groupId") String id, String slidebarType){
        return assetAllocationService.getScaleMark(id,slidebarType);
    }
}
