package com.tieto.parser;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * Read parser configuration from XML and store parsers in parsers list. Use
 * getParser() to get a parser.
 */
@Getter
@Setter
public class ParserManager {
    private List<Parser> parsers;

    public ParserManager() {
        parsers = new ArrayList<Parser>();
    }

    public ParserManager(InputStream configInputSream) {
        parsers = readParsers(configInputSream);
    }

    public ParserManager(String configFilename) throws FileNotFoundException {
        parsers = readParsers(new FileInputStream(configFilename));

    }

    public List<Parser> readParsers(InputStream inputStream) {
        ParserConfig parserConfig = (ParserConfig) getXStream().fromXML(inputStream);
        return parserConfig.getParsers();
    }

    public Parser getParser(String id) {
        if (id == null) {
            return null;
        }
        for (Parser parser : parsers) {
            if (id.equals(parser.getId())) {
                return parser;
            }
        }
        return null;
    }

    private XStream getXStream() {
        XStream xStream = new XStream(new DomDriver());
        xStream = new XStream(new DomDriver());
        xStream.alias("parsers", ParserConfig.class);
        xStream.addImplicitCollection(ParserConfig.class, "parsers");
        xStream.alias("parser", Parser.class);
        xStream.useAttributeFor(Parser.class, "id");
        xStream.useAttributeFor(Parser.class, "lineBreak");
        xStream.addImplicitCollection(Parser.class, "textParsers", "block", Block.class);
        xStream.addImplicitCollection(Parser.class, "textParsers", "lineSequenceRecord",
                LineSequenceRecord.class);
        xStream.alias("lineSequenceRecord", LineSequenceRecord.class);
        xStream.useAttributeFor(LineSequenceRecord.class, "search");
        xStream.alias("sequenceLine", SequenceLine.class);
        xStream.addImplicitCollection(LineSequenceRecord.class, "textParsers", "sequenceLine",
                SequenceLine.class);
        xStream.useAttributeFor(Block.class, "start");
        xStream.useAttributeFor(Block.class, "end");
        xStream.useAttributeFor(Block.class, "searchRegExp");
        xStream.useAttributeFor(Block.class, "className");
        xStream.useAttributeFor(Block.class, "offset");
        xStream.useAttributeFor(Block.class, "length");
        xStream.useAttributeFor(Block.class, "trim");
        xStream.useAttributeFor(Block.class, "error");
        xStream.useAttributeFor(Block.class, "count");
        xStream.alias("version", String.class);
        xStream.addImplicitCollection(Block.class, "textParsers", "block", Block.class);
        xStream.addImplicitCollection(Block.class, "textParsers", "lineSequenceRecord",
                LineSequenceRecord.class);
        xStream.addImplicitCollection(Block.class, "textParsers", "line", Line.class);
        xStream.addImplicitCollection(Block.class, "textParsers", "field", Field.class);
        xStream.useAttributeFor(Line.class, "lineNumber");
        xStream.useAttributeFor(Field.class, "start");
        xStream.useAttributeFor(Field.class, "end");
        xStream.useAttributeFor(Field.class, "attribute");
        xStream.useAttributeFor(Field.class, "offset");
        xStream.useAttributeFor(Field.class, "length");
        xStream.useAttributeFor(Field.class, "trim");
        xStream.useAttributeFor(Field.class, "type");
        xStream.useAttributeFor(Field.class, "converter");
        return xStream;
    }
}
