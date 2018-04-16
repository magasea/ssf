package com.shellshellfish.aaas.datamanager.repositories.mongo;



import com.shellshellfish.aaas.datamanager.model.RangeIndicator;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoRangeIndictorRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<RangeIndicator, Long> {

	@Query("{\"code\":?0}")
    List<RangeIndicator> findByCode(String code);


}
