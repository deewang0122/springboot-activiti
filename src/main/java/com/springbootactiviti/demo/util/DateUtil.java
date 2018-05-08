package com.springbootactiviti.demo.util;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.*;
import java.util.Date;

/*
*   日期工具类
* */
public class DateUtil {
    public static SimpleDateFormat sdf =   new SimpleDateFormat( " yyyy-MM-dd HH:mm:ss " );
    public static SimpleDateFormat sdfday =   new SimpleDateFormat( "yyyy-MM-dd" );
    public static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    public static Date date = new Date();

    public static Date parse(String source) {
        Date date = null;
        try {
            date = sdf.parse(source);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strDate2Date(String strDate) {

        Date date = null;
        try {
            date = df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static Date strDate2DateDay(String strDate) {

        Date date = null;
        try {
            date = sdfday.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
    /*
    * 获取当前时间yyyy-MM-dd
    * */
    public static LocalDate getTodayDate(){
        LocalDate localDate = LocalDate.now();
        return localDate;
    }
    /*
    * date to string by format
    * */
    public static String formatDate(Date date,String pattern) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
        return dateFormat.format(date);
    }
    /*
    * 0 点到晚上 12点
    * */
    public static Date getStartDate(String dataFormat) {
        SimpleDateFormat sdfStart = new SimpleDateFormat(dataFormat);
        return strToDate(sdfStart.format(date));
    }

    private static Date strToDate(String strDate) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
}
