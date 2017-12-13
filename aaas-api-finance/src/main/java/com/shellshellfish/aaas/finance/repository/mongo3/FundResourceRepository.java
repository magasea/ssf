package com.shellshellfish.aaas.finance.repository.mongo3;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.shellshellfish.aaas.finance.model.dao.Fundresources;

public interface FundResourceRepository extends MongoRepository<Fundresources, String> {
	Fundresources findOneByCode(String code); 
}
