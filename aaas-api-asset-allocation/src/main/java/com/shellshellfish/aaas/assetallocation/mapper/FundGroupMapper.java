package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public interface FundGroupMapper {

    List<String> getSubGroupIdByGroupId(String fundGroupId);

    List<Interval> selectFundGroup(Map map);

    List<Interval> selectAllFundGroupNum(Map param);

    List<Interval> selectById(Map map);

    List<Interval> getProportionOne(Map map);

    List<Interval> getProportion(Map map);

    List<Interval> getProportionGroupByFundTypeTwo(Map map);

    List<Interval> getFnameAndProportion(Map map);

    List<Interval> getFundCode(Map map);

    List<String> getFundGroupCodeList(Map map);

    List<String> getGroupCodeList(Map map);

    List<Date> getGroupDateList(Map map);

    List<Interval> getInterval(Map map);

    Interval selectReturnAndPullback(Map map);

    List<RiskIncomeInterval> getPerformanceVolatility(Map map);

    RiskIncomeInterval getMaxLoss(@Param("oemId") Integer oemId, @Param("custRisk") String custRisk,
                                  @Param("investmentHorizon") String investmentHorizon);

    List<Interval> getRevenueContribution(Map map);

    List<RiskController> getRiskController(Map map);

    List<RiskIncomeInterval> getScaleMark(Map map);

    List<RiskIncomeInterval> getScaleMarkFromChoose(@Param("id") String id,
                                                    @Param("slidebarType") String slidebarType,
                                                    @Param("standardType") String standardType);

    String getFundGroupNameById(@Param("id") String id, @Param("oemId") int oemId);

    String getFundGroupHistoryTime(Map map);

    String getFundGroupHistoryTimeByRiskLevel(Map map);

    List<FundNetVal> getFundNetValue(Map map);

    List<FundGroupHistory> getHistory(Map map);

    List<FundGroupHistory> getHistoryOne(Map map);

    List<FundGroupHistory> getHistoryAll(Map map);

    List<FundNetVal> getSharpeRatio(Map map);

    List<FundGroupExpectedIncome> getExpectedIncome(Map map);

    int updateSharpeRatio(Map map);

    int updateAllSharpeRatio(@Param("sharpeRatios") List<Map> sharpeRatios, @Param("oemId") int oemId);

    List<FundNetVal> getNavadj(Map map);

    List<FundNetVal> getNavadjByNavDates(Map map);

    List<Map> getNavlatestdateCount(Map map);

    List<FundNetVal> getNavadjBenchmark(Map map);

    List<FundGroupHistory> selectMaximumRetracement(Map map);

    int insertFundGroupHistory(@Param("fundGroupHistoryList") List<FundGroupHistory>
                                       fundGroupHistoryList, @Param("oemId") Integer oemId);

    int batchInsertFundGroupHistory(List<Map> mapList, @Param("oemId") Integer oemId);

    int batchInsertFundGroupHistoryBenchmark(List<Map> mapList, @Param("oemId") Integer oemId);

    int updateMaximumRetracement(Map map);

    int batchUpdateMaximumRetracement(List<Map> mapList, @Param("oemId") Integer oemId);


    int batchUpdateMaximumRetracementByRiskLevel(List<Map> mapList, @Param("oemId") Integer oemId);

    String getGroupStartTime(Map map);

    List<EfficientFrontier> getEfficientFrontier(Map map);

    List<FundNetVal> getNavadjStartTime(Map map);

    List<FundNetVal> getNavadjFromStartDate(Map map);

    List<FundNetVal> getNavadjByNavDate(Map map);

    List<FundNetVal> getNavadjEndTime(Map map);

    List<Interval> getAllIdAndSubId(@Param("oemId") Integer oemId);

    String getRiskNum(@Param("id") String id);

    int batchUpdateContribution(List<Map> mapList);

    int updateMaxLoss(@Param("fundGroupId") String fundGroupId, @Param("subGroupId") String subGroupId, @Param("maxLoss") Double maxLoss);

    int updateExpectedMaximumRetracement(Map map);

    //查询 fund_group_basic 中全部 code
    List<String> findAllGroupCode(@Param("oemId") Integer oemId);

    //查询产品组合中 code
    List<String> findGroupCode(@Param("oemId") Integer oemId);

    //查询基准组合中 code
    List<String> findBenchmarkCode(@Param("oemId") Integer oemId);

    //查询组合中有效 group_id 中 code
    List<FundCombination> findAllGroupId(@Param("oemId") Integer oemId);

    //取 MVO 方法计算的数据 insert into fund_group_details
    Integer insertIntoFundGroupDetails(List<FundCombination> list, @Param("oemId") Integer oemId);

    //取 MVO 方法计算的数据 insert into fund_group_sub
    Integer insertIntoFundGroupSub(List<FundCombination> fundCombinationList, @Param("oemId") Integer oemId);

    //将 fund_group_details 中 数据 备份到 fund_group_details_history
    Integer transIntoFundGroupDetailsHistory(@Param("oemId") Integer oemId);

    //将 fund_group_sub 中 数据 备份到 fund_group_sub_history
    Integer transIntoFundGroupSubHistory(@Param("oemId") Integer oemId);

    //将 fund_group_details 中 数据 删除
    Integer deleteFundGroupDetails(@Param("oemId") Integer oemId);

    //将 fund_group_sub 中 数据 删除
    Integer deleteFundGroupSub(@Param("oemId") Integer oemId);

    void updateMaximumLosses(Map<String, Object> query);
}
