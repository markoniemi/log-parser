package com.tieto.parser;

public abstract class TextParser {

    /**
     * Parse input to classes.
     */
    abstract protected void parse(ParserData parserData, String input, String clsName);
}