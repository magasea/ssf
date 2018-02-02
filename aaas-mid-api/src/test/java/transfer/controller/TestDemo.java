package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;

import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;

/**
 * @Author pierre
 * 18-1-3
 */

@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration
public class TestDemo {


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}

	String groupId = "6";
	String subGroupId = "111111";

	@Test
	public void testDemo2() {

		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
		.when().
				post("/phoneapi-ssf/contributions")
		.then()
				.statusCode(HttpStatus.OK.value())
				.body("result", notNullValue())
				.body("result", hasKey("msg"));


	}
	@Test
	public void jsonSchemaTestDemo() {

		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId).filter(new ResponseLoggingFilter())
		.when().
				post("/phoneapi-ssf/contributions")
		.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath("test-demo-schema.json"));


	}

	@Test
	public void getJsonTestDemo() {


	String json =
		given()
				.param("groupId", groupId)
				.param("subGroupId", subGroupId)
		.when().
				post("/phoneapi-ssf/contributions")
		.asString();

		System.out.println(json);

	}
}
