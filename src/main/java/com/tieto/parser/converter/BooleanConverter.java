package com.tieto.parser.converter;


/**
 * Converts 'Y' to true and any other string to false
 */
public class BooleanConverter implements Converter<Boolean> {

	public Boolean convert(String input) {
		if (input.equals("Y")) {
			return Boolean.TRUE;
		} else {
			return Boolean.FALSE;
		}
	}
}
