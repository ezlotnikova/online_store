package com.gmail.ezlotnikova.service.util.converter;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

import static com.gmail.ezlotnikova.service.util.converter.ConverterConstant.DATE_TIME_FORMAT_PATTERN;

@Component
public class DateTimeConverter {

    public String convertTimestampToString(Timestamp timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_PATTERN);
        return dateFormat.format(timestamp);
    }

    public Timestamp getCurrentTimestamp() {
        Date date= new Date();
        long time = date.getTime();
        return  new Timestamp(time);
    }

}