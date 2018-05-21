package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import org.springframework.stereotype.Repository;

/**
 * @Author pierre 18-2-24
 */
@Repository
public interface MongoDailyAmountCustom {

    DailyAmountAggregation getUserAssetAndIncome(String userUuid, String startDate, String endDate,
                                                 Long prodId);

}
