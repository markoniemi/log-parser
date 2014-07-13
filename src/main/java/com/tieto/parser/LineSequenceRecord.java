package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;

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
@ToString(callSuper = true)
@Slf4j
public class LineSequenceRecord extends Line {

    @Override
    protected void parse(ParserData parserData, String input, String className) {
        this.lineBreak = parserData.getLineBreak();
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
                for (TextParser textParser : textParsers) {
                    if (textParser instanceof Line) {
                        log.trace("{} delegating {} to child parsers", this, splitInput);
                        Line line = (Line) textParser;
                        int lineNumber = i + line.getLineNumber();
                        if (lineNumber >= 0 && lineNumber < splitInputs.size()) {
                            line.parse(parserData, splitInputs.get(lineNumber), className);
                        }
                    }
                }
                addValueObjectToList(parserData);
            }
        }
    }

    protected List<String> splitInput(String input) {
        List<String> splitInputs = new ArrayList<String>();
        int currentOffset = 0;
        int endIndex = input.indexOf(lineBreak);
        while (endIndex != -1) {
            String splitInput = input.substring(currentOffset, endIndex);
            splitInputs.add(splitInput);
            currentOffset = endIndex + lineBreak.length();
            endIndex = input.indexOf(lineBreak, currentOffset);
        }
        return splitInputs;
    }
    

    private boolean isMatchingLine(String splitInput) {
        boolean isMatchingLine = false;
        if (search != null && splitInput.indexOf(search) != -1) {
            isMatchingLine = true;
        } else if (searchRegExp != null && findRegExp(splitInput)) {
            isMatchingLine = true;
        }
        return isMatchingLine;
    }
}
