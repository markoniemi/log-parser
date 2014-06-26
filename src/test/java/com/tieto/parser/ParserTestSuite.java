package com.tieto.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({ 
    ParserManagerTest.class, 
    FieldTest.class, 
    LineTest.class, 
    BlockTest.class,
    LineSequenceRecordTest.class, 
    HgPullLogTest.class,
    HgPushLogTest.class,
    ParserTest.class
})
public class ParserTestSuite {
}
