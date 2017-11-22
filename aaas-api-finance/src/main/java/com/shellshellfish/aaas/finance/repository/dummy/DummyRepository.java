package com.shellshellfish.aaas.finance.repository.dummy;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shellshellfish.aaas.finance.model.Dummy;

public interface DummyRepository extends MongoRepository<Dummy, String> {
	
}