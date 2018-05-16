package com.shellshellfish.aaas.userinfo.service.impl;

/**
 * @Author pierre.chen
 * @Date 18-5-16
 */

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "pretest")
public class CheckUiProductDetailServiceTest {

    @Autowired
    CheckUiProductDetailService checkUiProductDetailService;


    @Test
    public void testCheck() {
        checkUiProductDetailService.check();
    }

}
