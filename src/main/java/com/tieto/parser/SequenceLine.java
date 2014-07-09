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
        if (this.className != null) {
            className = this.className;
        }
        if (isMatchingLine(input)) {
            log.debug("{} found matching line, delegating to child parses.", this);
            if (textParsers != null) {
                for (TextParser textParser : textParsers) {
                    textParser.parse(parserData, input, className);
                }
            }
        }
    }

    private boolean isMatchingLine(String splitInput) {
        boolean isMatchingLine = true;
        if (search != null) {
            if (splitInput.indexOf(search) == -1) {
                isMatchingLine = false;
            }
        } else if (searchRegExp != null) {
            if (!splitInput.matches(searchRegExp)) {
                Pattern pattern = Pattern.compile(searchRegExp);
                Matcher matcher = pattern.matcher(splitInput);
                if (!matcher.find()) {
                    isMatchingLine = false;
                }
            }
        }
        if (error != null) {
            if (splitInput.contains(error)) {
                isMatchingLine = false;
            }
        }
        return isMatchingLine;
    }
}
