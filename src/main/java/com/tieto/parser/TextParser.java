package com.tieto.parser;

public interface TextParser {

    /**
     * Parse input to classes.
     */
    void parse(ParserData parserData, String input, String clsName);

}