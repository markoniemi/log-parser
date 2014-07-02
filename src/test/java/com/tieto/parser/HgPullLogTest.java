package com.tieto.parser;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.JAXBException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.model.LogEntry;

@Slf4j
public class HgPullLogTest {
    @Test
    public void parsePullLog() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("pullLogEntry");
        List<Object> list = parser.parse(FileUtils.readFileToString(new File("src/test/resources/hg_pull_output.log")));
        Assert.assertEquals(259, list.size());
        LogEntry logEntry = (LogEntry) list.get(0);
        Assert.assertNotNull(logEntry.getTime());
    }

    @Test
    public void parsePullLog2() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("pullLogEntry");
        List<Object> list = parser.parse(FileUtils.readFileToString(new File("src/test/resources/hg_pull_output_two_records.log")));
        Assert.assertEquals(2, list.size());
        LogEntry logEntry = (LogEntry) list.get(0);
        Assert.assertNotNull(logEntry.getTime());
    }
    private Date createTime(String time) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm", new Locale("en"));
        return dateFormat.parse(time);
    }
}
