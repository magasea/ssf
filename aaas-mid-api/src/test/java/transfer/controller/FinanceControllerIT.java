package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;

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

import java.nio.charset.Charset;

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

	private static final long TIMEOUT = 3000L;

	@LocalServerPort
	private int port;


	@Before
	public void setup() {
		RestAssured.port = port;
	}


	/**
	 * 目的：校验"系统首页"接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/finance-home
	 * 接口作用：根据参数获取指定的系统首页信息
	 * 参数：{uuid ：用户id，isTestFlag ：是否测评（1-已做 0-未做），testResult ：测评结果}
	 */
	@Test
	public void financeHomeTest() {
		String uuid = "66e35442-a9f6-4854-8f87-dd3e9eb47f2c";
		// 0:未做 1:已做
		String isTestFlag = "1";
		String testResult =new String("平衡型".getBytes(),Charset.forName("ISO-8859-1"));
		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("isTestFlag", isTestFlag)
				.param("testResult", testResult)
				.when()
				.post(FINANCE_HOME)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(FINANCE_HOME_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验"配置收益贡献"接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/contributions
	 * 接口作用：根据参数获取指定的收益贡献
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void contributionsTest() {
		String groupId = "6";
		String subGroupId = "60048";


		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(CONTRIBUTIONS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(CONTRIBUTIONS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/checkPrdDetails
	 * 接口作用：根据参数获取指定理财产品的详细数据
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void checkPrdDetailsTest() {
		String groupId = "8";
		String subGroupId = "80048";

		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(CHECK_PRD_DETAILS)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(CHECK_PRD_DETAILS_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(6000L))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/effectiveFrontierPoints
	 * 接口作用：根据参数获取最优的理财产品组合
	 * 参数：{groupId ：产品组ID}
	 */
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/financeFrontPage
	 * 接口作用：获取理财页面的数据
	 * 参数：{}
	 */
	@Test
	public void financeFrontPageTest() {

		given().filter(new ResponseLoggingFilter())
				.post(FINANCE_FRONT_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(FINANCE_FRONT_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/futureExpectationPage
	 * 接口作用：根据参数获取指定产品未来预期数据
	 * 参数：{uuid ：用户id，groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void futureExpectationPageTest() {

		String uuid = "1";
		String groupId = "6";
		String subGroupId = "60048";
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getExpAnnualAndMaxReturn
	 * 接口作用：根据参数获取指定产品的历史收益率和最大回撤
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void getExpannualAndMaxReturnTest() {

		String groupId = "6";
		String subGroupId = "60048";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(GET_EXPANNUAL_AND_MAX_RETURN)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(GET_EXPANNUAL_AND_MAX_RETURN_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/globalConfigurationPage
	 * 接口作用：根据参数获取指定产品的全球配置数据
	 * 参数：{uuid ：用户id，groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void globalConfigurationPageTest() {

		String uuid = "1";
		String groupId = "4";
		String subGroupId = "40048";
		given().filter(new ResponseLoggingFilter())
				.param("uuid", uuid)
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.post(GLOBAL_CONFIGURATION_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(GLOBAL_CONFIGURATION_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT));
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/historicalPerformancePage
	 * 接口作用：根据参数获取指定产品的历史业绩数据
	 * 参数：{groupId ：产品组ID，subGroupId ：子产品组ID，productName ：产品名称}
	 */
	@Test
	public void historicalPerformancePageTest() {

		String groupId = "4";
		String subGroupId = "40048";
		String productName = "贝贝鱼1号“御?安守”组合";
		given().filter(new ResponseLoggingFilter())
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.param("productName",productName)
				.post(HISTORICAL_PERFORMANCE_PAGE)
				.then()
				.log().all()
				.body(matchesJsonSchemaInClasspath(HISTORICAL_PERFORMANCE_PAGE_JSON_SCHEMA))
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/inComeSlidebarPoints
	 * 接口作用：根据产品组ID获取预期收益率调整有多少个点
	 * 参数：{groupId ：产品组ID}
	 */
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/optAdjustment
	 * 接口作用：根据投资期限与风险承受级别获取指定的组合数据
	 * 参数：{invstTerm ：投资期限，riskLevel ：风险承受级别}
	 */
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/optimizations
	 * 接口作用：
	 * 参数：{groupId ：产品组ID，riskPointValue ：风险率，incomePointValue ：收益率}
	 */
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/riskMangementPage
	 * 接口作用：根据参数获取指定产品的风险控制数据
	 * 参数：{uuid ：用户ID，groupId ：产品组ID，subGroupId ：子产品组ID}
	 */
	@Test
	public void riskMangementPageTest() {

		String uuid = "1";
		String groupId = "6";
		String subGroupId = "60048";
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
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/riskSlidebarPoints
	 * 接口作用：根据参数获取指定产品的预期收益率调整有多少个点
	 * 参数：{groupId ：产品组ID}
	 */
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
				.time(lessThan(TIMEOUT))
				.using();
	}

}
