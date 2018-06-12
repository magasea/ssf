package com.shellshellfish.aaas.datacollection.server.repositories;

import com.shellshellfish.aaas.datacollection.server.model.CoinFundYieldRate;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public interface CoinFundYieldrateRepository extends MongoRepository<CoinFundYieldRate, Long> {


    /**
     * @param code
     * @param startDate 开始时间秒数（非毫秒数）
     * @param endDate   结束时间秒数 （非毫秒数）
     * @return
     */

    List<CoinFundYieldRate> findByCodeAndQuerydateBetweenOrderByQuerydate(String code, Long startDate, Long
            endDate);
}
