package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.PortfolioInfo;
import com.shellshellfish.aaas.userinfo.service.UserAssetService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "dev")
public class UserAssetServiceTest {


    @Autowired
    UserAssetService userAssetService;

    @Test
    public void test() {
        Long userProdId = 27L;
        PortfolioInfo list = userAssetService.calculateUserAssetAndIncome(userProdId, InstantDateUtil.now());
        Assert.assertNotNull(list);
        Assert.assertNotNull(list.getTotalAssets());
    }

}