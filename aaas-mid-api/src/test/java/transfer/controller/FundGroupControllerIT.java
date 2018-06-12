package transfer.controller;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.lessThan;

import javafx.scene.media.SubtitleTrack;
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

	private static final long TIMEOUT = 3000L;

	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	/**
	 * 目的：校验接口是否返回数据与数据格式是否正确
	 * 接口：/phoneapi-ssf/getMyProductDetail
	 * 接口作用：根据参数获取我的智投组合详情数据
	 * 参数：{
	 *     uuid ：用户ID，prodId ：产品ID，
	 *     groupId ：产品组ID，stbGroupId ：子产品组ID
	 * }
	 */
	@Test
	public void getProductDetailTest() {
		String uuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		String prodId = "178";
		String groupId = "18";
		String subGroupId = "18000";
		given()
				.param("uuid", uuid)
				.param("prodId", prodId)
				.param("groupId",groupId)
				.param("subGroupId", subGroupId)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(GET_MY_PRODUCT_DETAIL)
				.then().log().all()
				.assertThat()
				.body(matchesJsonSchemaInClasspath(GET_MY_PRODUCT_DETAIL_SCHEMA_NAME)).time(lessThan(TIMEOUT));
	}


}

