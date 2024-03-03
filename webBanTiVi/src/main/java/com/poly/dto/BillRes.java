package com.poly.dto;

import com.poly.entity.BillStatus;
import com.poly.entity.PaymentMethod;
import com.poly.entity.Voucher;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillRes {

    Integer id;
    Integer customer;
    String code;
    Date createDate;
    Date paymentDate;
    BigDecimal totalPrice;
    int paymentStatus;
    BillStatus billStatus;
    PaymentMethod paymentMethod;
    Voucher voucher;
    String note;

}
