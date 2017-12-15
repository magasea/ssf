package com.shellshellfish.aaas.account.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("测试项目是否正常启动")
public class TestController {


	@ApiOperation("测试项目是否正常启动， 返回接收到的数据")

	@RequestMapping(value = "/echo/{arg}", method = RequestMethod.POST)
	public String echo(@PathVariable String arg) {
		return arg;
	}
	
	


}
