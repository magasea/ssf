package com.shellshellfish.datamanager.repositories.mongo;


import java.util.List;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.shellshellfish.datamanager.model.FundCodes;


@Repository
public interface MongoFundCodesRepository extends

		org.springframework.data.mongodb.repository.MongoRepository<FundCodes, Long> {

	List<FundCodes> findAll();


	@Query("{\"code\":?0}")
	List<FundCodes> findByCode(String code);
}
