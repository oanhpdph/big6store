package com.poly.dto;

import lombok.Data;

import java.util.List;

@Data
public class Dto {
    private Integer group;
    private Integer type;
    private List<Attribute> listAttributes;

}

