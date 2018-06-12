package com.shellshellfish.aaas.assetallocation.service;

import java.time.LocalDate;

public interface FundGroupIndexService {


    void calculateAnnualVolatilityAndAnnualYield(String groupId, String subGroupId, LocalDate startDate, int oemId);

    void calculateAnnualVolatilityAndAnnualYield(int oemId);
}
