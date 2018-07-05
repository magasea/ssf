package com.shellshellfish.aaas.common.utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.Set;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.springframework.util.CollectionUtils;

/**
 * Created by chenwei on 2018- 一月 - 08
 */
public class TradeUtilTest {

  @Test
  public void getReadableDateTime() throws Exception {
    System.out.println(TradeUtil.getUTCTime());
    System.out.println(TradeUtil.getReadableDateTime(TradeUtil.getUTCTime()));
  }

  @Test
  public void getZZOpenId() throws Exception {
    String origin = "612727198301116032";
    System.out.println(TradeUtil.getZZOpenId(origin));
  }

  @Test
  public void getZZOpenIds() throws Exception {
    String[] origins = {"11022319850127211X","11022619850111011X","11022619850127211X",
        "11022619850127212X","352230198703172130","362522198709220031","370181199001026835",
        "370181199001206536","411327198710181169","412827199205132051","",
        "612727198301116032"};
    for(String origin : origins){
      System.out.println(TradeUtil.getZZOpenId(origin));
    }
  }

  @Test
  public void getUTCTimeTodayStartTime() throws Exception {
    System.out.println(ZoneId.systemDefault().getId());
    long currentZoneDayStartTime = TradeUtil.getUTCTimeTodayStartTime(ZoneId.systemDefault().getId
        ());
    long currentUtcDayTime = TradeUtil.getUTCTime();
    long offset = currentUtcDayTime - currentZoneDayStartTime;
    System.out.println("currentZoneDayTime - currentZoneDayStartTime = " +
        offset );

  }

  @Test
  public void getUTCTime() throws Exception {
    System.out.println(TradeUtil.getUTCTime());
  }

  @Test
  public void getUTCTime1HourBefore() throws Exception {
    System.out.println(TradeUtil.getUTCTime1HourBefore());
  }

  @Test
  public void getUTCTime1DayBefore() throws Exception {
    Long utcTimeNow = TradeUtil.getUTCTime();
    Long utcTime1HourBefore = TradeUtil.getUTCTime1HourBefore();
    Long utcTime1DayBefore = TradeUtil.getUTCTime1DayBefore();
    System.out.println(utcTimeNow);
    System.out.println(utcTime1HourBefore);
    System.out.println(utcTime1DayBefore);

    Long deltaOfHour = utcTimeNow - utcTime1HourBefore;
    Long deltaOfDay = utcTimeNow - utcTime1DayBefore;
    BigDecimal cacluateDayInHour = BigDecimal.valueOf(deltaOfDay).divide(BigDecimal.valueOf
        (deltaOfHour), BigDecimal.ROUND_HALF_UP);
    System.out.println("deltaOfDay:" + deltaOfDay + " deltaOfHour:" + deltaOfHour + " "
        + "caculateDayInHour:" + cacluateDayInHour);
    Set<String> allZoneIds = ZoneId.getAvailableZoneIds();
    for(String zoneId: allZoneIds){
      System.out.println(zoneId);
    }
    LocalDateTime dateTime = LocalDateTime.now();
    LocalDateTime oneHourBefore = dateTime.plusHours(-1);
    LocalDateTime oneDayBefore = dateTime.plusDays(-1);

    ZonedDateTime utcDateTime = ZonedDateTime.of(dateTime, ZoneId.of("UTC"));
    ZonedDateTime utcDateTime1HourBefore = ZonedDateTime.of(oneHourBefore, ZoneId.of("UTC"));
    ZonedDateTime utcDateTime1DayBefore = ZonedDateTime.of(oneDayBefore, ZoneId.of("UTC"));

    Long utcDateTimeInLong = utcDateTime.toInstant().toEpochMilli();
    Long utcDateTime1HourBeforeInLong = utcDateTime1HourBefore.toInstant().toEpochMilli();
    Long utcDateTime1DayBeforeInLong = utcDateTime1DayBefore.toInstant().toEpochMilli();

    System.out.println(utcDateTimeInLong);
    System.out.println(utcDateTime1HourBeforeInLong);
    System.out.println(utcDateTime1DayBeforeInLong);

    deltaOfHour = utcDateTimeInLong - utcDateTime1HourBeforeInLong;
    deltaOfDay = utcDateTimeInLong - utcDateTime1DayBeforeInLong;
    cacluateDayInHour = BigDecimal.valueOf(deltaOfDay).divide(BigDecimal.valueOf
        (deltaOfHour), BigDecimal.ROUND_HALF_UP);
    System.out.println("deltaOfDay:" + deltaOfDay + " deltaOfHour:" + deltaOfHour + " "
        + "caculateDayInHour:" + cacluateDayInHour);


  }

  @Test
  public void getTplusNDayOfWork(){
    System.out.println(TradeUtil.getTplusNDayOfWork(TradeUtil.getUTCTime(), 1));
  }

  @Test
  public void getSpecificTime(){
    System.out.println(TradeUtil.getUTCOfSpecificTimeToday(6,30));
    System.out.println(TradeUtil.getUTCOfSpecificTimeToday(7,30));
  }

  @Test
  public void getDateOfSpecificTimeToday(){
    System.out.println(TradeUtil.getDateOfSpecificTimeToday(6,30).toString());
  }

  @Test
  public void getDateOfSpecificTime(){
    System.out.println(TradeUtil.getUTCTimeOfSpecificTime(2018,01, 26, 20, 1));
    System.out.println(TradeUtil.getUTCTimeOfSpecificTime(2018,01, 26, 20, 8));
  }

  @Test
  public void testPassword(){
//    System.out.println(MD5.)

    String test = String.format("%s-%s","test", "name");
    System.out.println(test);
  }

  @Test
  public void testLongParseNagativeStr(){
    String orig = "-10000";
    System.out.println(Long.parseLong(orig));
  }

  @Test
  public void getOrderIdByOutsideOrderNo() throws Exception {
    System.out.println(TradeUtil.getOrderIdByOutsideOrderNo("955843600000000015166096170491078",
        1078L));
  }

  @Test
  public void testDividLong(){
    System.out.println(TradeUtil.getLongWithDiv(110480L, 100L));
  }

  @Test
  public void testDiffDays() throws Exception {
    System.out.println(TradeUtil.getDifferentDays(TradeUtil.getUTCTime(), "2018-06-01"));
  }

  @Test
  public void getDayBefore() {
    System.out.println(TradeUtil.getDayBefore("2018-06-01", 1));
    System.out.println(TradeUtil.getDayBefore("20180601", 1));
  }

  @Test
  public void getLongNumWithMul1000000() {
    System.out.println(TradeUtil.getLongNumWithMul1000000(1.44164D));
  }
  @Test
  public void testBigDecimalRoundUp(){
    Long shares = 380L;
    Long navadj = 1000063L;
    BigDecimal number = new BigDecimal(shares*1000000L);
    BigDecimal divider = new BigDecimal(navadj);
    BigDecimal result = number.divide(divider, 2, RoundingMode.HALF_DOWN);
    System.out.println(result);
    System.out.println(TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundUp(shares*1000000L,
        navadj).intValue());
  }

  @Test
  public void testBigDecimalRoundDown(){
    Long shares = 380L;
    Long sellPercent = 4555L;
    Long number = shares*sellPercent;
    Long dividerL = 10000L;
    BigDecimal num = new BigDecimal(number);
    BigDecimal divider = new BigDecimal(dividerL);
    BigDecimal result = num.divide(divider, 2, RoundingMode.HALF_DOWN);
    System.out.println(result);
    System.out.println(TradeUtil.getBigDecimalNumWithDivOfTwoLongAndRundDown(shares*sellPercent,
        dividerL).intValue());
  }

  @Test
  public void testTimeDiff(){
    Long now = TradeUtil.getUTCTime();
    try {
      Thread.sleep(1000);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Long nowNew = TradeUtil.getUTCTime();
    System.out.println(now - nowNew);
  }

  @Test
  public void testCollectionSort(){
    ArrayList<Long> values = new ArrayList<>();
    for(long idx = 0; idx< 10; idx++){
      values.add(idx);
    }
    System.out.println("Before sort");
    for(long item: values){
      System.out.println(item);
    }
    Collections.sort(values, new Comparator<Long>() {
      @Override
      public int compare(Long o1, Long o2) {
        return Math.toIntExact(o2 - o1);
      }
    });
    System.out.println("after sort");
    for(long item: values){
      System.out.println(item);
    }

  }

  @Test
  public void transferDate(){
    String confirmdate = "20180909";
    if(!StringUtils.isEmpty(confirmdate) && !confirmdate.contains("-")){
      if(confirmdate.length() >= 8){
        System.out.println(confirmdate.substring(0,4)+"-"+confirmdate.substring(4,6)+"-" +
            confirmdate.substring(6,8));
      }
    }
  }
}