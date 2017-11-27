package com.shellshellfish.aaas.assetallocation.repository.dummy;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.shellshellfish.aaas.assetallocation.model.Dummy;

public interface DummyRepository extends MongoRepository<Dummy, String> {
	
}