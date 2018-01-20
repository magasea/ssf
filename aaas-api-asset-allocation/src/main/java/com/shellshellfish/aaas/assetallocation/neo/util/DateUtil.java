package com.shellshellfish.aaas.assetallocation.neo.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2018/1/19
 * Desc:
 */
public class DateUtil {

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public static Date getDateFromFormatStr(String formatStr) {
        try {
            return sdf.parse(formatStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String formatDate(Date date) {
        return sdf.format(date);
    }

}
