package com.shellshellfish.aaas.userinfo.dao.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.ConfirmResult;
import com.shellshellfish.aaas.userinfo.model.FundNet;
import com.shellshellfish.aaas.userinfo.service.impl.OneFundApiService;
import java.time.LocalDate;
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
public class OneFundApiServiceTest {

	@Autowired
	OneFundApiService oneFundApiService;


	@Test
	public void testGetAllNet() {
		String fundCode = "000149.OF";
		Integer startIndex = 0;
		Integer count = 1;

		try {
			List<FundNet> result = oneFundApiService.getFundNets(fundCode, startIndex, count);
			Assert.assertNotNull(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetNet() {
		String fundCode = "000149.OF";

		try {
			FundNet result = oneFundApiService.getFundNet(fundCode, LocalDate.now().plusDays(-2L));
			Assert.assertNotNull(result);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testGetAllConfirmList() {
		String fundCode = "000149.OF";
		String openId = TradeUtil.getZZOpenId("370181199001206536");
		try {
			List<ConfirmResult> result = oneFundApiService
					.getConfirmResults(openId, fundCode, "20180123");
			Assert.assertNotNull(result);
			System.out.println(result);
			for (ConfirmResult confirm : result) {
				Assert.assertEquals("20180123", confirm.getConfirmdate());
			}

			List<ConfirmResult> result2 = oneFundApiService
					.getConfirmResults(openId, fundCode, "20180124");
			Assert.assertNotNull(result2);
			System.out.println(result2);
			for (ConfirmResult confirm : result2) {
				Assert.assertEquals("20180124", confirm.getConfirmdate());
				System.out.println(confirm);
			}
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}


}
