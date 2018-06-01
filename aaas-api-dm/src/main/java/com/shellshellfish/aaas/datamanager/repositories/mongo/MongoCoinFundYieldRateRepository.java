package com.shellshellfish.aaas.datamanager.repositories.mongo;


import com.shellshellfish.aaas.datamanager.model.CoinFundYieldRate;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoCoinFundYieldRateRepository extends MongoRepository<CoinFundYieldRate, Long> {

    CoinFundYieldRate findFirstByCodeAndQueryDateStrLessThanEqualOrderByQueryDateStrDesc(String code, String
            queryDateStr);

    CoinFundYieldRate findFirstByCodeOrderByQueryDateStr(String code);
}
