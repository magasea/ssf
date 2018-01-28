package com.shellshellfish.aaas.userinfo.repositories.funds;

import com.shellshellfish.aaas.userinfo.model.dao.FundYieldRate;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MongoFundYieldRateRepository extends MongoRepository<FundYieldRate, Long> {

	FundYieldRate findFirstByCodeAndQueryDateBefore(String code, Long queryDate, Sort sort);
}
