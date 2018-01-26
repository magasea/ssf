package com.shellshellfish.aaas.common.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @Author pierre 18-1-25
 *
 * java8 日期工具类 用来替换旧版本的 {@link SSFDateUtils}
 */
public class InstantDateUtil {

	private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	/**
	 * 计算两个时间之间相差天数 时间格式 : yyyy-MM-dd
	 */
	public static long getDaysBetween(String fromDate, String toDate) {
		return getDaysBetween(fromDate, toDate, DEFAULT_DATE_FORMAT_PATTERN);
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
		return time.toInstant(ZoneOffset.UTC).getEpochSecond();

	}

	public static void main(String[] args) {
		LocalDate now = format("2015-01-05");
		LocalDateTime time = now.atTime(23, 59, 59);
		System.out.println(time.toInstant(ZoneOffset.UTC).getEpochSecond());
	}

}
