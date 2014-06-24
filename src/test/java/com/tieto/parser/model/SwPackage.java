package com.tieto.parser.model;

import lombok.Data;

@Data
public class SwPackage {
    private String version;
    private String name;
    private String status;
    private String info;
    private Boolean def;
    private Boolean act;
}
