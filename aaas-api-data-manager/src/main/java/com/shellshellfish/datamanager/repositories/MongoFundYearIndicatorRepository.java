package com.shellshellfish.datamanager.repositories;


import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;



import com.shellshellfish.datamanager.model.FundYearIndicator;
import com.shellshellfish.datamanager.model.FundYeildRate;

@Repository
public interface MongoFundYearIndicatorRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<FundYearIndicator, Long> {

    
	@Query("{\"code\":{$in:?0},\"querystdate\":?1,\"queryenddate\":?2}") //$orderby: {code : -1}
    List<FundYearIndicator> findByCodeAndQuerydate(String[] code,long fromtime,long totime);

    @Query("{\"code\":?0,\"querydate\" : {\"$gte\":?1, \"$lte\":?2}}")
	List<FundYearIndicator> getHistoryNetByCodeAndQuerydate(String code,long fromtime,long totime);
    

    @Query("{\"code\":?0,\"querydate\" : ?1}")
	List<FundYearIndicator> getHistoryNetByCodeAndQuerydate(String code,long time);
    
}
