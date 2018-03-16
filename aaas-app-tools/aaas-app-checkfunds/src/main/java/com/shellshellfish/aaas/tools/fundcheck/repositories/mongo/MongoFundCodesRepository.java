package com.shellshellfish.aaas.tools.fundcheck.repositories.mongo;


import com.shellshellfish.aaas.tools.fundcheck.model.FundCodes;
import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;


@Repository
public interface MongoFundCodesRepository extends

		org.springframework.data.mongodb.repository.MongoRepository<FundCodes, Long> {

	List<FundCodes> findAll();


	@Query("{\"code\":?0}")
	List<FundCodes> findByCode(String code);
}
