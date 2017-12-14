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

    List<Interval> selectById(Map map);

    List<Interval> getProportion(Map map);

    List<FundGroupDetails> efficientFrontier(Map map);

    List<Interval> getinterval(Map map);

    int updateStatus(Map map);

    int insertFundGroup(FundGroup fundGroup);

    int insertFundGroupDetail(List<FundGroupDetails> fundGroupDetailslist);

    Interval selectReturnAndPullback(Map map);

    List<RiskIncomeInterval> getPerformanceVolatility(Map map);

    List<Interval> getRevenueContribution(Map map);

    List<RiskController> getRiskController(Map map);

    List<RiskIncomeInterval> getScaleMark(@Param("id") String id);

    List<FundGroupBuy> getFundGroupBuy(@Param("id") String id);

    List<FundNetVal> getFundNetValue(Map map);

    List<FundGroupHistory> getHistory(Map map);

    List<FundNetVal> getSharpeRatio(Map map);

}
