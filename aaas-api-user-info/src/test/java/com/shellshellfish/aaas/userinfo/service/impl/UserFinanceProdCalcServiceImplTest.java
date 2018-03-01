package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.service.UserFinanceProdCalcService;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

@RunWith(value= SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="test")
@Ignore
public class UserFinanceProdCalcServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(UserFinanceProdCalcServiceImplTest.class);

    @Autowired
    private UserFinanceProdCalcService userFinanceProdCalcService;

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
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
//        final Calendar cal = Calendar.getInstance();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
//        String today = simpleDateFormat.format(cal.getTime());
//        fundCodeList.forEach(fundCode -> {
//                userFinanceProdCalcService.initDailyAmount("shellshellfish", today, fundCode);
//        });

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
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171222");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171223");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171224");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171225");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171226");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171227");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171228");
            userFinanceProdCalcService.calcDailyAsset("shellshellfish", 2000L, fundCode, "20171229");
        }
    }

    @Test
    public void testCalcIntervalAmount() throws Exception {
        for(String fundCode:fundCodeList) {
            userFinanceProdCalcService.calcIntervalAmount("shellshellfish", 2000L, fundCode, "20170101");
        }
    }

    @Test
    public void testTotalDailyAsset() throws Exception {
        BigDecimal dailyAsset = userFinanceProdCalcService.calcTotalDailyAsset("shellshellfish");
        logger.info("dailyAsset:{}", dailyAsset);
    }

    @Test
    public void testDailyCalculationOfToday() throws Exception {
        long start = System.currentTimeMillis();
        userFinanceProdCalcService.dailyCalculation();
        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
    }

    @Test
    public void testDailyCalculation() throws Exception {
        long start = System.currentTimeMillis();
        List<String> days = Arrays.asList(//"20171222", "20171223", "20171224", "20171225", "20171226", "20171227", "20171228", "20171229", "20171230", "20171231",
                                          "20180101", "20180102", "20180103");
        days.forEach(day -> {
            try {
                userFinanceProdCalcService.dailyCalculation(day);
            } catch (Exception e) {
                logger.error("exception:",e);
            }
        });

        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
    }

    @Test
    public void testCalcYieldRate() throws Exception {
     //   testCalcDailyAsset();
     //   testCalcIntervalAmount();

        long start = System.currentTimeMillis();
        BigDecimal yieldRate = userFinanceProdCalcService.calcYieldRate("shellshellfish", 2000L,"20171222", "20171226");
        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
        logger.info("yieldRate: {}", yieldRate);
    }

    @Test
    public void testTotalAssetYieldRate() {
        long start = System.currentTimeMillis();
        BigDecimal yieldRate = userFinanceProdCalcService.calcYieldRate("shellshellfish","20171226", "20180104");
        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
        logger.info("total asset yieldRate: {}", yieldRate);
    }

    @Test
    public void testTotalAssetYieldValue(){
        long start = System.currentTimeMillis();
        BigDecimal yieldValue1 = userFinanceProdCalcService.calcYieldValue("shellshellfish","20171226", "20171231");
        BigDecimal yieldValue2 = userFinanceProdCalcService.calcYieldValue("shellshellfish","20171231", "20180104");
        BigDecimal yieldValue3 = userFinanceProdCalcService.calcYieldValue("shellshellfish","20171226", "20180104");
        long end = System.currentTimeMillis();
        logger.info("duration: {}", end - start);
        logger.info("total asset yieldValue1: {}, yieldValue2: {}, yieldValue3: {}", yieldValue1, yieldValue2, yieldValue3);
    }

    @Test
    public void addRandomValue(){
        List<DailyAmount> dailyAmountList = mongoTemplate.findAll(DailyAmount.class);
        for(DailyAmount dailyAmount : dailyAmountList) {
            if (dailyAmount.getAsset() != null && dailyAmount.getAsset().compareTo(BigDecimal.ZERO) != 0) {
                double noise = new Random().nextInt(10);
                if (noise < 3 || noise > 7) {
                   noise = - noise;
                }
                noise /= 10;
                BigDecimal  percent = BigDecimal.valueOf((100-noise)/100d);
                BigDecimal  amount = dailyAmount.getAsset().multiply(percent);
                dailyAmount.setAsset(amount);

                Query query = new Query();
                query.addCriteria(Criteria.where("userUuid").is(dailyAmount.getUserUuid()))
                        .addCriteria(Criteria.where("date").is(dailyAmount.getDate()))
                        .addCriteria(Criteria.where("prodId").is(dailyAmount.getProdId()))
                        .addCriteria(Criteria.where("fundCode").is(dailyAmount.getFundCode()));

                Update update = new Update();
                update.set("asset", amount);
                mongoTemplate.findAndModify(query, update, DailyAmount.class);
            }
        }
    }


}