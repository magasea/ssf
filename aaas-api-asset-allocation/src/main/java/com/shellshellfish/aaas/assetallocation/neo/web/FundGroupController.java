package com.shellshellfish.aaas.assetallocation.neo.web;

import com.shellshellfish.aaas.assetallocation.neo.returnType.FundAllReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.FundReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.assetallocation.neo.returnType.Return;
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
    public FundAllReturn selectAllFundGroup() {
        return fundGroupService.selectAllFundGroup();
    }

    /**
     * 产品类别比重
     * @param groupId
     * @param subGroupId
     * @return
     */
    @ApiOperation("产品类别比重")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/sub-groups/{subGroupId}/one-type", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getProportionOne(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        return fundGroupService.getProportionOne(groupId, subGroupId);
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
        return fundGroupService.getFnameAndProportion(groupId, subGroupId);
    }

    /**
     * 返回首页五个产品
     * @return
     */
    @ApiOperation("返回首页五个产品")
    @RequestMapping(value = "/api/asset-allocation/products/home-page", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getPerformanceVolatilityHomePage() {
        return fundGroupService.getPerformanceVolatilityHomePage();
    }

    /**
     * 按照ID查询基金组合明细，按照 基金二级分类 进行分组
     * @param groupId
     * @return
     */
    @ApiOperation("返回单个基金组合产品信息")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn selectById(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        return fundGroupService.getProportionGroupByFundTypeTwo(groupId, subGroupId);
    }

    /**
     * 预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)
     * @param groupId
     * @param returntype (1:预期年化收益       2:预期最大回撤)
     * @param subGroupId
     * @return
     */
    @ApiOperation("预期年化收益(action=calcExpectedAnnualizedReturn), 预期最大回撤(action=calcExpectedMaxPullback)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/opt", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> selectReturnAndPullback(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="1") String returntype) {
        Map<String, Object> map = fundGroupService.selectReturnAndPullback(groupId, returntype, subGroupId);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    /**
     * 配置收益贡献
     * @return
     */
    @ApiOperation("配置收益贡献")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/contributions", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getRevenueContribution(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        return fundGroupService.getRevenueContribution(groupId, subGroupId);
    }

    /**
     * 有效前沿线
     * @return
     */
    @ApiOperation("有效前沿线")
    @RequestMapping(value = "/api/asset-allocation/products/{groupId}/effective-frontier-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType efficientFrontier(@PathVariable("groupId") String groupId) {
        return fundGroupService.efficientFrontier(groupId);
    }

    /**
     * 预期收益率调整 风险率调整 最优组合(有效前沿线)
     * @param groupId
     * @param riskValue
     * @param returnValue
     * @return
     */
    @ApiOperation("预期收益率调整 风险率调整  最优组合(有效前沿线)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/optimizations", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public FundReturn getInterval(@PathVariable("groupId") String groupId, @RequestParam(defaultValue="0.13") String riskValue, @RequestParam(defaultValue="0.15") String returnValue) {
        return fundGroupService.getInterval(groupId, riskValue, returnValue);
    }

    /**
     * 风险控制
     * @param groupId
     * @return
     */
    @ApiOperation("风险控制")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/risk-controls", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getRiskController(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        return fundGroupService.getRiskController("1", "2");
    }

    /**
     * 获取组合风险等级
     * @param groupId
     * @return
     */
    @ApiOperation("组合风险等级")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/risk-level", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public Return getCustRiskController(@PathVariable("groupId") String groupId) {
        if (StringUtils.isEmpty(groupId)) {
            return null;
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
    public ReturnType getMeansAndNoticesReturn(@PathVariable("uuid") String uuid) {
        return fundGroupService.getMeansAndNoticesReturn();
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
        return fundGroupService.getPerformanceVolatility(riskLevel, investmentPeriod);
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
        return fundGroupService.getHistoricalPerformance(fund_group_id, subGroupId);
    }

    /**
     * 分段数据
     * @param groupId
     * @param slidebarType  (risk_num    风险率,income_num  收益率)
     * @return
     */
    @ApiOperation("滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getScaleMark(@PathVariable("groupId") String groupId, String slidebarType) {
        return fundGroupService.getScaleMark(groupId, slidebarType);
    }

    /**
     * 分段数据  已选好的数据
     * @param groupId
     * @param slidebarType  (risk_num    风险率, income_num  收益率)
     * @return
     */
    @ApiOperation("已选好的滑动条分段数据")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/slidebar-points-from-choose", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getScaleMarkFromChoose(@PathVariable("groupId") String groupId, String slidebarType) {
        return fundGroupService.getScaleMarkFromChoose(groupId, slidebarType);
    }

    /**
     * 组合收益率(最大回撤)走势图     (几个月以来每天)
     *
     * @param groupId
     * @param subGroupId
     * @param mouth
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     */
    @ApiOperation("组合收益率(最大回撤)走势图-每天")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncome(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="-1") int mouth, @RequestParam(defaultValue="income") String returnType) {
        return fundGroupService.getFundGroupIncome(groupId, subGroupId, mouth, returnType);
    }

    /**
     * 组合收益率(最大回撤)走势图     (自组合基金成立以来的每天)
     *
     * @param groupId
     * @param subGroupId
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     * @throws ParseException
     */
    @ApiOperation("组合收益率(最大回撤)走势图-自组合基金成立以来的每天")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncomeAll(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="income") String returnType) {
        return fundGroupService.getFundGroupIncomeAll(groupId, subGroupId, returnType);
    }

    /**
     * 组合收益率(最大回撤)走势图     (一周以来每天)
     *
     * @param groupId
     * @param subGroupId
     * @param returnType      查询类型（income：收益率，其他的：最大回撤）
     * @return
     */
    @ApiOperation("组合收益率(最大回撤)走势图-每天(一周以来)")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/portfolio-yield-week", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundGroupIncomeWeek(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId, @RequestParam(defaultValue="income") String returnType) {
        return fundGroupService.getFundGroupIncomeWeek(groupId, subGroupId, returnType);
    }

    /**
     * 净值增长率(净值增长)走势图     一周以来以来每天
     *
     * @param id
     * @param subGroupId
     * @return
     */
    @ApiOperation("组合各种类型净值收益")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/fund-navadj", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getFundNetValue(String id, String subGroupId, @RequestParam(defaultValue="1") String returnType) {
        return fundGroupService.getFundNetValue(id, subGroupId, returnType);
    }

    /**
     * 未来收益走势图
     *
     * @param groupId
     * @param subGroupId
     * @return
     */
    @ApiOperation("未来收益走势图")
    @RequestMapping(value = "/api/asset-allocation/product-groups/{groupId}/sub-groups/{subGroupId}/expected-income", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ReturnType getExpectedIncome(@PathVariable("groupId") String groupId, @PathVariable("subGroupId") String subGroupId) {
        return fundGroupService.getExpectedIncome(groupId, subGroupId);
    }

    @ApiOperation("拉数据所需基金代码")
    @RequestMapping(value = "/api/asset-allocation/product-groups/fund-code", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> findAllGroupCode() {
        return fundGroupService.findAllGroupCode();
    }

}
