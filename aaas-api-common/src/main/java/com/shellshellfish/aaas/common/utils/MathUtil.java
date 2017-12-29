package com.shellshellfish.aaas.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public class MathUtil {

  public static Long getLongPriceFromDoubleOrig(Double inputD){
    BigDecimal b = new BigDecimal(inputD, MathContext.DECIMAL64);
    return  b.multiply(BigDecimal.valueOf(100L)).longValue();
  }

}
