package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import com.shellshellfish.aaas.assetallocation.neo.entity.FundNetVal;

import java.util.Date;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/17
 * Desc:
 */
//@Mapper
public interface FundNetValMapper {

    //根据时间查询净值表数据
    List<FundNetVal> getAllByDate(Date selectDate);

    //根据时间查询净值表code数据
    List<String> getAllCodeByDate(Date selectDate);

    //根据时间查询阶段扫描最大时间
    Date getMaxNavDateByDate(Date selectDate);

    //查询配置表计算风险率的参数number
    Integer getNumberFromSysConfig(String type);

    //根据基金代码以及时间查询净值表数据
    List<CovarianceModel> getDataByCodeAndDate(CovarianceModel covarianceModel);

    //更新数据 adjustedFactor (复权因子)
    Integer updateAdjustedFactor(FundNetVal fundNetVal);
}
