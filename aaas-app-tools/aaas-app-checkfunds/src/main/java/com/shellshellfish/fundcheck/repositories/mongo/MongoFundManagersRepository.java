package com.shellshellfish.fundcheck.repositories.mongo;


import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.fundcheck.model.FundManagers;

@Repository
public interface MongoFundManagersRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<FundManagers, Long> {

	@Query("{\"基金经理\":?0}")
	List<FundManagers> findByManagername(String name);
    

}
