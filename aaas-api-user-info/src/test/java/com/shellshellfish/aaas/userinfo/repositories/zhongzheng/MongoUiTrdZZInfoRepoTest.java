package com.shellshellfish.aaas.userinfo.repositories.zhongzheng;

import com.shellshellfish.aaas.common.enums.TrdOrderOpTypeEnum;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.utils.InstantDateUtil;
import com.shellshellfish.aaas.userinfo.model.dao.MongoUiTrdZZInfo;
import com.shellshellfish.aaas.userinfo.repositories.mongo.MongoUiTrdZZInfoRepo;
import io.swagger.models.auth.In;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author pierre.chen
 * @Date 18-5-2
 */
@RunWith(SpringRunner.class)
@ActiveProfiles("dev")
@DataMongoTest
public class MongoUiTrdZZInfoRepoTest {

    @Autowired
    MongoUiTrdZZInfoRepo mongoUiTrdZZInfoRepo;

    @Test
    public void test() {
        final long userProdId = 41;
        String fundCode = "000149.OF";

        LocalDate startDate = LocalDate.of(2018, 01, 22);
        List<MongoUiTrdZZInfo> result = mongoUiTrdZZInfoRepo
                .findByUserProdIdAndFundCodeAndTradeTypeAndTradeStatusAndConfirmDateGreaterThan(userProdId, fundCode,
                        TrdOrderOpTypeEnum.BUY.getOperation(), TrdOrderStatusEnum.CONFIRMED.getStatus(),
                        InstantDateUtil.format(startDate, InstantDateUtil.yyyyMMdd));


        List<String> list = new ArrayList<>(result.size());

        for (MongoUiTrdZZInfo mongoUiTrdZZInfo : result) {
            list.add(mongoUiTrdZZInfo.getConfirmDate());
        }
        Assert.assertTrue("mongo Data after  do not 　contains current day ", list.contains(InstantDateUtil.format
                (startDate, InstantDateUtil.yyyyMMdd)));
    }

}
