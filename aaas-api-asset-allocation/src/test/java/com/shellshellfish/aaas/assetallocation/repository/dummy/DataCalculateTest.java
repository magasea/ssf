package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.service.CovarianceCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.DailyFundService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupDataService;
import com.shellshellfish.aaas.assetallocation.neo.util.CalculateMaxdrawdowns;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/12/25
 * Desc:
 */
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringBootTest
//@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class DataCalculateTest {

    @Autowired
    private  FundGroupDataService fundGroupDataService;
    @Autowired
    private  FundCalculateService fundCalculateService;
    @Autowired
    private  CovarianceCalculateService covarianceCalculateService;
    @Autowired
    private DailyFundService dailyFundService;

    // 测试每日接口
    /*
     * 获取基金每日数据并insert into：fund_net_value 以及 fund_basic
     *
     */
    @Test
    public void insertDailyFundTest() {
        long start = System.currentTimeMillis();
        System.out.println("start: " + start);
//        String code="000905SH";
//        String startDate="2017-12-20";
//        String endDate="2018-01-12";
//        dailyFundService.insertDailyData(code,startDate,endDate);

        dailyFundService.insertDailyFund();

        long end = System.currentTimeMillis();
        long elapse = end - start;
        System.out.println("end: " + end);
        System.out.println("elapse: " + elapse);
    }

    /*
     * 计算每周的收益率以及风险率,insert into table:fund_calculate_data_week
     */
    @Test
    public void calculateDataOfWeekTest() {
        long start = System.currentTimeMillis();
        System.out.println("start: " + start);

        fundCalculateService.calculateDataOfWeek();

        long end = System.currentTimeMillis();
        long elapse = end - start;
        System.out.println("end: " + end);
        System.out.println("elapse: " + elapse);
    }

    @Test
    public void insertFundGroupDataTest() {
        long start = System.currentTimeMillis();
        System.out.println("start: " + start);

        fundGroupDataService.insertFundGroupData();

        long end = System.currentTimeMillis();
        long elapse = end - start;
        System.out.println("end: " + end);
        System.out.println("elapse: " + elapse);
    }

    @Test
    public void calculateCovarianceOfWeekTest() {
        covarianceCalculateService.calculateCovarianceOfWeek();
    }

    @Test
    public void calculateMaxdrawdownTest() {
        double[] netValueArr = {0.0,1.0,2.0,3,10,7};
        Double maxdrawdown = CalculateMaxdrawdowns.calculateMaxdrawdown(netValueArr);
        System.out.println(maxdrawdown);
    }

    @Test
    public void test6() {
        long start = System.currentTimeMillis();
        System.out.println("start: " + start);

        List<String> codeList=new ArrayList<>();
//        codeList.add("000366.OF");
//        codeList.add("000406.OF");
//        codeList.add("400013.OF");
//        codeList.add("000217.OF");
//        codeList.add("000614.OF");
//        codeList.add("001541.OF");
//        codeList.add("000696.OF");
//        codeList.add("000248.OF");

        codeList.add("000696.OF"); // 000696.OF	汇添富环保行业股票型证券投资基金	股票型基金	普通股票型基金
        codeList.add("000395.OF"); // 000395.OF	汇添富安心中国债券型证券投资基金	债券型基金	长期纯债型基金
        String strDay = "2018-01-31";
        fundGroupDataService.insertFundGroupDatas(25, codeList, strDay);

        long end = System.currentTimeMillis();
        long elapse = end - start;
        System.out.println("end: " + end);
        System.out.println("elapse: " + elapse);
    }

}
