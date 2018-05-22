package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.dao.DailyAmountAggregation;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author pierre 18-2-24
 */
@Repository
public interface MongoDailyAmountCustom {

    List<DailyAmountAggregation> getUserAssetAndIncome(String date, Long prodId);

}
