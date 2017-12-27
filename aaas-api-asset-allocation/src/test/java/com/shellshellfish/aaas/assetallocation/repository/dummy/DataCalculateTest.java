package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.service.CovarianceCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.DailyFundService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundCalculateService;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupDataService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: yongquan.xiong
 * Date: 2017/12/25
 * Desc:
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("test")
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
        dailyFundService.insertDailyFund();
    }

    @Test
    public void test5(){
        List<String> listCode=new ArrayList<>();
        listCode.add("002068.OF");
        String sDate="2015-01-01";
        String eDate="2017-12-31";

    }


}
