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
	public void testResourceWrapper() throws JsonProcessingException {
		ResourceWrapper<Dummy> resourceDummy = new ResourceWrapper<>();
		Dummy dummy = new Dummy(1, "dummy");
				
		resourceDummy.setName("Dummy");
		resourceDummy.setItem(dummy);

		System.out.println(objectMapper.writeValueAsString(resourceDummy));
	}
	
	@Test
	public void testCollectionResourceWrapper() throws JsonProcessingException {
		CollectionResourceWrapper<List<Dummy>> resourceDummies = new CollectionResourceWrapper<>();
		Dummy dummy = new Dummy(1, "dummy");
				
		List<Dummy> dummies = Arrays.asList(dummy, dummy);
		resourceDummies.setName("Dummies");
		resourceDummies.setTotal(3);
		resourceDummies.setItems(dummies);

		System.out.println(objectMapper.writeValueAsString(resourceDummies));
	}
}
