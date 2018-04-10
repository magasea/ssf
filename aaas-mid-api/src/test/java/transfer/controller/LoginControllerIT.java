package transfer.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;
import com.shellshellfish.aaas.common.utils.RandomPhoneNumUtil;
import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import com.shellshellfish.aaas.transfer.controller.LoginController;

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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginControllerIT {

	private String REQUEST_VERIFY_CODE = "/phoneapi-ssf/requestVerifyCode";

	private String USER_LOGIN = "/phoneapi-ssf/userlogin";

	private String FORGET_PASSWORD = "/phoneapi-ssf/forgottenPsw";

	private String LOGIN_OUT = "/phoneapi-ssf/logout";

	private String REGISTRATION = "/phoneapi-ssf/registration";

	private String RESET_PASSWORD = "/phoneapi-ssf/resetPsw";

	private String VERIFY_MSG_CODE = "/phoneapi-ssf/verifyMsgCode";

	private static String CLEAN_USER = "/phoneapi-ssf/test/clean/user/{uuid}";

	private static final String REQUEST_IS_SUCCESS = "1";

	private static final int DEFAULT_PASSWORD_LENGTH = 10;

	private static final long TIMEOUT = 60000L;

	//注册 登录所用的手机号  初始值随机生成
	private static String registration_login_phone_number = null;

	// 注册 登录共用密码  初始值随机生成
	private static String registration_login_password = null;

	// 注册成功用户uuid
	private static String registration_login_user_uuid = null;

	private static String login_user_token = null;


	@Autowired
	LoginController loginController;

	@Autowired
	TestRestTemplate restTemplate;



	public int port;

	@BeforeClass
	public static void prepareParameter() {
		registration_login_phone_number = RandomPhoneNumUtil.generatePhoneNumber();
		registration_login_password = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
	}


	@Before
	public void setup() {
		RestAssured.port = port;
	}

	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/registration
	 * 接口作用：用户注册
	 * 参数：{telNum ：手机号码，password ：密码，verifyCode ：验证码}
	 **/
	@Test
	public void a_registrationTest() {
		String verifyCode = getVerifyCode(registration_login_phone_number);

		given().filter(new ResponseLoggingFilter())
				.param("telNum", registration_login_phone_number)
				.param("password", registration_login_password)
				.param("verifyCode", verifyCode)
				.post(REGISTRATION)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT))
				.using();
	}

	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/userlogin
	 * 接口作用：用户登录
	 * 参数：{telNum ：手机号码，password ：密码}
	 **/
	@Test
	public void b_userLoginTest() {
		given().filter(new ResponseLoggingFilter())
				.param("telNum", registration_login_phone_number)
				.param("password", registration_login_password)
				.post(USER_LOGIN)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT));


		String body = given().filter(new ResponseLoggingFilter())
				.param("telNum", registration_login_phone_number)
				.param("password", registration_login_password)
				.post(USER_LOGIN)
				.asString();

		JSONObject bodyJson = JSONObject.parseObject(body);
		JSONObject result = bodyJson.getJSONObject("result");
		if (result != null && result.size() > 0) {
			login_user_token = result.getString("token");
			registration_login_user_uuid = result.getString("uuid");
		}

	}


	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/forgottenPsw
	 * 接口作用：忘记密码后重置密码
	 * 参数：{telNum ：手机号码，password ：密码，verifyCode ：验证码}
	 **/
	@Test
	public void c_forgetPswTest() {
		String password = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);
		try {
			Thread.sleep(60000);
		}catch (Exception e){
			System.out.println("错误信息："+e);
		}

		String verifyCode = getVerifyCode(registration_login_phone_number);
		given().filter(new ResponseLoggingFilter())
				.param("telNum", registration_login_phone_number)
				.param("password", password)
				.param("verifyCode", verifyCode)
				.post(FORGET_PASSWORD)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(120000L));

		registration_login_password = password;

	}


	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/resetPsw
	 * 接口作用：修改密码
	 * 参数：{telNum ：手机号码，newPWD ：新密码，oldPWD ：旧密码}
	 **/
	@Test
	public void d_resetPswTest() {
		String newPWD = RandomStringUtils.randomAlphanumeric(DEFAULT_PASSWORD_LENGTH);

		given().filter(new ResponseLoggingFilter())
				.param("uuid", registration_login_user_uuid)
				.param("newPWD", newPWD)
				.param("oldPWD", registration_login_password)
				.post(RESET_PASSWORD)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT));

		registration_login_password = newPWD;

	}


	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/logout
	 * 接口作用：用户推出
	 * 参数：{uuid ：用户ID，token ：用户口令}
	 **/
	@Test
	public void e_logoutTest() {

		given().filter(new ResponseLoggingFilter())
				.param("uuid", registration_login_user_uuid)
				.param("token", login_user_token)
				.post(LOGIN_OUT)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT));

	}

	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/requestVerifyCode
	 * 接口作用：发送验证码
	 * 参数：{telNum ：手机号码}
	 **/
	@Test
	public void requestVerifyCodeTest() {
		String telNum = RandomPhoneNumUtil.generatePhoneNumber();

		given().filter(new ResponseLoggingFilter())
				.param("telNum", telNum)
				.post(REQUEST_VERIFY_CODE)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.identifyingCode", notNullValue())
				.time(lessThan(TIMEOUT));

	}

	/**
	 * 注意：该测试必须要从头至尾，按顺序测试！
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/verifyMsgCode
	 * 接口作用：验证手机号与验证码是否正确
	 * 参数：{telNum ：手机号码，msgCode ：验证码}
	 **/
	@Test
	public void verifyMsgCodeTest() {
		String telNum = RandomPhoneNumUtil.generatePhoneNumber();
		String msgCode = getVerifyCode(telNum);

		given().filter(new ResponseLoggingFilter())
				.param("telNum", telNum)
				.param("msgCode", msgCode)
				.post(VERIFY_MSG_CODE)
				.then()
				.log().all()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.time(lessThan(TIMEOUT));

	}

	/**
	 * send and get verify code
	 *
	 * @param phone_number
	 * @return
	 */
	private String getVerifyCode(String phone_number) {
		Map<String, String> result =  (HashMap) loginController.sendVerifyCode(phone_number).getResult();
		return result.get("identifyingCode");
	}

	@AfterClass
	public static void cleanUser() {
		given().delete(CLEAN_USER, registration_login_user_uuid);
	}
}



