package com.tieto.parser.model;

import java.util.Date;


import lombok.Data;

@Data
public class Alarm {
    private int alarmNumber;
    private String alarmId;
    private String unitName;
    private Date time; 
    private String details;
    private String description;
    private Severity severity;
}
