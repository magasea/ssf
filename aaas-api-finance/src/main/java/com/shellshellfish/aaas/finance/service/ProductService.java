package com.shellshellfish.aaas.finance.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shellshellfish.aaas.finance.model.Product;
import com.shellshellfish.aaas.finance.repository.product.ProductRepository;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product addProduct(Product product) {
		return productRepository.save(product);
	}
	
	public Product getProduct(String id) {
		return productRepository.findOne(id);
	}
	
	public Page<Product> getAllProducts(Pageable pageable) {
		return productRepository.findAll(pageable);
	}
}
