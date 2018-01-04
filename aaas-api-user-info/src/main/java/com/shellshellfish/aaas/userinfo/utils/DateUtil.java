package com.shellshellfish.aaas.userinfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	public static Long getDateOneDayBefore(Date inputDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		cal.add(Calendar.DATE, -1); //minus number would decrement the days
		cal.getTime();
		return cal.getTime().getTime();
	}

	public static String getDateStrFromLong(Long timeLong) {
		if (timeLong.toString().length() <= 12) {
			//the time long value is in seconds
			timeLong = timeLong * 1000;
		}
		LocalDate date =
				Instant.ofEpochMilli(timeLong).atZone(ZoneId.systemDefault()).toLocalDate();
		System.out.println(date);
		return date.toString();
	}

	public static Long getDateLongOneDayBefore(Long inputTime) {
		Date inputDate = new Date(inputTime);
		return getDateOneDayBefore(inputDate);
	}

	public static String getDateType(Long inputTime) {
		Date date = new Date(inputTime + 60000 * 60 * 24);
		DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String result = format.format(date);
		return result;
	}

	/**
	 * 获取time 对应零点时间
	 *
	 * @param time
	 * @return
	 */
	public static long getDayTimeWithoutHms(Date time) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(time);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		return cal.getTimeInMillis();
	}

	/**
	 * 获取前一天零点时间
	 *
	 * @return
	 */
	public static long getDayTimeWithoutHmsOneDayBefore() {
		return getDayTimeWithoutHms(new Date(getDateOneDayBefore(new Date())));
	}

	/**
	 * 计算指定日期与今天相差天数
	 * @param date
	 * @return
	 */
	public static long getDaysToNow(Date date) {
		return getDiscrepantDays(date,new Date());
	}

	/**
	 * 计算两个日期之间相差天数
	 * @param start
	 * @param end
	 * @return
	 */
	public static int getDiscrepantDays(Date start, Date end) {
		Calendar aCalendar = Calendar.getInstance();

		aCalendar.setTime(start);

		int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);

		aCalendar.setTime(end);

		int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);

		return day2 - day1;

	}
	
	/**
	 * 获取指定某个日期之前几天的日期String，以yyyyMMdd格式返回
	 * @param start
	 * @param day
	 * @return
	 */
	public static String getSystemDatesAgo(Date start, int day) {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.add(Calendar.DAY_OF_MONTH, -day);
		date = c.getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		String dateNow = df.format(date);
		return dateNow;
	}
	

	public static void main(String[] args) {

		System.out.println(getSystemDatesAgo(new Date(),0));

	}
}
