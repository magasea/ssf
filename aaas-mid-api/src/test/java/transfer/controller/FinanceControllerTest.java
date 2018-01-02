package transfer.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.common.utils.URLutils;
import com.shellshellfish.aaas.model.JsonResult;
import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import com.shellshellfish.aaas.transfer.controller.FinanceController;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class FinanceControllerTest {


	@Autowired
	private FinanceController financeController;

	@Autowired
	TestRestTemplate testRestTemplate;


	@Test
	public void addBankCardsTest() {

	}

	@Test
	public void contributionsTest() {
		String groupId = "6";
		String subGroupId = "111111";
		JsonResult result = financeController.contributions(groupId, subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS, result.getHead().getStatus());
	}

	@Test
	public void portfolioYieldTest() {
		String groupId = "6";
		String subGroupId = "111111";
		JsonResult result = financeController.portfolioYield(groupId, subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS, result.getHead().getStatus());
	}

	@Test
	public void portfolioYieldWeekTest() {
		String groupId = "6";
		String subGroupId = "111111";
		JsonResult result = financeController.portfolioYieldWeek(groupId, subGroupId);

		Assert.assertEquals(JsonResult.SUCCESS, result.getHead().getStatus());
	}

	@Test
	public void riskNotificationsTest() {
		String groupId = "1";
		JsonResult result = financeController.riskNotifications(groupId);

		Assert.assertEquals(JsonResult.SUCCESS, result.getHead().getStatus());
	}


	@Test
	public void testDemo() {
		String groupId = "6";
		String subGroupId = "111111";

		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("groupId", groupId);
		params.add("subGroupId", subGroupId);

		JSONObject object = new JSONObject();
		object.put("groupId",groupId);
		object.put("subGroupId",subGroupId);
		String result = testRestTemplate.postForObject("/phoneapi-ssf/contributions",params, String.class);
		assertThat(result).contains("_total","maxMinBenchmarkMap");


	}

	@Test
	public  void testJSon() {
		String groupId = "6";
		String subGroupId = "111111";
		JSONObject object = new JSONObject();
		object.put("groupId",groupId);
		object.put("subGroupId",subGroupId);
		System.out.println(object.toJSONString());
	}
}
