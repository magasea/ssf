package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;

import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.omg.CORBA.TIMEOUT;
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
public class FundDetailedControllerIT {

	private String GET_FUND_COMPANY_DETAIL_INFO = "/phoneapi-ssf/getFundCompanyDetailInfo";
	private String GET_FUND_DETAILS = "/phoneapi-ssf/getFundDetails";
	private String GET_FUND_INFO_BY_CODE = "/phoneapi-ssf/getFundInfoBycode";
	private String GET_FUND_MANAGER = "/phoneapi-ssf/getFundManager";
	private String GET_FUND_NOTICES = "/phoneapi-ssf/getFundNotices";
	private String GET_HISTORY_NET_VALUE = "/phoneapi-ssf/getHistoryNetvalue";


	private String GET_FUND_COMPANY_DETAIL_INFO_SCHEMA_NAME = "fund-detail-controller-getFundCompanyDetailInfo.json";
	private String GET_FUND_DETAILS_SCHEMA_NAME = "fund-detail-controller-getFundDetails.json";
	private String GET_FUND_INFO_BY_CODE_SCHEMA_NAME = "fund-detail-controller-getFundInfoBycode.json";
	private String GET_FUND_MANAGER_SCHEMA_NAME = "fund-detail-controller-getFundIManager.json";
	private String GET_FUND_NOTICES_SCHEMA_NAME = "fund-detail-controller-getFundNotices.json";
	private String GET_HISTORY_NET_VALUE_SCHEMA_NAME = "fund-detail-controller-getHistoryNetValue.json";

	private static final String REQUEST_IS_SUCCESS = "1";
	private static final long TIMEOUT = 60000L;

	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getFundCompanyDetailInfo
	 * 接口作用：根据基金公司的名称获取该公司相应的基金数据
	 * 参数：{name ：基金公司名称}
	 */
	@Test
	public void getFundCompanyTest() {
		String name = "天弘基金管理有限公司";

		given()
				.queryParam("name", name)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_FUND_COMPANY_DETAIL_INFO)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_COMPANY_DETAIL_INFO_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getFundDetails
	 * 接口作用：根据基金编码与日期获取该基金的详细数据
	 * 参数：{code ：基金编码，date ：日期}
	 */
	@Test
	public void getFundDetailsTest() {
		String code = "000216.OF";
		String date = "2017-12-22";

		given()
				.param("code", code)
				.param("date", date)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_FUND_DETAILS)
				.then().log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_FUND_DETAILS_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getFundInfoBycode
	 * 接口作用：根据基金编码获取基金的大概数据
	 * 参数：{code ：基金编码}
	 */
	@Test
	public void getFundInfoBycodeTest() {

		String code = "000614.OF";

		given()
				.param("code", code)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_FUND_INFO_BY_CODE)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_FUND_INFO_BY_CODE_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getFundManager
	 * 接口作用：根据金金经理的姓名获取他管理的基金数据
	 * 参数：{name ：基金经理姓名}
	 */
	@Test
	public void getFundManagerTest() throws UnsupportedEncodingException {

		String name = "董阳阳";

		given()
				.queryParam("name", name)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_FUND_MANAGER)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_FUND_MANAGER_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getFundNotices
	 * 接口作用：根据基金编码获取该基金的公告信息数据
	 * 参数：{code ：基金编码}
	 */
	@Test
	public void getFundNoticesTest() {

		String code = "000001.OF";

		given()
				.param("code", code)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_FUND_NOTICES)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_FUND_NOTICES_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getHistoryNetvalue
	 * 接口作用：根据参数指定基金的收益走势&历史净值
	 * 参数：{code ：基金编码，type ：类型(1: 3month,2: 6month,3: 1year,4: 3year)，date ：日期}
	 */
	@Test
	public void getHistoryNetvalueTest() {

		String code = "000614.OF";
		String type = "1";
		String date = "2018-04-02";

		given()
				.param("code", code)
				.param("type", type)
				.param("date", date)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_HISTORY_NET_VALUE)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(GET_HISTORY_NET_VALUE_SCHEMA_NAME))
				.time(lessThan(TIMEOUT));
	}

}
