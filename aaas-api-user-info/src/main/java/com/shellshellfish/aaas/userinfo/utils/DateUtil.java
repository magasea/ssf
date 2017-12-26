package com.shellshellfish.aaas.userinfo.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Long getDateOneDayBefore(Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DATE, -1); //minus number would decrement the days
         cal.getTime();
        return cal.getTime().getTime();
    }

    public static String getDateStrFromLong(Long timeLong){
        if(timeLong.toString().length() <= 12){
            //the time long value is in seconds
            timeLong = timeLong*1000;
        }
        LocalDate date =
            Instant.ofEpochMilli(timeLong).atZone(ZoneId.systemDefault()).toLocalDate();
        System.out.println(date);
        return date.toString();
    }

    public static Long getDateLongOneDayBefore(Long inputTime){
        Date inputDate = new Date(inputTime);
        return getDateOneDayBefore(inputDate);
    }

    public static String getDateType(Long inputTime){
    	Date date = new Date(inputTime+60000*60*24);
    	DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	String result = format.format(date);
		return result;
    }
}
