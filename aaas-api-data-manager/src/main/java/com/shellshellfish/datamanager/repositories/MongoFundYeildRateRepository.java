package com.shellshellfish.datamanager.repositories;

import com.shellshellfish.datamanager.model.FundYeildRate;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public interface MongoFundYeildRateRepository extends MongoRepository<FundYeildRate, Long> {

  List<FundYeildRate> findByQuerydateAndCodeIsIn(Long queryDate, List<String> codes);

  List<FundYeildRate> findByCodeIsIn(List<String> codes);
}
