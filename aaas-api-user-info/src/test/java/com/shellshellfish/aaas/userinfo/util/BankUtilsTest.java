package com.shellshellfish.aaas.userinfo.util;


import com.shellshellfish.aaas.userinfo.utils.BankUtil;
import java.util.ArrayList;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

public class BankUtilsTest {

  @Test
  public void LntuTest() throws Exception {
    List<Boolean> result = new ArrayList<Boolean>();
    result.add(true);
    result.add(true);
    result.add(true);
    List<Boolean> luhn = new ArrayList<Boolean>();
    luhn.add(BankUtil.Luhn("6217880800011766583"));
    luhn.add(BankUtil.Luhn("6228480031349852314"));
    luhn.add(BankUtil.Luhn("6217710201904166"));
    Assert.assertEquals(luhn,result);
  }

}