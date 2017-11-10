package com.shellshellfish.aaas.risk.util;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.shellshellfish.aaas.risk.model.Question;

public class ResourceWrapperTest {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void testAdhocJsonProperty() throws JsonProcessingException {
		ResourceWrapper<Dummy> resourceDummy = new ResourceWrapper<>();
		Dummy dummy = new Dummy(1, "dummy");
		
		resourceDummy.setResource(dummy);
		
		
		
		System.out.println(objectMapper.writeValueAsString(resourceDummy));
	}
}
