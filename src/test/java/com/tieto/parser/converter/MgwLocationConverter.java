package com.tieto.parser.converter;

import com.tieto.parser.Field;
import com.tieto.parser.converter.Converter;

/**
 * Converts MGW specific location string to more generic location string.
 */
public class MgwLocationConverter extends Converter {

	private static final int LENGTH = 4;
	private static final int OFFSET = 5;

	public Object convert(String input) {
		Field shelf = new Field();
		shelf.setOffset(0);
		shelf.setLength(LENGTH);
		shelf.setTrim(true);
		String shelfString = shelf.parseValue(input);
		Field location = new Field();
		location.setOffset(OFFSET);
		location.setLength(LENGTH);
		location.setTrim(true);
		String locationString = location.parseValue(input);
		return shelfString + "-" + locationString;
	}
}
