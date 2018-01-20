package com.shellshellfish.aaas.common.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import org.springframework.util.StringUtils;

/**
 * Created by chenwei on 2017- 十二月 - 29
 */

public class MathUtil {

  public static Long getLongPriceFromDoubleOrig(Double inputD){
    BigDecimal b = new BigDecimal(inputD, MathContext.DECIMAL64);
    return  b.multiply(BigDecimal.valueOf(100L)).longValue();
  }

  public static Double getDoubleValueFromStrWitDefaultOpt(String input){
    Double result = StringUtils.isEmpty(input)? Double.MIN_VALUE:Double.valueOf(input);
    return result;
  }

  public static BigDecimal round(BigDecimal d, int scale, boolean roundUp) {
    int mode = (roundUp) ? BigDecimal.ROUND_UP : BigDecimal.ROUND_DOWN;
    return d.setScale(scale, mode);
  }
}
