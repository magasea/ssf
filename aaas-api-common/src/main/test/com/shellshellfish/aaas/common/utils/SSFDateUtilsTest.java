package com.shellshellfish.aaas.common.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2017- 十二月 - 26
 */
public class SSFDateUtilsTest {

  @Test
  public void getYestdayDateInLong() throws Exception {
    System.out.println(SSFDateUtils.getYestdayDateInLong());
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-12-29"));
  }

  @Test
  public void getDateLongVal() throws Exception {
    System.out.println( SSFDateUtils.getDateStrFromLong(1513785600L));
    System.out.println( SSFDateUtils.getDateStrFromLong(1514217600L));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-01"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-12-01"));
  }

}