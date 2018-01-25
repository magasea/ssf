package com.shellshellfish.aaas.common.utils;

import java.time.LocalDate;
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

	public static void main(String[] args) {
		System.out.println(format(LocalDate.now(),"yyyyMMdd"));
	}

}
