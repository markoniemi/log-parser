package com.tieto.parser.model;

import java.util.Date;

import lombok.Data;

@Data
public class LicenseInfo {
    private int id;
    private int neId;
    private String code;
    private String name;
    private String filename;
    private String serialNumber;
    private String targetNetworkElementType;
    private String state;
    private Date startDate;
    private Date expirationTime;
    private int expirationWarningPeriod;
    private int capacity;
}
