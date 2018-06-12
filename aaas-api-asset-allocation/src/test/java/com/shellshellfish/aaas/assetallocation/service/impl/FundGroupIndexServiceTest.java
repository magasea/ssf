package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.AssetAllocationApp;
import com.shellshellfish.aaas.assetallocation.service.FundGroupIndexService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by pierre
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssetAllocationApp.class)
@ActiveProfiles("it")
public class FundGroupIndexServiceTest {
    @Autowired
    FundGroupIndexService fundGroupIndexService;

    @Test
    public void calculateAnnualVolatilityAndAnnualYeildTest() {
        String groupId = "1";
        String subGroupId = "10048";
        for (int i = 1; i <= 15; i++) {
            fundGroupIndexService.calculateAnnualVolatilityAndAnnualYield(String.valueOf(i), String.valueOf(i) +
                    "0048", null, 1);
        }
    }

}