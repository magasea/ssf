package com.shellshellfish.aaas.assetallocation.repository.dummy;

import com.shellshellfish.aaas.assetallocation.neo.returnType.*;
import com.shellshellfish.aaas.assetallocation.neo.secvice.FundGroupService;
import com.shellshellfish.aaas.assetallocation.service.impl.FinanceProductServiceImpl;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductBaseInfo;
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
    }

    @Test
    public void getProduct() {
        FundAllReturn fundAllReturn = allocationService.selectAllFundGroup();
        assertTrue(fundAllReturn.getName().equals("基金组合"));
    }

    @Test
    public void selectById() {
        FundReturn fundReturn = allocationService.selectById("1", "1");
        assertTrue(fundReturn.getName().equals("富国天成红利"));
    }

    @Test
    public void selectReturnAndPullback() {
        Map<String, Object> map = allocationService.selectReturnAndPullback("1", "1", "1");
        assertTrue(map.get("name").equals("预期年化收益"));
    }

    @Test
    public void getRevenueContribution() {
        ReturnType revenueContributionReturn = allocationService.getRevenueContribution("1", "1");
        assertTrue(revenueContributionReturn.getName().equals("配置收益贡献"));
    }

    @Test
    public void efficientFrontier() {
        ReturnType revenueContributionReturn = allocationService.efficientFrontier("1", "2");
        assertTrue(revenueContributionReturn.getName().equals("有效前沿线数据"));
    }

    @Test
    public void getinterval() {
        FundReturn fundReturn = allocationService.getinterval("1", "0.13", "0.15");
        assertTrue(fundReturn.getMinRiskLevel() == 0.1);
    }

    @Test
    public void getRiskController() {
        ReturnType revenueContributionReturn = allocationService.getRiskController("1", "2");
        assertTrue(revenueContributionReturn.get_items().get(0).get("name").equals("股灾1"));
    }

    @Test
    public void getmeansAndNoticesRetrun() {
        ReturnType revenueContributionReturn = allocationService.getmeansAndNoticesRetrun();
        assertTrue(revenueContributionReturn.getName().equals("风险控制通知"));
    }

    @Test
    public void getPerformanceVolatility() {
        PerformanceVolatilityReturn performanceVolatilityReturn = allocationService.getPerformanceVolatility( "C1", "1");
        assertTrue(performanceVolatilityReturn.getName().equals("模拟数据"));
        assertTrue(performanceVolatilityReturn.get_items().get(0).get("name").equals("模拟历史年化业绩"));
    }

    @Test
    public void getScaleMark() {
        ReturnType revenueContributionReturn = allocationService.getScaleMark("1", "risk");
        assertTrue(revenueContributionReturn.getName().equals("风险率"));
        assertTrue((new Double(revenueContributionReturn.get_items().get(0).get("value").toString()) - 0.13) == 0);
    }

    @Test
    public void getFundGroupIncome() throws ParseException {
        ReturnType d = allocationService.getFundGroupIncome("1","1",-1);
        System.out.println(d);
    }

    @Test
    public void financeProductService(){
        ProductBaseInfo productBaseInfo = new ProductBaseInfo();
        financeProductService.getProductInfo(productBaseInfo);
    }
}
