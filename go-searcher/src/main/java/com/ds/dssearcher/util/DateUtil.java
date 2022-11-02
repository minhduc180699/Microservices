package com.ds.dssearcher.util;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {
    public static List<String> add(Integer number, String type) throws Exception {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        String toDate = getCurrentDateToString(now);
        switch (type) {
            case "h":
                cal.add(Calendar.HOUR, - number);
                break;
            case "d":
                cal.add(Calendar.DAY_OF_MONTH, - number);
                break;
            case "w":
                cal.add(Calendar.DAY_OF_WEEK, - number);
                break;
            case "m":
                cal.add(Calendar.MONTH, - number);
                break;
            case "y":
                cal.add(Calendar.YEAR, - number);
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: " + type);
        }
        String fromDate = getCurrentDateToString(cal.getTime());
        return Arrays.asList(fromDate, toDate);
    }

    public static String getCurrentDateToString(Date date) throws Exception {
        if(date == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        return dateFormat.format(date);
    }

    public static Date getStringToDate(String dateStr) throws Exception {
        if(dateStr == null) return null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateStr);
    }
    public static void main(String[] args) {
        try {
            System.out.println(add(1, "y"));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
