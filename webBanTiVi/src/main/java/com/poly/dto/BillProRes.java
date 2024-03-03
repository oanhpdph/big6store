package com.poly.dto;

import com.poly.entity.Bill;
import com.poly.entity.Users;
import lombok.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BillProRes {

    List<Integer> product;
    Bill bill;
    Integer qty;
    String name;
    String address;
    String phoneNumber;
    String email;
    BigDecimal totalPrice;
    Integer paymentMethod;
    Integer voucher=-1;
    List<BigDecimal> reducedMoney;
    Users customer;
    List<Integer> quantity;
    //delivery
    String deliver;
    Date deliveryDate;
    Date receivedDate;
    BigDecimal deliveryFee;
    String note;
    Integer status;
}
