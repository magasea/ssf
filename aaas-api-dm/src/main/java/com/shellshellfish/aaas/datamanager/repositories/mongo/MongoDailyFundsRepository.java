package com.shellshellfish.aaas.datamanager.repositories.mongo;


import com.shellshellfish.aaas.datamanager.model.DailyFunds;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoDailyFundsRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<DailyFunds, Long> {

    
	@Query("{\"code\":{$in:?0}}")
    List<DailyFunds> findByCode(String[] code);

    @Query("{\"code\":?0,\"querydate\" : {\"$gte\":?1, \"$lte\":?2}}")
	List<DailyFunds> findByCodeAndQuerydate(String code,String fromdate,String todate);
    

}
