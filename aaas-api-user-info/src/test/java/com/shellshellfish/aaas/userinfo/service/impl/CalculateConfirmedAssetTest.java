package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.service.DayIndicatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "dev")
public class CalculateConfirmedAssetTest {


	@Autowired
	CalculateConfirmedAsset calculateConfirmedAsset;

	@Test
	public void test() {
		String fundCode = "000312.OF";
		Long userId = 5611L;
		Long userProdId = 27L;
		calculateConfirmedAsset.calculateConfirmedAsset(userProdId, userId, fundCode);
	}

}