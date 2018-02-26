package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.job.service.JobScheduleService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Author: hongming
 * Date: 2018/2/23
 * Desc:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class JobScheduleServiceTest {

    @Autowired
    private JobScheduleService jobScheduleService;

    @Test
    public void getFundGroupIncomeAllJobScheduleTest() {
        jobScheduleService.getFundGroupIncomeAllJobSchedule();
    }

}
