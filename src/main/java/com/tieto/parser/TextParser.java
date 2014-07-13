package com.tieto.parser;

public abstract class TextParser {

    /**
     * Parse input to classes.
     */
    protected abstract void parse(ParserData parserData, String input, String clsName);
}