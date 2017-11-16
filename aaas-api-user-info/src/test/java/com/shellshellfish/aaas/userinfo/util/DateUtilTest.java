package com.shellshellfish.aaas.userinfo.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class DateUtilTest {

  @Test
  public void getDateStrFromLong() throws Exception {
    System.out.println(DateUtil.getDateStrFromLong(684374400000L));
  }

}