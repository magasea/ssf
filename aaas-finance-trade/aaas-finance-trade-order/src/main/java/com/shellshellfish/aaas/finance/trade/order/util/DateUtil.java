package com.shellshellfish.aaas.finance.trade.order.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
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

    public static Long getDateLongValOneDayBefore(Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DATE, -1); //minus number would decrement the days
         cal.getTime();
        return cal.getTime().getTime();
    }

    public static Long getDateLongVal(Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
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
        return getDateLongValOneDayBefore(inputDate);
    }




}
