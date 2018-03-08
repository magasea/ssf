package com.shellshellfish.fundcheck.repositories.mongo;


import com.shellshellfish.fundcheck.model.FundBaseList;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoFundBaseListRepository extends MongoRepository<FundBaseList, Long> {

	FundBaseList findFirstByCode(String code);
}
