package com.tieto.parser.converter;

import com.tieto.parser.Field;
import com.tieto.parser.converter.Converter;

public class FlexiNSLocationConverter implements Converter {

	private static final int SHELF_OFFSET = 0;
	private static final int SHELF_LENGTH = 3;
	private static final int SLOT_OFFSET = 7;
	private static final int SLOT_LENGTH = 2;

	public Object convert(String input) {
		if (input != null) {
			Field shelf = new Field();
			shelf.setOffset(SHELF_OFFSET);
			shelf.setLength(SHELF_LENGTH);
			String shelfString = shelf.parseValue(input);
			Field slot = new Field();
			slot.setOffset(SLOT_OFFSET);
			slot.setLength(SLOT_LENGTH);
			String slotString = slot.parseValue(input);
			String location = shelfString + "-" + slotString;
			return location;
		} else {
			return null;
		}
	}
}
