package com.shellshellfish.aaas.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by chenwei on 2017- 十二月 - 26
 */
public class DateUtilTest {

  @Test
  public void getDateLongVal() throws Exception {
    System.out.println( DateUtil.getDateStrFromLong(1512057600L));
  }

}