package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

@RunWith(value= SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class UserFinanceProdCalcServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(UserFinanceProdCalcServiceImplTest.class);

    @Autowired
    private UserFinanceProdCalcService userFinanceProdCalcService;

    @Autowired
    @Qualifier("secondaryMongoTemplate")
    private MongoTemplate mongoTemplate;

    private List<String> fundCodeList = Arrays.asList("000614",
            "001987",
            "000216",
            "002068",
            "002163",
            "000395",
            "001490");

    @Before
    public void setUp() {
        final Calendar cal = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        String today = simpleDateFormat.format(cal.getTime());
        fundCodeList.forEach(fundCode -> {
                userFinanceProdCalcService.initDailyAmount("shellshellfish", today, fundCode);
        });

    }

    @After
    public void tearDown() {
//        mongoTemplate.dropCollection(DailyAmount.class);
    }

    @Test
    public void testCalcTotalDailyAsset() throws Exception {

    }

    @Test
    public void testCalcDailyAsset() throws Exception {
        for(String fundCode:fundCodeList) {
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171222");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171223");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171224");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171225");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171226");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171227");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171228");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", fundCode, "20171229");
        }
    }

    @Test
    public void testCalcIntervalAmount() throws Exception {
        for(String fundCode:fundCodeList) {
            userFinanceProdCalcService.calcIntervalAmount("shellshellfish", fundCode, "20170101");
        }
    }

    @Test
    public void testCalcYieldRate() throws Exception {
     //   testCalcDailyAsset();
     //   testCalcIntervalAmount();

        long start = System.currentTimeMillis();
        BigDecimal yieldRate = userFinanceProdCalcService.calcYieldRate("shellshellfish", "20171226", "20171229");
        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
        logger.info("yieldRate: {}", yieldRate);
    }

}