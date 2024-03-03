package com.poly.dto;

import lombok.*;

import java.sql.Date;
import java.util.List;


@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EvaluateRes {
    Integer id;
    Integer customer;
    Integer product;
    Date dateCreate;
    Integer point;
    String comment;
    boolean active;
    List<String> image;
    Integer page = 1;
    Integer size = 10;
}
