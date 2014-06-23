package com.tieto.parser.model;

import org.junit.Test;

import lombok.Data;

@Data
public class TestClass {
    private String value;
    private long longValue;
    private boolean booleanValue;
    @Test
    public void dummy() {
        // TODO rename TestClass or exclude from maven test
    }
}
