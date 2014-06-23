package com.tieto.parser.converter;


/**
 * Converts hex to Integer.
 */
public class HexConverter implements Converter<Integer> {
	private static final int RADIX = 16;

	public Integer convert(String input) {
		return Integer.parseInt(input, RADIX);
	}
}
