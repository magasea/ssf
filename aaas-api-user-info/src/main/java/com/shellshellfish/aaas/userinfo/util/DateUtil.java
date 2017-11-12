package com.shellshellfish.aaas.userinfo.util;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {
    public static Long getDateOneDayBefore(Date inputDate){
        Calendar cal = Calendar.getInstance();
        cal.setTime(inputDate);
        cal.add(Calendar.DATE, -1); //minus number would decrement the days
        return cal.getTime().getTime();
    }

    public static Long getDateLongOneDayBefore(Long inputTime){
        Date inputDate = new Date(inputTime);
        return getDateOneDayBefore(inputDate);
    }

}
