package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang.StringUtils;

/**
 * Block splits input into text blocks and passes parsing to textParsers.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j
@Getter
@Setter
@ToString(of = { "className" })
public class Block extends TextParser {
    @XmlElements({
        @XmlElement(name="block", type=Block.class),
        @XmlElement(name="field", type=Field.class),
        @XmlElement(name="line", type=Line.class),
        @XmlElement(name="lineSequenceRecord", type=LineSequenceRecord.class),
        @XmlElement(name="sequenceLine", type=SequenceLine.class),
    })
    protected List<TextParser> textParsers;
    @XmlAttribute
    protected String start;
    @XmlAttribute
    protected String end;
    @XmlAttribute
    protected String searchRegExp;
    @XmlAttribute
    protected String className;
    @XmlAttribute
    protected boolean trim;
    @XmlAttribute
    protected int offset;
    @XmlAttribute
    protected int length;
    @XmlAttribute
    protected String error;
    @XmlAttribute
    protected int count;

    @Override
    protected void parse(ParserData parserData, String input, String clsName) {
        if (input == null) {
            return;
        }
        if (this.className != null) {
            clsName = this.className;
        }
        List<String> splitInputs = splitInput(input);
        int inputCount = splitInputs.size();
        if (count != 0) {
            inputCount = (inputCount > count ? count : inputCount);
        }
        for (int i = 0; i < inputCount; i++) {
            String splitInput = splitInputs.get(i);
            log.trace("{} delegating {} to child parsers", this, splitInput);
            delegateParse(parserData, splitInput, clsName);
        }
    }

    /**
     * Split input into smaller pieces. If end and start is null, return string
     * from offset.
     */
    protected List<String> splitInput(String input) {
        List<String> splitInputs = new ArrayList<String>();
        String endString = this.end;
        String startString = this.start;
        if (searchRegExp != null) {
            Pattern pattern = Pattern.compile(searchRegExp);
            splitInputs = splitInput(input, pattern);
        } else if (endString == null && startString == null) {
            // if end and start is null, use offset
            splitInputs.add(getAtOffset(input));
        } else if (endString != null && startString != null) {
            // both end and start are defined
            splitInputs = splitInput(input, startString, endString);
        } else if (endString != null && startString == null) {
            // start string is not defined, use only end string
            splitInputs = splitInput(input, endString);
        } else if (endString == null && startString != null) {
            // end string is not defined use only start string
            splitInputs = splitInput(input, startString, true);
        }
        return splitInputs;
    }

    /**
     * Delegates split inputs to child parsers.
     */
    protected void delegateParse(ParserData parserData, String input, String clsName) {
        if (textParsers != null) {
            for (TextParser textParser : textParsers) {
                textParser.parse(parserData, input, clsName);
            }
        }
        if (this.className != null && parserData.getCurrentObject() != null) {
            parserData.getObjects().add(parserData.getCurrentObject());
            parserData.setCurrentObject(null);
        }
    }

    private String getAtOffset(String input) {
        String currentItem = null;
        if (offset < input.length()) {
            // if length is 0, return to the end of the input string
            if ((length == 0) || ((offset + length) > input.length())) {
                currentItem = input.substring(offset);
            } else {
                currentItem = input.substring(offset, offset + length);
            }
            if (trim) {
                currentItem = StringUtils.trim(currentItem);
            }
        }
        return currentItem;
    }

    private List<String> splitInput(String input, String startString, String endString) {
        List<String> splitInputs = new ArrayList<String>();
        int currentOffset = 0;
        int indexOfStartString = input.indexOf(startString, currentOffset);
        String substring = null;
        while (indexOfStartString != -1) {
            int indexOfEndString = input.indexOf(endString,
                    indexOfStartString + startString.length());
            if (indexOfEndString != -1) {
                substring = input.substring(indexOfStartString + startString.length(),
                        indexOfEndString);
                if (!StringUtils.isBlank(substring)) {
                    splitInputs.add(getAtOffset(substring));
                }
                currentOffset = indexOfEndString + endString.length();
            } else {
                substring = input.substring(indexOfStartString + startString.length());
                if (!StringUtils.isBlank(substring)) {
                    splitInputs.add(getAtOffset(substring));
                }
                currentOffset = input.length();
            }
            indexOfStartString = input.indexOf(startString, currentOffset);
        }
        return splitInputs;
    }

    private List<String> splitInput(String input, String endString) {
        List<String> splitInputs = new ArrayList<String>();
        int currentOffset = 0;
        int endIndex = input.indexOf(endString);
        while (endIndex != -1) {
            splitInputs.add(getAtOffset(input.substring(currentOffset, endIndex)));
            currentOffset = endIndex + endString.length();
            endIndex = input.indexOf(endString, currentOffset);
        }
        return splitInputs;
    }

    private List<String> splitInput(String input, String startString, boolean dummy) {
        List<String> splitInputs = new ArrayList<String>();
        int currentOffset = -1;
        int startIndex = input.indexOf(startString);
        if (startIndex != -1) {
            currentOffset = startIndex + startString.length();
            startIndex = input.indexOf(startString, currentOffset);
        }
        while (startIndex != -1) {
            splitInputs.add(getAtOffset(input.substring(currentOffset, startIndex)));
            currentOffset = startIndex + startString.length();
            startIndex = input.indexOf(startString, currentOffset);
        }
        if (currentOffset != -1) {
            splitInputs.add(getAtOffset(input.substring(currentOffset)));
        }
        return splitInputs;
    }

    private List<String> splitInput(String input, Pattern pattern) {
        List<String> splitInputs = new ArrayList<String>();
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            splitInputs.add(matcher.group());
        }
        return splitInputs;
    }
}
