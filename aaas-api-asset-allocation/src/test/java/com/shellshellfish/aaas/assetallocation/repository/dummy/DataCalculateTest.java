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
    public void insertDailyFundTest(){
//        String code="000905SH";
//        String startDate="2017-12-20";
//        String endDate="2018-01-12";
//        dailyFundService.insertDailyData(code,startDate,endDate);

        dailyFundService.insertDailyFund();
    }

    /*
     * 计算每周的收益率以及风险率,insert into table:fund_calculate_data_week
     */
    @Test
    public void calculateDataOfWeekTest(){
        fundCalculateService.calculateDataOfWeek();
    }

    @Test
    public void insertFundGroupDataTest(){
        fundGroupDataService.insertFundGroupData();
    }

    @Test
    public void calculateCovarianceOfWeekTest(){
        covarianceCalculateService.calculateCovarianceOfWeek();
    }

    @Test
    public void calculateMaxdrawdownTest(){
        double[] netValueArr = {0.0,1.0,2.0,3,10,7};
        Double maxdrawdown = CalculateMaxdrawdowns.calculateMaxdrawdown(netValueArr);
        System.out.println(maxdrawdown);
    }

}
