package com.tieto.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import com.tieto.parser.git.GitLogTest;
import com.tieto.parser.log4j.Log4jLogTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    ParserManagerTest.class, 
    FieldTest.class, 
    LineTest.class, 
    BlockTest.class,
    LineSequenceRecordTest.class, 
    HgPullLogTest.class,
    HgPushLogTest.class,
    ParserTest.class,
    GitLogTest.class,
    Log4jLogTest.class
})
public class ParserTestSuite {
}
