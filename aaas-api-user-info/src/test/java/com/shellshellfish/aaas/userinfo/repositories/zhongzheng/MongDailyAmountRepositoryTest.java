package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static com.shellshellfish.aaas.common.utils.InstantDateUtil.yyyyMMdd;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongDailyAmountRepositoryTest {

    @Autowired
    MongoDailyAmountRepository mongoDailyAmountRepository;


    @Test
    public void test() {
        Long userProdId = 12L;
        String fundCode = "001987.OF";
        List<DailyAmount> dailyAmountList = mongoDailyAmountRepository
                .findAllByUserProdIdAndFundCode(userProdId, fundCode, new Sort(
                        Direction.DESC, "date"));
        Assert.assertNotNull("result is null", dailyAmountList);
        Assert.assertNotNull("index 0 is null", dailyAmountList.get(0));
    }

    @Test
    public void testFindFirstByUserProdIdOrderByDateDesc() {

        Long userProdId = 2L;
        DailyAmount dailyAmount = mongoDailyAmountRepository.findFirstByUserProdIdOrderByDateDesc(userProdId);
        Assert.assertNotNull("result is null ", dailyAmount);
        System.out.println(dailyAmount);
        Assert.assertTrue("date of data it too early", InstantDateUtil.format(dailyAmount.getDate(), "yyyyMMdd")
                .isAfter(InstantDateUtil.now().plusDays(-2)));
    }

    @Test
    public void testGetUserAssetAndIncome() {

        Long userProdId = 181L;
        List<DailyAmountAggregation> amountAggregation = mongoDailyAmountRepository.getUserAssetAndIncome
                (InstantDateUtil.format(InstantDateUtil.now(), yyyyMMdd), userProdId);
        Assert.assertNotNull("", amountAggregation);
        Assert.assertTrue("总资产大于零", amountAggregation.get(0).getAsset().compareTo(BigDecimal.ZERO) >= 0);
    }

}


