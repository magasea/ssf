package com.shellshellfish.aaas.finance.trade.pay.service.impl;

import com.shellshellfish.aaas.common.grpc.datacollection.DCDailyFunds;
import com.shellshellfish.aaas.finance.trade.pay.PayServiceApplication;
import com.shellshellfish.aaas.finance.trade.pay.service.DataCollectionService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by developer4 on 2018- 六月 - 04
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = PayServiceApplication.class)
@ActiveProfiles(profiles = "dev")
public class DataCollectionServiceImplTest {
  @Autowired
  DataCollectionService dataCollectionService;

  @Test
  public void getFundDataOfDay() {
    List<String> codes = new ArrayList<>();
    codes.add("003474.OF");
    String startDate = "2018-05-31";
    String endDate = "2018-06-1";
    List<DCDailyFunds> dcDailyFunds = dataCollectionService.getFundDataOfDay(codes, startDate,
        endDate);
    dcDailyFunds.forEach(
        item->{
          System.out.println(item.getNavaccum());
          System.out.println(item.getNavadj());
          System.out.println(item.getNavsimiavgreturnp());
          System.out.println(item.getNavunit());
          System.out.println(item.getNavLatestDate());
          System.out.println(item.getQuerydate());
          System.out.println(item.getUpdate());
          System.out.println(item.getCode());
          System.out.println(item.getId());
          System.out.println(item.getNavreturnrankingp());
          System.out.println(item.getNavreturnrankingpctp());
        }
    );
  }
}
