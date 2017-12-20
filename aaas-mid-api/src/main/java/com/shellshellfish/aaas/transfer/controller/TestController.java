package com.shellshellfish.aaas.transfer.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

@RestController

@Api("测试项目是否正常启动")
public class TestController {


	@ApiOperation("测试项目是否正常启动， 返回接收到的数据")

	@RequestMapping(value = "/heartbeat/echo/{arg}", method = RequestMethod.POST)
	public String echo(@PathVariable String arg) {
		return arg;
	}
	
	


}