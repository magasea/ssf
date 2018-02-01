package com.shellshellfish.aaas.userinfo.repositories.funds;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
		Long startDate = 0L;
		Long endDate = 0L;
		Integer prodId = 46;

		Aggregation agg = newAggregation(

				match(Criteria.where("userUuid").is(userUuid)),
				match(Criteria.where("date").is("20180131")),
				match(Criteria.where("userProdId").is(prodId)),
				group("userProdId")
						.first("userProdId").as("userProdId")
						.sum("sellAmount").as("sellAmount")
						.sum("asset").as("asset")
						.sum("bonus").as("bonus")
						.sum("buyAmount").as("buyAmount")
		);
		DailyAmountAggregation results = zhongZhengMongoTemplate
				.aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getUniqueMappedResult();

		System.out.println(results);
	}

}


