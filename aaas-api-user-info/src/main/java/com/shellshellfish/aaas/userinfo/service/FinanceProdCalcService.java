package com.shellshellfish.aaas.userinfo.service;

import java.math.BigDecimal;

public interface FinanceProdCalcService {

    BigDecimal calcTotalDailyAsset(String userUuid) throws Exception;

    BigDecimal calcDailyAsset(String userUuid, String fundCode) throws Exception;

    BigDecimal calcIntervalAmount(String userUuid, String fundCode, String startDate) throws Exception;

    BigDecimal calcTotalAssetOfFinanceProduct();
}
