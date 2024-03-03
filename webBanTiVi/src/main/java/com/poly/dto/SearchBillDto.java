package com.poly.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SearchBillDto {

    public String date = "";
    public String key = "";
    public String billStatus = "";
    public Integer paymentStatus = -1;
    public Integer sort = 1;

}
