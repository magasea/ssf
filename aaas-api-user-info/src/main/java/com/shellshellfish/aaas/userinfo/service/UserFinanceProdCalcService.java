package com.shellshellfish.aaas.userinfo.service;

import java.math.BigDecimal;

public interface UserFinanceProdCalcService {

    BigDecimal calcTotalDailyAsset(String userUuid) throws Exception;

    BigDecimal calcDailyAsset(String userUuid, String fundCode, String date) throws Exception;

    void calcIntervalAmount(String userUuid, String fundCode, String startDate) throws Exception;

    void initDailyAmount(String userUuid, String date, String fundCode);

    BigDecimal calcYieldRate(String userUuid, String startDate, String endDate);

    BigDecimal calcTotalAssetOfFinanceProduct();
}
