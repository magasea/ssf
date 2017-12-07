package com.shellshellfish.aaas.assetallocation.neo.mapper;

import com.shellshellfish.aaas.assetallocation.neo.entity.CovarianceModel;
import org.apache.ibatis.annotations.Param;

/**
 * Author: yongquan.xiong
 * Date: 2017/11/23
 * Desc:协方差相关
 */
public interface CovarianceMapper {

    //插入日协方差数据
    Integer insertCovarianceOfDay(CovarianceModel covarianceModel);

    //插入周协方差数据
    Integer insertCovarianceOfWeek(CovarianceModel covarianceModel);

    //插入月协方差数据
    Integer insertCovarianceOfMonth(CovarianceModel covarianceModel);

    //插入年协方差数据
    Integer insertCovarianceOfYear(CovarianceModel covarianceModel);

    //查询协方差
    Double findCovariance(@Param("tableName") String tableName, @Param("codeA") String codeA, @Param("codeB") String codeB, @Param("selectDate") String selectDate);


}
