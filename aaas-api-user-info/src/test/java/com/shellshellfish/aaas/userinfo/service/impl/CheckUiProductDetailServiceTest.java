package com.shellshellfish.aaas.userinfo.service.impl;

/**
 * @Author pierre.chen
 * @Date 18-5-16
 */

import com.shellshellfish.aaas.common.utils.TradeUtil;
import com.shellshellfish.aaas.userinfo.UserInfoApp;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUserFundQuantityLog;
import com.shellshellfish.aaas.userinfo.service.CheckUiProductDetailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserInfoApp.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles = "dev")
public class CheckUiProductDetailServiceTest {

    @Autowired
    CheckUiProductDetailService checkUiProductDetailService;


    @Autowired
    CheckUiProductDetailServiceImpl checkUiProductDetailServiceImpl;

    @Test
    public void testCheck() {
        checkUiProductDetailService.check();
    }


    @Test
    public void testBulkUpdate() {

        List<MongoUserFundQuantityLog> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            MongoUserFundQuantityLog mongoUserFundQuantityLog = new MongoUserFundQuantityLog();
            mongoUserFundQuantityLog.setUserProdId(new Long(i));
            mongoUserFundQuantityLog.setProductStatusMap(new HashMap());
            mongoUserFundQuantityLog.setFundQuantityMap(new HashMap());
            mongoUserFundQuantityLog.setUiTrdZZInfoList(null);
            mongoUserFundQuantityLog.setCreateTime(TradeUtil.getUTCTime());
            mongoUserFundQuantityLog.setUpdateTime(TradeUtil.getUTCTime());
            list.add(mongoUserFundQuantityLog);
        }
        checkUiProductDetailServiceImpl.saveOrUpdate(list);
    }

}
