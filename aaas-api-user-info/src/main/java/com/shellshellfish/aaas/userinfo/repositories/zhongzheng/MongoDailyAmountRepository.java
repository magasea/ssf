package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.DailyAmount;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @Author pierre 18-2-24
 */
public interface MongoDailyAmountRepository extends MongoRepository<DailyAmount, Long> {

    List<DailyAmount> findAllByUserProdIdAndFundCode(Long userProdId, String fundCode, Sort sort);

    DailyAmount findFirstByUserProdIdOrderByDateDesc(Long userProdId);

}
