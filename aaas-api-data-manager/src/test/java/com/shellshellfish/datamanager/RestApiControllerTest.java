package com.shellshellfish.datamanager;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@RunWith(SpringRunner.class)
@SpringBootTest(classes=com.shellshellfish.datamanager.AccountServiceApplication.class)
//@ActiveProfiles(profiles="prod")
public class RestApiControllerTest {
	
	//@Autowired
	private MockMvc mvc;
	
	@Autowired
    protected WebApplicationContext wac;

	public static String URL_HEAD="/api/useraccount";
	
	@Before()  //这个方法在每个方法执行之前都会执行一遍
    public void setup() {
		mvc = MockMvcBuilders.webAppContextSetup(wac).build();  //初始化MockMvc对象
    }
	
	
	
	
	
			
}
