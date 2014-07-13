package com.tieto.parser.converter;

/**
 * Converts 'Y' to true and any other string to false
 */
public class BooleanConverter extends Converter<Boolean> {

    public Boolean convert(String input) {
        if ("Y".equals(input)) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
