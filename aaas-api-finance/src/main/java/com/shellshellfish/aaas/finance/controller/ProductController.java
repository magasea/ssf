package com.shellshellfish.aaas.finance.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.shellshellfish.aaas.finance.model.Product;
import com.shellshellfish.aaas.finance.service.ProductService;
import com.shellshellfish.aaas.finance.util.CollectionResourceWrapper;
import com.shellshellfish.aaas.finance.util.Links;
import com.shellshellfish.aaas.finance.util.ResourceWrapper;


@RestController
@RequestMapping("/api/ssf-finance")
public class ProductController {
	
	private final Logger logger = LoggerFactory.getLogger(ProductController.class);
	
	@Autowired
	private ProductService productService;
	
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET)
	public ResponseEntity<ResourceWrapper<Product>> addProduct(@PathVariable Product product, HttpServletRequest request) {
		logger.debug("add product:{}", product);
		product = productService.addProduct(product);
		ResourceWrapper<Product> resource = new ResourceWrapper<>(product);
		resource.getLinks().setSelf(request.getContextPath());
		
		return ResponseEntity.ok().body(resource);		
	}
	
	@RequestMapping(value = "/products/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE) 
	public ResponseEntity<ResourceWrapper<Product>> getProduct(@PathVariable String id, HttpServletRequest request) {
		logger.debug("get a product by uuid:{}", id);
		
		Product product = productService.getProduct(id);
		if (product == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
			
		} else {
			ResourceWrapper<Product> resource = new ResourceWrapper<>(product);
			resource.getLinks().setSelf(request.getContextPath());
			return new ResponseEntity<>(resource, HttpStatus.OK);	
		}	
		
	}
	
	@RequestMapping(value = "/products", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<CollectionResourceWrapper<List<Product>>> getAllProducts(Pageable pageable, HttpServletRequest request) {
		logger.debug("get all products. pageable:{}", pageable);
		
		Page<Product> page = productService.getAllProducts(pageable);
		
		CollectionResourceWrapper<List<Product>> resource = new CollectionResourceWrapper<>(page.getContent());
		
		// TODO: add links here
		Links links = resource.getLinks();
		links.setNext(String.format("/api/ssf-finance/products?page=%d&size=%d", pageable.getPageNumber() + 1, pageable.getPageSize()));
		if (pageable.getPageNumber() > 0) {
			links.setPrev(String.format("/api/ssf-finance/products?page=%d&size=%d", pageable.getPageNumber() - 1, pageable.getPageSize()));
		}
		links.setSelf(request.getContextPath());
		for(Product product: page.getContent()) {
			links.setRelated("/api/ssf-finance/products/asdfasdf/trends");	
		}
		
		
		return new ResponseEntity<>(resource, HttpStatus.OK);
	}
	
}
