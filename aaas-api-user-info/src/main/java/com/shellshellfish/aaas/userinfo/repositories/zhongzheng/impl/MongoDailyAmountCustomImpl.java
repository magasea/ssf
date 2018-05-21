package com.shellshellfish.aaas.userinfo.repositories.zhongzheng.impl;

import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.group;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.match;
import static org.springframework.data.mongodb.core.aggregation.Aggregation.newAggregation;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public class MongoDailyAmountCustomImpl implements MongoDailyAmountCustom {

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    private MongoTemplate zhongZhengMongoTemplate;


    @Override
    public DailyAmountAggregation getUserAssetAndIncome(String userUuid, String startDate, String endDate, Long prodId) {
        Aggregation agg = newAggregation(
                match(Criteria.where("userUuid").is(userUuid)),
                match(Criteria.where("date").gte(startDate).lte(endDate)),
                match(Criteria.where("userProdId").is(prodId)),
                group("userProdId")
                        .sum("sellAmount").as("sellAmount")
                        .sum("asset").as("asset")
                        .sum("bonus").as("bonus")
                        .sum("buyAmount").as("buyAmount")
        );
        return zhongZhengMongoTemplate
                .aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getUniqueMappedResult();

    }
}
