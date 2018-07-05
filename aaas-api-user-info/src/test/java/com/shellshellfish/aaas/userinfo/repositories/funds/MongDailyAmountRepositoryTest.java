package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author pierre 18-1-12
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class MongDailyAmountRepositoryTest {

	@Autowired
	@Qualifier("zhongZhengMongoTemplate")
	private MongoTemplate zhongZhengMongoTemplate;

	/**
	 * 聚合使用 统计每个用户的文章数量
	 */
	@Test
	public void aggregation() {

		String userUuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String startDate = "20180123";
		String endDate = "20180201";

		Aggregation agg = newAggregation(
				match(Criteria.where("userUuid").is(userUuid)),
				match(Criteria.where("date").gte(startDate).lte(endDate)),
				match(Criteria.where("userProdId").is(46)),
				group("userProdId")
						.sum("sellAmount").as("sellAmount")
						.sum("asset").as("asset")
						.sum("bonus").as("bonus")
						.sum("buyAmount").as("buyAmount")
		);
		DailyAmountAggregation dailyAmountAggregation = zhongZhengMongoTemplate
				.aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getUniqueMappedResult();

		System.out.println(dailyAmountAggregation);
	}

}


