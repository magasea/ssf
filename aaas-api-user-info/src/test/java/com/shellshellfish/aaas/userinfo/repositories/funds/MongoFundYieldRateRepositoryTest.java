package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongoFundYieldRateRepositoryTest {

	@Autowired
	MongoFundYieldRateRepository mongoFundYieldRateRepository;


	@Test
	public void getFundYieldRateTest() {
		String fundCode = "000614.OF";

		FundYieldRate fundYieldRate = mongoFundYieldRateRepository
				.findFirstByCodeAndQueryDateBefore(fundCode,
						InstantDateUtil.getEpochSecondOfZero("20180126", "yyyyMMdd"),
						new Sort(new Order(Direction.DESC, "querydate")));
		Assert.assertEquals(1.297, fundYieldRate.getUnitNav().doubleValue(), 0.000);
	}
}


