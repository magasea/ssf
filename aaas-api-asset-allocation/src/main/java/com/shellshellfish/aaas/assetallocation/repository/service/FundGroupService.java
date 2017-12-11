package com.shellshellfish.aaas.assetallocation.repository.service;

import com.shellshellfish.aaas.assetallocation.model.Dummy;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FundGroupService extends MongoRepository<Dummy, String> {
}
