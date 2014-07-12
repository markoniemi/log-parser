package com.tieto.parser;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.model.Alarm;
import com.tieto.parser.model.TestClass;
import com.tieto.parser.model.UnitInfo;

public class BlockTest {
    public static final String LINE_BREAK = System.getProperty("line.separator");

    @Test
    public void splitInputOffset() {
        Block block = new Block();
        block.setOffset(5);
        List<String> splitInput = block.splitInput("012345678");
        Assert.assertEquals("5678", splitInput.get(0));
        block.setLength(2);
        splitInput = block.splitInput("012345678");
        Assert.assertEquals("56", splitInput.get(0));
        block.setLength(5);
        splitInput = block.splitInput("012345678");
        Assert.assertEquals("5678", splitInput.get(0));
    }

    @Test
    public void splitInputOffsetWithTooBigOffset() {
        Block block = new Block();
        block.setOffset(10);
        block.setLength(5);
        List<String> splitInput = block.splitInput("012345678");
        Assert.assertNull(splitInput.get(0));
    }

    @Test
    public void splitInputWithTrim() {
        Block block = new Block();
        block.setOffset(5);
        block.setLength(5);
        block.setTrim(true);
        List<String> splitInput = block.splitInput(" 012345678 ");
        Assert.assertEquals("45678", splitInput.get(0));
    }

    @Test
    public void splitInputStartEnd() {
        Block block = new Block();
        block.setStart("START");
        block.setEnd("END");
        List<String> splitInput = block.splitInput("START INPUT END");
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        block.setStart("START");
        block.setEnd("END");
        splitInput = block.splitInput("START INPUT END START INPUT END");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        Assert.assertEquals(" INPUT ", splitInput.get(1));
        splitInput = block.splitInput("START INPUT END START INPUT END START INPUT END");
        Assert.assertEquals(3, splitInput.size());
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        Assert.assertEquals(" INPUT ", splitInput.get(1));
        Assert.assertEquals(" INPUT ", splitInput.get(2));
        splitInput = block.splitInput(" INPUT ");
        Assert.assertEquals(0, splitInput.size());
        // no end
        splitInput = block.splitInput("START INPUT ");
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        // no end and no text after start
        splitInput = block.splitInput("START");
        Assert.assertEquals(0, splitInput.size());
        // empty strings between start and end
        splitInput = block.splitInput("STARTEND STARTEND STARTEND");
        Assert.assertEquals(0, splitInput.size());
        // empty strings between start and end
    }

    @Test
    public void splitInputStart() {
        Block block = new Block();
        block.setStart("START");
        List<String> splitInput = block.splitInput("START INPUT ");
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        splitInput = block.splitInput("START INPUT START INPUT ");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        Assert.assertEquals(" INPUT ", splitInput.get(1));
        splitInput = block.splitInput(" INPUT ");
        Assert.assertEquals(0, splitInput.size());
    }

    @Test
    public void splitInputEnd() {
        Block block = new Block();
        block.setEnd("END");
        List<String> splitInput = block.splitInput(" INPUT END");
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        splitInput = block.splitInput(" INPUT END INPUT END");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        Assert.assertEquals(" INPUT ", splitInput.get(1));
        splitInput = block.splitInput(" INPUT ");
        Assert.assertEquals(0, splitInput.size());
        block.setEnd("" + LINE_BREAK + "");
        splitInput = block.splitInput(" INPUT " + LINE_BREAK + " INPUT " + LINE_BREAK + "");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertEquals(" INPUT ", splitInput.get(0));
        Assert.assertEquals(" INPUT ", splitInput.get(1));
    }

    @Test
    public void splitInputSearchRegExp() {
        Block block = new Block();
        block.setSearchRegExp("\\d+-\\d+");
        List<String> splitInput = block.splitInput("123-23");
        Assert.assertEquals("123-23", splitInput.get(0));
        block = new Block();
        block.setSearchRegExp("\\d+-\\d+");
        splitInput = block.splitInput("123-23 45-23");
        Assert.assertEquals("123-23", splitInput.get(0));
        Assert.assertEquals("45-23", splitInput.get(1));
        Assert.assertEquals(2, splitInput.size());
        block = new Block();
        // record.setSearchRegExp("IN LOC\\s+.*+\\d+-\\d+");
        block.setSearchRegExp("IN LOC\\s+.*\\d+-\\d+");
        splitInput = block.splitInput("BDCU-0       IN LOC  1A005-08");
        Assert.assertEquals("IN LOC  1A005-08", splitInput.get(0));
    }

    @Test
    public void regExp() throws Exception {
        Block block = new Block();
        block.setSearchRegExp("IN LOC\\s+.*\\d+-\\d+");
        block.setClassName("com.tieto.parser.model.UnitInfo");
        Block subBlock = new Block();
        subBlock.setSearchRegExp("\\s..\\d\\d\\d-\\d\\d");
        List<TextParser> subBlocks = new ArrayList<TextParser>();
        subBlocks.add(subBlock);
        block.setTextParsers(subBlocks);
        List<TextParser> fields = new ArrayList<TextParser>();
        Field field = new Field();
        field.setAttribute("cabinet");
        field.setOffset(1);
        field.setLength(2);
        fields.add(field);
        field = new Field();
        field.setAttribute("location");
        field.setOffset(3);
        field.setLength(6);
        fields.add(field);
        subBlock.setTextParsers(fields);
        ParserData parserData = new ParserData();
        block.parse(parserData, "BDCU-0       IN LOC  1A005-08", null);
        UnitInfo unitInfo = (UnitInfo) parserData.getObjects().get(0);
        Assert.assertEquals("1A", unitInfo.getCabinet());
        Assert.assertEquals("005-08", unitInfo.getLocation());
    }

    @Test
    public void parseWithoutObject() throws Exception {
        Block block = new Block();
        block.setClassName("com.tieto.parser.model.UnitInfo");
        block.setOffset(0);
        block.setLength(5);
        Field field = new Field();
        field.setOffset(2);
        field.setLength(3);
        field.setAttribute("name");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        block.setTextParsers(fields);
        // List<Object> list = new ArrayList<Object>();
        ParserData parserData = new ParserData();
        block.parse(parserData, "12345", null);
        UnitInfo unitInfo = (UnitInfo) parserData.getObjects().get(0);
        Assert.assertEquals("345", unitInfo.getName());
    }

    @Test
    public void parseCount() throws Exception {
        Block block = new Block();
        block.setStart("START");
        block.setEnd("END");
        block.setClassName("com.tieto.parser.model.TestClass");
        block.setCount(1);
        Field field = new Field();
        field.setOffset(0);
        field.setLength(5);
        field.setTrim(true);
        field.setAttribute("value");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        block.setTextParsers(fields);
        ParserData parserData = new ParserData();
        block.parse(parserData, "START 123 END START 345 END", null);
        Assert.assertEquals(1, parserData.getObjects().size());
        TestClass testClass = (TestClass) parserData.getObjects().get(0);
        Assert.assertEquals("123", testClass.getValue());
        // count is bigger than the amount of blocks found
        parserData = new ParserData();
        block.setCount(3);
        block.parse(parserData, "START 123 END START 345 END", null);
        Assert.assertEquals(2, parserData.getObjects().size());
        testClass = (TestClass) parserData.getObjects().get(1);
        Assert.assertEquals("345", testClass.getValue());
    }

    @Test
    public void parseWithoutChildren() throws Exception {
        Block block = new Block();
        block.setStart("START");
        block.setEnd("END");
        block.setClassName("com.tieto.parser.model.TestClass");
        ParserData parserData = new ParserData();
        block.parse(parserData, "START 123 END START 345 END", null);
        Assert.assertEquals(0, parserData.getObjects().size());
    }
    
    @Test
    public void parseBlockHierarcy() throws Exception {
        Block block = new Block();
        block.setClassName("com.tieto.parser.model.UnitInfo");
        block.setOffset(0);
        block.setLength(7);
        Block subBlock = new Block();
        subBlock.setOffset(2);
        subBlock.setLength(5);
        List<TextParser> blocks = new ArrayList<TextParser>();
        blocks.add(subBlock);
        block.setTextParsers(blocks);
        Field field = new Field();
        field.setOffset(2);
        field.setLength(3);
        field.setAttribute("name");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        subBlock.setTextParsers(fields);
        ParserData parserData = new ParserData();
        block.parse(parserData, "0123456789", null);
        UnitInfo unitInfo = (UnitInfo) parserData.getObjects().get(0);
        Assert.assertEquals("456", unitInfo.getName());
    }

    @Test
    public void parseBlockHierarcy2() throws Exception {
        Block block = new Block();
        block.setStart("RECORD_START");
        block.setEnd("RECORD_END");
        Block subBlock = new Block();
        subBlock.setStart("SUB_START");
        subBlock.setClassName("com.tieto.parser.model.UnitInfo");
        subBlock.setEnd("SUB_END");
        List<TextParser> blocks = new ArrayList<TextParser>();
        blocks.add(subBlock);
        block.setTextParsers(blocks);
        Field field = new Field();
        field.setOffset(0);
        field.setLength(5);
        field.setTrim(true);
        field.setAttribute("name");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        subBlock.setTextParsers(fields);
        ParserData parserData = new ParserData();
        block.parse(parserData,
                "RECORD_START SUB_START 123 SUB_END SUB_START 345 SUB_END RECORD_END", null);
        Assert.assertEquals(2, parserData.getObjects().size());
        UnitInfo unitInfo = (UnitInfo) parserData.getObjects().get(0);
        Assert.assertEquals("123", unitInfo.getName());
        unitInfo = (UnitInfo) parserData.getObjects().get(1);
        Assert.assertEquals("345", unitInfo.getName());
    }
    
    @Test
    public void testAlarm() throws Exception {
        LineSequenceRecord lineSequenceRecord = new LineSequenceRecord();
        lineSequenceRecord.setSearchRegExp(".*\\*+\\s+((ALARM)).*");
        lineSequenceRecord.setClassName("com.tieto.parser.model.Alarm");
        List<TextParser> sequenceLines = new ArrayList<TextParser>();
        SequenceLine sequenceLine = new SequenceLine();
        sequenceLine.setLineNumber(1);
        // sequenceLine.setSearchRegExp("\\s\\s\\s\\s\\d\\d\\d\\d\\s\\s\\s\\s[A-Za-z0-9].*");
        sequenceLine.setSearchRegExp("\\s{4}\\d{4}\\s{4}[A-Za-z0-9].*");
        sequenceLines.add(sequenceLine);
        lineSequenceRecord.setTextParsers(sequenceLines);

        Field field = new Field();
        field.setOffset(3);
        field.setLength(6);
        field.setTrim(true);
        field.setAttribute("alarmNumber");
        field.setType("int");
        List<TextParser> fields = new ArrayList<TextParser>();
        fields.add(field);
        sequenceLine.setTextParsers(fields);
        ParserData parserData = new ParserData();
        parserData.setLineBreak(LINE_BREAK);
        lineSequenceRecord
                .parse(parserData,
                        "    (40983) IPA2800                          EQUIPM       2011-09-16 14:09:37.29"
                                + LINE_BREAK
                                + ""
                                + "*** ALARM   NONE                                                                "
                                + LINE_BREAK
                                + ""
                                + "    2070    LINK SET UNAVAILABLE                                            "
                                + LINE_BREAK + ""
                                + "    CCNETM  RSMU-0                                         "
                                + LINE_BREAK + "" + "    0010 01 00001811 08 " + LINE_BREAK + "",
                        null);
        Assert.assertEquals(1, parserData.getObjects().size());
        Alarm alarm = (Alarm) parserData.getObjects().get(0);
        Assert.assertEquals(2070, alarm.getAlarmNumber());
    }

    @Test
    public void parseWithNullInput() throws Exception {
        Block block = new Block();
        ParserData parserData = new ParserData();
        block.parse(parserData, null, null);
    }
}
