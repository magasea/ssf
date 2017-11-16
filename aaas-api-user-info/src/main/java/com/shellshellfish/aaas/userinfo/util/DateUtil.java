package com.shellshellfish.aaas.userinfo.util;

import java.text.SimpleDateFormat;
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
        Date date=new Date(timeLong);
        SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");
        String dateText = df2.format(date);
        System.out.println(dateText);
        return  dateText;
    }

    public static Long getDateLongOneDayBefore(Long inputTime){
        Date inputDate = new Date(inputTime);
        return getDateOneDayBefore(inputDate);
    }



}
