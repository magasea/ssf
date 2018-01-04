package transfer.controller;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import io.restassured.RestAssured;
import io.swagger.annotations.ApiImplicitParam;
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
public class FundGroupControllerTest {

	private String GET_MY_PRODUCT_DETAIL = "/phoneapi-ssf/getMyProductDetail";


	private String GET_MY_PRODUCT_DETAIL_SCHEMA_NAME = "fund-group-controller-getMyProductDetail.json";


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void getProductDetailTest() {
		String uuid = "shellshellfish";
		String prodId = "41";
		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.filter(new LogFilter())
				.when()
				.post(GET_MY_PRODUCT_DETAIL)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_MY_PRODUCT_DETAIL_SCHEMA_NAME));
	}


}

