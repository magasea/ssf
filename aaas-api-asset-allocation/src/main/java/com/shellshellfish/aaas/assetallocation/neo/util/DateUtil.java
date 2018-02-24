package com.shellshellfish.aaas.assetallocation.neo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Author: yongquan.xiong
 * Date: 2018/1/19
 * Desc:
 */
public class DateUtil {

    private static final Logger logger = LoggerFactory.getLogger(DateUtil.class);

    public static final String DATE_FORMAT = "yyyy-MM-dd";
    public static final SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);

    public synchronized static Date getDateFromFormatStr(String formatStr) {
        try {
            return sdf.parse(formatStr);
        } catch (ParseException e) {
            logger.error("日期数据转换失败");
            e.printStackTrace();
        }
        return null;
    }

    public synchronized static String formatDate(Date date) {
        return sdf.format(date);
    }

}
