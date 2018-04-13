package com.shellshellfish.aaas.common.utils;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * @Author pierre 18-1-25
 * <p>
 * java8 日期工具类 用来替换旧版本的 {@link SSFDateUtils}
 */
public class InstantDateUtil {


    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter
            .ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String holiday[] = {"2018-01-01", "2018-02-15", "2018-02-16", "2018-02-19",
            "2018-02-20",
            "2018-02-21", "2018-04-05", "2018-04-06", "2018-04-30", "2018-05-01", "2018-06-18",
            "2018-09-24",
            "2018-10-01", "2018-10-02", "2018-10-03", "2018-10-04", "2018-10-05"};
    private static final String weekend[] = {};
    private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";


    /**
     * 借鉴日
     **/
    private static List<LocalDate> holidayList = new ArrayList<>();

    /**
     * 周末调休工作日
     **/
    private static List<LocalDate> weekendList = new ArrayList<>();

    /**
     * 计算两个时间之间相差天数 时间格式 : yyyy-MM-dd
     */
    public static long getDaysBetween(String fromDate, String toDate) {
        return getDaysBetween(fromDate, toDate, DEFAULT_DATE_FORMAT_PATTERN);
    }

    public static LocalDate now() {
        return LocalDate.now(ZoneId.systemDefault());
    }

    /**
     * 计算两个时间之间相差天数
     */
    public static long getDaysBetween(String fromDate, String toDate, String pattern) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        LocalDate startDate = LocalDate.parse(fromDate, formatter);
        LocalDate endDate = LocalDate.parse(toDate, formatter);
        return ChronoUnit.DAYS.between(startDate, endDate);

    }

    /**
     * 字符串转{@link LocalDate}
     *
     * @param date pattern: yyyy-MM-dd
     */
    public static LocalDate format(String date) {
        return format(date, DEFAULT_DATE_FORMAT_PATTERN);
    }

    /**
     * 字符串转{@link LocalDate}
     */
    public static LocalDate format(String date, String pattern) {
        return LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
    }

    /**
     * {@link LocalDate} 转String
     */
    public static String format(LocalDate date, String pattern) {
        return DateTimeFormatter.ofPattern(pattern).format(date);
    }

    /**
     * {@link LocalDate} 转String
     */
    public static String format(LocalDate date) {
        return DateTimeFormatter.ofPattern(DEFAULT_DATE_FORMAT_PATTERN).format(date);
    }


    /**
     * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z. <p> The epoch second
     * count is a simple incrementing count of seconds where second 0 is 1970-01-01T00:00:00Z. The
     * nanosecond part of the day is returned by {@code getNanosOfSecond}. TimeZone:  UTC
     *
     * @param date pattern: yyyy-MM-dd
     * @return the seconds from the epoch of 1970-01-01T00:00:00Z
     */

    public static Long getEpochSecondOfZero(String date) {
        LocalDate localDate = format(date);
        return getEpochSecondOfZero(localDate);
    }

    /**
     * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z. <p> The epoch second
     * count is a simple incrementing count of seconds where second 0 is 1970-01-01T00:00:00Z. The
     * nanosecond part of the day is returned by {@code getNanosOfSecond}. TimeZone:  UTC
     *
     * @return the seconds from the epoch of 1970-01-01T00:00:00Z
     */
    public static Long getEpochSecondOfZero(String date, String pattern) {
        LocalDate localDate = format(date, pattern);
        return getEpochSecondOfZero(localDate);
    }

    /**
     * Gets the number of seconds from the Java epoch of 1970-01-01T00:00:00Z. <p> The epoch second
     * count is a simple incrementing count of seconds where second 0 is 1970-01-01T00:00:00Z. The
     * nanosecond part of the day is returned by {@code getNanosOfSecond}. TimeZone:  UTC
     *
     * @return the seconds from the epoch of 1970-01-01T00:00:00Z
     */
    public static Long getEpochSecondOfZero(LocalDate date) {
        LocalDateTime time = date.atTime(0, 0, 0);
        return time.toEpochSecond(OffsetDateTime.now(ZoneId.systemDefault()).getOffset());
    }


    public static LocalDate toLocalDate(long epochSecond) {

        return LocalDateTime.ofInstant(Instant.ofEpochSecond(epochSecond), ZoneId.systemDefault())
                .toLocalDate();
    }

    /**
     * 当前日期加上工作日数量
     */

    public static String getTplusNDayNWeekendOfWork(Long startTime, int count) {
        if (holidayList == null || holidayList.size() == 0) {
            for (String str : holiday) {
                initHolidayList(str, 1);
            }
            for (String str : weekend) {
                initHolidayList(str, 2);
            }
        }

        int flag = count / Math.abs(count);

        count = Math.abs(count);

        LocalDateTime localDateTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());

        LocalDateTime localDateTimeLimit = LocalDateTime
                .of(localDateTime.toLocalDate(), LocalTime.of(15, 0));

        if (localDateTime.isAfter(localDateTimeLimit)) {
            count += flag;
        }

        LocalDate localDate = localDateTime.toLocalDate();
        for (int i = 0; i < count; i++) {
            localDate = localDate.plusDays(flag);
            if (checkHoliday(localDate)) {
                i--;
            }
        }

        return format(localDate);

    }

    public static boolean checkHoliday(LocalDate date) {
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY || date.getDayOfWeek() == DayOfWeek.SATURDAY) {
            for (LocalDate localDate : weekendList) {
                if (date.equals(localDate)) {
                    return false;
                }
            }
            return true;
        }
        for (LocalDate localDate : holidayList) {
            if (localDate.equals(date)) {
                return true;
            }
        }
        return false;
    }

    public static void initHolidayList(String date, int flag) {

        if (flag == 1) {
            holidayList.add(format(date));
        } else if (flag == 2) {
            weekendList.add(format(date));
        }
    }

    public static boolean isDealDay(long startTime) {
        if (holidayList == null || holidayList.size() == 0) {
            for (String str : holiday) {
                initHolidayList(str, 1);
            }
            for (String str : weekend) {
                initHolidayList(str, 2);
            }
        }
        LocalDateTime localDateTime = LocalDateTime
                .ofInstant(Instant.ofEpochMilli(startTime), ZoneId.systemDefault());
        LocalDate localDate = localDateTime.toLocalDate();
        return checkHoliday(localDate);
    }


    public static String getDayOfWeekName(LocalDateTime localDateTime) {
        DayOfWeek dayOfWeek = localDateTime.getDayOfWeek();
        Map<String, String> weekDayMap = new HashMap<String, String>();
        weekDayMap.put("SUNDAY", "周日");
        weekDayMap.put("MONDAY", "周一");
        weekDayMap.put("TUESDAY", "周二");
        weekDayMap.put("WEDNESDAY", "周三");
        weekDayMap.put("THURSDAY", "周四");
        weekDayMap.put("FRIDAY", "周五");
        weekDayMap.put("SATURDAY", "周六");
        String dayOfWeekName = weekDayMap.get(dayOfWeek + "");
        System.out.println("getDayOfWeek : " + dayOfWeekName);
        return dayOfWeekName;
    }

    public static String getDayConvertString(Long dateTime) {
        String date = "";
        if (dateTime != null) {
            date = dateTimeFormatter.format(
                    LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime), ZoneId.systemDefault()));
        }
        return date;
    }

    public static Date localDate2Date(LocalDate localDate) {
        ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneId.systemDefault());
        return Date.from(zonedDateTime.toInstant());
    }

    public static LocalDate date2LocalDate(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }


    public static void main(String[] args) {
        System.out.println(InstantDateUtil.now().with(TemporalAdjusters.lastDayOfMonth()));
    }

}
