package com.gmail.ezlotnikova.service.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.gmail.ezlotnikova.service.util.converter.constant.DateTimeUtilConstant.DATE_TIME_FORMAT_PATTERN;

public class DateTimeUtil {

    public static String convertTimestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        return dateFormat.format(timestamp);
    }

    public static Timestamp getCurrentTimestamp() {
        Date date = new Date();
        long time = date.getTime();
        return new Timestamp(time);
    }

}