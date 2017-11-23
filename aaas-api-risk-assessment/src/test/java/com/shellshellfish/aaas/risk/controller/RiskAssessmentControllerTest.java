package com.shellshellfish.aaas.risk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class RiskAssessmentControllerTest {

	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();	        
	}
	
	@Test
	public void testGetQuestions() throws Exception {
		mvc.perform(get("/api/riskassessments/banks/1/users/1/assessment"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))        
        .andExpect(jsonPath("$._schemaVersion").value("0.1.1"))
        ;
	}
	
}
