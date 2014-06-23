package com.tieto.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * SequenceLine is one line in LineSequenceRecord. When parsing, there is no
 * need to split input, since LineSequenceRecord has already done that.
 * 
 * @see com.tieto.parser.LineSequenceRecord
 */
public class SequenceLine extends Line {
    /**
     * There is no need to split input, since LineSequenceRecord has already
     * done that.
     */
    @Override
    public void parse(ParserData parserData, String input, String className) {
        if (this.className != null) {
            className = this.className;
        }
        if (searchRegExp != null) {
            Pattern pattern = Pattern.compile(searchRegExp);
            Matcher matcher = pattern.matcher(input);
            if (!matcher.find()) {
                return;
            }
        }
        if (textParsers != null) {
            for (TextParser textParser : textParsers) {
                textParser.parse(parserData, input, className);
            }
        }
    }
}
