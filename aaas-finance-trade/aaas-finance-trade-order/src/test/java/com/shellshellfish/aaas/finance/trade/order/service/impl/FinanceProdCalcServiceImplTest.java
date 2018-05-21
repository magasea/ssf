package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.common.grpc.finance.product.ProductMakeUpInfo;
import com.shellshellfish.aaas.finance.trade.order.model.DistributionResult;
import com.shellshellfish.aaas.finance.trade.order.model.TradeLimitResult;
import com.shellshellfish.aaas.finance.trade.order.service.FinanceProdCalcService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("dev")
@Ignore
public class FinanceProdCalcServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(FinanceProdCalcServiceImplTest.class);

    private List<ProductMakeUpInfo> productMakeUpInfoList;

    @Autowired
    private FundInfoZhongZhengApiService fundInfoService;

    @Autowired
    private FinanceProdCalcService financeProdCalcService;

    @Before
    public void setUp(){
        productMakeUpInfoList = Arrays.asList(
                new ProductMakeUpInfo(100021L, 3L, "", "000614.OF", "华安德国30(DAX)ETF联接(QDIIEnum)", 1000),
                new ProductMakeUpInfo(100021L, 3L, "", "001987.OF", "东方金元宝货币", 3668),
                new ProductMakeUpInfo(100021L, 3L, "", "000216.OF", "华安黄金易ETF联接A", 1000),
                new ProductMakeUpInfo(100021L, 3L, "", "002068.OF", "东方多策略灵活配置混合C", 1000),
                new ProductMakeUpInfo(100021L, 3L, "", "002163.OF", "东方惠新灵活配置混合C",1000),
                new ProductMakeUpInfo(100021L, 3L, "",  "000395.OF", "汇添富安心中国债券A", 1331),
                new ProductMakeUpInfo(100021L, 3L, "", "001490.OF", "汇添富国企创新股票", 1000)
        );
    }

    @Test
    public void testGetMinBuyAmount() throws Exception {
        List<BigDecimal> minAmountList = new ArrayList<>();

        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService.getTradeLimits(info.getFundCode(), "022");
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double minValue = Double.parseDouble(tradeLimitResult.getMinValue());
                minAmountList.add(BigDecimal.valueOf(minValue/(info.getFundShare()/10000d)));
            }
        }
        logger.info("minAmountList: {}", minAmountList);
        BigDecimal amount1 =  Collections.max(minAmountList);
        logger.info("amount1: {}", amount1);

        Date start = new Date();
        BigDecimal amount2 = financeProdCalcService.getMinBuyAmount(productMakeUpInfoList);
        Date end = new Date();
        logger.info("duration: {}", end.getTime() - start.getTime());

        logger.info("amount2: {}", amount2);
        assertEquals(amount1, amount2);
    }

    @Test
    public void testGetMaxBuyAmount() throws Exception {
        List<BigDecimal> maxAmountList = new ArrayList<>();
        for(ProductMakeUpInfo info: productMakeUpInfoList) {
            List<TradeLimitResult> results = fundInfoService.getTradeLimits(info.getFundCode(), "022");
            if (results != null && results.size() > 0) {
                TradeLimitResult tradeLimitResult = results.get(0);
                Double maxValue = Double.parseDouble(tradeLimitResult.getMaxValue());
                maxAmountList.add(BigDecimal.valueOf(maxValue/(info.getFundShare()/10000d)));
            }
        }

        logger.info("maxAmountList: {}", maxAmountList);
        BigDecimal amount1 = Collections.min(maxAmountList);
        logger.info("amount1: {}", amount1);

        Date start = new Date();
        BigDecimal amount2 = financeProdCalcService.getMaxBuyAmount(productMakeUpInfoList);
        Date end = new Date();
        logger.info("duration: {}", end.getTime() - start.getTime());
        logger.info("amount2: {}", amount2);
        assertEquals(amount1, amount2);
    }

    @Test
    public void testGetPoundageOfBuyFund() throws Exception {
        Date start = new Date();
        DistributionResult distributionResult = financeProdCalcService.getPoundageOfBuyFund(BigDecimal.valueOf(5000), productMakeUpInfoList);
        Date end = new Date();
        logger.info("duration: {}", end.getTime() - start.getTime());
        logger.info("distributionResult: {}", new ObjectMapper().writeValueAsString(distributionResult));
    }

    @Test
    public void testGetPoundageOfSellFund() throws Exception {
        Date start = new Date();
        DistributionResult distributionResult = financeProdCalcService.getPoundageOfSellFund(BigDecimal.valueOf(5000), productMakeUpInfoList);
        Date end = new Date();
        logger.info("duration: {}", end.getTime() - start.getTime());
        logger.info("distributionResult: {}", new ObjectMapper().writeValueAsString(distributionResult));
    }

}
