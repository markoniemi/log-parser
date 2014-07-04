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

import org.apache.commons.lang.StringUtils;

/**
 * Parser text to java classes. parser.xml is a hierarchy of parsers,
 * textParsers and field. Parser delegates parsing to textParsers, which in turn
 * delegates to child textParsers and fields.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@ToString(of = { "id" })
public class Parser extends TextParser {
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
        parserData.setLineBreak(createLineBreak(lineBreak));
        parse(parserData, input, null);
        return parserData.getObjects();
    }

    @Override
    public void parse(ParserData parserData, String input, String clsName) {
        for (TextParser textParser : textParsers) {
            textParser.parse(parserData, input, clsName);
        }
    }

    /**
     * Use system line break if xml does not define it.
     */
    private String createLineBreak(String lineBreak) {
        if (!StringUtils.isEmpty(lineBreak)) {
            return lineBreak;
        }
        return System.getProperty("line.separator");
    }
}
