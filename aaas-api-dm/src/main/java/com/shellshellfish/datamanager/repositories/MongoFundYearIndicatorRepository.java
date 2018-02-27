package com.shellshellfish.datamanager.repositories;


import com.shellshellfish.datamanager.model.FundYearIndicator;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFundYearIndicatorRepository extends MongoRepository<FundYearIndicator, Long> {

	FundYearIndicator getFirstByCodeAndQuerydateBefore(String code, long endtime, Sort sort);


}
