package com.shellshellfish.aaas.assetallocation.neo.web;

import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.secvice.FundGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/27.
 */
@RestController
public class FundGroupController {

    @Autowired
    FundGroupService fundGroupService;

    /**
     * 查询所有基金组合
     * @return
     */
    @ApiOperation("返回所有基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundAllReturn selectAllFundGroup(){
        FundAllReturn far = fundGroupService.selectAllFundGroup();
        return far;
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    @ApiOperation("返回单个基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn selectById(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        FundReturn fr= fundGroupService.selectById(id,subGroupId);
        return fr;
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param returntype(1:预期年化收益       2:预期最大回撤)
     * @param subGroupId
     *
     *
     * @return
     */
    @ApiOperation("预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> selectReturnAndPullback(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId,@RequestParam(defaultValue="1") String returntype){
        Map<String,Object> map = new HashMap<String,Object>();
        map= fundGroupService.selectReturnAndPullback(id,returntype,subGroupId);
        return new ResponseEntity<Map<String,Object>>(map, HttpStatus.OK);
    }

    /**
     * 配置收益贡献
     * @return
     */
    @ApiOperation("配置收益贡献")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRevenueContribution(@PathVariable("groupId") String uuid, @PathVariable("subGroupId") String subGroupId){
        RevenueContributionReturn rcb = fundGroupService.getRevenueContribution(uuid,subGroupId);
        return rcb;
    }

    /**
     * 有效前沿线
     * @return
     */
    @ApiOperation("有效前沿线")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn efficientFrontier(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId){
        RevenueContributionReturn aReturn = fundGroupService.efficientFrontier(id,subGroupId);
        return aReturn;
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     * @param id
     * @param riskValue
     * @param returnValue
     * @return
     */
    @ApiOperation("预期收益率调整 风险率调整  最优组合(有效前沿线)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/optimizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn getinterval(@PathVariable("groupId") String id, @RequestParam(defaultValue="0.13") String riskValue,@RequestParam(defaultValue="0.15") String returnValue){
        FundReturn fr = fundGroupService.getinterval(id,riskValue,returnValue);
        return fr;
    }

    /**
     * 风险控制
     * @param id
     * @return
     */
    @ApiOperation("风险控制")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/risk-controls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getRiskController(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        RevenueContributionReturn rct = fundGroupService.getRiskController(id,subGroupId);
        return rct;
    }

    /**
     * 风险控制手段与通知
     * @param uuid
     * @return
     */
    @ApiOperation("风险控制手段与通知")
    @RequestMapping(value = "/api/asset-allocation/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getmeansAndNoticesRetrun(@PathVariable("uuid") String uuid){
        RevenueContributionReturn man = fundGroupService.getmeansAndNoticesRetrun();
        return man;
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param uuid
     * @param riskLevel
     * @param investmentPeriod
     * @return
     */
    @ApiOperation("模拟历史年化业绩与模拟历史年化波动率")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getPerformanceVolatility(@PathVariable("groupId") String uuid,@RequestParam(defaultValue="1") String riskLevel,@RequestParam(defaultValue="1") String investmentPeriod) {
        PerformanceVolatilityReturn riskIncomeIntervals= fundGroupService.getPerformanceVolatility(uuid,riskLevel,investmentPeriod);
        return riskIncomeIntervals;
    }

    /**
     * 分段数据
     * @param id
     * @param slidebarType（risk风险率     income收益率）
     * @return
     */
    @ApiOperation("滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public RevenueContributionReturn getScaleMark(@PathVariable("groupId") String id, String slidebarType){
        RevenueContributionReturn smk = fundGroupService.getScaleMark(id,slidebarType);
        return smk;
    }

}
