package transfer.controller;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.notNullValue;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
public class UserInfoControllerIT {


	private String ADD_BANKCARDS = "/phoneapi-ssf/addBankCards";

	private String ASSET = "/phoneapi-ssf/asset";

	private String BANKS = "/phoneapi-ssf/banks";

	private String CHI_COMBINATION = "/phoneapi-ssf/chicombination";

	private String INVATION_FRIENDS = "/phoneapi-ssf/invationFriends";

	private String PERSONAL_INFORMATION = "/phoneapi-ssf/personalInformation";

	private String SELECT_BANKS = "/phoneapi-ssf/selectbanks";

	private String SELL_RESULT = "/phoneapi-ssf/sellresult";

	private String SYSTEM_MSG = "/phoneapi-ssf/systemMsg";

	private String TRADE_RECORDS = "/phoneapi-ssf/traderecords";

	private String TRADE_RESULT = "/phoneapi-ssf/traderesult";


	private static final String REQUEST_IS_SUCCESS = "1";


	@LocalServerPort
	public int port;

	@Autowired
	LoginController loginController;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/addBankCards
	 * 接口作用：添加银行卡
	 * 参数：{
	 *     uuid ：用户ID，name ：用户姓名
	 *     bankCard ：银行卡号，idcard ：身份证号
	 *     mobile ：手机号，verifyCode ：验证码
	 * }
	 */
	@Test
	public void addBankCardsTest() {
		String uuid = "61f34f2f-4c06-4a58-a517-d1f16a15bfb8";
		String name = "zhangsan";
		String bankCard = RandomPhoneNumUtil.generateBankCardNoMatchingLuhn();
		String idcard = "11022619850127211X";
		String mobile = RandomPhoneNumUtil.generatePhoneNumber();
		String verifyCode = getVerifyCode(mobile);


		given()
				.param("uuid", uuid)
				.param("name", name)
				.param("bankCard", bankCard)
				.param("idcard", idcard)
				.param("mobile", mobile)
				.param("verifyCode", verifyCode)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(ADD_BANKCARDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.status", notNullValue());
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/asset
	 * 接口作用：获取用户的资产总览数据
	 * 参数：{
	 *     uuid ：用户ID，totalAssets ：总资产
	 *     dailyReturn ：日收益，totalRevenue ：累计收益
	 *     totalRevenueRate ：累计收益率
	 * }
	 */
	@Test
	public void assetTest() {
		String uuid = "shellshellfish";
		String totalAssets = "946.91";
		String dailyReturn = "-24.85";
		String totalRevenue = "-31.04";
		String totalRevenueRate = "0";


		given()
				.param("uuid", uuid)
				.param("totalAssets", totalAssets)
				.param("dailyReturn", dailyReturn)
				.param("totalRevenue", totalRevenue)
				.param("totalRevenueRate", totalRevenueRate)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(ASSET)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.totalRevenueRate", notNullValue())
				.body("result.totalAssets", notNullValue())
				.body("result.dailyReturn", notNullValue())
				.body("result.totalRevenue", notNullValue())
				.body("result.trendYield.date[0]", notNullValue())
				.body("result.trendYield.value[0]", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/banks
	 * 接口作用：根据银行卡号获得银行卡名称
	 * 参数：{
	 *     bankNum :银行卡号
	 * }
	 */
	@Test
	public void banksTest() {
		String bankNum = "6210986802084484920";

		given()
				.param("bankNum", bankNum)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(BANKS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankCode", notNullValue())
				.body("result.bankName", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/chicombination
	 * 接口作用：获得用户的智投组合数据
	 * 参数：{
	 *     uuid ：用户ID
	 * }
	 */
	@Test
	public void chiCombinationTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(CHI_COMBINATION)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result", notNullValue())
				.body("result.totalIncome[0]", notNullValue())
				.body("result.updateDate[0]", notNullValue())
				.body("result.totalAssets[0]", notNullValue())
				.body("result.dailyIncome[0]", notNullValue())
				.body("result.totalIncomeRate[0]", notNullValue())
				.body("result.prodId[0]", notNullValue())
				.body("result.title[0]", notNullValue())
				.body("result.createDate[0]", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/personalInformation
	 * 接口作用：获得个人信息数据
	 * 参数：{
	 *     uuid ：用户ID
	 * }
	 */
	@Test
	public void personalInformationTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(PERSONAL_INFORMATION)
				.then().log().all()
				.assertThat()
				.body("result.uuid", notNullValue())
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.userBaseInfo.id", notNullValue())
				.body("result.userBaseInfo.cellPhone", notNullValue())
				.body("result.userBaseInfo", hasKey("birthAge"))
				.body("result.userBaseInfo", hasKey("occupation"))
				.body("result.userBaseInfo", hasKey("passwordhash"))
				.body("result.userBaseInfo", hasKey("riskLevel"));
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/selectbanks
	 * 接口作用：获得用户的银行卡列表
	 * 参数：{
	 *     uuid ：用户ID
	 * }
	 */
	@Test
	public void selectBanksTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SELECT_BANKS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankCode[0]", notNullValue())
				.body("result.bankcardNum[0]", notNullValue())
				.body("result.bankType[0]", notNullValue())
				.body("result.bankName[0]", notNullValue())
				.body("result.bankcardSecurity[0]", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/sellresult
	 * 接口作用：根据用户、产品编号等得到交易结果（赎回）
	 * 参数：{
	 *     uuid ：用户ID，prodId ：产品ID
	 *     buyfee ：预计费用，bankName ：银行卡名称
	 *     bankCard ：银行卡号
	 * }
	 */
	@Test
	public void sellresultTest() {
		String uuid = "69ad9732-f9cd-49e9-a71f-0462cc6b4d8e";
		String prodId = "21";
		String buyfee = "100";
		String bankName = "中国银行";
		String bankCard = "0859";


		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.param("buyfee", buyfee)
				.param("bankName", bankName)
				.param("bankCard", bankCard)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SELL_RESULT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.bankInfo", notNullValue())
				.body("result.buyfee", notNullValue())
				.body("result.date2", notNullValue())
				.body("result.date1", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/systemMsg
	 * 接口作用：获得系统消息
	 * 参数：{
	 *     uuid ：用户ID
	 * }
	 */
	@Test
	public void systemMsgTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SYSTEM_MSG)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result._items.date[0]", notNullValue())
				.body("result._items.content[0]", notNullValue())
				.body("result.uuid", notNullValue());
	}

	//	@Test
	//TODO  此处用到RabbitMQ
	public void traderecordsTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(TRADE_RECORDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS));
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/traderesult
	 * 接口作用：根据用户ID、产品ID等数据获得指定的交易结果（购买）
	 * 参数：{
	 *     uuid ：用户ID，prodId ：产品ID
	 *     buyfee ：预计费用，bankName ：银行卡名称
	 *     bankCard ：银行卡号
	 * }
	 */
	@Test
	public void tradeResultTest() {

		String uuid = "1";
		String prodId = "1";
		String buyfee = "1";
		String bankName = "1";
		String bankCard = "1";


		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.param("buyfee", buyfee)
				.param("bankName", bankName)
				.param("bankCard", bankCard)
				.when()
				.post(TRADE_RESULT)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.date", notNullValue())
				.body("result.bankInfo", notNullValue())
				.body("result.buyfee", notNullValue());
	}

	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/invationFriends
	 * 接口作用：获得智投推送消息
	 * 参数：{
	 *     uuid ：用户ID
	 * }
	 */
	@Test
	public void invationFriendsTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";


		given()
				.param("uuid", uuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(INVATION_FRIENDS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.uuid", notNullValue())
				.body("result._items", notNullValue());

	}

	/**
	 * send and get verify code
	 *
	 * @param phone_number
	 * @return
	 */
	private String getVerifyCode(String phone_number) {
		Map<String, String> result = (HashMap) loginController.sendVerifyCode(phone_number).getResult();
		return result.get("identifyingCode");
	}
}
