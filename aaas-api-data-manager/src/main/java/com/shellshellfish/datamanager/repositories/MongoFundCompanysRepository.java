package com.shellshellfish.datamanager.repositories;



import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.datamanager.model.FundCompanys;


@Repository
public interface MongoFundCompanysRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<FundCompanys, Long> {

	@Query("{\"基金公司\":?0}")
	List<FundCompanys> findByCompanyname(String name);
    

}
