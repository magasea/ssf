package com.shellshellfish.fundcheck.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2018- 三月 - 14
 */

public class CheckerUtilsTest {

  @Test
  public void check(){
    String origin = "1.999999999";
    String target = "2.00";
    System.out.println(CheckerUtils.rundUpCheckEq(origin, target));

  }

}
