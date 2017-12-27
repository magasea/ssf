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

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
public class FundGroupServiceTest {

    @Autowired
    private FundGroupService allocationService;
    @Autowired
    private FinanceProductServiceImpl financeProductService;

    @Test
    public void date() throws ParseException {
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
    public void aa() {
        Double [] ExpReturn = { 0.0054, 0.0531, 0.0779, 0.0934, 0.0130 };
        Double[][] ExpCovariance = {{0.0569,  0.0092,  0.0039,  0.0070,  0.0022},
                {0.0092,  0.0380,  0.0035,  0.0197,  0.0028},
                {0.0039,  0.0035,  0.0997,  0.0100,  0.0070},
                {0.0070,  0.0197,  0.0100,  0.0461,  0.0050},
                {0.0022,  0.0028,  0.0070,  0.0050,  0.0573}};
        for (int i = 0;i<10000;i++){
            System.out.println(i);
            MVO.efficientFrontier(ExpReturn, ExpCovariance, 10);
        }
    }

    @Test
    public void getProduct() {
        FundAllReturn fundAllReturn = allocationService.selectAllFundGroup();
    }

    @Test
    public void selectById() {
        FundReturn fundReturn = allocationService.selectById("2", "2000");
    }

    @Test
    public void selectReturnAndPullback() {
        Map<String, Object> map = allocationService.selectReturnAndPullback("1", "1", "1");
    }

    @Test
    public void getRevenueContribution() {
        ReturnType revenueContributionReturn = allocationService.getRevenueContribution("1", "1");
    }

    @Test
    public void efficientFrontier() {
        ReturnType revenueContributionReturn = allocationService.efficientFrontier("1");
    }

    @Test
    public void getinterval() {
        FundReturn fundReturn = allocationService.getinterval("1", "0.13", "0.15");
    }

    @Test
    public void getRiskController() {
        ReturnType revenueContributionReturn = allocationService.getRiskController("1", "2");
    }

    @Test
    public void getmeansAndNoticesRetrun() {
        ReturnType revenueContributionReturn = allocationService.getmeansAndNoticesRetrun();
    }

    @Test
    public void getPerformanceVolatility() {
        PerformanceVolatilityReturn performanceVolatilityReturn = allocationService.getPerformanceVolatility( "C1", "1");
    }

    @Test
    public void getScaleMark() {
        ReturnType revenueContributionReturn = allocationService.getScaleMark("1", "risk");
    }

    @Test
    public void getFundGroupIncome() throws ParseException {
        ReturnType d = allocationService.getFundGroupIncome("1","1",-1,"income");
        System.out.println(d);
    }

    @Test
    public void financeProductService(){
        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        productBaseInfo.setProdId(2L);
        productBaseInfo.setGroupId(2000L);
        List<ProductMakeUpInfo> a = financeProductService.getProductInfo(productBaseInfo);
    }

    @Test
    public void getNavadj(){
        //allocationService.getAllIdAndSubId();
        allocationService.getNavadj("2","2002");
    }

    /*@Test
    public void getNavadjBenchmark(){
        allocationService.getNavadjBenchmark("C1");
        allocationService.getNavadjBenchmark("C2");
        allocationService.getNavadjBenchmark("C3");
        allocationService.getNavadjBenchmark("C4");
        allocationService.getNavadjBenchmark("C5");
    }*/

    /*@Test
    public void sharpeRatio(){
        int i = allocationService.sharpeRatio("3","30089");
        System.out.println(i);
    }*/

    /*@Test
    public void contribution(){
        allocationService.contribution();
    }*/
}
