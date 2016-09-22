package com.tieto.parser.log4j;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.Parser;
import com.tieto.parser.ParserManager;

public class Log4jLogIT {
    @Test
    public void parseLog() throws IOException, JAXBException {
        ParserManager parserManager = new ParserManager("src/test/resources/config/parsers.xml");
        Parser parser = parserManager.getParser("log4j");
        @SuppressWarnings("unchecked")
        List<LogEvent> list = (List<LogEvent>) parser.parse(FileUtils.readFileToString(new File("src/test/resources/log4j/log4j.log")));
        Assert.assertEquals(400, list.size());
        for (LogEvent logEvent : list) {
            Assert.assertNotNull(logEvent.getTime());
            Assert.assertNotNull(logEvent.getMessage());
            Assert.assertNotNull(logEvent.getLevel());
        }
    }
}
