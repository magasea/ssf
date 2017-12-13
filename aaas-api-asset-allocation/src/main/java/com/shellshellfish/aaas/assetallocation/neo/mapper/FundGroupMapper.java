package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by wangyinuo on 2017/11/16.
 */
public interface FundGroupMapper {

    List<Interval> selectFundGroup(Map map);

    int insertRecommendHistory(Map map);

    List<Interval> selectAllFundGroup();

    List<Interval> selectAllFundGroupNum();

    List<Interval> selectById(@Param("id") String id, @Param("subGroupId") String subGroupId);

    List<Interval> getProportion( @Param("id") String id, @Param("subId") String subId);

    List<FundGroupDetails> efficientFrontier(@Param("uuid") String uuid,@Param("subGroupId") String subGroupId);

    List<Interval> getinterval(Map map);

    int updateStatus(Map map);

    int insertFundGroup(FundGroup fundGroup);

    int insertFundGroupDetail(List<FundGroupDetails> fundGroupDetailslist);

    Interval selectReturnAndPullback(Map map);

    List<RiskIncomeInterval> getPerformanceVolatility(Map map);

    List<Interval> getRevenueContribution(Map map);

    List<RiskController> getRiskController(@Param("id") String id, @Param("subGroupId") String subGroupId);

    List<RiskIncomeInterval> getScaleMark(@Param("id") String id);

    List<FundGroupBuy> getFundGroupBuy(@Param("id") String id);

    List<FundNetVal> getFundNetValue(@Param("fund_income_type") String fund_income_type, @Param("starttime") String starttime, @Param("endtime") String endtime);

    List<FundGroupHistory> getHistory(Map map);

}
