package com.shellshellfish.aaas.userinfo.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.shellshellfish.aaas.userinfo.utils.DateUtil;

public class SSFDateUtilsTest {

  @Test
  public void getDateStrFromLong() throws Exception {
    System.out.println(DateUtil.getDateStrFromLong(1510883855L));
  }

}