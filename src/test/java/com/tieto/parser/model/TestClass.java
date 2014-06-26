package com.tieto.parser.model;

import java.util.Date;

import lombok.Data;

@Data
public class TestClass {
    private String value;
    private long longValue;
    private boolean booleanValue;
    private Date date;
    public void dummy() {
        // TODO rename TestClass or exclude from maven test
    }
}
