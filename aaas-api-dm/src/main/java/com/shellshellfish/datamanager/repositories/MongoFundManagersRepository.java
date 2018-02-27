package com.shellshellfish.datamanager.repositories;


import java.math.BigInteger;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.datamanager.model.FundCodes;
import com.shellshellfish.datamanager.model.FundManagers;

@Repository
public interface MongoFundManagersRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<FundManagers, Long> {

	@Query("{\"基金经理\":?0}")
	List<FundManagers> findByManagername(String name);
    

}
