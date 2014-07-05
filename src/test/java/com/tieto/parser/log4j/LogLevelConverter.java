package com.tieto.parser.log4j;

import org.apache.log4j.Level;

import com.tieto.parser.converter.Converter;

public class LogLevelConverter extends Converter<Level> {
    public Level convert(String input) {
        return Level.toLevel(input);
    }
}
