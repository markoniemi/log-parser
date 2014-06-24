package com.tieto.parser;

import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.model.Alarm;
import com.tieto.parser.model.TestClass;
import com.tieto.parser.model.UnitInfo;

public class FieldTest {
    @Test
    public void parseValueOffset() {
        Field field = new Field();
        field.setOffset(5);
        String value = field.parseValue("0123456789");
        Assert.assertEquals("56789", value);
        field.setLength(2);
        value = field.parseValue("0123456789");
        Assert.assertEquals("56", value);
    }
    
    @Test
    public void parseTooLongOffset() {
        Field field = new Field();
        field.setOffset(10);
        String value = field.parseValue("0123456789");
        Assert.assertNull(value);
    }

    @Test
    public void parseStart() {
        Field field = new Field();
        field.setStart("START");
        String value = field.parseValue("START..123");
        Assert.assertEquals("..123", value);
        field.setOffset(2);
        value = field.parseValue("START..123");
        Assert.assertEquals("123", value);
        field.setLength(2);
        value = field.parseValue("START..123");
        Assert.assertEquals("12", value);
        field.setLength(4);
        value = field.parseValue("START..123");
        Assert.assertEquals("123", value);
    }

    @Test
    public void parseEnd() {
        Field field = new Field();
        field.setEnd("END");
        String value = field.parseValue("123..END");
        Assert.assertEquals("123..", value);
    }

    @Test
    public void parseStartEnd() {
        Field field = new Field();
        field.setStart("START");
        field.setEnd("END");
        String value = field.parseValue("START123END");
        Assert.assertEquals("123", value);
        field.setStart("START");
        field.setEnd("END");
        value = field.parseValue("START123");
        Assert.assertNull(value);
        field.setTrim(true);
        value = field.parseValue("START 123 END");
        Assert.assertEquals("123", value);
    }

    @Test
    public void testCreateMethodName() {
        Field field = new Field();
        field.setAttribute("name");
        field.createMethodName();
        Assert.assertEquals("setName", field.getMethodName());
    }

    @Test
    public void parse() {
        Field field = new Field();
        field.setAttribute("value");
        field.setOffset(0);
        field.setLength(2);
        ParserData parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "123", null);
        Assert.assertEquals("12", ((TestClass) parserData.getCurrentObject()).getValue());
    }

    @Test
    public void parseWithNullInput() {
        Field field = new Field();
        ParserData parserData = new ParserData();
        field.parse(parserData, null, null);
    }

    @Test
    public void testSetStringValue() throws Exception {
        Field field = new Field();
        field.setType("java.lang.String");
        field.setAttribute("name");
        ParserData parserData = new ParserData();
        String valueString = new String("abc");
        field.setValue(parserData, valueString, "com.tieto.parser.model.UnitInfo");
        Assert.assertEquals("abc", ((UnitInfo) parserData.getCurrentObject()).getName());
    }

    @Test
    public void testSetIntegerValue() throws Exception {
        Field field = new Field();
        field.setType("int");
        field.setAttribute("alarmNumber");
        ParserData parserData = new ParserData();
        String valueString = new String("10");
        field.setValue(parserData, valueString, "com.tieto.parser.model.Alarm");
        Assert.assertEquals(10, ((Alarm) parserData.getCurrentObject()).getAlarmNumber());
    }

    @Test
    public void testSetLongValue() throws Exception {
        Field field = new Field();
        field.setType("long");
        field.setAttribute("longValue");
        ParserData parserData = new ParserData();
        String valueString = new String("10");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertEquals(10, ((TestClass) parserData.getCurrentObject()).getLongValue());
    }

    @Test
    public void testSetBooleanValue() throws Exception {
        Field field = new Field();
        field.setType("boolean");
        field.setAttribute("booleanValue");
        ParserData parserData = new ParserData();
        String valueString = new String("true");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertTrue(((TestClass) parserData.getCurrentObject()).isBooleanValue());
    }

    @Test
    public void testSearchRegExp() {
        Field field = new Field();
        field.setAttribute("value");
        field.setSearchRegExp("\\d+-\\d+");
        ParserData parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "123-23", null);
        Assert.assertEquals("123-23", ((TestClass) parserData.getCurrentObject()).getValue());

        field = new Field();
        field.setAttribute("value");
        field.setSearchRegExp("\\d+-\\d+");
        parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "00 00 123-23 45 65", null);
        Assert.assertEquals("123-23", ((TestClass) parserData.getCurrentObject()).getValue());
    }
    
    @Test
    public void testSearchRegExpNotFound() {
        Field field = new Field();
        field.setAttribute("value");
        field.setSearchRegExp("\\d+-\\d+");
        ParserData parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "", null);
        Assert.assertNull(((TestClass) parserData.getCurrentObject()).getValue());
    }
}
