package transfer.controller;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import io.restassured.RestAssured;
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

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class FundDetailedControllerTest {

	private String GET_FUND_COMPANY = "/phoneapi-ssf/getFundCompany";
	private String GET_FUND_DETAILS = "/phoneapi-ssf/getFundDetails";
	private String GET_FUND_INFO = "/phoneapi-ssf/getFundInfo";
	private String GET_FUND_MANAGER = "/phoneapi-ssf/getFundManager";
	private String GET_FUND_NOTICES = "/phoneapi-ssf/getFundNotices";
	private String GET_HISTORY_NET_VALUE = "/phoneapi-ssf/getHistoryNetvalue";



	private String GET_FUND_COMPANY_SCHEMA_NAME = "fund-detail-controller-getFundCompany.json";
	private String GET_FUND_DETAILS_SCHEMA_NAME = "fund-detail-controller-getFundDetails.json";
	private String GET_FUND_INFO_SCHEMA_NAME = "fund-detail-controller-getFundInfo.json";
	private String GET_FUND_MANAGER_SCHEMA_NAME = "fund-detail-controller-getFundIManager.json";
	private String GET_FUND_NOTICES_SCHEMA_NAME = "fund-detail-controller-getFundNotices.json";
	private String GET_HISTORY_NET_VALUE_SCHEMA_NAME = "fund-detail-controller-getHistoryNetValue.json";


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void getFundCompanyTest() {
		String name = "天弘基金管理有限公司";

		given()
				.param("name", name)
				.filter(new LogFilter())
				.when()
				.post(GET_FUND_COMPANY)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_COMPANY_SCHEMA_NAME));
	}
	@Test
	public void getFundDetailsTest() {
		String groupId= "2";
		String subGroupId= "2001";
		String codes= "000001.OF";

		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
				.param("codes", codes)
				.filter(new LogFilter())
				.when()
				.post(GET_FUND_DETAILS)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_DETAILS_SCHEMA_NAME));
	}
	@Test
	public void getFundInfoTest() {

		String code= "000001.OF";

		given()
				.param("code", code)
				.filter(new LogFilter())
				.when()
				.post(GET_FUND_INFO)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_INFO_SCHEMA_NAME));
	}
	@Test
	public void getFundManagerTest() {

		String name= "董阳阳";

		given()
				.param("name", name)
				.filter(new LogFilter())
				.when()
				.post(GET_FUND_MANAGER)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_MANAGER_SCHEMA_NAME));
	}
	@Test
	public void getFundNoticesTest() {

		String code= "000001.OF";

		given()
				.param("code", code)
				.filter(new LogFilter())
				.when()
				.post(GET_FUND_NOTICES)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_FUND_NOTICES_SCHEMA_NAME));
	}
	@Test
	public void getHistoryNetvalueTest() {

		String code= "000001.OF";
		String type= "1";
		String data= "2018-12-28";

		given()
				.param("code", code)
				.param("type", type)
				.param("data", data)
				.filter(new LogFilter())
				.when()
				.post(GET_HISTORY_NET_VALUE)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_HISTORY_NET_VALUE_SCHEMA_NAME));
	}

}
