package com.tieto.parser;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

public class LineSequenceRecordTest {
    public static final String LINE_BREAK = System.getProperty("line.separator");

    @Test
    public void testRegExp() {
        String string = new String("*** ALARM");
        Assert.assertTrue(string.matches(".*\\*+\\s+((ALARM)|(DISTUR)).*"));
    }
    @Test
    public void searchRegExp() {
        LineSequenceRecord record = new LineSequenceRecord();
        record.setSearchRegExp(".*\\*+\\s+((ALARM)|(DISTUR)).*");
        record.setLineBreak(LINE_BREAK);
        List<String> splitInput = record.splitInput("** ALARM" + LINE_BREAK + "*   DISTUR"
                + LINE_BREAK + "*  ALARM" + LINE_BREAK + "");
        Assert.assertTrue(splitInput.get(0).startsWith("** ALARM"));
        Assert.assertTrue(splitInput.get(1).startsWith("*   DISTUR"));
        Assert.assertTrue(splitInput.get(2).startsWith("*  ALARM"));
    }
    @Test
    public void search() {
        LineSequenceRecord record = new LineSequenceRecord();
        record.setLineBreak(LINE_BREAK);
        record.setSearch("test");
        List<String> splitInput = record.splitInput("test 1"+LINE_BREAK+"test 2"+LINE_BREAK+"aaaa 2+lineBreak");
        Assert.assertEquals(2, splitInput.size());
        Assert.assertTrue(splitInput.get(0).startsWith("test"));
        Assert.assertTrue(splitInput.get(1).startsWith("test"));
    }
}
