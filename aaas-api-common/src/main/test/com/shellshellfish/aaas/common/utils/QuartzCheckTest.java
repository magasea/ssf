package com.shellshellfish.aaas.common.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2018- 六月 - 15
 */

public class QuartzCheckTest {
  @Test
  public void getYestdayDateInLong() throws Exception {
    String cronExpr = "0 0/5 0 ? * *  *1";
    System.out.println(org.quartz.CronExpression.isValidExpression(cronExpr));
  }
}
