package transfer.controller;

import com.shellshellfish.aaas.dto.Answer;
import com.shellshellfish.aaas.dto.OptionItem;
import com.shellshellfish.aaas.dto.SurveyResult;
import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import io.restassured.RestAssured;
import io.restassured.filter.log.ResponseLoggingFilter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @Author pierre
 * 17-12-19
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("it")
@EnableAutoConfiguration
public class RishControllerTest {


	private String SURVEY_RESULTS = "/phoneapi-ssf/surveyresults";
	private String SURVEY_TEMPLATES_LATEST = "/phoneapi-ssf/surveytemplates/latest";


	private String SURVEY_TEMPLATES_LATEST_JSON_SCHEMA = "riskController-surveyTemplatesLatest.json";


	private static final String REQUEST_IS_SUCCESS = "1";


	@LocalServerPort
	public int port;

	@Before
	public void setup() {
		RestAssured.port = port;
	}


	@Test
	public void surveyresultsTest() {
		String bankUuid = "1";
		String userUuid = "3a0bc5f0-c491-4718-a6c2-dc716ae308f9";
		SurveyResult surveyResult = new SurveyResult();


		OptionItem optionItem = new OptionItem();
		optionItem.setContent("1");
		optionItem.setName("1");
		optionItem.setOrdinal(0);
		optionItem.setScore(0);
		optionItem.setText("1");

		Answer answer = new Answer();
		answer.setSelectedOption(optionItem);

		List<Answer> list = new ArrayList<>(1);
		surveyResult.setAnswers(list);


		given().contentType("application/json")
				.body(surveyResult)
				.queryParam("bankUuid", bankUuid)
				.queryParam("userUuid", userUuid)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SURVEY_RESULTS)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body("result.riskLevel", notNullValue())
				.body("result.msg", notNullValue());
	}


	@Test
	public void surveyTemplatesLatestTest() {
		String bankId = "1";

		given()
				.param("bankId", bankId)
				.filter(new ResponseLoggingFilter())
				.when()
				.post(SURVEY_TEMPLATES_LATEST)
				.then().log().all()
				.assertThat()
				.body("head.status", equalTo(REQUEST_IS_SUCCESS))
				.body(matchesJsonSchemaInClasspath(SURVEY_TEMPLATES_LATEST_JSON_SCHEMA));
	}

}