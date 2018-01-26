package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
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
@DataMongoTest
public class MongoFundYieldRateRepositoryTest {

	@Autowired
	MongoFundYieldRateRepository mongoFundYieldRateRepository;


	@Test
	public void deleteByCardNumberTest() {
		String fundCode = "000614.OF";

		FundYieldRate fundYieldRate = mongoFundYieldRateRepository
				.findFirstByCodeAndQueryDateBefore(fundCode,
						InstantDateUtil.getEpochSecondOfZero("20150106", "yyyyMMdd"),
						new Sort(new Order(Direction.DESC, "querydate")));
		Assert.assertEquals(0.947D, fundYieldRate.getUnitNav());
	}
}


