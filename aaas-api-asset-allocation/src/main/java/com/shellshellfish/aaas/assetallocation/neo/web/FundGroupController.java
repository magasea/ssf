package com.shellshellfish.aaas.assetallocation.neo.web;

import com.shellshellfish.aaas.assetallocation.neo.returnType.FundAllReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.FundReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.ReturnType;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.List;
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
     * 产品类别比重
     * @param fund_group_id
     * @param fund_group_sub_id
     * @return
     */
    @ApiOperation("产品类别比重")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/one-type", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getProportionOne(@PathVariable("groupId") String fund_group_id,@PathVariable("subGroupId") String fund_group_sub_id){
        ReturnType fr = fundGroupService.getProportionOne(fund_group_id, fund_group_sub_id);
        return fr;
    }

    /**
     * 组合内基金名称及其百分比
     * @param groupId
     * @param subGroupId
     * @return
     */
    @ApiOperation("组合内基金名称及其百分比")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/fname-proportion", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFnameAndProportion(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        ReturnType fr = fundGroupService.getFnameAndProportion(groupId, subGroupId);
        return fr;
    }

    /**
     * 返回首页五个产品
     * @return
     */
    @ApiOperation("返回首页五个产品")
    @RequestMapping(value = "/api/asset-allocation/products/home-page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getPerformanceVolatilityHomePage(){
        ReturnType fr = fundGroupService.getPerformanceVolatilityHomePage();
        return fr;
    }

    /**
     * 按照ID查询基金组合明细
     * @param id
     * @return
     */
    @ApiOperation("返回单个基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn selectById(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        FundReturn fr= fundGroupService.selectById(id, subGroupId);
        return fr;
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param id
     * @param returntype (1:预期年化收益       2:预期最大回撤)
     * @param subGroupId
     * @return
     */
    @ApiOperation("预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String,Object>> selectReturnAndPullback(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="1") String returntype){
        Map<String,Object> map = fundGroupService.selectReturnAndPullback(id, returntype, subGroupId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 配置收益贡献
     * @return
     */
    @ApiOperation("配置收益贡献")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getRevenueContribution(@PathVariable("groupId") String uuid, @PathVariable("subGroupId") String subGroupId){
        ReturnType rcb = fundGroupService.getRevenueContribution(uuid, subGroupId);
        return rcb;
    }

    /**
     * 有效前沿线
     * @return
     */
    @ApiOperation("有效前沿线")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType efficientFrontier(@PathVariable("groupId") String id){
        ReturnType aReturn = fundGroupService.efficientFrontier(id);
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
    public FundReturn getInterval(@PathVariable("groupId") String id, @RequestParam(defaultValue="0.13") String riskValue, @RequestParam(defaultValue="0.15") String returnValue){
        FundReturn fr = fundGroupService.getInterval(id, riskValue, returnValue);
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
        ReturnType rct = fundGroupService.getRiskController(id, subGroupId);
        return rct;
    }

    /**
     * 获取组合风险等级
     * @param groupId
     * @return
     */
    @ApiOperation("组合风险等级")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/risk-level", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public String getCustRiskController(@PathVariable("groupId") String groupId){
        if (StringUtils.isEmpty(groupId)) {
            return "";
        }
        return fundGroupService.getCustRiskByGroupId(groupId);
    }

    /**
     * 风险控制手段与通知
     * @param uuid
     * @return
     */
    @ApiOperation("风险控制手段与通知")
    @RequestMapping(value = "/api/asset-allocation/products/{uuid}/risk-notifications", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getMeansAndNoticesReturn(@PathVariable("uuid") String uuid){
        ReturnType man = fundGroupService.getMeansAndNoticesReturn();
        return man;
    }

    /**
     * 模拟历史年化业绩与模拟历史年化波动率
     * @param riskLevel
     * @param investmentPeriod
     * @return
     */
    @ApiOperation("模拟历史年化业绩与模拟历史年化波动率")
    @RequestMapping(value = "/api/asset-allocation/product-groups", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getPerformanceVolatility(@RequestParam(defaultValue="C1") String riskLevel, @RequestParam(defaultValue="1") String investmentPeriod) {
        PerformanceVolatilityReturn riskIncomeIntervals = fundGroupService.getPerformanceVolatility(riskLevel, investmentPeriod);
        return riskIncomeIntervals;
    }

    /**
     * 历史业绩
     * @param fund_group_id
     * @param subGroupId
     * @return
     */
    @ApiOperation("历史业绩")
    @RequestMapping(value = "/api/asset-allocation/product-groups/historicalPer-formance", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public PerformanceVolatilityReturn getHistoricalPerformance(@RequestParam(defaultValue="6") String fund_group_id, @RequestParam(defaultValue="6000") String subGroupId) {
        PerformanceVolatilityReturn riskIncomeIntervals = fundGroupService.getHistoricalPerformance(fund_group_id, subGroupId);
        return riskIncomeIntervals;
    }

    /**
     * 分段数据
     * @param id
     * @param slidebarType  (risk_num    风险率,income_num  收益率)
     * @return
     */
    @ApiOperation("滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getScaleMark(@PathVariable("groupId") String id, String slidebarType){
        ReturnType smk = fundGroupService.getScaleMark(id, slidebarType);
        return smk;
    }

    /**
     * 分段数据  已选好的数据
     * @param id
     * @param slidebarType  (risk_num    风险率, income_num  收益率)
     * @return
     */
    @ApiOperation("已选好的滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points-from-choose", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getScaleMarkFromChoose(@PathVariable("groupId") String id, String slidebarType){
        ReturnType smk = fundGroupService.getScaleMarkFromChoose(id, slidebarType);
        return smk;
    }

    /**
     * 组合收益率(最大回撤)走势图     (几个月以来每天)
     *
     * @param id
     * @param subGroupId
     * @param mouth
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率(最大回撤)走势图-每天")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncome(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="-1") int mouth, @RequestParam(defaultValue="income") String returnType) throws ParseException {
        ReturnType smk = fundGroupService.getFundGroupIncome(id, subGroupId, mouth, returnType);
        return smk;
    }

    /**
     * 组合收益率(最大回撤)走势图     (自组合基金成立以来的每天)
     *
     * @param id
     * @param subGroupId
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率(最大回撤)走势图-自组合基金成立以来的每天")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncomeAll(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="income") String returnType) {
        ReturnType smk = fundGroupService.getFundGroupIncomeAll(id, subGroupId, returnType);
        return smk;
    }

    /**
     * 组合收益率(最大回撤)走势图     (一周以来每天)
     *
     * @param id
     * @param subGroupId
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率(最大回撤)走势图-每天(一周以来)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-week", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncomeWeek(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="income") String returnType) throws ParseException {
        ReturnType smk = fundGroupService.getFundGroupIncomeWeek(id, subGroupId, returnType);
        return smk;
    }

    /**
     * 净值增长率(净值增长)走势图     一周以来以来每天
     *
     * @param id
     * @param subGroupId
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合各种类型净值收益")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/fund-navadj", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundNetValue(String id, String subGroupId, @RequestParam(defaultValue="1") String returnType) throws ParseException {
        ReturnType smk = fundGroupService.getFundNetValue(id, subGroupId, returnType);
        return smk;
    }

    /**
     * 未来收益走势图
     *
     * @param id
     * @param subGroupId
     * @return
     * @throws ParseException
     */
    @ApiOperation("未来收益走势图")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/expected-income", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getExpectedIncome(@PathVariable("groupId") String id, @PathVariable("subGroupId") String subGroupId){
        ReturnType smk = fundGroupService.getExpectedIncome(id, subGroupId);
        return smk;
    }

    @ApiOperation("拉数据所需基金代码")
    @RequestMapping(value = "/api/asset-allocation/product-groups/fund-code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> findAllGroupCode(){
        return fundGroupService.findAllGroupCode();
    }

}
