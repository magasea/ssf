package com.shellshellfish.aaas.tools.fundcheck.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2018- 三月 - 14
 */

public class CheckerUtilsTest {

  @Test
  public void check(){
    String origin = "1.0";
    String target = "1";
    System.out.println(CheckerUtils.rundUpCheckEq(origin, target));

  }

}
