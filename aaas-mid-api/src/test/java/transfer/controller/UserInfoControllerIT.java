package transfer.controller;

import com.shellshellfish.aaas.common.utils.RandomPhoneNumUtil;
import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import com.shellshellfish.aaas.transfer.controller.LoginController;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration
public class UserInfoControllerIT {


	private String ADD_BANKCARDS = "/phoneapi-ssf/addBankCards";

	private String ASSET = "/phoneapi-ssf/asset";

	private String BANKS = "/phoneapi-ssf/banks";

	private String CHI_COMBINATION = "/phoneapi-ssf/chicombination";

	private String INVATION_FRIENDS = "/phoneapi-ssf/invationFriends";

	private String PERSONAL_INFORMATION = "/phoneapi-ssf/personalInformation";

	private String SELECT_BANKS = "/phoneapi-ssf/selectbanks";

	private String SELL_RESULT = "/phoneapi-ssf/sellresult";

	private String SYSTEM_MSG = "/phoneapi-ssf/systemMsg";

	private String TRADE_RECORDS = "/phoneapi-ssf/traderecords";

	private String TRADE_RESULT = "/phoneapi-ssf/traderesult";


	private static final String REQUEST_IS_SUCCESS = "1";


	@LocalServerPort
	public int port;

	@Autowired
	LoginController loginController;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void addBankCardsTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String name = "zhangsan";
		String bankCard = RandomPhoneNumUtil.generateBankCardNoMatchingLuhn();
		String idcard = "11022619850127211X";
		String mobile = RandomPhoneNumUtil.generatePhoneNumber();
		String verifyCode = getVerifyCode(mobile);

		given()
				.param("uuid", uuid)
				.param("name", name)
				.param("bankCard", bankCard)
				.param("idcard", idcard)
				.param("mobile", mobile)
				.param("verifyCode", verifyCode)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(ADD_BANKCARDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.status", notNullValue());
	}

	@Test
	public void assetTest() {
		String uuid = "1";
		String totalAssets = "1";
		String dailyReturn = "1";
		String totalRevenue = "1";
		String totalRevenueRate = "1";


		given()
				.param("uuid", uuid)
				.param("totalAssets", totalAssets)
				.param("dailyReturn", dailyReturn)
				.param("totalRevenue", totalRevenue)
				.param("totalRevenueRate", totalRevenueRate)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(ASSET)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.totalRevenueRate", notNullValue())
				.body("result.totalAssets", notNullValue())
				.body("result.dailyReturn", notNullValue())
				.body("result.totalRevenue", notNullValue())
				.body("result.trendYield.date[0]", notNullValue())
				.body("result.trendYield.value[0]", notNullValue());
	}

	@Test
	public void banksTest() {
		String bankNum = "6210986802084484920";

		given()
				.param("bankNum", bankNum)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(BANKS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankCode", notNullValue())
				.body("result.bankName", notNullValue());
	}

	@Test
	public void chiCombinationTest() {
		String uuid = "69ad9732-f9cd-49e9-a71f-0462cc6b4d8e";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(CHI_COMBINATION)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.result", notNullValue())
				.body("result.result.totalIncome[0]", notNullValue())
				.body("result.result.updateDate[0]", notNullValue())
				.body("result.result.totalAssets[0]", notNullValue())
				.body("result.result.dailyIncome[0]", notNullValue())
				.body("result.result.totalIncomeRate[0]", notNullValue())
				.body("result.result.prodId[0]", notNullValue())
				.body("result.result.title[0]", notNullValue())
				.body("result.result.createDate[0]", notNullValue())
				.body("result.result.status[0]", notNullValue());
	}

	@Test
	public void personalInformationTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(PERSONAL_INFORMATION)
				.then().log().all()
				.assertThat()
				.body("result.uuid", notNullValue())
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.userBaseInfo.id", notNullValue())
				.body("result.userBaseInfo.cellPhone", notNullValue())
				.body("result.userBaseInfo", hasKey("birthAge"))
				.body("result.userBaseInfo", hasKey("occupation"))
				.body("result.userBaseInfo", hasKey("passwordhash"))
				.body("result.userBaseInfo", hasKey("riskLevel"));
	}

	@Test
	public void selectBanksTest() {
		String uuid = "3ed64c5d-1cdf-4837-b06c-be8c28537cf1";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SELECT_BANKS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankCode[0]", notNullValue())
				.body("result.bankcardNum[0]", notNullValue())
				.body("result.bankType[0]", notNullValue())
				.body("result.bankName[0]", notNullValue())
				.body("result.bankcardSecurity[0]", notNullValue());
	}

	@Test
	public void sellresultTest() {
		String uuid = "1";
		String prodId = "1";
		String buyfee = "1";
		String bankName = "1";
		String bankCard = "1";


		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.param("buyfee", buyfee)
				.param("bankName", bankName)
				.param("bankCard", bankCard)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SELL_RESULT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankInfo", notNullValue())
				.body("result.buyfee", notNullValue())
				.body("result.date2", notNullValue())
				.body("result.date1", notNullValue());
	}

	@Test
	public void systemMsgTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SYSTEM_MSG)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result._items.date[0]", notNullValue())
				.body("result._items.content[0]", notNullValue())
				.body("result.uuid", notNullValue());
	}

	//	@Test
	//TODO  此处用到RabbitMQ
	public void traderecordsTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(TRADE_RECORDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS));
	}

	@Test
	public void tradeResultTest() {

		String uuid = "1";
		String prodId = "1";
		String buyfee = "1";
		String bankName = "1";
		String bankCard = "1";


		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.param("buyfee", buyfee)
				.param("bankName", bankName)
				.param("bankCard", bankCard)
				.when()
				.post(TRADE_RESULT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.date", notNullValue())
				.body("result.bankInfo", notNullValue())
				.body("result.buyfee", notNullValue());
	}

	@Test
	public void invationFriendsTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(INVATION_FRIENDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.uuid", notNullValue())
				.body("result._items", notNullValue());

	}

	/**
	 * send and get verify code
	 *
	 * @param phone_number
	 * @return
	 */
	private String getVerifyCode(String phone_number) {
		Map<String, String> result = (HashMap) loginController.sendVerifyCode(phone_number).getResult();
		return result.get("identifyingCode");
	}
}
