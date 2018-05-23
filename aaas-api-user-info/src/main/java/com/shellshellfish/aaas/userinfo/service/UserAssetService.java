package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;

import java.time.LocalDate;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public interface UserAssetService {

    /**
     * 计算用户持仓资产和收益
     */
    PortfolioInfo calculateUserAssetAndIncome(Long userProdId, LocalDate endDate);

    /**
     * 计算用户部分确认的持仓组合　的总资产和收益
     */

    PortfolioInfo calculateUserAssetAndIncomePartialConfirmed(Long userId, Long prodId, LocalDate endDate);
}
