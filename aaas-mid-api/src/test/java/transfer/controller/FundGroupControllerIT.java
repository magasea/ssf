package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

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
public class FundGroupControllerIT {

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
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_MY_PRODUCT_DETAIL)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_MY_PRODUCT_DETAIL_SCHEMA_NAME));
	}


}

