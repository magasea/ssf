package com.shellshellfish.aaas.assetallocation.neo.web;

import com.shellshellfish.aaas.assetallocation.AssetAllocationApp;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssetAllocationApp.class,webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration
public class FundGroupControllerIT {




    private static final String REQUEST_IS_SUCCESS = "1";


    public int port;

    @Before
    public void setup(){
        RestAssured.port = port;
    }

    @Test
    public void selectAllFundGroup() {

    }

    @Test
    public void getProportionOne() {
    }

    @Test
    public void getFnameAndProportion() {
    }

    @Test
    public void getPerformanceVolatilityHomePage() {
    }

    @Test
    public void selectById() {
    }

    @Test
    public void selectReturnAndPullback() {
    }

    @Test
    public void getRevenueContribution() {
    }

    @Test
    public void efficientFrontier() {
    }

    @Test
    public void getInterval() {
    }

    @Test
    public void getRiskController() {
    }

    @Test
    public void getCustRiskController() {
    }

    @Test
    public void getMeansAndNoticesReturn() {
    }

    @Test
    public void getPerformanceVolatility() {
    }

    @Test
    public void getHistoricalPerformance() {
    }

    @Test
    public void getScaleMark() {
    }

    @Test
    public void getScaleMarkFromChoose() {
    }

    @Test
    public void getFundGroupIncome() {
    }

    @Test
    public void getFundGroupIncomeAll() {
    }

    @Test
    public void getFundGroupIncomeWeek() {
    }

    @Test
    public void getFundNetValue() {
    }

    @Test
    public void getExpectedIncome() {
    }

    @Test
    public void findAllGroupCode() {
    }
}