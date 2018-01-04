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
public class DayIndicatorServiceTest {


	@Autowired
	DayIndicatorService dayIndicatorService;

	@Test
	public void getDailyFunds() throws Exception {
//		DayIndicatorQuery.Builder builder = DayIndicatorQuery.newBuilder();
//		builder.setCode("000312.OF");
//		List<DayIndicatorRpc> dailyFundsList = dayIndicatorService.getDayIndicatorsByCode(builder.build());
//		for (DayIndicatorRpc dayIndicatorRpc : dailyFundsList) {
//			System.out.println(dayIndicatorRpc.getClose());
//		}
//		if(dailyFundsList.size()>0){
//			Assert.isTrue("12345".equals(dailyFundsList.get(0).getAvgprice()),"数据错误");
//		}
	}

}