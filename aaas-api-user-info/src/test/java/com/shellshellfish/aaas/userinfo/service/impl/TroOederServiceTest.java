package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
//import com.shellshellfish.aaas.userinfo.service.TrdOrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "dev")
public class TroOederServiceTest {


//	@Autowired
//	TrdOrderService trdOrderService;

	@Test
	public void getOrderInfoTest() throws Exception {
//		String orderId = "1231230001000001513604604626";
//		TrdOrderInfo info = trdOrderService.getOrderInfo(orderId);
//		Assert.notNull(info,"返回结果为空");
	}

}