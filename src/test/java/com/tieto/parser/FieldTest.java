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
        // start string is not found
        value = field.parseValue("..123");
        Assert.assertEquals(null, value);
    }

    @Test
    public void parseEnd() {
        Field field = new Field();
        field.setEnd("END");
        String value = field.parseValue("123..END");
        Assert.assertEquals("123..", value);
        // end string is not found
        value = field.parseValue("123..");
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
        // start string is not found
        value = field.parseValue(" 123 END");
        Assert.assertEquals(null, value);
    }

    @Test
    public void createMethodName() {
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
    public void parseWithTooLongLength() {
        Field field = new Field();
        field.setAttribute("value");
        field.setOffset(0);
        field.setLength(4);
        ParserData parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "123", null);
        Assert.assertEquals("123", ((TestClass) parserData.getCurrentObject()).getValue());
    }

    @Test
    public void parseWithNullInput() {
        Field field = new Field();
        ParserData parserData = new ParserData();
        field.parse(parserData, null, null);
    }

    @Test
    public void setStringValue() throws Exception {
        Field field = new Field();
        field.setType("java.lang.String");
        field.setAttribute("name");
        ParserData parserData = new ParserData();
        String valueString = new String("abc");
        field.setValue(parserData, valueString, "com.tieto.parser.model.UnitInfo");
        Assert.assertEquals("abc", ((UnitInfo) parserData.getCurrentObject()).getName());
    }

    @Test
    public void setIntegerValue() throws Exception {
        Field field = new Field();
        field.setType("int");
        field.setAttribute("alarmNumber");
        ParserData parserData = new ParserData();
        String valueString = new String("10");
        field.setValue(parserData, valueString, "com.tieto.parser.model.Alarm");
        Assert.assertEquals(10, ((Alarm) parserData.getCurrentObject()).getAlarmNumber());
    }

    @Test
    public void setLongValue() throws Exception {
        Field field = new Field();
        field.setType("long");
        field.setAttribute("longValue");
        ParserData parserData = new ParserData();
        String valueString = new String("10");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertEquals(10, ((TestClass) parserData.getCurrentObject()).getLongValue());
    }

    @Test
    public void setBooleanValue() throws Exception {
        Field field = new Field();
        field.setType("boolean");
        field.setAttribute("booleanValue");
        ParserData parserData = new ParserData();
        String valueString = new String("true");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertTrue(((TestClass) parserData.getCurrentObject()).isBooleanValue());
    }

    @Test
    public void setDateWithConverter() throws Exception {
        Field field = new Field();
        field.setType("java.util.Date");
        field.setAttribute("date");
        Converter converter = new Converter();
        converter.setClassName("com.tieto.parser.converter.DateConverter");
        field.setConverter(converter);
        ParserData parserData = new ParserData();
        String valueString = new String("2014-06-25 17:13:00");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertNotNull(((TestClass) parserData.getCurrentObject()).getDate());
        Assert.assertEquals(1403705580000L, ((TestClass) parserData.getCurrentObject()).getDate().getTime());
        // use constructor parameter for converter
        converter.setParameter("yyyy-MM-dd hh:mm:ss,SSS");
        parserData = new ParserData();
        valueString = new String("2014-06-25 17:13:00,123");
        field.setValue(parserData, valueString, "com.tieto.parser.model.TestClass");
        Assert.assertNotNull(((TestClass) parserData.getCurrentObject()).getDate());
        Assert.assertEquals(1403705580123L, ((TestClass) parserData.getCurrentObject()).getDate().getTime());
    }

    @Test(expected = ParseException.class)
    public void parseWithError() {
        Field field = new Field();
        field.setOffset(0);
        field.setLength(2);
        field.setAttribute("value");
        field.setMethodName("nonexistent");
        ParserData parserData = new ParserData();
        // failOnError = false
        field.parse(parserData, "123-23", "com.tieto.parser.model.TestClass");
        // failOnError = true
        parserData.setFailOnError(true);
        field.parse(parserData, "123-23", "com.tieto.parser.model.TestClass");
        Assert.fail();
    }
    
    @Test
    public void setStringValueWithMethod() throws Exception {
        Field field = new Field();
        field.setType("java.lang.String");
        field.setAttribute("name");
        field.setMethodName("setName");
        ParserData parserData = new ParserData();
        String valueString = new String("abc");
        field.setValue(parserData, valueString, "com.tieto.parser.model.UnitInfo");
        Assert.assertEquals("abc", ((UnitInfo) parserData.getCurrentObject()).getName());
    }
    
    @Test
    public void searchRegExp() {
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
    public void searchRegExpNotFound() {
        Field field = new Field();
        field.setAttribute("value");
        field.setSearchRegExp("\\d+-\\d+");
        ParserData parserData = new ParserData();
        parserData.setCurrentObject(new TestClass());
        field.parse(parserData, "", null);
        Assert.assertNull(((TestClass) parserData.getCurrentObject()).getValue());
    }
}
