package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public interface UserAssetService {

    /**
     * 计算用户持仓资产和收益
     */
    PortfolioInfo calculateUserAssetAndIncome(String userUuid, Long prodId,
                                              String startDate, String endDate);

    /**
     * 计算用户部分确认的持仓组合　的总资产和收益
     */

    PortfolioInfo calculateUserAssetAndIncomePartialConfirmed(String uuid, Long userId, Long prodId,
                                                              String startDay, String endDay);
}
