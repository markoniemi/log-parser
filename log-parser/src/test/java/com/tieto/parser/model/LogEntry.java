package com.tieto.parser.model;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class LogEntry {
    Date time;
    String branch;
    int files;
}
