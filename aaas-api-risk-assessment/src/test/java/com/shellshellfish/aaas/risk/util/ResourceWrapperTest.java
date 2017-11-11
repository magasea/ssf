package com.shellshellfish.aaas.risk.util;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.util.ReflectionUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ResourceWrapperTest {
	
	private ObjectMapper objectMapper = new ObjectMapper();
	
	@Test
	public void testAdhocJsonProperty() throws JsonProcessingException {
		ResourceWrapper<List<Dummy>> resourceDummy = new ResourceWrapper<>();
		Dummy dummy = new Dummy(1, "dummy");
				
		List<Dummy> dummies = Arrays.asList(dummy, dummy);
		resourceDummy.setResource(dummies);
		//ReflectionUtils.findMethod(Dummy.class, "resource");
		
		System.out.println(objectMapper.writeValueAsString(resourceDummy));
	}
}
