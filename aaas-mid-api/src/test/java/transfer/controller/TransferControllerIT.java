package transfer.controller;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class TransferControllerIT {


	private String BUY_DETAILS = "/phoneapi-ssf/buyDetails";
	private String GET_EST_PUR_AMOUNT = "/phoneapi-ssf/getEstPurAmount";
	private String SELL_FUND_PAGE = "/phoneapi-ssf/sellFundPage";
	private String SELL_PRODUCT = "/phoneapi-ssf/sellProduct";
	private String SUBSCRIBE_FUND = "/phoneapi-ssf/subscribeFund";


	private String BUY_DETAILS_JSON_SCHEMA = "transferController-buyDetails.json";
	private String GET_EST_PUR_AMOUNT_JSON_SCHEMA = "transferController-getEstPurAmount.json";
	private String SELL_FUND_PAGE_JSON_SCHEMA = "transferController-sellFundPage.json";
	private String SELL_PRODUCT_JSON_SCHEMA = "";
	private String SUBSCRIBE_FUND_JSON_SCHEMA = "";

	private static final String REQUEST_IS_SUCCESS = "1";


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void surveyresultsTest() {
		String userUuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String orderId = "1231230001000001513604604626";


		given()
				.param("orderId", orderId)
				.param("userUuid", userUuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(BUY_DETAILS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(BUY_DETAILS_JSON_SCHEMA));
	}

	@Test
	public void getEstPurAmountTest() {
		String groupId = "2";
		String subGroupId = "2000";
		String totalAmount = "5000";


		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.param("totalAmount", totalAmount)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_EST_PUR_AMOUNT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_EST_PUR_AMOUNT_JSON_SCHEMA));
	}

	@Test
	public void sellFundPageTest() {
		String groupId = "2";
		String subGroupId = "2000";
		String totalAmount = "5000";


		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.param("totalAmount", totalAmount)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SELL_FUND_PAGE)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(SELL_FUND_PAGE_JSON_SCHEMA));
	}

	//	@Test
	//TODO
	public void sellProductTest() {
		String userUuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String orderId = "1231230001000001513604604626";


		given()
				.param("orderId", orderId)
				.param("userUuid", userUuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(BUY_DETAILS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(BUY_DETAILS_JSON_SCHEMA));
	}

	//	@Test
	//TODO
	public void subscribeFundTest() {
		String userUuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String orderId = "1231230001000001513604604626";


		given()
				.param("orderId", orderId)
				.param("userUuid", userUuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(BUY_DETAILS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(BUY_DETAILS_JSON_SCHEMA));
	}


}
