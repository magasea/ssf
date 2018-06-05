package com.shellshellfish.aaas.userinfo.service.impl;

import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class)
@ActiveProfiles(profiles = "test")
public class CalculateConfirmedAssetTest {


    @Autowired
    CalculateConfirmedAsset calculateConfirmedAsset;

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    @Test
    public void test() {
        String fundCode = "040036.OF";
        String confirmDate = "20180604";
        Long userProdId = 281L;
        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList = mongoUiTrdZZInfoRepo.findAllByUserProdIdAndFundCodeAndConfirmDate
                (userProdId, fundCode, confirmDate);
        com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo mongoUiTrdZZInfo = new com.shellshellfish.aaas
                .common.message.order.MongoUiTrdZZInfo();
        BeanUtils.copyProperties(mongoUiTrdZZInfoList.get(0), mongoUiTrdZZInfo);


        calculateConfirmedAsset.calculateConfirmedAsset(mongoUiTrdZZInfo);
    }

}