package com.shellshellfish.aaas.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class SSFDateUtils {

	public static Long getDateLongValOneDayBefore(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate = sdf.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		cal.add(Calendar.DATE, -1); //minus number would decrement the days
		cal.getTime();
		return cal.getTime().getTime();
	}


	public static Long getDateLongVal(String dateStr) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date inputDate = sdf.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		cal.getTime();
		return cal.getTime().getTime();
	}

	public static Long getDateLongValOneDayBefore(Date inputDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
		cal.add(Calendar.DATE, -1); //minus number would decrement the days
		cal.getTime();
		return cal.getTime().getTime();
	}


	public static Long getDateLongVal(Date inputDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(inputDate);
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
		return getDateLongValOneDayBefore(inputDate);
	}

	public static Long getCurrentDateInLong() {
		LocalTime nowInUtc = LocalTime.now(Clock.systemUTC());
		return nowInUtc.toNanoOfDay();
	}


	public static final String DATE_FORMAT = "yyyy-MM-dd";

	public static final SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);


	public static Date getDateOneDayBefore() {

		return getDateOneDayBefore(new Date());
	}

	public static Date getDateOneDayBefore(Date baseDate) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		cal.add(Calendar.DATE, -1);
		cal.getTime();
		return cal.getTime();
	}


	public static Long getYestdayDateInLong() {

		StringBuilder sb = new StringBuilder();

		Calendar today = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
//        Calendar today  = Calendar.getInstance();
		today.add(Calendar.DATE, -1);
		today.set(Calendar.HOUR_OF_DAY, 0);
		today.set(Calendar.MINUTE, 0);
		today.set(Calendar.SECOND, 0);
		today.set(Calendar.MILLISECOND, 0);

		return today.getTimeInMillis() / 1000L;
	}


	public static Long getLongTimeOfThreeYearsBefore() {
		return getDayOfThreeYearsBefore().getTime();
	}

	public static Date getDayOfThreeYearsBefore() {
		return getDayOfThreeYearsBefore(new Date());
	}


	public static Date getDayOfThreeYearsBefore(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.YEAR, -3);
		return cal.getTime();
	}

	public static void main(String[] args) {
		System.out.println(new Date());
		System.out.println(getDayOfThreeYearsBefore());
	}

}
