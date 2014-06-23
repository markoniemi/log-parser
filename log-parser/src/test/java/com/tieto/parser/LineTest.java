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
    public void testSplitInput() {
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
    public void testError() {
        Line line = new Line();
        line.setError("ERROR:");
        line.setLineBreak(LINE_BREAK);
        List<String> splitInput = line.splitInput("ERROR:a" + LINE_BREAK + "b" + LINE_BREAK + "");
        Assert.assertEquals("b", splitInput.get(0));
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
