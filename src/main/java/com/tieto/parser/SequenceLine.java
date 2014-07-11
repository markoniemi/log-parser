package com.tieto.parser;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
            log.trace("{} delegating {} to child parsers", this, input);
            if (textParsers != null) {
                for (TextParser textParser : textParsers) {
                    textParser.parse(parserData, input, className);
                }
            }
        }
    }

    private boolean isMatchingLine(String splitInput) {
        boolean isMatchingLine = true;
        if (search != null && splitInput.indexOf(search) == -1) {
            isMatchingLine = false;
        } else if (searchRegExp != null && !findRegExp(splitInput)) {
            isMatchingLine = false;
        }
        if (error != null && splitInput.contains(error)) {
            isMatchingLine = false;
        }
        return isMatchingLine;
    }
}
