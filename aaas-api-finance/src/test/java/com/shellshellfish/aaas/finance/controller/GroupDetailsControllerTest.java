package com.shellshellfish.aaas.finance.controller;

import com.shellshellfish.aaas.finance.FinanceApp;
import com.shellshellfish.aaas.finance.model.dto.FundCompany;
import com.shellshellfish.aaas.finance.model.dto.FundManager;
import com.shellshellfish.aaas.finance.trade.model.FundIncome;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @Author pierre
 * 17-12-21
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceApp.class)
@ActiveProfiles("dev")
public class GroupDetailsControllerTest {

	@Autowired
	GroupDetailsController groupDetailsController;


	@Test
	public void getFundManagerTest() {
		String testManagerName = "董阳阳";
		FundManager result = groupDetailsController.getFundManager(testManagerName);
		Assert.assertNotNull(result);

	}

	@Test
	public void getFundInfoTest() {
		String testManagerName = "000001.OF";
		String result = groupDetailsController.getFundInfo(testManagerName);
		Assert.assertNotNull(result);
	}

	@Test
	public void getFundCompanyTest() {
		String testManagerName = "天弘基金管理有限公司";
		FundCompany result = groupDetailsController.getFundCompany(testManagerName);
		Assert.assertNotNull(result);
	}

	@Test
	public void getFundNoticesTest() {
		String testManagerName = "000001.OF";
		List result = groupDetailsController.getFundNotices(testManagerName);
		Assert.assertNotNull(result);
	}

	@Test
	public void getFundIncomeTest() {
		String fundcode = "002163";
		String userUuid = "shellshellfish";
		FundIncome result = groupDetailsController.getFundIncome(userUuid, fundcode);
		System.out.println(result);
		Assert.assertNotNull(result);
	}

}
