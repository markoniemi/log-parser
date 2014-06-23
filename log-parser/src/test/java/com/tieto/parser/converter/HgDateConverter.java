package com.tieto.parser.converter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.tieto.parser.converter.Converter;

/**
 * Converts time string to Date.
 */
public class HgDateConverter implements Converter<Date> {
    public Date convert(String input) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy",
                    new Locale("en"));
            return dateFormat.parse(input);
        } catch (ParseException e) {
            throw new com.tieto.parser.ParseException(e.getMessage(), e);
        }
    }
}
