package com.shellshellfish.aaas.finance.repository.dummy;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.finance.model.Dummy;
import com.shellshellfish.aaas.finance.model.dao.Fundresources;
import com.shellshellfish.aaas.finance.repository.mongo2.DummyRepository;
import com.shellshellfish.aaas.finance.repository.mongo3.FundResourceRepository;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DummyRepositoryTest {

	@Autowired
	@Qualifier("secondaryMongoTemplate")
	private MongoTemplate secondaryMongoTemplate;
	
	@Autowired
	@Qualifier("thirdMongoTemplate")
	private MongoTemplate thirdMongoTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DummyRepository dummyRepository;
	
	@Autowired
	private FundResourceRepository fundResourceRepository;
		
	@Test	
	public void testDummy() throws JsonProcessingException {
		Dummy dummy = new Dummy();
		dummy.setName("dummy");
		dummy.setDesc("hello world");
		
		secondaryMongoTemplate.save(dummy);
		
		List<Dummy> dummies = dummyRepository.findAll();
		System.out.println(objectMapper.writeValueAsString(dummies));
	}
	
	@Test	
	public void testResouces() throws JsonProcessingException {
		Fundresources fundResources = new Fundresources();
		fundResources.setMgrage("张三");
		fundResources.setCode("000");
		
		thirdMongoTemplate.save(fundResources);
		
		List<Fundresources> fundResourcesList = fundResourceRepository.findAll();
		System.out.println(objectMapper.writeValueAsString(fundResourcesList));
	}
}
