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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        String confirmDate = "20180606";
        List<MongoUiTrdZZInfo> mongoUiTrdZZInfoList = mongoUiTrdZZInfoRepo.findAllByConfirmDate(confirmDate);
        ExecutorService es = Executors.newFixedThreadPool(30);
        List<com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo> confirmInfo = new ArrayList<>();
        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : mongoUiTrdZZInfoList) {
            com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo mongoUiTrdZZInfode = new com.shellshellfish
                    .aaas
                    .common.message.order.MongoUiTrdZZInfo();
            BeanUtils.copyProperties(mongoUiTrdZZInfo, mongoUiTrdZZInfode);
            confirmInfo.add(mongoUiTrdZZInfode);
        }

        for (int i = 0; i < confirmInfo.size(); i++) {
            com.shellshellfish.aaas.common.message.order.MongoUiTrdZZInfo zzinfo = confirmInfo.get(i);
            es.submit(() -> calculateConfirmedAsset.calculateConfirmedAsset(zzinfo));
        }

    }

}