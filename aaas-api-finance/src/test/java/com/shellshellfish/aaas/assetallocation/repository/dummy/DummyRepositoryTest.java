package com.shellshellfish.aaas.assetallocation.repository.dummy;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class DummyRepositoryTest {

	@Autowired
	@Qualifier("secondaryMongoTemplate")
	private MongoTemplate secondaryMongoTemplate;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Autowired
	private DummyRepository dummyRepository;
		
	@Test
	public void testDummy() throws JsonProcessingException {
		Dummy dummy = new Dummy();
		dummy.setName("dummy");
		dummy.setDesc("hello world");
		
		secondaryMongoTemplate.save(dummy);
		
		List<Dummy> dummies = dummyRepository.findAll();
		System.out.println(objectMapper.writeValueAsString(dummies));
	}
	
	
}
