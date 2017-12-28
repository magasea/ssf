package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.service.CovarianceCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.DailyFundService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupDataService;
import com.shellshellfish.aaas.assetallocation.neo.util.CalculateMaxdrawdowns;
import com.shellshellfish.aaas.common.utils.DateUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

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

    @Test
   public void test1(){
        fundGroupDataService.insertFundData();
   }


    @Test
    public void test2(){
        fundCalculateService.calculateDataOfWeek();
    }

    @Test
    public void test3(){
        covarianceCalculateService.calculateCovarianceOfWeek();
    }

    //测试每日接口
    @Test
    public void test4(){

       String code="470068.OF";
       String startDate="2017-12-20";
       String endDate="2017-12-26";

        dailyFundService.insertDailyData(code,startDate,endDate);
//        dailyFundService.insertDailyFund();

    }

    @Test
    public void test5(){
        CalculateMaxdrawdowns calculateMaxdrawdowns=new CalculateMaxdrawdowns();
        double[] temp={0.0,1.0,2.0,3,10,7};
        Double value=calculateMaxdrawdowns.calculateMaxdrawdown(temp);

        System.out.println(value);

    }


}
