package com.shellshellfish.aaas.account.controller;

import com.shellshellfish.aaas.account.AccountServiceApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = AccountServiceApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@ActiveProfiles(profiles = "dev")
public class TestControllerTest {


	@Autowired
	TestController testController;


	@Test
	public void cleanUser() throws Exception {
		String uuid = "8cfc433a-b683-45e2-92e3-e980c11479a0";
		Assert.assertTrue(testController.cleanUser(uuid));
	}


}
