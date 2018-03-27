package com.shellshellfish.aaas.tools.fundcheck.utils;

import com.shellshellfish.aaas.common.utils.TradeUtil;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by chenwei on 2018- 三月 - 14
 */

public class CheckerUtils {

  public static boolean rundUpCheckEq(String originDouble, String targetDouble){
    int shortestSmallNum = 0;
    boolean roundUpForOrigin = true;
    if(originDouble.contains(".")){
      shortestSmallNum = originDouble.length() - 1 - originDouble.indexOf('.');
    }else{
      shortestSmallNum = 0;
      System.out.println(String.format("originDouble:{} targetDouble:{}", originDouble, targetDouble));
    }
    if(targetDouble.contains(".")){
      int tempNum = targetDouble.length() -1 - targetDouble.indexOf('.');
      if(shortestSmallNum > tempNum){
        shortestSmallNum = tempNum;
        roundUpForOrigin = false;
      }
    }else{
      shortestSmallNum = 0;
      System.out.println(String.format("originDouble:{} targetDouble:{}", originDouble, targetDouble));
    }
    BigDecimal origBigDecimal = new BigDecimal(originDouble);
    BigDecimal targBigDecimal = new BigDecimal(targetDouble);
//    System.out.println(shortestSmallNum);
    origBigDecimal = origBigDecimal.setScale(shortestSmallNum, RoundingMode.HALF_UP);
//    System.out.println(origBigDecimal);
    targBigDecimal = targBigDecimal.setScale(shortestSmallNum,RoundingMode.HALF_UP);
//    System.out.println(targBigDecimal);
    if(origBigDecimal.equals(targBigDecimal)){
      return true;
    }else{
      return false;
    }
  }
}
