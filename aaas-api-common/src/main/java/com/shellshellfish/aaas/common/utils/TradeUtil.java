package com.shellshellfish.aaas.common.utils;

import com.google.common.hash.Hashing;
import com.shellshellfish.aaas.common.enums.TrdOrderStatusEnum;
import com.shellshellfish.aaas.common.enums.TrdZZCheckStatusEnum;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.TimeZone;


public class TradeUtil {
  private static String generateOrderId(Long bankId, int tradeBrokerId ){
    Long utcTime = getUTCTime();
    System.out.println(utcTime);
    StringBuilder sb = new StringBuilder();
    sb.append(bankId).append(String.format("%04d", tradeBrokerId)).append(String.format("%018d", utcTime));
    return sb.toString();
  }

  public static String generateOrderIdByBankCardNum(String bankCardNum, int tradeBrokerId){
    Long utcTime = getUTCTime();
    System.out.println(utcTime);
    Long disOrderedBankCardId = Long.valueOf(bankCardNum.substring(bankCardNum.length() -4 )+
        bankCardNum.substring(0, 3));
    StringBuilder sb = new StringBuilder();
    sb.append(disOrderedBankCardId).append(String.format("%04d", tradeBrokerId)).append(String.format("%018d", utcTime));
    return sb.toString();
  }

  public static Long getUTCTime(){

    ZonedDateTime utcDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"));
    return utcDateTime.toInstant().toEpochMilli();
  }

  public static Long getUTCTime1HourBefore(){

    ZonedDateTime utcDateTime = ZonedDateTime.of(LocalDateTime.now().plusHours(-1), ZoneId.of
        ("UTC"));
    return utcDateTime.toInstant().toEpochMilli();

  }

  public static Long getUTCTimeHoursBefore(int hours){
    int absHours = Math.abs(hours);
    ZonedDateTime utcDateTime = ZonedDateTime.of(LocalDateTime.now().plusHours(-absHours), ZoneId.of
        ("UTC"));
    return utcDateTime.toInstant().toEpochMilli();

  }

  public static Long getUTCTime1DayBefore(){
    ZonedDateTime utcDateTime = ZonedDateTime.of(LocalDateTime.now().plusDays(-1), ZoneId.of
        ("UTC"));
    return utcDateTime.toInstant().toEpochMilli();
  }

  public static Long getUTCTimeTodayStartTime(String zoneId){
    long utcDateStartTimeOffset = ZonedDateTime.now(ZoneId.of(zoneId)).toOffsetDateTime().toInstant
        ().toEpochMilli();
    System.out.println("utcDateStartTime：" + utcDateStartTimeOffset);
    ZonedDateTime utcDateTime = ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC"));
    long currentUTCtime = utcDateTime.toInstant().toEpochMilli();
    System.out.println("currentUTCtime：" + currentUTCtime);

    return currentUTCtime - utcDateStartTimeOffset;
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

  public static BigDecimal getBigDecimalNumWithDiv10000(Long originNum){


    return new BigDecimal(originNum).divide(BigDecimal.valueOf(10000));

  }

  public static BigDecimal getBigDecimalNumWithDiv10000(Integer originNum){


    return new BigDecimal(originNum).divide(BigDecimal.valueOf(10000));

  }
  public static int getPayFlowStatus(String kkstat){
    if(kkstat.equals(TrdZZCheckStatusEnum.CONFIRMSUCCESS) || kkstat.equals(TrdZZCheckStatusEnum.REALTIMECONFIRMSUCESS)){
      return TrdOrderStatusEnum.CONFIRMED.getStatus();
    }else if(kkstat.equals(TrdZZCheckStatusEnum.CONFIRMFAILED) || kkstat.equals
        (TrdZZCheckStatusEnum.NOTHANDLED)){
      return TrdOrderStatusEnum.FAILED.getStatus();
    }else{
      return TrdOrderStatusEnum.PAYWAITCONFIRM.getStatus();
    }
  }

  public static String getSHA256encoding(String originStr){
    String sha256hex = Hashing.sha256()
        .hashString(originStr, StandardCharsets.UTF_8)
        .toString();
    return sha256hex;
  }

  public static String getZZOpenId(String personId){
    String sha256hex = getSHA256encoding(personId+"shellshellfish");
    return sha256hex;
  }




}
