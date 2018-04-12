package com.shellshellfish.aaas.transfer.service.impl;

import static org.junit.Assert.*;

import com.shellshellfish.aaas.transfer.TransferServiceApplication;
import com.shellshellfish.aaas.transfer.service.GrpcOemInfoService;
import java.util.Map;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chenwei on 2018- 四月 - 08
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = TransferServiceApplication.class)
@ActiveProfiles(profiles = "dev")
public class GrpcOemInfoServiceImplTest {
  @Autowired
  GrpcOemInfoService grpcOemInfoService;

  @Test
  public void getOemInfo(){


    Long oemId = 1L;
    Map<String, String> oemInfos = grpcOemInfoService.getOemInfoById(oemId);
    oemInfos.forEach(
        (key, value) ->{
          System.out.println(key);
          System.out.println(value);
        }
    );
  }

}