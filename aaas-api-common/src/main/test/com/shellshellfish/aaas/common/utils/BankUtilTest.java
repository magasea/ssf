package com.shellshellfish.aaas.common.utils;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Created by chenwei on 2018- 一月 - 26
 */
public class BankUtilTest {



  @Test
  public void getCodeOfBank() throws Exception {
    String cardNum = "9111211210005837099";
    System.out.println(BankUtil.getCodeOfBank(cardNum));
    System.out.println(BankUtil.getNameOfBank(cardNum));
    System.out.println(BankUtil.getZZBankNameFromOriginBankName(BankUtil.getNameOfBank(cardNum)));
  }

}