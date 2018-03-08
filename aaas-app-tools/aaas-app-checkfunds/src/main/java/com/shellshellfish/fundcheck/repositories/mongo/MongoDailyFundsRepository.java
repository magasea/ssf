package com.shellshellfish.fundcheck.repositories.mongo;


import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.fundcheck.model.DailyFunds;

@Repository
public interface MongoDailyFundsRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<DailyFunds, Long> {

    
	@Query("{\"code\":{$in:?0}}")
    List<DailyFunds> findByCode(String[] code);

    @Query("{\"code\":?0,\"querydate\" : {\"$gte\":?1, \"$lte\":?2}}")
	List<DailyFunds> findByCodeAndQuerydate(String code,String fromdate,String todate);
    

}
