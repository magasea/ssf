package com.shellshellfish.aaas.assetallocation.mapper;

import com.shellshellfish.aaas.assetallocation.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.entity.Dailyfunds;
import com.shellshellfish.aaas.assetallocation.entity.FundNetVal;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:
 */
//@Mapper
public interface FundNetValMapper {

    //根据时间以及code查询净值表数据
    List<FundNetVal> getAllDataByCodeAndDate(HashMap<String,Object> codeMap);

    //根据时间查询净值表数据
    List<FundNetVal> getAllByDate(Map<String,Object> codeMap);

    //根据时间查询阶段扫描最大时间
    Date getMaxNavDateByDate(Date selectDate);

    //查询配置表计算风险率的参数number
    Integer getNumberFromSysConfig(String type);

    //根据基金代码以及时间查询净值表数据
    List<CovarianceModel> getDataByCodeAndDate(CovarianceModel covarianceModel);

    //更新数据 adjustedFactor (复权因子)
    Integer updateAdjustedFactor(FundNetVal fundNetVal);

    //根据时间查询阶段扫描最大时间
    Date getMaxNavDateByCode(String code);

    //将通过rpc获取到的每日基金数据插入 fund_net_value
    Integer insertDailyDataToFundNetVal(List<Dailyfunds> dailyfundsList);

    //将通过rpc获取到的每日基金数据批量更新到 fund_net_value
    Integer batchUpdateDailyDataToFundNetVal(List<Dailyfunds> dailyfundsList);

    //将通过rpc获取到的每日基金数据更新到 fund_net_value
    Integer updateDailyDataToFundNetVal(Dailyfunds dailyfunds);

    //将通过rpc获取到的每日基金基础数据插入 fund_basic
    Integer insertBasicDataToFundBasic(Dailyfunds dailyfunds);

    //根据code 查询 fund_basic 表中是否已有数据
    String findBasicDataByCode(String code);

    //根据 codeList 查询基金最晚成立日
    Date getMinNavDateByCodeList(List<String> codeList);

    // 根据 codeList 查询基金最近的净值更新日期
    Date getMaxNavDateByCodeList(List<String> codeList);

    BigDecimal getLatestNavAdj(@Param("code") String code,@Param("date") LocalDate date);
}
