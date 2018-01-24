package com.shellshellfish.aaas.common.utils;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import java.math.BigDecimal;
import java.time.Instant;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-24
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class MongoTemplateTest {


	@Autowired
	@Qualifier("secondaryMongoTemplate")
	private MongoTemplate mongoTemplate;


	@Test
	public void testUpsert() {
		DailyAmount dailyAmount = new DailyAmount();
		dailyAmount.setUserProdId(1L);
		dailyAmount.setAsset(BigDecimal.ONE);
		dailyAmount.setBonus(BigDecimal.TEN);
		dailyAmount.setBuyAmount(BigDecimal.ZERO);
		Instant instant = Instant.now();
		dailyAmount.setDate(instant.toString());
		dailyAmount.setProdId(0L);
		dailyAmount.setFundCode("123456");
		dailyAmount.setUserUuid("uuid");
		dailyAmount.setAsset(BigDecimal.TEN);

		mongoTemplate.save(dailyAmount);

		Query query = new Query();
		query.addCriteria(Criteria.where("userProdId").is(1L))
				.addCriteria(Criteria.where("date").is(instant.toString()));

		Update update = new Update();
		update.set("fundCode", "fundCode");

		DailyAmount dailyAmount1 = mongoTemplate.findAndModify(query, update, DailyAmount.class);
		System.out.println(dailyAmount1);

		DailyAmount dailyAmount2 = mongoTemplate.findOne(query, DailyAmount.class);

		Assert.assertEquals("fundCode", dailyAmount2.getFundCode());
		Assert.assertEquals("uuid", dailyAmount2.getUserUuid());
		Assert.assertEquals(BigDecimal.TEN, dailyAmount2.getAsset());
	}

}
