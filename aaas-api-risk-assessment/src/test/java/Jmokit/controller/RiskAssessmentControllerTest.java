package Jmokit.controller;

/*使用Jmockit进行单元测试*/

import com.shellshellfish.aaas.risk.controller.RiskAssessmentController;
import com.shellshellfish.aaas.risk.model.dto.SurveyResultDTO;
import com.shellshellfish.aaas.risk.model.dto.UserRiskAssessmentDTO;
import com.shellshellfish.aaas.risk.service.impl.SurveyResultServiceImpl;
import com.shellshellfish.aaas.risk.utils.ResourceWrapper;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@RunWith(JMockit.class)
public class RiskAssessmentControllerTest {
	@Tested
	RiskAssessmentController controller;
	@Injectable
	SurveyResultServiceImpl surveyResultService;
	@Test
	public  void testGetRiskAssessment() throws  Exception
	{
			//打桩 输入方法预期执行结果
			new NonStrictExpectations(){{
				surveyResultService.getSurveyResultsByUserId("uuid-of-user-xxx");
				List<SurveyResultDTO> list=new ArrayList<>();
				list.add(new SurveyResultDTO());
				System.out.println(list.size());
				result=list;
			}};
			ResponseEntity<ResourceWrapper<UserRiskAssessmentDTO>> responseEntity = controller.getRiskAssessment("1", "uuid-of-user-xxx");
			Assert.assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
			//验证方法是否被调用及调用次数
			new Verifications(){{
				surveyResultService.getSurveyResultsByUserId("uuid-of-user-xxx");
				times=1;
			}};
		}
}
