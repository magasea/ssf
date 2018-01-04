package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.service.YearIndicatorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "dev")
public class YearIndicatorServiceTest {


	@Autowired
	YearIndicatorService yearIndicatorService;

	@Test
	public void getHistoryNetByCodeAndQuerydateTest() throws Exception {
//		YearIndicatorQuery.Builder builder = YearIndicatorQuery.newBuilder();
//		builder.setCode("255010.OF");
//		builder.setQueryDate(DateUtil.getDayTimeWithoutHmsOneDayBefore());
//		YearIndicatorRpc yearIndicatorRpc = yearIndicatorService.getHistoryNetByCodeAndQuerydate(builder.build());
//
//		Assert.notNull(yearIndicatorRpc,"数据正确");
	}

}