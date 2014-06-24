package com.tieto.parser.model;

import lombok.Data;

@Data
public class CpuLoad {
    private String unitName;
    private Integer cpuLoad;
}
