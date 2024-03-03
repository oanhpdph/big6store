package com.poly.dto;


import lombok.*;

import java.sql.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VoucherCustomerRes {
    Integer customer;
    Integer voucher;
    Date dateStart;
    Date dateEnd;
    Boolean active;

}
