package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.FundNet;
import com.shellshellfish.aaas.userinfo.service.FundTradeApiService;
import java.math.BigDecimal;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre 18-1-24
 */

@RunWith(value = SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class FundInfoServiceTest {

	@Autowired
	FundTradeApiService fundTradeApiService;

	@Test
	public void testGetAllNet() throws Exception {
		String fundCode = "000149.OF";

		try {
			BigDecimal result = fundTradeApiService.getRate(fundCode, "024");
			System.out.println(result);
			Assert.assertNotNull(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


}
