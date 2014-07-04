package com.tieto.parser.log4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.tieto.parser.converter.Converter;

/**
 * Converts time string to Date.
 */
public class LogDateConverter implements Converter<Date> {
    public Date convert(String input) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss,SSS",
                    new Locale("en"));
            return dateFormat.parse(input);
        } catch (ParseException e) {
            throw new com.tieto.parser.ParseException(e.getMessage(), e);
        }
    }
}
