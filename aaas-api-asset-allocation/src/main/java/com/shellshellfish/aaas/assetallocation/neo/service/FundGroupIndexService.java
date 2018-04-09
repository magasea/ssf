package com.shellshellfish.aaas.assetallocation.neo.service;

import java.time.LocalDate;

public interface FundGroupIndexService {

    void calculateAnnualVolatilityAndAnnualYield(String groupId, String subGroupId, LocalDate startDate);

    void calculateAnnualVolatilityAndAnnualYield(LocalDate startDate);
}
