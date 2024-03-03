package com.poly.dto;

import com.poly.entity.Product;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;


public interface CouponRes {
    Integer getId();
    String getValue();
    String getImage();
    Boolean getActive();
    @Temporal(TemporalType.DATE)
    Date getDate_Start();
    @Temporal(TemporalType.DATE)
    Date getDate_End();
    Product getProduct();

}
