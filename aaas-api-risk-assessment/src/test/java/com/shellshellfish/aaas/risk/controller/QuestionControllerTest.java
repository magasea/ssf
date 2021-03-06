package com.shellshellfish.aaas.risk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class QuestionControllerTest {

	@Autowired
	protected WebApplicationContext context;
	
	private MockMvc mvc;
	
	@Before
	public void setUp() {
		mvc = MockMvcBuilders.webAppContextSetup(context).build();	        
	}
	
	@Test
	public void testGetQuestions() throws Exception {
		mvc.perform(get("/api/riskassessments/banks/bankxxx/questions?page=0&size=2&user-uuid=xxx"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))        
        .andExpect(jsonPath("$._items").isArray())
        ;
	}
	
}
