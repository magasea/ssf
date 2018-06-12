package com.shellshellfish.aaas.transfer.controller;


import com.shellshellfish.aaas.oeminfo.model.JsonResult;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api("测试项目是否正常启动")
@RequestMapping("/phoneapi-ssf/test/clean")
public class TestController {


	@Value("${shellshellfish.user-login-url}")
	private String loginUrl;

	@Value("${shellshellfish.data-manager-url}")
	private String dataManagerUrl;

	@Value("${shellshellfish.trade-order-url}")
	private String tradeOrderUrl;

	@Autowired
	RestTemplate restTemplate;

	@ApiOperation("测试接口 用于清理测试时注册的用户数据")
	@RequestMapping(value = "/user/{uuid}", method = RequestMethod.DELETE)
	@Transactional
	public void cleanUser(@PathVariable String uuid) {
		String methodUrl = "/api/useraccount/test/clean/user/{uuid}";
		restTemplate.delete(loginUrl + methodUrl, uuid);
	}



	@RequestMapping(value = "/wpUpdateFundResource", method = RequestMethod.POST)
	public  void updateFundResource(){
		System.out.println("开始执行mid-api");
		MultiValueMap<String, String> requestEntity = new LinkedMultiValueMap<>();

		String fundInfos=restTemplate
				.getForEntity(tradeOrderUrl + "/test-financetrade/wpGetFundInfoList", String.class).getBody();
		System.out.println(fundInfos);
		requestEntity.add("fundInfos", fundInfos);
		String result=restTemplate
				.postForEntity(dataManagerUrl + "/api/test-datamanage/wpUpdateFundResource",requestEntity, String.class).getBody();

	}


	@ResponseBody
	@RequestMapping(value = "/updateAllFundinfo",method = RequestMethod.GET)
	public String updateAllFundinfo(){
		String fundInfos=restTemplate
				.getForEntity(tradeOrderUrl + "/test-financetrade/updateAllFundinfo", String.class).getBody();
		return "更新成功";
	}

}
