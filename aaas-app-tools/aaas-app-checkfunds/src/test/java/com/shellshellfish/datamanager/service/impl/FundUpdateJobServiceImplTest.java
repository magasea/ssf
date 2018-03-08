package com.shellshellfish.datamanager.service.impl;

import com.shellshellfish.fundcheck.service.FundUpdateJobService;
import java.io.File;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 三月 - 08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles(profiles="dev")
public class FundUpdateJobServiceImplTest {

  @Autowired
  FundUpdateJobService fundUpdateJobService;

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;

  @Value("${shellshellfish.csvFileOriginName}")
  String csvFileOriginName;

  @Test
  public void checkAndUpdateFunds() throws Exception {
    fundUpdateJobService.checkAndUpdateFunds(csvFilePath + File.separator+ csvFileOriginName);
  }


}
