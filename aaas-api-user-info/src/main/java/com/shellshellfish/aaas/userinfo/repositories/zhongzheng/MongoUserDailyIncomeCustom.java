package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.dao.UserDailyIncomeAggregation;
import org.springframework.stereotype.Repository;

/**
 * @Author pierre 18-2-24
 */
@Repository
public interface MongoUserDailyIncomeCustom {

    /**
     * 获取用户累计收益，包含持仓和已经全部赎回的
     *
     * @return
     */
    UserDailyIncomeAggregation getTotalIncome(Long userId);

}
