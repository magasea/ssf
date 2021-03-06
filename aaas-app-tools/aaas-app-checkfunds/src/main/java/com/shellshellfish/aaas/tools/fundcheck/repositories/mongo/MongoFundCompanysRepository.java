package com.shellshellfish.aaas.tools.fundcheck.repositories.mongo;



import com.shellshellfish.aaas.tools.fundcheck.model.FundCompanys;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFundCompanysRepository extends
    
    org.springframework.data.mongodb.repository.MongoRepository<FundCompanys, Long> {

	@Query("{\"基金公司\":?0}")
	List<FundCompanys> findByCompanyname(String name);
    

	@Query("{\"代码\":?0}")
	List<FundCompanys> findByCode(String code);
    
}
