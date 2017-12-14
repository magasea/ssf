package com.shellshellfish.aaas.datacollection.client.controller;


import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {



	@RequestMapping(value = "/echo/{arg}", method = RequestMethod.POST)
	public String echo(@PathVariable String arg) {
		return arg;
	}
	
	


}
