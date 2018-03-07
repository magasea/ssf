package com.shellshellfish.datamanager.repositories.mongo;



import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


import com.shellshellfish.datamanager.model.ListedFundCodes;

@Repository
public interface MongoListedFundCodesRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<ListedFundCodes, Long> {

	@Query("{\"code\":?0}")
    List<ListedFundCodes> findByCode(String code);


}
