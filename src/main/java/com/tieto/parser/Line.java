package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAttribute;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

/**
 * Line is a textParser which splits input into lines and passes parsing to
 * textParsers and fields.
 */
@Slf4j
@Getter
@Setter
@ToString(of = { "lineNumber", "search", "searchRegExp" })
public class Line extends Block {
    @XmlAttribute
    protected Integer lineNumber;
    @XmlAttribute
    protected String lineBreak;
    @XmlAttribute
    protected String search;

    /**
     * Split input into lines and pass it to children.
     */
    @Override
    protected void parse(ParserData parserData, String input, String className) {
        this.lineBreak = createLineBreak(parserData.getLineBreak());
        if (input == null) {
            return;
        }
        if (this.className != null) {
            className = this.className;
        }
        List<String> splitInputs = splitInput(input);
        for (String splitInput : splitInputs) {
            log.trace("{} delegating {} to child parsers", this, splitInput);
            if (textParsers != null) {
                for (TextParser textParser : textParsers) {
                    textParser.parse(parserData, splitInput, className);
                }
            }
            addValueObjectToList(parserData);
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
            String splitInput = input.substring(currentOffset, endIndex);
            if (isMatchingLine(splitInput, currentLineNumber)) {
                splitInputs.add(splitInput);
            }
            currentLineNumber++;
            currentOffset = endIndex + lineBreak.length();
            endIndex = input.indexOf(lineBreak, currentOffset);
        }
        return splitInputs;
    }

    private boolean isMatchingLine(String splitInput, int currentLineNumber) {
        boolean isMatchingLine = true;
        if (search != null && splitInput.indexOf(search) == -1) {
            isMatchingLine = false;
        } else if (searchRegExp != null && !findRegExp(splitInput)) {
            isMatchingLine = false;
        } else if (lineNumber != null && currentLineNumber != lineNumber) {
            isMatchingLine = false;
        }
        if (error != null && splitInput.contains(error)) {
            isMatchingLine = false;
        }
        return isMatchingLine;
    }

    protected boolean findRegExp(String splitInput) {
        Pattern pattern = Pattern.compile(searchRegExp);
        Matcher matcher = pattern.matcher(splitInput);
        return matcher.find();
    }
}
