package com.tieto.parser.log4j;

import java.util.Date;

import lombok.Data;

import org.apache.log4j.Level;

@Data
public class LogEvent {
    private Level level;
    private Date time;
    private String className;
    private String message;
}
