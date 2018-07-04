package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.userinfo.model.DailyAmount;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * @Author pierre 18-2-24
 */
public interface MongoDailyAmountRepository extends MongoRepository<DailyAmount, Long>, MongoDailyAmountCustom {

    List<DailyAmount> findAllByUserProdIdAndFundCode(Long userProdId, String fundCode, Sort sort);

    DailyAmount findFirstByUserProdIdOrderByDateDesc(Long userProdId);

}
