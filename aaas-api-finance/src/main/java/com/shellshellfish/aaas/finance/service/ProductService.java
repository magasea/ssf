package com.shellshellfish.aaas.finance.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.shellshellfish.aaas.finance.model.Product;

public interface ProductService {
	public Product getProduct(String id);
	public Page<Product> getAllProducts(Pageable pageable);
	public Product addProduct(Product product);
}