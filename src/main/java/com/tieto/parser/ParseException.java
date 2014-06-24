package com.tieto.parser;

public class ParseException extends RuntimeException {
    private static final long serialVersionUID = 8268597866750822198L;

    public ParseException(String message, Exception e) {
        super(message, e);
    }
}
