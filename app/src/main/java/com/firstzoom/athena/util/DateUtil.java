package com.firstzoom.athena.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
    public static String getDisplayReviewDateFormat(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL");
        String dateTime = simpleDateFormat.format(date);
        return dateTime;
    }

    public static String getDisplayDate(Date date) {
       // SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd LLL HH:mm");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("LLL dd HH:mm");
        String dateTime = simpleDateFormat.format(date);
        return dateTime;
    }

}
