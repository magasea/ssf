package com.shellshellfish.aaas.userinfo.service;

import java.math.BigDecimal;

public interface FinanceProdCalcService {

    BigDecimal calcTotalDailyAsset(String userUuid) throws Exception;

    BigDecimal calcTotalAssetOfFinanceProduct();
}
