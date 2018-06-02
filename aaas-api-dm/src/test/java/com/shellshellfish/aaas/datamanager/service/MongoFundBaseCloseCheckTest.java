package com.shellshellfish.aaas.datamanager.service;

import com.shellshellfish.aaas.datamanager.DataManagerServiceApplication;
import com.shellshellfish.aaas.datamanager.service.impl.MongoFundBaseCloseCheck;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @Author pierre.chen
 * @Date 18-6-2
 */

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = DataManagerServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class MongoFundBaseCloseCheckTest {

    @Autowired
    MongoFundBaseCloseCheck mongoFundBaseCloseCheck;

    @Test
    public void testCheck() {
        mongoFundBaseCloseCheck.check();
    }
}
