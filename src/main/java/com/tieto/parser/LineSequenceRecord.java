package com.tieto.parser;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * LineSequenceRecord is a special type of textParser needed for parsing alarms.
 * Alarms have only one string that can be used for finding an alarm record in
 * MML-output. This string is not on the first line of record, but on second.
 * Therefore LineSequenceRecord splits output into lines, but is aware of other
 * lines. Together with SequenceLine it can parse alarms.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString(of = { "search" })
@Slf4j
public class LineSequenceRecord extends Line {
    // TODO move to line
    @XmlAttribute
    protected String search;

    @Override
    protected void parse(ParserData parserData, String input, String className) {
        log.debug("Parsing using {}", this);
        this.lineBreak = createLineBreak(parserData.getLineBreak());
        if (input == null) {
            return;
        }
        if (this.className != null) {
            className = this.className;
        }
        List<String> splitInputs = splitInput(input);
        for (int i = 0; i < splitInputs.size(); i++) {
            String splitInput = splitInputs.get(i);
            if (isMatchingLine(splitInput)) {
                // TODO consider changing to parseLine, so parse could be inherited from Line
                for (TextParser textParser : textParsers) {
                    if (textParser instanceof Line) {
                        Line line = (Line) textParser;
                        int lineNumber = i + line.getLineNumber();
                        if (lineNumber >= 0 && lineNumber < splitInputs.size()) {
                            line.parse(parserData, splitInputs.get(lineNumber), className);
                        }
                    }
                }
                if (this.className != null && parserData.getCurrentObject() != null) {
                    parserData.getObjects().add(parserData.getCurrentObject());
                    parserData.setCurrentObject(null);
                }
            }
        }
    }

    private boolean isMatchingLine(String splitInput) {
        boolean isMatchingLine = false;
        if (search != null) {
            if (splitInput.indexOf(search) != -1) {
                isMatchingLine = true;
            }
        } else if (searchRegExp != null) {
            if (splitInput.matches(searchRegExp)) {
                isMatchingLine = true;
            }
        }
        return isMatchingLine;
    }
}
