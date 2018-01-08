package com.shellshellfish.aaas.common.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2017- 十二月 - 26
 */
public class SSFDateUtilsTest {

  @Test
  public void getYestdayDateInLong() throws Exception {
    System.out.println(SSFDateUtils.getYestdayDateInLong());
//    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-12-29"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-25"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-24"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-23"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-22"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-21"));
  }

  @Test
  public void getDateLongVal() throws Exception {
    System.out.println( SSFDateUtils.getDateStrFromLong(1513785600L));
    System.out.println( SSFDateUtils.getDateStrFromLong(1514217600L));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-11-01"));
    System.out.println(SSFDateUtils.getDateLongValOneDayBefore("2017-12-01"));
  }

}