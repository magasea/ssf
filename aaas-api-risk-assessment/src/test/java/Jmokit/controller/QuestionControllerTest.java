package Jmokit.controller;

/*使用Jmockit进行单元测试*/

import com.shellshellfish.aaas.risk.controller.QuestionController;
import com.shellshellfish.aaas.risk.model.dto.SurveyTemplateDTO;
import com.shellshellfish.aaas.risk.service.QuestionService;
import com.shellshellfish.aaas.risk.utils.PageWrapper;
import mockit.Injectable;
import mockit.NonStrictExpectations;
import mockit.Tested;
import mockit.Verifications;
import mockit.integration.junit4.JMockit;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import static org.junit.Assert.*;
@RunWith(JMockit.class)
public class QuestionControllerTest {
	@Tested
	QuestionController questionController;
	@Injectable
	QuestionService questionService;
	@Test
	public void testgetAllQuestions(@Injectable Pageable pageabe,@Injectable Page<SurveyTemplateDTO> pages) throws  Exception{

		new NonStrictExpectations(){{
			 questionService.findByTitleAndVersion(pageabe, "南京银行个人客户风险评估表", "1.0");
		result=pages;
	}};
		ResponseEntity<PageWrapper<SurveyTemplateDTO>> responseEntity = questionController.getAllQuestions("1", pageabe, 2, 0, "sort");
		assertEquals(HttpStatus.OK,responseEntity.getStatusCode());
		assertEquals("风险测评",responseEntity.getBody().getName());
		//验证方法是否被调用及调用次数
		new Verifications(){{
			questionService.findByTitleAndVersion(pageabe, "南京银行个人客户风险评估表", "1.0");
			times=1;
		}};
	}

	
}
