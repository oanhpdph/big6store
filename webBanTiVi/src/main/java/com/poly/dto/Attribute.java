package com.poly.dto;

import lombok.Data;

@Data
public class Attribute {
    private Integer id;
    private String value;
    private String name;
    private boolean active;
    private boolean variant;
    private int page = 1;
    private int size = 10;
}
