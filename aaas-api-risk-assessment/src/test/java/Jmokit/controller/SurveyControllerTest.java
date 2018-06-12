package Jmokit.controller;

/*使用Jmockit进行单元测试*/

import com.shellshellfish.aaas.risk.controller.SurveyController;
import com.shellshellfish.aaas.risk.model.dao.SurveyResult;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.service.impl.SurveyResultServiceImpl;
import com.shellshellfish.aaas.risk.service.impl.SurveyTemplateServiceImpl;
import com.shellshellfish.aaas.risk.utils.ResourceWrapper;
import mockit.*;
import mockit.integration.junit4.JMockit;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Map;

import static org.junit.Assert.*;
@RunWith(JMockit.class)
public class SurveyControllerTest {
		@Tested
		SurveyController surveyController;
		@Test
		public void testGetSurveyTemplate(@Injectable 	SurveyTemplateServiceImpl surveyTemplateService)throws  Exception {
			new NonStrictExpectations(){{
				SurveyTemplateDTO surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");
				result=null;
			}};
			ResponseEntity<ResourceWrapper<SurveyTemplateDTO>> responseEntity = surveyController.getSurveyTemplate("1");
			assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
			assertEquals("/api/riskassessment/banks/1/surveytemplates/latest",responseEntity.getBody().getLinks().getSelf());
			new Verifications(){{
				SurveyTemplateDTO surveyTemplate = surveyTemplateService.getSurveyTemplate("南京银行个人客户风险评估表", "1.0");
				times=1;
			}};
		}
		@Test
		public  void TestsaveSurveyResult(@Injectable SurveyResultServiceImpl surveyResultService,@Injectable SurveyResult surveyResult )throws  Exception{
			new NonStrictExpectations(){{
				surveyController.saveSurveyResult("1","1",surveyResult);
			}};
			ResponseEntity<Map<String, String>> responseEntity = surveyController.saveSurveyResult("1", "1", surveyResult);
			assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
			assertEquals("保守型",responseEntity.getBody().get("riskLevel"));
			new Verifications(){{
				surveyController.saveSurveyResult("1","1",surveyResult);
				times=1;
			}};
		}
}
