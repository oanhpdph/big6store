package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.sql.Date;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueDto {

    private Date date;
    private BigDecimal revenue;
}
