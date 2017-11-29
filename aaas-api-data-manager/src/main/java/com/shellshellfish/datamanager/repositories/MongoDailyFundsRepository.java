package com.shellshellfish.datamanager.repositories;


import java.math.BigInteger;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.datamanager.model.DailyFunds;
import com.shellshellfish.datamanager.model.FundCodes;

@Repository
public interface MongoDailyFundsRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<DailyFunds, Long> {

    @Query("{\"code\":{$in:?0},\"querydate\":?1}")
    List<DailyFunds> findByCodeAndDate(String[] code,String date);

	List<DailyFunds> findAll();
    

}
