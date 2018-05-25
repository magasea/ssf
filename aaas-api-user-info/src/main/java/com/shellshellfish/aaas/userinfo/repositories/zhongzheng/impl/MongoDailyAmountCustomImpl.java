package com.shellshellfish.aaas.userinfo.repositories.zhongzheng.impl;

import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoDailyAmountCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import java.util.List;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public class MongoDailyAmountCustomImpl implements MongoDailyAmountCustom {

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    private MongoTemplate zhongZhengMongoTemplate;


    @Override
    public List<DailyAmountAggregation> getUserAssetAndIncome(String date, Long prodId) {
        Aggregation agg = newAggregation(
                match(Criteria.where("date").lte(date)),
                match(Criteria.where("userProdId").is(prodId)),
                group("userProdId", "date")
                        .first("date").as("date")
                        .sum("asset").as("asset")
                , sort(Sort.Direction.DESC, "date")
                , limit(2));
        return zhongZhengMongoTemplate
                .aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getMappedResults();

    }

    @Override
    public List<DailyAmountAggregation> getUserAssetAndIncomeByCode(String date, Long prodId, String fundCode) {
        Aggregation agg = newAggregation(
                match(Criteria.where("date").lte(date)),
                match(Criteria.where("userProdId").is(prodId)),
                match(Criteria.where("fundCode").is(fundCode)),
                group("userProdId", "date")
                        .first("date").as("date")
                        .sum("asset").as("asset")
                , sort(Sort.Direction.DESC, "date")
                , limit(2));
        return zhongZhengMongoTemplate
                .aggregate(agg, "dailyAmount", DailyAmountAggregation.class).getMappedResults();

    }

}
