package com.shellshellfish.aaas.tools.fundcheck.repositories.mongo;


import com.shellshellfish.aaas.tools.fundcheck.model.FundYearIndicator;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFundYearIndicatorRepository extends MongoRepository<FundYearIndicator, Long> {

	FundYearIndicator getFirstByCodeAndQuerydateBefore(String code, long endtime, Sort sort);


}
