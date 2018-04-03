package com.shellshellfish.aaas.assetallocation.service.impl;

import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

/**
 *
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("it")
public class FundGroupServiceImplTest {
    @Autowired
    FundGroupService fundGroupService;

    @Test
    public void getNavlatestdateCount() throws Exception {
        String groupId = "1";
        String subGroupId = "10058";
        List<LocalDate> result = fundGroupService.getNavlatestdateCount(groupId, subGroupId);
        Assert.assertNotNull("数据为空", result);
    }

    @Test
    public void testCalculateGroupNavadj() {
        String groupId = "1";
        String subGroupId = "1000";
        fundGroupService.calculateGroupNavadj(groupId, subGroupId, null);
    }

    @Test
    public void testCalculateMaxRetracement() {
        String groupId = "1";
        String subGroupId = "1000";
        fundGroupService.calculateMaxRetracement(groupId, subGroupId, LocalDate.now().plusDays(-1));
    }

    @Test
    public void testCalculateMaxRetracementFromList() {
        String groupId = "1";
        String subGroupId = "1000";
        fundGroupService.calculateMaxRetracement(groupId, subGroupId);
    }

}