package com.poly.dto;

import com.poly.entity.Product;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public interface VoucherRes {
    Integer getId();
    String getNameVoucher();
    int getValue();
    String getImage();
    @Temporal(TemporalType.DATE)
    Date getDate_Start();
    @Temporal(TemporalType.DATE)
    Date getDate_End();
    Product getProduct();

}
