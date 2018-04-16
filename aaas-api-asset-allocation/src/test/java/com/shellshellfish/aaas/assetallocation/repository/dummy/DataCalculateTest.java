package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.service.impl.CovarianceCalculateService;
import com.shellshellfish.aaas.assetallocation.service.impl.DailyFundService;
import com.shellshellfish.aaas.assetallocation.service.impl.FundCalculateService;
import com.shellshellfish.aaas.assetallocation.service.impl.FundGroupDataService;
import com.shellshellfish.aaas.assetallocation.util.CalculateMaxdrawdowns;
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
    public void insertFundGroupDatasTest() {
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

//        codeList.add("000366.OF");
//        codeList.add("400009.OF");
//        codeList.add("400016.OF");
//        codeList.add("400020.OF");
//        codeList.add("400023.OF");
//        codeList.add("000149.OF");
//        String strDay = "2018-01-31";
//        fundGroupDataService.insertFundGroupDatas(1, codeList, strDay);

//        codeList.add("400009.OF");
//        codeList.add("400016.OF");
//        codeList.add("400020.OF");
//        codeList.add("400023.OF");
//        codeList.add("000406.OF");
//        codeList.add("000217.OF");
//        codeList.add("000312.OF");
//        String strDay = "2018-01-31";
//        fundGroupDataService.insertFundGroupDatas(6, codeList, strDay);

//        codeList.add("000366.OF");
//        codeList.add("400009.OF");
//        codeList.add("000406.OF");
//        codeList.add("001495.OF");
//        codeList.add("000312.OF");
//        codeList.add("000614.OF");
//        codeList.add("001541.OF");
//        codeList.add("001694.OF");
//        String strDay = "2018-01-31";
//        fundGroupDataService.insertFundGroupDatas(9, codeList, strDay);

//        codeList.add("000905.SH");
//        codeList.add("000906.SH");
//        codeList.add("H11001.CSI");
//        String strDay = "2018-01-31";
//        fundGroupDataService.insertFundGroupDatas(0, codeList, strDay);

        long end = System.currentTimeMillis();
        long elapse = end - start;
        System.out.println("end: " + end);
        System.out.println("elapse: " + elapse);
    }

}
