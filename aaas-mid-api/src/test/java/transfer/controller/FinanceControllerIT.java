package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
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
public class FinanceControllerIT {

	private static final String FINANCE_HOME = "/phoneapi-ssf/finance-home";

	private static final String FINANCE_FRONT_PAGE = "/phoneapi-ssf/financeFrontPage";

	private static final String CHECK_PRD_DETAILS = "/phoneapi-ssf/checkPrdDetails";

	private static final String HISTORICAL_PERFORMANCE_PAGE = "/phoneapi-ssf/historicalPerformancePage";

	private static final String FUTURE_EXPECTATION_PAGE = "/phoneapi-ssf/futureExpectationPage";

	private static final String RISK_MANGEMENT_PAGE = "/phoneapi-ssf/riskMangementPage";

	private static final String GLOBAL_CONFIGURATION_PAGE = "/phoneapi-ssf/globalConfigurationPage";

	private static final String GET_EXPANNUAL_AND_MAX_RETURN = "/phoneapi-ssf/getExpAnnualAndMaxReturn";

	private static final String OPT_ADJUSTMENT = "/phoneapi-ssf/optAdjustment";

	private static final String OPTIMIZATIONS = "/phoneapi-ssf/optimizations";

	private static final String INCOME_SLIDEBAR_POINTS = "/phoneapi-ssf/inComeSlidebarPoints";

	private static final String RISK_SLIDEBAR_POINTS = "/phoneapi-ssf/riskSlidebarPoints";

	private static final String EFFECTIVE_FRONTIER_POINTS = "/phoneapi-ssf/effectiveFrontierPoints";

	private static final String CONTRIBUTIONS = "/phoneapi-ssf/contributions";


	private static final String FINANCE_HOME_JSON_SCHEMA = "financeController-financeHome.json";

	private static final String FINANCE_FRONT_PAGE_JSON_SCHEMA = "financeController-financeFrontPage.json";

	private static final String CHECK_PRD_DETAILS_JSON_SCHEMA = "financeController-checkPrdDetails.json";

	private static final String HISTORICAL_PERFORMANCE_PAGE_JSON_SCHEMA = "financeController-historicalPerformancePage.json";

	private static final String FUTURE_EXPECTATION_PAGE_JSON_SCHEMA = "financeController-futureExpectationPage.json";

	private static final String RISK_MANGEMENT_PAGE_JSON_SCHEMA = "financeController-riskMangementPage.json";

	private static final String GLOBAL_CONFIGURATION_PAGE_JSON_SCHEMA = "financeController-globalConfigurationPage.json";

	private static final String GET_EXPANNUAL_AND_MAX_RETURN_JSON_SCHEMA = "financeController-getExpAnnualAndMaxReturn.json";

	private static final String OPT_ADJUSTMENT_JSON_SCHEMA = "financeController-optAdjustment.json";

	private static final String OPTIMIZATIONS_JSON_SCHEMA = "financeController-optimizations.json";

	private static final String INCOME_SLIDEBAR_POINTS_JSON_SCHEMA = "financeController-inComeSlidebarPoints.json";

	private static final String RISK_SLIDEBAR_POINTS_JSON_SCHEMA = "financeController-riskSlidebarPoints.json";

	private static final String EFFECTIVE_FRONTIER_POINTS_JSON_SCHEMA = "financeController-effectiveFrontierPoints.json";

	private static final String CONTRIBUTIONS_JSON_SCHEMA = "financeController-contributions.json";

	private static final String REQUEST_IS_SUCCESS = "1";


	@LocalServerPort
	private int port;


	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void financeHomeTest() {
		String uuid = "1";
		// 0:未做 1:已做
		String isTestFlag = "0";
		String testResult = "平衡型";

		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("isTestFlag", isTestFlag)
				.param("testResult", testResult)
				.post(FINANCE_HOME)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(FINANCE_HOME_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void contributionsTest() {
		String groupId = "6";
		String subGroupId = "111111";


		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(CONTRIBUTIONS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(CONTRIBUTIONS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void checkPrdDetailsTest() {
		String groupId = "2";
		String subGroupId = "2000";

		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(CHECK_PRD_DETAILS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(CHECK_PRD_DETAILS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void effectiveFrontierPointsTest() {
		String groupId = "2";

		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.post(EFFECTIVE_FRONTIER_POINTS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(EFFECTIVE_FRONTIER_POINTS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void financeFrontPageTest() {

		given().filter(new ResponseLoggingFilter())
				.post(FINANCE_FRONT_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(FINANCE_FRONT_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void futureExpectationPageTest() {

		String uuid = "1";
		String groupId = "6";
		String subGroupId = "111111";
		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(FUTURE_EXPECTATION_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(FUTURE_EXPECTATION_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void getExpannualAndMaxReturnTest() {

		String groupId = "6";
		String subGroupId = "111111";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(GET_EXPANNUAL_AND_MAX_RETURN)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(GET_EXPANNUAL_AND_MAX_RETURN_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void globalConfigurationPageTest() {

		String uuid = "1";
		String groupId = "4";
		String subGroupId = "4009";
		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(GLOBAL_CONFIGURATION_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(GLOBAL_CONFIGURATION_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue());
	}

	@Test
	public void historicalPerformancePageTest() {

		String groupId = "6";
		String subGroupId = "111111";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(HISTORICAL_PERFORMANCE_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(HISTORICAL_PERFORMANCE_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void incomeSlidebarPointsTest() {

		String groupId = "6";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.post(INCOME_SLIDEBAR_POINTS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(INCOME_SLIDEBAR_POINTS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void optAdjustmentTest() {

		String invstTerm = "1";
		String riskLevel = "C1";
		given().filter(new ResponseLoggingFilter())
				.queryParam("invstTerm", invstTerm)
				.queryParam("riskLevel", riskLevel)
				.post(OPT_ADJUSTMENT)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(OPT_ADJUSTMENT_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void optimizationsTest() {

		String groupId = "1";
		String riskPointValue = "0.0213";
		String incomePointValue = "0.0451";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("riskPointValue", riskPointValue)
				.param("incomePointValue", incomePointValue)
				.post(OPTIMIZATIONS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(OPTIMIZATIONS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void riskMangementPageTest() {

		String uuid = "1";
		String groupId = "6";
		String subGroupId = "100058";
		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(RISK_MANGEMENT_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(RISK_MANGEMENT_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

	@Test
	public void riskSlidebarPoints() {

		String groupId = "6";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.post(RISK_SLIDEBAR_POINTS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(RISK_SLIDEBAR_POINTS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.using();
	}

}
