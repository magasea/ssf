package com.shellshellfish.aaas.userinfo.repositories.zhongzheng.impl;

import com.shellshellfish.aaas.userinfo.model.dao.UserDailyIncomeAggregation;
import com.shellshellfish.aaas.userinfo.repositories.zhongzheng.MongoUserDailyIncomeCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.query.Criteria;

import static org.springframework.data.mongodb.core.aggregation.Aggregation.*;

/**
 * @Author pierre.chen
 * @Date 18-5-21
 */
public class MongoUserDailyIncomeCustomImpl implements MongoUserDailyIncomeCustom {

    @Autowired
    @Qualifier("zhongZhengMongoTemplate")
    private MongoTemplate zhongZhengMongoTemplate;

    private static final String TABLE_NAME = "user_daily_income";

    private static final String CREATE_DATE_STR = "createDateStr";
    private static final String USER_ID = "userId";
    private static final String CREATE_DATE = "createDate";
    private static final String UPDATE_DATE = "updateDate";
    private static final String DAILY_INCOME = "dailyIncome";
    private static final String ACCUMULATIVE_INCOME = "accumulativeIncome";


    @Override
    public UserDailyIncomeAggregation getTotalIncome(Long userId) {
        Aggregation agg = newAggregation(
                match(Criteria.where(USER_ID).is(userId)),
                group(CREATE_DATE_STR)
                        .first(CREATE_DATE_STR).as("date")
                        .sum(ACCUMULATIVE_INCOME).as("totalIncome")
                , sort(Sort.Direction.DESC, "date")
                , limit(1));
        return zhongZhengMongoTemplate
                .aggregate(agg, TABLE_NAME, UserDailyIncomeAggregation.class).getUniqueMappedResult();
    }
}
