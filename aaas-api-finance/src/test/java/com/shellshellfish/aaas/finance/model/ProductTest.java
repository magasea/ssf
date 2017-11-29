package com.shellshellfish.aaas.finance.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.configuration.MongoConfiguration;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class ProductTest {

	@Autowired
	private MongoTemplate mongoTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@Rollback(false)
	public void testProduct() throws JsonProcessingException {
		
		Product product = new Product();
		product.setAnnualizedReturn(0.075d);
		product.setMaxPullback(0.035d);
		product.setName("贝贝鱼理财产品A");
		product.setRiskLevel("保守型");
		Map<String, Double> assetsRatios = new LinkedHashMap<>();
		assetsRatios.put("利率债", 0.09d);
		assetsRatios.put("信用债", 0.08d);
		assetsRatios.put("大盘股票", 0.3d);
		assetsRatios.put("香港股票", 0.18d);
		assetsRatios.put("小盘股票", 0.15);
		assetsRatios.put("其他1", 0.15d);
		assetsRatios.put("其他2", 0.05d);
		
		product.setAssetsRatios(assetsRatios);
						
		mongoTemplate.save(product);
		
		System.out.println(objectMapper.writeValueAsString(product));
	}
}
