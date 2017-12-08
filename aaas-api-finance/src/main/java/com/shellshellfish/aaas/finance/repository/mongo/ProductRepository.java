package com.shellshellfish.aaas.finance.repository.mongo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.RepositoryDefinition;

import com.shellshellfish.aaas.finance.model.Product;


public interface ProductRepository extends MongoRepository<Product, String> {
	List<Product> findAllByNameAndRiskLevel(String name, String riskLevel);	
	Page<Product> findAll(Pageable pageable);
	
}
