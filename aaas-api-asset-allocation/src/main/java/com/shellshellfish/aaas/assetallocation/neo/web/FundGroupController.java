package com.shellshellfish.aaas.assetallocation.neo.web;

import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.secvice.FundGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
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
    public ReturnType getRevenueContribution(@PathVariable("groupId") String uuid, @PathVariable("subGroupId") String subGroupId){
        ReturnType rcb = fundGroupService.getRevenueContribution(uuid,subGroupId);
        return rcb;
    }

    /**
     * 有效前沿线
     * @return
     */
    @ApiOperation("有效前沿线")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType efficientFrontier(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        ReturnType aReturn = fundGroupService.efficientFrontier(id,subGroupId);
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
    public ReturnType getRiskController(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        ReturnType rct = fundGroupService.getRiskController(id,subGroupId);
        return rct;
    }

    /**
     * 风险控制手段与通知
     * @param uuid
     * @return
     */
    @ApiOperation("风险控制手段与通知")
    @RequestMapping(value = "/api/asset-allocation/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getmeansAndNoticesRetrun(@PathVariable("uuid") String uuid){
        ReturnType man = fundGroupService.getmeansAndNoticesRetrun();
        return man;
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param riskLevel
     * @param investmentPeriod
     * @return
     */
    @ApiOperation("模拟历史年化业绩与模拟历史年化波动率")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getPerformanceVolatility(@RequestParam(defaultValue="1") String riskLevel,@RequestParam(defaultValue="1") String investmentPeriod) {
        PerformanceVolatilityReturn riskIncomeIntervals= fundGroupService.getPerformanceVolatility(riskLevel,investmentPeriod);
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
    public ReturnType getScaleMark(@PathVariable("groupId") String id, String slidebarType){
        ReturnType smk = fundGroupService.getScaleMark(id,slidebarType);
        return smk;
    }

    /**
     * 组合收益率走势图
     *
     * @param id
     * @param subGroupId
     * @param mouth      几个月以来每天
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率走势图-每天")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncome(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId,@RequestParam(defaultValue="-1") int mouth) throws ParseException {
        ReturnType smk = fundGroupService.getFundGroupIncome(id,subGroupId,mouth);
        return smk;
    }

    /**
     * 组合收益率走势图
     *
     * @param id
     * @param subGroupId
     * @param mouth      几个月以来每月
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率走势图-每月")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-mounth", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncomeMounth(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId,@RequestParam(defaultValue="-12") int mouth) throws ParseException {
        ReturnType smk = fundGroupService.getFundGroupIncomeMounth(id,subGroupId,mouth);
        return smk;
    }

    /**
     * 组合内各基金收益率走势图
     *
     * @param id
     * @param subGroupId
     * @param mouth      几个月以来
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合内各基金收益率走势图")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/single-yield", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundIncome(@PathVariable("groupId") String id,@PathVariable("subGroupId") String subGroupId,@RequestParam(defaultValue="-1") int mouth) throws ParseException {
        ReturnType smk = fundGroupService.getFundIncome(id,subGroupId,mouth);
        return smk;
    }
}