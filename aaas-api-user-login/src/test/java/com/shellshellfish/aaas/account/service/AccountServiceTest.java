package com.shellshellfish.aaas.account.service;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles = "test")
public class AccountServiceTest {

    @Autowired
    AccountService accountService;


    @Test
    public void testSendMessage() {
        String result = accountService.sendSmsMessage("18817573489");
        Assert.assertNotNull(result, "短信发送失败！");
    }
}
