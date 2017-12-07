package com.shellshellfish.aaas.finance.service;

import com.shellshellfish.aaas.finance.returnType.FundAllReturn;
import com.shellshellfish.aaas.finance.returnType.FundReturn;
import com.shellshellfish.aaas.finance.returnType.PerformanceVolatilityReturn;
import com.shellshellfish.aaas.finance.returnType.RevenueContributionReturn;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class AssetAllServiceTest {

    @Autowired
    private AssetAllocationService allocationService;

    @Test
    public void getProduct() {
        FundAllReturn fundAllReturn = allocationService.getProduct();
        assertTrue(fundAllReturn.getName().equals("基金组合"));
    }
    @Test
    public void selectById(){
        FundReturn fundReturn = allocationService.selectById("1","1");
        assertTrue(fundReturn.getName().equals("富国天成红利"));
    }

    @Test
    public void selectReturnAndPullback(){
        Map<String,Object> map = allocationService.selectReturnAndPullback("1","1","1");
        assertTrue(map.get("name").equals("预期年化收益"));
    }
    @Test
    public void getRevenueContribution(){
        RevenueContributionReturn revenueContributionReturn = allocationService.getRevenueContribution("1","1");
        assertTrue(revenueContributionReturn.getName().equals("配置收益贡献"));
    }

    @Test
    public void efficientFrontier(){
        RevenueContributionReturn revenueContributionReturn = allocationService.efficientFrontier("1","2");
        assertTrue(revenueContributionReturn.getName().equals("有效前沿线数据"));
    }

    @Test
    public void getinterval(){
        FundReturn fundReturn = allocationService.getinterval("1","0.13","0.15");
        assertTrue(fundReturn.getMinRiskLevel()==0.1);
    }

    @Test
    public void getRiskController(){
        RevenueContributionReturn revenueContributionReturn = allocationService.getRiskController("1","2");
        assertTrue(revenueContributionReturn.get_items().get(0).get("name").equals("股灾1"));
    }

    @Test
    public void
    getmeansAndNoticesRetrun(){
        RevenueContributionReturn revenueContributionReturn = allocationService.getmeansAndNoticesRetrun("1");
        assertTrue(revenueContributionReturn.getName().equals("风险控制通知"));
    }

    @Test
    public void getPerformanceVolatility() {
        PerformanceVolatilityReturn performanceVolatilityReturn = allocationService.getPerformanceVolatility("1","1","1");
        assertTrue(performanceVolatilityReturn.getName().equals("模拟数据"));
        assertTrue(performanceVolatilityReturn.get_items().get(0).get("name").equals("模拟历史年化业绩"));
    }

    @Test
    public void getScaleMark(){
        RevenueContributionReturn revenueContributionReturn = allocationService.getScaleMark("1","risk");
        assertTrue(revenueContributionReturn.getName().equals("风险率"));
        assertTrue((new Double(revenueContributionReturn.get_items().get(0).get("value").toString())-0.13)==0);
    }
}
