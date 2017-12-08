package com.shellshellfish.aaas.finance.trade.order.util;

import java.util.Calendar;
import java.util.TimeZone;

public class TradeUtil {
  public static String generateOrderId(int bankId, int tradeBrokerId ){
    Long utcTime = getUTCTime();
    System.out.println(utcTime);
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("%4d", bankId)).append(String.format("%4d", tradeBrokerId)).append
        (String.format("%018d", utcTime.toString()));
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
}
