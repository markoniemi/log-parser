package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;

import org.apache.commons.lang.StringUtils;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

/**
 * Line is a textParser which splits input into lines and passes parsing to
 * textParsers and fields.
 */
@Slf4j
@Getter
@Setter
@ToString(of = { "lineNumber" })
public class Line extends Block {
    // TODO change to int
    @XmlAttribute
    private Integer lineNumber;
    @XmlAttribute
    protected String lineBreak;

    /**
     * Split input into lines and pass it to children.
     */
    @Override
    public void parse(ParserData parserData, String input, String className) {
        this.lineBreak = createLineBreak(parserData.getLineBreak());
        if (input == null) {
            return;
        }
        if (this.className != null) {
            className = this.className;
        }
        List<String> splitInputs = splitInput(input);
        for (String splitInput : splitInputs) {
            log.debug("{}", splitInput);
            if (textParsers != null) {
                for (TextParser textParser : textParsers) {
                    textParser.parse(parserData, splitInput, className);
                }
            }
            if (this.className != null && parserData.getCurrentObject() != null) {
                parserData.getObjects().add(parserData.getCurrentObject());
                parserData.setCurrentObject(null);
            }
        }
    }

    /**
     * Use system line break if xml does not define it.
     */
    // TODO scan for linebreak in text, so we can support both linebreaks
    protected String createLineBreak(String lineBreak) {
        if (!StringUtils.isEmpty(lineBreak)) {
            return lineBreak;
        }
        return System.getProperty("line.separator");
    }    

    protected List<String> splitInput(String input) {
        List<String> splitInputs = new ArrayList<String>();
        int currentOffset = 0;
        int currentLineNumber = 0;
        int endIndex = input.indexOf(lineBreak);
        while (endIndex != -1) {
            if (lineNumber == null) {
                String splitInput = input.substring(currentOffset, endIndex);
                if (error == null || !splitInput.contains(error)) {
                    splitInputs.add(splitInput);
                }
            } else if (lineNumber == currentLineNumber) {
                String splitInput = input.substring(currentOffset, endIndex);
                if (error == null || !splitInput.contains(error)) {
                    splitInputs.add(splitInput);
                }
            }
            currentLineNumber++;
            currentOffset = endIndex + lineBreak.length();
            endIndex = input.indexOf(lineBreak, currentOffset);
        }
        return splitInputs;
    }
}
