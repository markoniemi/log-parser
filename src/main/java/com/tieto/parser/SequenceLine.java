package com.tieto.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * SequenceLine is one line in LineSequenceRecord. When parsing, there is no
 * need to split input, since LineSequenceRecord has already done that.
 * 
 * @see com.tieto.parser.LineSequenceRecord
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j
public class SequenceLine extends Line {
    /**
     * There is no need to split input, since LineSequenceRecord has already
     * done that.
     */
    @Override
    protected void parse(ParserData parserData, String input, String className) {
        log.debug("Parsing using {}", this);
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
