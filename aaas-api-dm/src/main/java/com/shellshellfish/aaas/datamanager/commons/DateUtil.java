package com.shellshellfish.aaas.datamanager.commons;

import java.util.Calendar;
import java.util.Date;

/**
 * yyyy-MM-dd HH:mm:ss
 *
 * @author zhaoyb
 */
@Deprecated
public class DateUtil {

	private static Date add(Date date, int calendarField, int amount) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(date);
			c.add(calendarField, amount);
			return c.getTime();
		}
	}


	/**
	 * 增加天
	 */
	public static Date addDays(Date date, int amount) {
		return add(date, 5, amount);
	}


}