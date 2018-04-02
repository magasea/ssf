package com.shellshellfish.aaas.tools.fundcheck.utils;

import org.junit.Test;

/**
 * Created by chenwei on 2018- 三月 - 14
 */

public class CheckerUtilsTest {

  @Test
  public void check(){
    String origin = "1.348097";
    String target = "1.34809714";
    System.out.println(CheckerUtils.rundUpCheckEq(origin, target));

  }

}
