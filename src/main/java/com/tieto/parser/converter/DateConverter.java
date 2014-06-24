package com.tieto.parser.converter;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * Converts time string to Date.
 */
public class DateConverter implements Converter<Date> {
	public Date convert(String input) {
		Date date = null;
		try {
			DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
			date = (Date) formatter.parse(input);
		} catch (ParseException e) {
			try {
				DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
				date = (Date) formatter.parse(input);
			} catch (ParseException e1) {
			    throw new com.tieto.parser.ParseException(e1.getMessage(), e1);
			}
		}
		return date;
	}
}
