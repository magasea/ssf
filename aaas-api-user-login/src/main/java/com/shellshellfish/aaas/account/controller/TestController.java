package com.shellshellfish.aaas.account.controller;


import com.shellshellfish.aaas.account.repositories.mysql.UserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api("测试项目是否正常启动")
@RequestMapping("/api/useraccount/test/clean")
public class TestController {


	@Autowired
	private UserRepository userRepository;

	@ApiOperation("测试接口 用于清理测试时注册的用户数据")
	@RequestMapping(value = "/user/{uuid}", method = RequestMethod.DELETE)
	@Transactional
	public boolean cleanUser(@PathVariable String uuid) {
		Integer num = userRepository.deleteByUuid(uuid);
		if (num != null && num > 0) {
			return true;
		}
		return false;
	}


}
