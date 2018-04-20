package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration
public class TransferControllerIT {


	private String GET_EST_PUR_AMOUNT = "/phoneapi-ssf/getEstPurAmount";
	private String SELL_FUND_PAGE = "/phoneapi-ssf/sellFundPage";
	private String SELL_PRODUCT = "/phoneapi-ssf/sellProduct";
	private String SUBSCRIBE_FUND = "/phoneapi-ssf/subscribeFund";
	private String MAXMIN_VALUE = "/phoneapi-ssf/maxminValue";
	private String PURCHASE_PLAN = "/phoneapi-ssf/purchase-plan";

	private String GET_EST_PUR_AMOUNT_JSON_SCHEMA = "transferController-getEstPurAmount.json";
	private String SELL_FUND_PAGE_JSON_SCHEMA = "transferController-sellFundPage.json";
	private String SELL_PRODUCT_JSON_SCHEMA = "";
	private String SUBSCRIBE_FUND_JSON_SCHEMA = "";

	private static final String REQUEST_IS_SUCCESS = "1";

	private static final long TIMEOUT = 3000L;

	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getEstPurAmount
	 * 接口作用：获取预计费用,以及投资组合的每一支基金的费用
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID，totalAmount ：购买的总金额}
	 */
	@Test
	public void getEstPurAmountTest() {
		String groupId = "2";
		String subGroupId = "20048";
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
				.body(matchesJsonSchemaInClasspath(GET_EST_PUR_AMOUNT_JSON_SCHEMA))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/sellFundPage
	 * 接口作用：获得指定产品赎回界面的数据
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID，totalAmount ：购买总金额}
	 */
	@Test
	public void sellFundPageTest() {
		String groupId = "2";
		String subGroupId = "20048";
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
				.body(matchesJsonSchemaInClasspath(SELL_FUND_PAGE_JSON_SCHEMA))
				.time(lessThan(TIMEOUT));
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
				.post(SELL_PRODUCT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(SELL_PRODUCT_JSON_SCHEMA))
				.time(lessThan(TIMEOUT));
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
				.post(SUBSCRIBE_FUND)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(SUBSCRIBE_FUND_JSON_SCHEMA))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据
	 * 接口：/phoneapi-ssf/maxminValue
	 * 接口作用：获取指定产品购买的最大值最小值数据
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void maxminValueTest(){
		String groupId = "12";
		String subGroupId = "120048";

		given()
				.param("groupId",groupId)
				.param("subGroupId",subGroupId)
				.when()
				.post(MAXMIN_VALUE)
				.then().log().all()
				.assertThat()
				.body("head.status",equalTo(REQUEST_IS_SUCCESS))
				.body("result",notNullValue())
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据
	 * 接口：/phoneapi-ssf/purchase-plan
	 * 接口作用：获取指定产品的购买方案数据
	 * 参数：{uuid ：用户ID，groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void purchase_planTest(){
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String groupId = "12";
		String subGroupId = "120048";

		given()
				.param("uuid",uuid)
				.param("groupId",groupId)
				.param("subGroupId",subGroupId)
				.when()
				.post(PURCHASE_PLAN)
				.then().log().all()
				.assertThat()
				.body("head.status",equalTo(REQUEST_IS_SUCCESS))
				.body("result",notNullValue())
				.time(lessThan(TIMEOUT));
	}


}
