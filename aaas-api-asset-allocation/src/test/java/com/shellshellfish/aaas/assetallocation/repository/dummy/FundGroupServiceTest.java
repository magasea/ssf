package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.service.FundGroupService;
import com.shellshellfish.aaas.assetallocation.neo.util.MVO;
import com.shellshellfish.aaas.assetallocation.service.impl.FinanceProductServiceImpl;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FundGroupServiceTest {

    @Autowired
    private FundGroupService fundGroupService;
    @Autowired
    private FinanceProductServiceImpl financeProductService;

    @Test
    public void dateTest() throws ParseException {
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        //ca.setTime(new Date()); //设置时间为当前时间2017-12-09
        ca.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016-12-31"));
        //ca.add(Calendar.YEAR, -1); //年份减1
        ca.add(Calendar.MONTH, -1);//求前一月
        //ca.add(Calendar.DATE, +1);//前一天
        Date lastMonth = ca.getTime(); //结果
        //System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(lastMonth));//2017-11-10
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(lastMonth));//2016-11-30

        ca.setTime(new Date());
        ca.set(Calendar.DAY_OF_MONTH, 1);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));
        ca.setTime(new SimpleDateFormat("yyyy-MM-dd").parse("2016-01-31"));
        ca.add(Calendar.MONTH, 1);
        ca.add(Calendar.DAY_OF_MONTH, -1);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd").format(ca.getTime()));

        double d = 0.2;
        double c = 0.4;
        double a = d/c;
        System.out.println(a);
    }

    @Test
    public void aaTest() {
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {
                {0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};

        Double LOW_BOUND = 0.05; // 调用 MVO 权重 下限
        Double UP_BOUND = 0.95; // 调用 MVO 权重 上限
        for (int i = 0; i < 10000; i++){
            System.out.println(i);
            MVO.efficientFrontier(ExpReturn, ExpCovariance, 10, LOW_BOUND, UP_BOUND);
        }
    }

    @Test
    public void getProductTest() {
        FundAllReturn fundAllReturn = fundGroupService.selectAllFundGroup();
    }

    @Test
    public void selectByIdTest() {
        FundReturn fundReturn = fundGroupService.selectById("2", "2000");
    }

    @Test
    public void selectReturnAndPullbackTest() {
        Map<String, Object> map = fundGroupService.selectReturnAndPullback("1", "1", "1");
    }

    @Test
    public void getRevenueContributionTest() {
        ReturnType revenueContributionReturn = fundGroupService.getRevenueContribution("1", "1");
    }

    @Test
    public void efficientFrontierTest() {
        ReturnType revenueContributionReturn = fundGroupService.efficientFrontier("1");
    }

    @Test
    public void getIntervalTest() {
        FundReturn fundReturn = fundGroupService.getInterval("1", "0.13", "0.15");
    }

    @Test
    public void getRiskControllerTest() {
        ReturnType revenueContributionReturn = fundGroupService.getRiskController("1", "2");
    }

    @Test
    public void getmeansAndNoticesRetrunTest() {
        ReturnType revenueContributionReturn = fundGroupService.getMeansAndNoticesReturn();
    }

    @Test
    public void getPerformanceVolatilityTest() {
        PerformanceVolatilityReturn performanceVolatilityReturn = fundGroupService.getPerformanceVolatility( "C1", "1");
    }

    @Test
    public void getScaleMarkTest() {
        ReturnType revenueContributionReturn = fundGroupService.getScaleMark("1", "risk");
    }

    @Test
    public void getFundGroupIncomeTest() throws ParseException {
        ReturnType e = fundGroupService.getFundGroupIncomeAll("14", "140049", "income");
        System.out.println(e);
    }

    @Test
    public void financeProductServiceTest() {
        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        productBaseInfo.setProdId(2L);
        productBaseInfo.setGroupId(2000L);
        List<ProductMakeUpInfo> productInfos = financeProductService.getProductInfo(productBaseInfo);
    }

    @Test
    public void getAllIdAndSubIdTest() {
        fundGroupService.getAllIdAndSubId();
    }

    @Test
    public void getNavadjTest() {
        fundGroupService.getNavadj("1","1000");
    }

    @Test
    public void updateExpectedMaxRetracementTest() {
        fundGroupService.updateExpectedMaxRetracement("1","1000");
    }

    @Test
    public void maximumLossesTest() {
        fundGroupService.maximumLosses("1","1000");
    }

    @Test
    public void getNavadjBenchmarkTest() {
        fundGroupService.getNavadjBenchmark("C1");
        fundGroupService.getNavadjBenchmark("C2");
        fundGroupService.getNavadjBenchmark("C3");
        fundGroupService.getNavadjBenchmark("C4");
        fundGroupService.getNavadjBenchmark("C5");
    }

    @Test
    public void sharpeRatioTest() {
        int effectRow = fundGroupService.sharpeRatio("3","30089");
        System.out.println(effectRow);
    }

    @Test
    public void contributionTest() {
        fundGroupService.contribution();
    }

    @Test
    public void getCustRiskByGroupIdTest() {
        String groupId = "5";
        String riskLevel = fundGroupService.getCustRiskByGroupId(groupId);
        System.out.println(riskLevel);
    }
}
