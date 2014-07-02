package com.tieto.parser;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.model.LogEntry;

@Slf4j
public class HgPushLogTest {
    @Test
    public void parsePushLog() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("pullLogEntry");
        List<Object> list = parser.parse(FileUtils.readFileToString(new File("src/test/resources/hg_push_output.log")));
        Assert.assertEquals(185, list.size());
        LogEntry logEntry = (LogEntry) list.get(0);
        Assert.assertNotNull(logEntry.getTime());
    }
}
