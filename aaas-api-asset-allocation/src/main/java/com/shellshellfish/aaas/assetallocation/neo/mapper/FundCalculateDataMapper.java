package com.shellshellfish.aaas.assetallocation.neo.mapper;


import com.shellshellfish.aaas.assetallocation.neo.entity.FundCalculateData;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/23
 * Desc:收益率与风险率相关
 */
public interface FundCalculateDataMapper {

    //插入数据到fund_calculate_data_day
    Integer insertFundCalculateDataDay(FundCalculateData fundCalculateData);

    //插入数据到fund_calculate_data_week
    Integer insertFundCalculateDataWeek(FundCalculateData fundCalculateData);

    //插入数据到fund_calculate_data_month
    Integer insertFundCalculateDataMonth(FundCalculateData fundCalculateData);

    //插入数据到fund_calculate_data_year
    Integer insertFundCalculateDataYear(FundCalculateData fundCalculateData);

    //查询收益率
    Double findYieldRatio(@Param("tableName") String tableName, @Param("code") String code, @Param("selectDate") String selectDate);

    //查询风险率(方差)
    Double findRiskRatio(@Param("tableName") String tableName, @Param("code") String code, @Param("selectDate") String selectDate);

    //查询复权单位净值
    List<FundCalculateData> findNavadjByType(@Param("tableName") String tableName, @Param("code") String code, @Param("startDate") String startDate, @Param("endDate") String endDate);

    //查询复权单位净值
    List<Double> findNavadjByCodeAndDate(@Param("tableName") String tableName, @Param("code") String code, @Param("startDate") String startDate, @Param("endDate") String endDate);

}
