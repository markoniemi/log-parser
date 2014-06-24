package com.tieto.parser;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

import org.apache.commons.lang.StringUtils;

/**
 * Parser text to java classes. parser.xml is a hierarchy of parsers,
 * textParsers and field. Parser delegates parsing to textParsers, which in turn
 * delegates to child textParsers and fields.
 */
@Getter
@Setter
public class Parser implements TextParser {
    private String id;
    private List<TextParser> textParsers;
    protected String lineBreak;

    public List<Object> parse(String input) {
        ParserData parserData = new ParserData();
        parserData.setLineBreak(createLineBreak(lineBreak));
        parse(parserData, input, null);
        return parserData.getObjects();
    }

    @Override
    public void parse(ParserData parserData, String input, String clsName) {
        for (TextParser textParser : textParsers) {
            textParser.parse(parserData, input, clsName);
        }
    }

    /**
     * Use system line break if xml does not define it.
     */
    private String createLineBreak(String lineBreak) {
        if (!StringUtils.isEmpty(lineBreak)) {
            return lineBreak;
        }
        return System.getProperty("line.separator");
    }
}
