package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.tieto.parser.model.TestClass;

public class LineTest {
    public static final String LINE_BREAK = System.getProperty("line.separator");

    @Test
    public void splitInput() {
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        List<String> splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals("a", splitInput.get(0));
        Assert.assertEquals("b", splitInput.get(1));
        line = new Line();
        line.setLineNumber(1);
        line.setLineBreak(LINE_BREAK);
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals("b", splitInput.get(0));
    }

    @Test
    public void splitInputWithError() {
        Line line = new Line();
        line.setError("ERROR:");
        line.setLineBreak(LINE_BREAK);
        List<String> splitInput = line.splitInput("ERROR:a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals("b", splitInput.get(0));
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertEquals("a", splitInput.get(0));
        line.setLineNumber(0);
        splitInput = line.splitInput("ERROR:a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(0, splitInput.size());
        line.setLineNumber(0);
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("a", splitInput.get(0));
    }

    @Test
    public void splitInputWithLineNumber() {
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        line.setLineNumber(1);
        List<String> splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("b", splitInput.get(0));
        line.setLineNumber(0);
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("a", splitInput.get(0));
    }

    @Test
    public void splitInputWithSearch() {
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        line.setSearch("a");
        List<String> splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("a", splitInput.get(0));
        line.setSearch("b");
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("b", splitInput.get(0));
    }
    
    @Test
    public void splitInputWithSearchRegExp() {
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        line.setSearchRegExp("a");
        List<String> splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("a", splitInput.get(0));
        line.setSearchRegExp("b");
        splitInput = line.splitInput("a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals(1, splitInput.size());
        Assert.assertEquals("b", splitInput.get(0));
    }
    
    @Test
    public void parse() {
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        line.setClassName("com.tieto.parser.model.TestClass");
        Field field = new Field();
        field.setAttribute("value");
        field.setOffset(0);
        field.setLength(1);
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        line.setTextParsers(fields);
        ParserData parserData = new ParserData();
        parserData.setLineBreak(LINE_BREAK);
        line.parse(parserData, "a" + LINE_BREAK + "b" + LINE_BREAK + "", null);
        List<Object> objects = parserData.getObjects();
        Assert.assertEquals(2, objects.size());
        TestClass testClass = (TestClass) objects.get(0);
        Assert.assertEquals("a", testClass.getValue());
    }

    @Ignore
    @Test
    public void parseBlock() {
        Block block = new Block();
        block.setClassName("com.tieto.parser.model.TestClass");
        block.setEnd("END");
        Line line = new Line();
        line.setLineBreak(LINE_BREAK);
        Field field = new Field();
        field.setAttribute("value");
        field.setOffset(0);
        field.setLength(1);
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        line.setTextParsers(fields);
        List<TextParser> lines = new ArrayList<TextParser>();
        lines.add(line);
        block.setTextParsers(lines);
        ParserData parserData = new ParserData();
        block.parse(parserData, "a" + LINE_BREAK + "b" + LINE_BREAK + "END" + "c" + LINE_BREAK + "d" + LINE_BREAK + "END", null);
        List<Object> objects = parserData.getObjects();
        Assert.assertEquals(2, objects.size());
        TestClass testClass = (TestClass) objects.get(0);
        Assert.assertEquals("a", testClass.getValue());
    }
    
    @Test
    public void parseWithNullInput() {
        Line line = new Line();
        ParserData parserData = new ParserData();
        line.parse(parserData, null, null);
    }

    @Ignore("not yet working")
    @Test
    public void search() {
        LineSequenceRecord record = new LineSequenceRecord();
        record.setSearch("test");
        Field field = new Field();
        field.setOffset(4);
        field.setAttribute("value");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        record.setTextParsers(fields);
        ParserData parserData = new ParserData();
        record.parse(parserData, "test 1" + LINE_BREAK + "test 2" + LINE_BREAK + "aaaa 2",
                "com.tieto.parser.model.TestClass");
        List<Object> objects = parserData.getObjects();
        Assert.assertEquals(2, objects.size());
        TestClass testClass = (TestClass) objects.get(0);
        Assert.assertEquals("1", testClass.getValue());
        testClass = (TestClass) objects.get(1);
        Assert.assertEquals("2", testClass.getValue());
    }
}
