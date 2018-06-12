package com.shellshellfish.aaas.datamanager.service.impl;

import com.shellshellfish.aaas.tools.fundcheck.FundCheckServiceApplication;
import com.shellshellfish.aaas.tools.fundcheck.service.FundUpdateJobService;
import com.shellshellfish.aaas.tools.fundcheck.utils.FileUtils;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 三月 - 08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FundCheckServiceApplication.class, webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles(profiles="dev")
public class FundUpdateJobServiceImplTest {

  @Autowired
  FundUpdateJobService fundUpdateJobService;

  @Value("${shellshellfish.csvFilePath}")
  String csvFilePath;

  @Value("${shellshellfish.csvFundFileOriginName}")
  String csvFundFileOriginName;

  @Test
  public void checkAndUpdateFunds() throws Exception {


    String pathFile = "D:\\working\\aaas\\temp\\csvFundsInfo";
    String targetFile = pathFile + ".updated";
    FileUtils.trimFile(pathFile, targetFile);
    FileInputStream fis = new FileInputStream(pathFile);
    InputStreamReader isr = new InputStreamReader(fis);

    // the name of the character encoding returned
    String test= "12345";
    System.out.println(test.substring(0, test.length() -1));
    System.out.print("Character Encoding: "+isr.getEncoding());
    fundUpdateJobService.checkAndUpdateFunds(targetFile);
  }


}
