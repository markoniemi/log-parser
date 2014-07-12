package com.tieto.parser;

import java.util.List;

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
 * Parser text to java classes. parser.xml is a hierarchy of parsers,
 * textParsers and field. Parser delegates parsing to textParsers, which in turn
 * delegates to child textParsers and fields.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Slf4j
@Getter
@Setter
@ToString(of = { "id" })
public class Parser extends TextParser {
    public static final String LINUX_LINE_BREAK = "\n";
    public static final String WIN_LINE_BREAK = "\r\n";
    @XmlAttribute
    private String id;
    @XmlElements({
        @XmlElement(name="block", type=Block.class),
        @XmlElement(name="field", type=Field.class),
        @XmlElement(name="line", type=Line.class),
        @XmlElement(name="lineSequenceRecord", type=LineSequenceRecord.class),
        @XmlElement(name="sequenceLine", type=SequenceLine.class),
    })
    private List<TextParser> textParsers;
    @XmlAttribute
    protected String lineBreak;

    public List<?> parse(String input) {
        return parse(input, false);
    }
    
    public List<?> parse(String input, boolean failOnError) {
        ParserData parserData = new ParserData();
        parserData.setFailOnError(failOnError);
        parserData.setLineBreak(createLineBreak(lineBreak, input));
        parse(parserData, input, null);
        return parserData.getObjects();
    }

    @Override
    protected void parse(ParserData parserData, String input, String clsName) {
        log.debug("Parsing using {}", this);
        for (TextParser textParser : textParsers) {
            textParser.parse(parserData, input, clsName);
        }
    }

    /**
     * Use system line break if xml does not define it.
     */
    private String createLineBreak(String lineBreak, String input) {
        if (!StringUtils.isEmpty(lineBreak)) {
            return lineBreak;
        }
        return scanLineBreak(input);
    }

    private String scanLineBreak(String input) {
        if (input.contains(LINUX_LINE_BREAK)) {
            return LINUX_LINE_BREAK;
        }
        return WIN_LINE_BREAK;
    }
}
