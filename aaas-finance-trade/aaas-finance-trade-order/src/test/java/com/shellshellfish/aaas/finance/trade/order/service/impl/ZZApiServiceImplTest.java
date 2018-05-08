package com.shellshellfish.aaas.finance.trade.order.service.impl;

import com.shellshellfish.aaas.finance.trade.order.model.ZZBankInfo;
import com.shellshellfish.aaas.finance.trade.order.service.ZZApiService;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by developer4 on 2018- 五月 - 07
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class ZZApiServiceImplTest {

  @Autowired
  ZZApiService zzApiServiceImpl;

  @Test
  public void getZZSupportedBanks() throws Exception {
    List<ZZBankInfo> zzBankInfoList =  zzApiServiceImpl.getZZSupportedBanks();
    System.out.println(zzBankInfoList.size());

  }
}
