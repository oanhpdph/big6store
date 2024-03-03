package com.poly.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountBillProductReturnDto {
    private int idBillProduct;
    private int totalAcceptReturn;
}
