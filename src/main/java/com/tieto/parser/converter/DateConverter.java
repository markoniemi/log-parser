package com.tieto.parser.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Converts time string to Date.
 */
public class DateConverter extends Converter<Date> {
    private static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private String format = DEFAULT_DATE_FORMAT;

    public DateConverter() {
        // need to define default constructor when there is another constructor
    }
    
    public DateConverter(String format) {
        this.format = format;
    }

    public Date convert(String input) {
        Date date = null;
        try {
            DateFormat formatter = new SimpleDateFormat(format, new Locale("en"));
            date = (Date) formatter.parse(input);
        } catch (ParseException e) {
            throw new com.tieto.parser.ParseException(e.getMessage(), e);
        }
        return date;
    }
}
