package com.tieto.parser;

import java.io.FileInputStream;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class ParserManagerTest {

    @Test
    public void readParsers() throws Exception {
        ParserManager parserManager = new ParserManager(new FileInputStream(
                "src/test/resources/config/parsers-test.xml"));
        List<Parser> parsers = parserManager.getParsers();
        Assert.assertNotNull(parsers);
        Parser parser = parsers.get(0);
        Assert.assertNotNull(parser);
        Assert.assertEquals("parserId", parser.getId());
        Assert.assertEquals("lineBreak", parser.getLineBreak());
        List<TextParser> textParsers = parser.getTextParsers();
        Assert.assertNotNull(textParsers);
        Assert.assertEquals(5, textParsers.size());
        Block block = (Block) textParsers.get(0);
        Assert.assertEquals("start", block.getStart());
        Assert.assertEquals("end", block.getEnd());
        Field field = (Field) textParsers.get(1);
        Assert.assertEquals("start", field.getStart());
        Line line = (Line) textParsers.get(2);
        Assert.assertEquals(Integer.valueOf(1), line.getLineNumber());
        LineSequenceRecord lineSequenceRecord = (LineSequenceRecord) textParsers.get(3);
        Assert.assertEquals("search", lineSequenceRecord.getSearch());
        SequenceLine sequenceLine = (SequenceLine) textParsers.get(4);
        Assert.assertEquals(Integer.valueOf(1), sequenceLine.getLineNumber());
    }

    @Test
    public void getParsers() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Assert.assertNotNull(parserManager.getParsers().get(0));
        Assert.assertNotNull(parserManager.getParsers().get(0).getTextParsers());
        Block block = (Block) parserManager.getParsers().get(1).getTextParsers().get(0);
        Line line = (Line) block.getTextParsers().get(0);
        Assert.assertNull(line.getLineNumber());
    }

    @Test
    public void getParser() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitInfoIPA");
        Assert.assertNotNull(parser);
        Assert.assertEquals("unitInfoIPA", parser.getId());
        parser = parserManager.getParser("unitInfoMaster");
        Assert.assertNotNull(parser);
        Assert.assertEquals("unitInfoMaster", parser.getId());
    }
}
