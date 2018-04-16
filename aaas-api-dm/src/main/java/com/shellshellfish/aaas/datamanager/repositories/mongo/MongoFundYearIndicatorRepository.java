package com.shellshellfish.aaas.datamanager.repositories.mongo;


import com.shellshellfish.aaas.datamanager.model.FundYearIndicator;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFundYearIndicatorRepository extends MongoRepository<FundYearIndicator, Long> {

	FundYearIndicator getFirstByCodeAndQuerydateBefore(String code, long endtime, Sort sort);


}
