package com.tieto.parser.converter;

import com.tieto.parser.converter.Converter;
import com.tieto.parser.model.Severity;

public class AlarmSeverityConverter implements Converter {
    public Object convert(String input) {
        String trimmedInput = input.trim();
        if ("****".equals(trimmedInput)) {
            return Severity.CRITICAL;
        } else if ("***".equals(trimmedInput)) {
            return Severity.MAJOR;
        } else if ("**".equals(trimmedInput)) {
            return Severity.MINOR;
        } else if ("*".equals(trimmedInput)) {
            return Severity.WARNING;
        }
        return Severity.OK;
    }
}
