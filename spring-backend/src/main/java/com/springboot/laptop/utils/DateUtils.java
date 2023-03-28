package com.springboot.laptop.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

public class DateUtils {

    public static LocalDateTime dateToLocalDateTime(Date dateValue) {
        return new Timestamp(dateValue.getTime()).toLocalDateTime();
    }
}
