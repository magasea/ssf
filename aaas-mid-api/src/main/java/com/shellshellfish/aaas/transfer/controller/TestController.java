package com.shellshellfish.aaas.transfer.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@Api("测试项目是否正常启动")
@RequestMapping("/phoneapi-ssf/test/clean")
public class TestController {


	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	@Autowired
	RestTemplate restTemplate;

	@ApiOperation("测试接口 用于清理测试时注册的用户数据")
	@RequestMapping(value = "/user/{uuid}", method = RequestMethod.DELETE)
	@Transactional
	public void cleanUser(@PathVariable String uuid) {
		String methodUrl = "/api/useraccount/test/clean/user/{uuid}";
		restTemplate.delete(loginUrl + methodUrl, uuid);
	}


}
