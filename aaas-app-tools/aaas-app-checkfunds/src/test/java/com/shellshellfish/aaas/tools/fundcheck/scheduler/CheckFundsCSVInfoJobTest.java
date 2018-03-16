package com.shellshellfish.aaas.tools.fundcheck.scheduler;

import com.shellshellfish.aaas.tools.fundcheck.FundCheckServiceApplication;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import java.nio.file.Paths;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 三月 - 13
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FundCheckServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="dev")
public class CheckFundsCSVInfoJobTest {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;

  @Value("${shellshellfish.csvFundFileOriginName}")
  String csvFundFileOriginName;

  @Value("${shellshellfish.csvBaseFileOriginName}")
  String csvBaseFileOriginName;

  @Autowired
  private FundUpdateJobService fundUpdateJobService;

  @Test
  public void execute() throws Exception {
    fundUpdateJobService.checkAndUpdateFunds(Paths.get(csvFilePath, csvFundFileOriginName)
        .toString());

    fundUpdateJobService.checkAndUpdateFunds(Paths.get(csvFilePath, csvBaseFileOriginName)
        .toString());

  }



}
