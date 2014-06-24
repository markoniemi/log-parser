package com.tieto.parser;

import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.Line;
import com.tieto.parser.Parser;
import com.tieto.parser.ParserManager;

public class ParserManagerTest {

    @Test
    public void testRead() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Assert.assertNotNull(parserManager.getParsers().get(0));
        // Assert.assertNotNull(parserManager.getParsers().get(0).fields);
        Assert.assertNotNull(parserManager.getParsers().get(0).getTextParsers());
        Block block = (Block) parserManager.getParsers().get(1).getTextParsers().get(0);
        Line line = (Line) block.getTextParsers().get(0);
        Assert.assertNull(line.getLineNumber());
    }

    @Test
    public void testGetConfig() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitInfoIPA");
        Assert.assertNotNull(parser);
        Assert.assertEquals("unitInfoIPA", parser.getId());
    }

    @Test
    public void testGetDefault() throws Exception {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("unitInfoMaster");
        Assert.assertNotNull(parser);
        Assert.assertEquals("unitInfoMaster", parser.getId());
    }
}
