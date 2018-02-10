package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dto.BankcardDetailBodyDTO;
import com.shellshellfish.aaas.userinfo.service.OrderRpcService;
import com.shellshellfish.aaas.userinfo.service.PayGrpcService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class OpenAccountServiceTest {


	@Autowired
	OrderRpcService orderGrpcService;

	@Test
	public void openAccountTest() throws Exception {
		BankcardDetailBodyDTO bankcardDetailBodyDTO = new BankcardDetailBodyDTO();
		bankcardDetailBodyDTO.setBankName("-1");
		bankcardDetailBodyDTO.setBankName("中国银行");
		bankcardDetailBodyDTO.setCardCellphone("123456789");
		bankcardDetailBodyDTO.setCardUserName("hello");
		bankcardDetailBodyDTO.setCardUuId("45687913");
		bankcardDetailBodyDTO.setCardNumber("56487615135464641");
		bankcardDetailBodyDTO.setCardUserPid("456465431351543134");
		bankcardDetailBodyDTO.setUserId(-1L);

		String result = orderGrpcService.openAccount(bankcardDetailBodyDTO);
		Assert.assertNotNull(result);
		Assert.assertNotEquals("-1", result);
	}

}