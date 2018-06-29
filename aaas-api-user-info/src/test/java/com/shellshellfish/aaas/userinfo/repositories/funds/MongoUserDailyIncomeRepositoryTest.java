package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.UserDailyIncomeAggregation;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoUserDailyIncomeRepository;
import javafx.scene.layout.BackgroundImage;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongoUserDailyIncomeRepositoryTest {

    @Autowired
    MongoUserDailyIncomeRepository mongoUserDailyIncomeRepository;

    @Test
    public void testGetTotalIncome() {
        Long userId = 5628L;
        UserDailyIncomeAggregation aggregation = mongoUserDailyIncomeRepository
                .getTotalIncome(userId);

        Assert.assertNotNull(aggregation);
        Assert.assertNotNull(aggregation.getTotalIncome());
        Assert.assertEquals("日期不正确", aggregation.getDate(), LocalDate.now().plusDays(-1).toString());
        Assert.assertTrue(BigDecimal.ZERO.compareTo(aggregation.getTotalIncome()) != 0);

    }

}


