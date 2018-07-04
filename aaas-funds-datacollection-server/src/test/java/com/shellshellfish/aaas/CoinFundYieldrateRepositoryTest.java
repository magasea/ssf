package com.shellshellfish.aaas;

import com.shellshellfish.aaas.datacollection.server.DataServerServiceApplication;
import com.shellshellfish.aaas.datacollection.server.model.CoinFundYieldRate;
import com.shellshellfish.aaas.datacollection.server.repositories.CoinFundYieldrateRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

import static org.hamcrest.Matchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataServerServiceApplication.class)
@ActiveProfiles("test")
@WebAppConfiguration
public class CoinFundYieldrateRepositoryTest {

    @Autowired
    CoinFundYieldrateRepository coinFundYieldrateRepository;

    @Autowired
    MongoTemplate mongoTemplate;
    @Test
    public void test() {
        String code = "003474.OF";
        Long startDate = 1420992000L;
        Long endDate = System.currentTimeMillis();

        List<CoinFundYieldRate> coinFundYieldRates = coinFundYieldrateRepository.findByCodeAndQuerydateBetweenOrderByQuerydate(code, startDate, endDate);
        Assert.assertNotNull("测试通过", coinFundYieldRates);
        Assert.assertThat(coinFundYieldRates, notNullValue());
    }

}

