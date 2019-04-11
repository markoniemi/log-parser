package com.tieto.parser.converter;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DateConverterTest {

    @Test
    public void convert() {
        DateConverter dateConverter = new DateConverter("EEE MMM dd hh:mm:ss z yyyy");
        Date date = dateConverter.convert("Sat Mar 30 23:50:01 EET 2013");
        Assert.assertNotNull(date);
    }
}
