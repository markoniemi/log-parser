package com.tieto.parser;

import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.extern.slf4j.Slf4j;

import org.junit.Assert;
import org.junit.Test;

import com.tieto.parser.converter.HgDateConverter;
@Slf4j
public class HgDateConverterTest {

    @Test
    public void convert() {
        HgDateConverter dateConverter = new HgDateConverter();
        Date date = (Date) dateConverter.convert("Thu Oct 24 23:35:01 EEST 2013");
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
        log.info(dateFormat.format(date));
        Assert.assertEquals("24.10.2013 23:35", dateFormat.format(date));
    }
}
