package transfer.controller;

import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import com.shellshellfish.aaas.transfer.controller.FinanceController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class)
@ActiveProfiles("test")
public class FinanceControllerTest {


	@Autowired
	private FinanceController financeController;



	@Test
	public void addBankCardsTest(){

	}

	@Test
	public void contributionsTest(){
		String groupId ="6";
		String subGroupId = "111111";
		JsonResult result =financeController.contributions(groupId,subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS,result.getHead().getStatus());
	}

	@Test
	public void portfolioYieldTest(){
		String groupId ="6";
		String subGroupId = "111111";
		JsonResult result =financeController.portfolioYield(groupId,subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS,result.getHead().getStatus());
	}
	@Test
	public void portfolioYieldWeekTest(){
		String groupId ="6";
		String subGroupId = "111111";
		JsonResult result =financeController.portfolioYieldWeek(groupId,subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS,result.getHead().getStatus());
	}
	@Test
	public void riskNotificationsTest(){
		String groupId ="1";
		JsonResult result =financeController.riskNotifications(groupId);

		Assert.assertEquals(JsonResult.SUCCESS,result.getHead().getStatus());
	}

}
