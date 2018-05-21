package com.shellshellfish.aaas.userinfo.service;

import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public interface UserAssetService {

    PortfolioInfo calculateUserAssetAndIncome(String userUuid, Long prodId,
                                                   String startDate, String endDate);
}
