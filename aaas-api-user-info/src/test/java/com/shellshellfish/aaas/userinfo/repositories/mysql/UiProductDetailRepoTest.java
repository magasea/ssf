package com.shellshellfish.aaas.userinfo.repositories.mysql;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.common.enums.SystemUserEnum;
import com.shellshellfish.aaas.common.utils.TradeUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.query.Param;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 一月 - 25
 */
@RunWith(value= SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class UiProductDetailRepoTest {

  @Autowired
  UiProductDetailRepo uiProductDetailRepo;

  @Test
  public void updateByAddBackQuantity() throws Exception {
    Long fundQuantity = 10L;
    Long updateDate = TradeUtil.getUTCTime();
    Long updateBy = SystemUserEnum.SYSTEM_USER_ENUM.getUserId();
    Long userProdId = 1L;
    String fundCode = "001987.OF";
    int status = 1;
    uiProductDetailRepo.updateByAddBackQuantity(fundQuantity,updateDate,updateBy,userProdId,
        fundCode,status);
  }

}