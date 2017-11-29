package com.shellshellfish.aaas.risk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.shellshellfish.aaas.risk.SsfRiskAssessmentApp;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SsfRiskAssessmentApp.class)
public class SurveyControllerTest {

	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();	        
	}
	
	@Test
	public void testGetSurveyTemplate() throws Exception {
		mvc.perform(get("/api/riskassessments/banks/1/surveytemplates/latest"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.version").value("1.0"))
        .andExpect(jsonPath("$._items").isArray())        
        ;
	}
	
	@Test
	public void testSaveSurveyResults() throws Exception {
		
		String requestJson = "{\r\n" + 
				"    \"id\" : null,\r\n" + 
				"    \"userId\" : \"uuid-of-user-xxx\",\r\n" + 
				"    \"surveyTemplateId\" : \"5a04267df3f60343ac267ee0\",\r\n" + 
				"    \"answers\" : [ \r\n" + 
				"        {\r\n" + 
				"            \"questionOrdinal\" : 1,\r\n" + 
				"            \"selectedOption\" : {\r\n" + 
				"                \"ordinal\" : 1,\r\n" + 
				"                \"name\" : \"A\",\r\n" + 
				"                \"content\" : \"5万元以下\",\r\n" + 
				"                \"score\" : 5\r\n" + 
				"            }\r\n" + 
				"        }\r\n" + 
				"    ]\r\n" + 
				"}";
		
		mvc.perform(post("/api/riskassessments/banks/1/users/1/surveyresults")
				.contentType(MediaType.APPLICATION_JSON)
				.content(requestJson))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn().getResponse().getContentAsString();
	}
	
}
