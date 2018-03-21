package com.shellshellfish.aaas.tools.fundcheck.repositories.mongo;



import com.shellshellfish.aaas.tools.fundcheck.model.ListedFundCodes;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoListedFundCodesRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<ListedFundCodes, Long> {

	@Query("{\"code\":?0}")
    List<ListedFundCodes> findByCode(String code);


}
