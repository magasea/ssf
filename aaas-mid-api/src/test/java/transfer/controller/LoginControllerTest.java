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
import static org.hamcrest.Matchers.*;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles("test")
@EnableAutoConfiguration
public class LoginControllerTest {

	private String REQUEST_VERIFY_CODE = "/phoneapi-ssf/requestVerifyCode";

	private String USER_LOGIN = "/phoneapi-ssf/userlogin";

	private String FORGET_PASSWORD = "/phoneapi-ssf/forgottenPsw";

	private String LOGIN_OUT = "/phoneapi-ssf/logout";

	private String REGISTRATION = "/phoneapi-ssf/registration";

	private String RESET_PASSWORD = "/phoneapi-ssf/resetPsw";

	private String VERIFY_MSG_CODE = "/phoneapi-ssf/verifyMsgCode";


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void requestVerifyCodeTest() {
		String TEL_NO = "15665487542";

		given().filter(new LogFilter())
				.param("telNum", TEL_NO)
				.post(REQUEST_VERIFY_CODE)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result.identifyingCode", notNullValue());

	}


	@Test
	public void forgetPswTest() {
		String TEL_NO = "13512345679";
		String PASSWORD = "zaq12wsx!";
		String VERIFY_CODE = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("telNum", TEL_NO)
				.param("password", PASSWORD)
				.param("verifyCode", VERIFY_CODE)
				.post(FORGET_PASSWORD)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}

	@Test
	public void registrationTest() {
		String TEL_NO = "13512345679";
		String PASSWORD = "zaq12wsx!";
		String VERIFY_CODE = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("telNum", TEL_NO)
				.param("password", PASSWORD)
				.param("verifyCode", VERIFY_CODE)
				.post(REGISTRATION)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}

	@Test
	public void logoutTest() {
		String UUID = "13512345679";
		String TOKEN = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("uuid", UUID)
				.param("token", TOKEN)
				.post(LOGIN_OUT)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}

	@Test
	public void resetPswTest() {
		String UUID = "13512345679";
		String OLD_PASSWORD = "zaq12wsx!";
		String NEW_PASSWORD = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("uuid", UUID)
				.param("newPWD", NEW_PASSWORD)
				.param("oldPWD", OLD_PASSWORD)
				.post(RESET_PASSWORD)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}


	@Test
	public void userLoginTest() {
		String TEL_NO = "13512345679";
		String PASSWORD = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("telNum", TEL_NO)
				.param("password", PASSWORD)
				.post(USER_LOGIN)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}

	@Test
	public void verifyMsgCodeTest() {
		String TEL_NO = "13512345679";
		String MSG_CODE = "zaq12wsx!";

		given().filter(new LogFilter())
				.param("telNum", TEL_NO)
				.param("msgCode", MSG_CODE)
				.post(VERIFY_MSG_CODE)
				.then()
				.log().all()
				.body("head.status", equalTo("1"))
				.body("result", notNullValue());

	}

}

