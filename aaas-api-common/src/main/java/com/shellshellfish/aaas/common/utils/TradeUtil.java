package com.shellshellfish.aaas.common.utils;

import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdPayFlowStatusEnum;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class TradeUtil {
  public static String generateOrderId(int bankId, int tradeBrokerId ){
    Long utcTime = getUTCTime();
    System.out.println(utcTime);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%04d", bankId)).append(String.format("%04d", tradeBrokerId)).append
        (String.format("%018d", utcTime));
    return sb.toString();
  }

  public static Long getUTCTime(){
    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    Calendar calendar = Calendar.getInstance(timeZone);
    return calendar.getTimeInMillis();
  }
  public static void main(String[] argv){
    TimeZone timeZone = TimeZone.getTimeZone("UTC");
    Calendar calendar = Calendar.getInstance(timeZone);
    calendar.set(9999,12,31);
    System.out.println(calendar.getTimeInMillis());
  }

  /**
   * 输入为有小数点的数字的字符串
   * 输出为精确到百分之一的整型
   * @param originNum
   * @return
   */
  public static Long getLongNumWithMul100(String originNum){

      BigDecimal origBigD = new BigDecimal(originNum);
      return  origBigD.multiply(BigDecimal.valueOf(100)).longValue();

  }

  /**
   * 输入为有小数点的数字的BigDecimal
   * 输出为精确到百分之一的整型
   * @param originNum
   * @return
   */
  public static Long getLongNumWithMul100(BigDecimal originNum){


    return  originNum.multiply(BigDecimal.valueOf(100)).longValue();

  }

  /**
   *
   * @param originNum
   * @return
   */
  public static double getDoubleWithDiv100(Long originNum){
    return Math.round(originNum/100.0);
  }

  public static BigDecimal getBigDecimalNumWithDiv100(Long originNum){


    return new BigDecimal(originNum).divide(BigDecimal.valueOf(100));

  }

  public static int getPayFlowStatus(String kkstat){
    if(kkstat.equals(TrdPayFlowStatusEnum.CONFIRMSUCCESS) || kkstat.equals(TrdPayFlowStatusEnum.REALTIMECONFIRMSUCESS)){
      return TrdOrderStatusEnum.CONFIRMED.getStatus();
    }else if(kkstat.equals(TrdPayFlowStatusEnum.CONFIRMFAILED) || kkstat.equals
        (TrdPayFlowStatusEnum.NOTHANDLED)){
      return TrdOrderStatusEnum.FAILED.getStatus();
    }else{
      return TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus();
    }
  }




}
