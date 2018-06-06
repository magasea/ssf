package com.shellshellfish.aaas.userinfo.dao.service.impl;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.project;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoClient;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import com.shellshellfish.aaas.userinfo.model.FundNet;
import com.shellshellfish.aaas.userinfo.service.impl.OneFundApiService;

import java.time.LocalDate;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.GroupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-24
 */

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class OneFundApiServiceTest {

    @Autowired
    OneFundApiService oneFundApiService;
    @Value("${spring.data.mongodb.host}")
    String host;

    @Value("${spring.data.mongodb.port}")
    int port;

    @Value("${spring.data.mongodb.database}")
    String database;

    @Test
    public void testGetAllNet() {
        String fundCode = "000614.OF";
        Integer startIndex = 0;
        Integer count = 1;

        try {
            List<FundNet> result = oneFundApiService.getFundNets(fundCode, startIndex, count);
            System.out.println(result.get(0));
            Assert.assertNotNull(result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetNet() {
        String fundCode = "004568.OF";

        try {
            FundNet result = oneFundApiService.getFundNet(fundCode, LocalDate.of(2017, 10, 8));
            FundNet result2 = oneFundApiService.getFundNet(fundCode, LocalDate.now().plusDays(-4L));
            System.out.println(result);
            System.out.println(result2);
            Assert.assertEquals(result.getUnitNet(), result2.getUnitNet());
            Assert.assertEquals(result.getTradeDate(), result2.getTradeDate());
            Assert.assertEquals("20180119", result.getTradeDate());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetAllConfirmList() {
        String fundCode = "000149.OF";
        String openId = TradeUtil.getZZOpenId("370181199001206536");
        try {
            List<ConfirmResult> result = oneFundApiService
                    .getConfirmResults(openId, fundCode, "20180123");
            Assert.assertNotNull(result);
            System.out.println(result);
            for (ConfirmResult confirm : result) {
                Assert.assertEquals("20180123", confirm.getConfirmdate());
            }

            List<ConfirmResult> result2 = oneFundApiService
                    .getConfirmResults(openId, fundCode, "20180124");
            Assert.assertNotNull(result2);
            System.out.println(result2);
            for (ConfirmResult confirm : result2) {
                Assert.assertEquals("20180124", confirm.getConfirmdate());
                System.out.println(confirm);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAggregationMongo() {

        MongoTemplate mongoTemplate = new MongoTemplate(new MongoClient(host, port),
                "zhongzheng");
        MatchOperation matchStage = match(new Criteria("asset").exists(true));
        ProjectionOperation projectStage = project("asset");
        Aggregation aggregation
                = Aggregation.newAggregation(projectStage);

        AggregationResults<DailyAmount> output
                = mongoTemplate.aggregate(aggregation, "asset", DailyAmount.class);
//		System.out.println(output.getUniqueMappedResult().getAsset());


        MatchOperation match = new MatchOperation(Criteria.where("asset").exists(true));
        GroupOperation group = Aggregation.group("userUuid").sum("asset").as("sum");

        Aggregation aggregate = Aggregation.newAggregation(match, group);

        AggregationResults<DailyAmount> orderAggregate = mongoTemplate.aggregate(aggregate,
                "dailyAmount", DailyAmount.class);

        if (orderAggregate != null) {
            System.out.println("Output ====>" + orderAggregate.getRawResults().get("result"));
            System.out.println("Output ====>" + orderAggregate.getRawResults().toJson());
        }

    }

    private GroupOperation getGroupOperation() {
        return group("asset")

                .sum("asset").as("totalRevenue");
    }

    private ProjectionOperation getProjectOperation() {
        return project("asset", "totalRevenue");
    }


}
