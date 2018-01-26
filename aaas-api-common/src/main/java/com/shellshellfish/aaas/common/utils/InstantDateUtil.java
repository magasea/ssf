package com.shellshellfish.aaas.common.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * @Author pierre 18-1-25
 *
 *         java8 日期工具类 用来替换旧版本的 {@link SSFDateUtils}
 */
public class InstantDateUtil {

	private static final String DEFAULT_DATE_FORMAT_PATTERN = "yyyy-MM-dd";

	private static List<Calendar> holidayList = new ArrayList<Calendar>();
	private static List<Calendar> weekendList = new ArrayList<Calendar>();

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
	 * @param date
	 *            pattern: yyyy-MM-dd
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
		// System.out.println(format(LocalDate.now(), "yyyyMMdd"));
		long time = TradeUtil.getUTCTime();
		System.out.println(getTplusNDayNWeekendOfWork(time, 15));
	}

	public static String getTplusNDayNWeekendOfWork(Long startTime, int count) {
		String result = "";
		String holiday[] = { "2018-01-01", "2018-02-15", "2018-02-16", "2018-02-17", "2018-02-18", "2018-02-19",
				"2018-02-20", "2018-02-21", "2018-04-05", "2018-04-06", "2018-04-07", "2018-05-29", "2018-05-30",
				"2018-06-01", "2018-06-16", "2018-06-17", "2018-06-18", "2018-09-22", "2018-09-23", "2018-09-24",
				"2018-10-01", "2018-10-02", "2018-10-03", "2018-10-04", "2018-10-05", "2018-10-06", "2018-10-07" };
		String weekend[] = { "2018-02-11", "2018-02-24", "2018-04-08", "2018-05-28", "2018-09-29", "2018-09-30" };
		try {
			for (String str : holiday) {
				initHolidayList(str, 1);
			}
			for (String str : weekend) {
				initHolidayList(str, 2);
			}
			DateFormat df = new SimpleDateFormat(DEFAULT_DATE_FORMAT_PATTERN);
			Calendar calendar = Calendar.getInstance();
			ZonedDateTime zonedDateTime = ZonedDateTime.ofInstant(Instant.ofEpochMilli(startTime),
					ZoneId.systemDefault());
			LocalDateTime localDateTimeLimit = LocalDateTime.of(zonedDateTime.toLocalDate(), LocalTime.of(15, 0));
			if (zonedDateTime.toLocalDateTime().toInstant(ZoneOffset.UTC).getEpochSecond() > localDateTimeLimit
					.toInstant(ZoneOffset.UTC).getEpochSecond()) {
				count++;
			}
			for (int i = 0; i < count; i++) {
				calendar.add(Calendar.DAY_OF_MONTH, 1);
				if (checkHoliday(calendar)) {
					i--;
				}
			}

			result = df.format(calendar.getTime());
		} catch (Exception e) {
			System.out.println(e.getClass());
			e.printStackTrace();
		}
		return result;
	}

	public static boolean checkHoliday(Calendar calendar) throws Exception {
		if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
				|| calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
			for (Calendar ca : weekendList) {
				if (ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
						&& ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
						&& ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
					return false;
				}
			}
			return true;
		}
		for (Calendar ca : holidayList) {
			if (ca.get(Calendar.MONTH) == calendar.get(Calendar.MONTH)
					&& ca.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH)
					&& ca.get(Calendar.YEAR) == calendar.get(Calendar.YEAR)) {
				return true;
			}
		}

		return false;
	}

	public static void initHolidayList(String date, int flag) {
		String[] da = date.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.YEAR, Integer.valueOf(da[0]));
		calendar.set(Calendar.MONTH, Integer.valueOf(da[1]) - 1);// 月份比正常小1,0代表一月
		calendar.set(Calendar.DAY_OF_MONTH, Integer.valueOf(da[2]));
		if (flag == 1) {
			holidayList.add(calendar);
		} else if (flag == 2) {
			weekendList.add(calendar);
		}
	}
}
