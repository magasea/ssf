package com.shellshellfish.aaas.common.utils;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Locale;
import java.util.Set;
import org.junit.Test;

/**
 * Created by chenwei on 2018- 一月 - 08
 */
public class TradeUtilTest {

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
//    for(String zoneId: allZoneIds){
//      System.out.println(zoneId);
//    }
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

}